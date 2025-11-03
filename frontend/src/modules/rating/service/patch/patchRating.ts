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
import type { PatchRatingPayload } from "../../types";

export default async function patchRating(
  payload: PatchRatingPayload
): Promise<Either<ApplicationError, void>> {
  const { grade, movieId } = payload;
  try {
    const { data: response } = await AxiosClient.patch<void>(
      `ratings/${encodeURIComponent(movieId)}`,
      { grade: grade }
    );
    return right(response);
  } catch (error) {
    let message = "Update rating failed";

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
