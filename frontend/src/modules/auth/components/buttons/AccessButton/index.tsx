"use client";

// External Library
import { LayoutDashboard } from "lucide-react";

// Components
import { Button } from "@/components/ui/button";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";

// Hooks
import { useNavigationHandler } from "@/shared/hooks/navigation/useNavigation";
import { useAuthStore } from "@/modules/auth/store/authStore";

export function AccessButton() {
  const { token } = useAuthStore();
  const { navigateTo } = useNavigationHandler();

  const handleAccessClick = () => {
    navigateTo("/recommendations");
  };

  return token ? (
    <TooltipProvider>
      <Tooltip>
        <TooltipTrigger asChild>
          <Button onClick={handleAccessClick} variant="ghost" size="icon">
            <LayoutDashboard className="h-5 w-5" />
            <span className="sr-only">Acessar</span>
          </Button>
        </TooltipTrigger>
        <TooltipContent>
          <p>Acessar</p>
        </TooltipContent>
      </Tooltip>
    </TooltipProvider>
  ) : null;
}
