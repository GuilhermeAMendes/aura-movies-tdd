// Types
import type { MovieId } from "../movie/types";

export interface Rating {
  movieId: MovieId;
  title: string;
  grade: string;
  lastGradedAt: string;
}

export interface GetRatingResponse {
  ratedMovies: Rating[];
}

export interface PostRatingPayload
  extends Omit<Rating, "lastGradedAt" | "title"> {}

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
