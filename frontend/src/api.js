import axios from "axios";

const api = axios.create({
  baseURL:
    import.meta.env.VITE_API_BASE_URL ||
    "http://localhost:8081/api/v1",
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("careerpilot_token");

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  // Let Axios/browser set the correct Content-Type automatically.
  // JSON requests will be serialized normally.
  // FormData requests will receive multipart/form-data with a boundary.
  if (config.data instanceof FormData) {
    delete config.headers["Content-Type"];
    delete config.headers["content-type"];
  }

  return config;
});

export default api;