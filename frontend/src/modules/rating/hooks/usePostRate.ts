"use client";

// External Library
import { useState } from "react";
import { toast } from "sonner";

// Service
import postRateService from "../service/post/postRating";

// Type Guard
import { isLeft } from "@/shared/patterns/either";

// Types
import type { PostRatingPayload } from "../types";

interface UsePostRateResponse {
  postRate: (payload: PostRatingPayload) => Promise<void>;
  isLoading: boolean;
}

export function usePostRate(): UsePostRateResponse {
  const [isLoading, setIsLoading] = useState(false);

  const postRate = async (payload: PostRatingPayload) => {
    setIsLoading(true);
    const result = await postRateService(payload);

    if (isLeft(result)) {
      toast.error("Erro ao criar avaliação", {
        description: result.value.message,
      });
      setIsLoading(false);
      return;
    }

    toast.success("Avaliação criada com sucesso!");
    setIsLoading(false);
  };

  return { postRate, isLoading };
}
