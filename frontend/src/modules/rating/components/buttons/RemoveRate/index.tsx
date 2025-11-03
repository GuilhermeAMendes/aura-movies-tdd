"use client";

// External Library
import { Trash, Loader2 } from "lucide-react";

// Components
import { Button } from "@/components/ui/button";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";

// Hooks
import { useAuthStore } from "@/modules/auth/store/authStore";
import { useDeleteRate } from "@/modules/rating/hooks/useDeleteRate";

// Types
interface RemoveRatePayload {
  id: string;
  onSuccess: () => void;
}

export function RemoveRate({ id, onSuccess }: RemoveRatePayload) {
  const { token } = useAuthStore();
  const { deleteRate, isLoading } = useDeleteRate();

  const handleAccessClick = async () => {
    if (isLoading) return;
    const result = await deleteRate({ movieId: id });
    if (result) onSuccess();
  };

  return token ? (
    <TooltipProvider>
      <Tooltip>
        <TooltipTrigger asChild>
          <Button
            onClick={handleAccessClick}
            variant="ghost"
            size="icon"
            disabled={isLoading}
          >
            {isLoading ? (
              <Loader2 className="h-5 w-5 animate-spin" />
            ) : (
              <Trash className="h-5 w-5 text-red-500" />
            )}
            <span className="sr-only">Remover</span>
          </Button>
        </TooltipTrigger>
        <TooltipContent>
          <p>Remover</p>
        </TooltipContent>
      </Tooltip>
    </TooltipProvider>
  ) : null;
}
