"use client";

// External Library
import { useState } from "react";
import { toast } from "sonner";

// Service
import patchRatingService from "../service/patch/patchRating";

// Type Guard
import { isLeft } from "@/shared/patterns/either";

// Types
import type { PatchRatingPayload } from "../types";

interface UsePatchRateResponse {
  patchRate: (payload: PatchRatingPayload) => Promise<boolean>;
  isLoading: boolean;
}

export function usePatchRate(): UsePatchRateResponse {
  const [isLoading, setIsLoading] = useState(false);

  const patchRate = async (payload: PatchRatingPayload) => {
    setIsLoading(true);
    const result = await patchRatingService(payload);

    if (isLeft(result)) {
      toast.error("Erro ao atualizar avaliação", {
        description: result.value.message,
      });
      setIsLoading(false);
      return false;
    }

    toast.success("Avaliação atualizada com sucesso!");
    setIsLoading(false);
    return true;
  };

  return { patchRate, isLoading };
}
