"use client";

// External Library
import { useState, useEffect } from "react";
import { toast } from "sonner";

// Store
import { useAuthStore } from "@/modules/auth/store/authStore";

// Service
import getMoviesService from "../service/get/getMovies";

// Error
import { ApplicationError } from "@/shared/errors/base/ApplicationError";

// Type Guard
import { isLeft } from "@/shared/patterns/either";

// Utils
import { shuffleContent } from "@/shared/utils/helper/functions/shuffleContent";

// Types
import type { Movie } from "@/modules/movie/types";

interface UseGetMoviesResponse {
  movies: Movie[];
  isLoading: boolean;
  error: ApplicationError | null;
}

export function useGetMovies(): UseGetMoviesResponse {
  const [movies, setMovies] = useState<Movie[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<ApplicationError | null>(null);

  const { token, isSessionRestored } = useAuthStore();

  const loadRecommendations = async () => {
    setIsLoading(true);
    setError(null);

    const result = await getMoviesService();

    if (isLeft(result)) {
      setError(result.value);
      toast.error("Erro ao buscar filmes", {
        description: result.value.message,
      });
      setIsLoading(false);
      return;
    }

    const { movies } = result.value;
    const shuffleMovies = shuffleContent(movies);
    setMovies(shuffleMovies);
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
