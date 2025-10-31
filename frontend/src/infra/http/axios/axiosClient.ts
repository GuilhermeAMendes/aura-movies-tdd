// External Library
import axios from "axios";

// Types
import type { AxiosError, InternalAxiosRequestConfig } from "axios";

// Store
import { useAuthStore } from "@/modules/auth/store/authStore";

// Constants
import { ERROR_CODE } from "@/shared/errors/constants/error";

const AUTH_URLS = ["authenticate", "register"];
const AUTH_ERROR_CODES = [ERROR_CODE.unauthorized, ERROR_CODE.forbidden];

export const AxiosClient = axios.create({
  baseURL: "http://localhost:8080/api/v1/",
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 10000,
});

AxiosClient.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = useAuthStore.getState().token;

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error: AxiosError) => {
    return Promise.reject(error);
  }
);

AxiosClient.interceptors.response.use(
  (res) => res,
  async (error: AxiosError) => {
    const originalRequest = error.config as InternalAxiosRequestConfig;

    if (!error.response) {
      return Promise.reject(error);
    }

    const { status } = error.response;

    const requestUrl = originalRequest.url || "";

    const isAuthError = AUTH_ERROR_CODES.some((code) => code === status);
    const isAuthEndpoint = AUTH_URLS.some((url) => requestUrl.includes(url));

    if (isAuthError && !isAuthEndpoint) {
      useAuthStore.getState().logout();
    }

    return Promise.reject(error);
  }
);
