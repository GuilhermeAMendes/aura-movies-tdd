// External Library
import { create } from "zustand";
import { devtools } from "zustand/middleware";

// Services
import registerService from "../services/register";
import loginService from "../services/login";

// Type Guard
import { isLeft, type Either } from "@/shared/patterns/either";

// Errors
import { ApplicationError } from "@/shared/errors/base/ApplicationError";

// Utils
import authStorage from "../utils/authStorage";

// Types
import type {
  LoginPayload,
  LoginResponse,
  RegisterPayload,
  RegisterResponse,
} from "../types";

interface AuthState {
  isLoading: boolean;
  login: (
    payload: LoginPayload
  ) => Promise<Either<ApplicationError, LoginResponse>>;
  register: (
    payload: RegisterPayload
  ) => Promise<Either<ApplicationError, RegisterResponse>>;
  logout: () => void;
}

export const useAuthStore = create<AuthState>()(
  devtools((set) => ({
    isLoading: false,

    login: async (payload: LoginPayload) => {
      set({ isLoading: true });

      const result = await loginService(payload);

      if (isLeft(result)) {
        set({ isLoading: false });
        return result;
      }

      authStorage.setToken(result.value.token);

      set({ isLoading: false });
      return result;
    },

    register: async (payload: RegisterPayload) => {
      set({ isLoading: true });

      const result = await registerService(payload);

      if (isLeft(result)) {
        set({ isLoading: false });
        return result;
      }

      set({ isLoading: false });
      return result;
    },

    logout: () => {
      authStorage.clear();
    },
  }))
);
