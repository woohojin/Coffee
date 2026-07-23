import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true,
});

let isRefreshing = false;
let failedQueue = [];

const processQueue = (error) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve();
    }
  });
  failedQueue = [];
};

// 401 감지 시 자동 토큰 재발급
axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // 401이고 재시도 안 한 요청이면 (단, 로그인 여부 확인용 /api/member/me는 401이 정상 응답이므로 제외)
    if (
      error.response?.status === 401 &&
      !originalRequest._retry &&
      !originalRequest.url?.includes("/api/member/me")
    ) {
      if (isRefreshing) {
        // 이미 refresh 중이면 대기열에 추가
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        })
          .then(() => axiosInstance(originalRequest))
          .catch((err) => Promise.reject(err));
      }

      originalRequest._retry = true;
      isRefreshing = true;

      try {
        // Refresh Token으로 재발급
        await axiosInstance.post("/api/auth/refresh");
        processQueue(null);
        // 원래 요청 재시도
        return axiosInstance(originalRequest);
      } catch (refreshError) {
        // 재발급 실패 → 로그인 페이지로
        processQueue(refreshError);
        window.location.href = "/member/memberSignIn";
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(error);
  },
);

export default axiosInstance;
