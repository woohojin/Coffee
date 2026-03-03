export async function apiRequest(url, options = {}) {
  try {
    const response = await fetch(url, {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        ...options.headers
      },
      credentials: 'same-origin'
    });

    const result = await response.json();

    if (!result.success) {
      if (result.message) {
        alert(result.message);
      }

      if (result.redirectUrl) {
        window.location.href = result.redirectUrl;
      }

      throw new Error(result.message || 'API 요청 실패');
    }

    return result.data;

  } catch (error) {
    console.error('API Error:', error);

    if (error.message === 'Failed to fetch') {
      alert('서버와의 연결에 실패했습니다.');
    }

    throw error;
  }
}

export async function apiGet(url) {
  return apiRequest(url, { method: 'GET' });
}

export async function apiPost(url, data) {
  return apiRequest(url, {
    method: 'POST',
    body: JSON.stringify(data)
  });
}

// customHeaders = apiRequest에 설정한 기본 header 설정을 무시하기 위해 (FormData)
export async function apiPostForm(url, formData, customHeaders = {}) {
  return apiRequest(url, {
    method: 'POST',
    body: formData,
    headers: customHeaders,
  });
}

export async function apiPut(url, data) {
  return apiRequest(url, {
    method: 'PUT',
    body: JSON.stringify(data)
  });
}

export async function apiDelete(url) {
  return apiRequest(url, { method: 'DELETE' });
}