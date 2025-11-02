"use client";

// External Library
import { useState, useEffect, useCallback } from "react";
import { toast } from "sonner";

// Store
import { useAuthStore } from "@/modules/auth/store/authStore";

// Service
import getMovieByIdService from "../service/get/getMovieById";

// Error
import { ApplicationError } from "@/shared/errors/base/ApplicationError";

// Type Guard
import { isLeft } from "@/shared/patterns/either";

// Types
import type { Movie } from "@/modules/movie/types";
import { Rating } from "@/modules/rating/types";

interface UseGetMovieByIdPayload {
  id: string;
}

interface UseGetMovieByIdResponse {
  movie: Movie | null;
  rating: Rating | null;
  isLoading: boolean;
  error: ApplicationError | null;
  refetch: () => void;
}

export function useGetMovieById({
  id,
}: UseGetMovieByIdPayload): UseGetMovieByIdResponse {
  const [movie, setMovie] = useState<Movie | null>(null);
  const [rating, setRating] = useState<Rating | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<ApplicationError | null>(null);

  const { token, isSessionRestored } = useAuthStore();

  const loadMovie = useCallback(async () => {
    setIsLoading(true);
    setError(null);

    const result = await getMovieByIdService({ id });

    if (isLeft(result)) {
      setError(result.value);
      toast.error("Erro ao buscar filme", {
        description: result.value.message,
      });
      setIsLoading(false);
      return;
    }

    const { movie, rating } = result.value;
    setMovie(movie);
    setRating(rating);
    setIsLoading(false);
  }, []);

  useEffect(() => {
    if (!isSessionRestored) return;
    if (!token) {
      setIsLoading(false);
      setMovie(null);
      return;
    }

    loadMovie();
  }, [token, isSessionRestored, id, loadMovie]);

  return { movie, rating, isLoading, error, refetch: loadMovie };
}
