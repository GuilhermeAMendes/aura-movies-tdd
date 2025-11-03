"use client";

// External Library
import { useState } from "react";
import { toast } from "sonner";

// Service
import deleteRateService from "../service/delete/deleteRate";

// Type Guard
import { isLeft } from "@/shared/patterns/either";

// Types

import { DeleteRatePayload } from "../types";

interface UseDeleteRateResponse {
  deleteRate: (payload: DeleteRatePayload) => Promise<boolean>;
  isLoading: boolean;
}

export function useDeleteRate(): UseDeleteRateResponse {
  const [isLoading, setIsLoading] = useState(false);

  const deleteRate = async (payload: DeleteRatePayload) => {
    setIsLoading(true);
    const result = await deleteRateService({ movieId: payload.movieId });

    if (isLeft(result)) {
      toast.error("Erro ao remover avaliação", {
        description: result.value.message,
      });
      setIsLoading(false);
      return false;
    }

    toast.success("Avaliação removida com sucesso!");
    setIsLoading(false);
    return true;
  };

  return { deleteRate, isLoading };
}
