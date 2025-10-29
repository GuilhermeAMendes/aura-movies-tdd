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
import type { GetRecommendationsResponse } from "../types";

export default async function getRecommendations(): Promise<
  Either<ApplicationError, GetRecommendationsResponse>
> {
  try {
    const { data: response } =
      await AxiosClient.get<GetRecommendationsResponse>("recommendations");
    return right(response);
  } catch (error) {
    let message = "Restore recommendations failed";

    if (isAxiosError(error)) {
      message =
        error.response?.data?.detail ||
        error.response?.data?.message ||
        error.message;
    } else if (error instanceof Error) {
      message = error.message;
    }

    return errorFactory("custom", message);
  }
}
