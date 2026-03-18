import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5174, // frontend 5173, admin 5174
    proxy: {
      "/api": "http://localhost:8080",
      "/files": "http://localhost:8080",
      "/member": "http://localhost:8080", // 로그인 엔드포인트
    },
  },
});
