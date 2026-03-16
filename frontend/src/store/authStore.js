import { useState, useEffect } from "react";
import axiosInstance from "../api/axiosInstance";

export function useAuth() {
  const [member, setMember] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axiosInstance
      .get("/api/member/me")
      .then((res) => setMember(res.data.data))
      .catch(() => setMember(null))
      .finally(() => setLoading(false));
  }, []);

  return { member, loading, setMember };
}
