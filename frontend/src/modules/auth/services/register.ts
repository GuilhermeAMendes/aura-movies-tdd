// Infra
import { AxiosClient } from "@/infra/http/axios/axiosClient";

// Type Guard
import { type Either, right } from "@/shared/patterns/either";

// Error
import { ApplicationError } from "@/shared/errors/base/ApplicationError";

// Factory
import errorFactory from "@/shared/errors/factory/errorFactory";

// Types
import type { RegisterPayload, RegisterResponse } from "../types";

export default async function register(
  payload: RegisterPayload
): Promise<Either<ApplicationError, RegisterResponse>> {
  try {
    const { data: response } = await AxiosClient.post<RegisterResponse>(
      "register",
      payload
    );
    return right(response);
  } catch (error) {
    const message =
      error.response?.data?.detail ||
      error.response?.data?.message ||
      error.message ||
      "Failed to register user";

    return errorFactory("validation", message);
  }
}
