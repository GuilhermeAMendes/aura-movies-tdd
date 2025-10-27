// External Library
import axios from "axios";

// Types
import type { AxiosError, InternalAxiosRequestConfig } from "axios";

// Utils
import { authStorage } from "@/modules/auth/utils/authStorage";

// Constants
import { ERROR_CODE } from "@/shared/errors/constants/error";

const AUTH_URLS = ["authenticate", "register"];
const AUTH_ERROR_CODES = [ERROR_CODE.unauthorized, ERROR_CODE.forbidden];

export const AxiosClient = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL,
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 10000,
});

AxiosClient.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = authStorage.getToken();

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

    const requestUrl = originalRequest.url || "";

    const { status } = error.response;

    const isAuthError = AUTH_ERROR_CODES.every((code) => code !== status);
    const isAuthEndpoint = AUTH_URLS.some((url) => requestUrl.includes(url));

    if (isAuthError && !isAuthEndpoint) {
      console.warn("Expires session.");
    }

    return Promise.reject(error);
  }
);
