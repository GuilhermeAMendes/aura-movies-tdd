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
import type { GetMovieByIdPayload, GetMovieByIdResponse } from "../types";

export default async function getMovieById({
  id,
}: GetMovieByIdPayload): Promise<
  Either<ApplicationError, GetMovieByIdResponse>
> {
  try {
    const { data: response } = await AxiosClient.get<GetMovieByIdResponse>(
      `movies/${encodeURIComponent(id)}`
    );
    return right(response);
  } catch (error) {
    let message = "Restore movie failed";

    if (isAxiosError(error)) {
      message =
        error.response?.data?.detail ||
        error.response?.data?.message ||
        error.message;
    } else if (error instanceof Error) {
      message = error.message;
    }

    return errorFactory("not_found", message);
  }
}
