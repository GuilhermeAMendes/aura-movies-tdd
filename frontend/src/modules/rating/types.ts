// Types
import type { MovieId } from "../movie/types";

export interface Rating {
  movieId: MovieId;
  grade: number;
  lastGradedAt: string;
}

export interface GetRatingResponse {
  ratings: Rating[];
}

export interface PostRatingPayload extends Omit<Rating, "lastGradedAt"> {}

export interface PostRatingResponse {
  rating: Rating;
}

export interface PatchRatingPayload {
  movieId: string;
  grade: string;
}

export interface DeleteRatePayload {
  movieId: string;
}
