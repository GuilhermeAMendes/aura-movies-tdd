"use client";

// External Library
import { useState, useEffect } from "react";
import { toast } from "sonner";

// Store
import { useAuthStore } from "@/modules/auth/store/authStore";

// Service
import getRatingsService from "../service/getRatings";

// Error
import { ApplicationError } from "@/shared/errors/base/ApplicationError";

// Type Guard
import { isLeft } from "@/shared/patterns/either";

// Types
import type { Rating } from "../types";

interface UseGetRatingsResponse {
  ratings: Rating[];
  isLoading: boolean;
  error: ApplicationError | null;
}

export function useGetRatings(): UseGetRatingsResponse {
  const [ratings, setRatings] = useState<Rating[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<ApplicationError | null>(null);

  const { token, isSessionRestored } = useAuthStore();

  const loadRatings = async () => {
    setIsLoading(true);
    setError(null);

    const result = await getRatingsService();

    if (isLeft(result)) {
      setError(result.value);
      toast.error("Erro ao buscar avaliações", {
        description: result.value.message,
      });
      setIsLoading(false);
      return;
    }

    setRatings(result.value.ratings);
    setIsLoading(false);
  };

  useEffect(() => {
    if (!isSessionRestored) return;
    if (!token) {
      setIsLoading(false);
      setRatings([]);
      return;
    }

    loadRatings();
  }, [token, isSessionRestored]);

  return { ratings, isLoading, error };
}
