// Infra
import { AxiosClient } from "@/infra/http/axios/axiosClient";

// Type Guard
import { type Either, right } from "@/shared/patterns/either";

// Error
import { ApplicationError } from "@/shared/errors/base/ApplicationError";

// Factory
import errorFactory from "@/shared/errors/factory/errorFactory";

// Types
import type { LoginPayload, LoginResponse } from "../types";

export default async function login(
  payload: LoginPayload
): Promise<Either<ApplicationError, LoginResponse>> {
  try {
    const { data: response } = await AxiosClient.post<LoginResponse>(
      "authenticate",
      payload
    );
    return right(response);
  } catch (error) {
    const message =
      error.response?.data?.detail ||
      error.response?.data?.message ||
      error.message ||
      "Authentication failed";
    return errorFactory("unauthorized", message);
  }
}
