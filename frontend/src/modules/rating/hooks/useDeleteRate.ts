"use client";

// External Library
import { useState, useEffect } from "react";
import { toast } from "sonner";

// Store
import { useAuthStore } from "@/modules/auth/store/authStore";

// Service
import getRatingsService from "../service/get/getRatings";

// Error
import { ApplicationError } from "@/shared/errors/base/ApplicationError";

// Type Guard
import { Either, isLeft } from "@/shared/patterns/either";

// Service
import deleteRateService from "../service/delete/deleteRate";

interface UseDeleteRateResponse {
  deleteRate: (id: string) => Promise<void>;
  isLoading: boolean;
}

export function useDeleteRate(): UseDeleteRateResponse {
  const [isLoading, setIsLoading] = useState(false);

  const deleteRate = async (id: string) => {
    setIsLoading(true);
    const result = await deleteRateService({ movieId: id });

    if (isLeft(result)) {
      toast.error("Erro ao remover avaliação", {
        description: result.value.message,
      });
      setIsLoading(false);
      return;
    }

    toast.success("Avaliação removida com sucesso!");
    setIsLoading(false);
  };

  return { deleteRate, isLoading };
}
