// Infra
import { AxiosClient } from "@/infra/http/axios/axiosClient";
import { isAxiosError } from "axios";

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
    let message = "Authentication failed";

    if (isAxiosError(error)) {
      message =
        error.response?.data?.detail ||
        error.response?.data?.message ||
        error.message;
    } else if (error instanceof Error) {
      message = error.message;
    }

    return errorFactory("unauthorized", message);
  }
}
