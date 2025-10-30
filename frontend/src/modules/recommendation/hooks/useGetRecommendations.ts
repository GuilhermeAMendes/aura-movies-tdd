"use client";

// External Library
import { useState, useEffect } from "react";
import { toast } from "sonner";

// Store
import { useAuthStore } from "@/modules/auth/store/authStore";

// Service
import getRecommendationsService from "../service/get/getRecommendations";

// Error
import { ApplicationError } from "@/shared/errors/base/ApplicationError";

// Type Guard
import { isLeft } from "@/shared/patterns/either";

// Types
import type { Movie } from "@/modules/movie/types";
import { shuffleContent } from "@/shared/utils/helper/functions/shuffleContent";

interface UseGetRecommendationsResponse {
  movies: Movie[];
  isLoading: boolean;
  error: ApplicationError | null;
}

export function useGetRecommendations(): UseGetRecommendationsResponse {
  const [movies, setMovies] = useState<Movie[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<ApplicationError | null>(null);

  const { token, isSessionRestored } = useAuthStore();

  const loadRecommendations = async () => {
    setIsLoading(true);
    setError(null);

    const result = await getRecommendationsService();

    if (isLeft(result)) {
      setError(result.value);
      toast.error("Erro ao buscar recomendações", {
        description: result.value.message,
      });
      setIsLoading(false);
      return;
    }

    const { recommendations } = result.value;
    const shuffleRecommendations = shuffleContent(recommendations);
    setMovies(shuffleRecommendations);
    setIsLoading(false);
  };

  useEffect(() => {
    if (!isSessionRestored) return;
    if (!token) {
      setIsLoading(false);
      setMovies([]);
      return;
    }

    loadRecommendations();
  }, [token, isSessionRestored]);

  return { movies, isLoading, error };
}
