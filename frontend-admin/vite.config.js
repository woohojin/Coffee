import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5174, // frontend 5173, admin 5174
    proxy: {
      "/api": "http://localhost:8080",
      "/files": "http://localhost:8080",
      "/member/memberSignInPro": "http://localhost:8080",
      "/member/memberLogout": "http://localhost:8080",
      "/css": "http://localhost:8080",
      "/image": "http://localhost:8080",
    },
  },
});
