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
import type { DeleteRatePayload } from "../../types";

export default async function deleteRate({
  movieId,
}: DeleteRatePayload): Promise<Either<ApplicationError, void>> {
  try {
    const { data: response } = await AxiosClient.delete<void>(
      `ratings/${encodeURIComponent(movieId)}`
    );
    return right(response);
  } catch (error) {
    let message = "Delete rate failed";

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
