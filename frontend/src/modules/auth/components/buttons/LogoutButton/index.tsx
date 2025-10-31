"use client";

// External Library
import { LogOut } from "lucide-react";

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

export function LogoutButton() {
  const { logout } = useAuthStore();

  const { navigateTo } = useNavigationHandler();

  const handleLogoutClick = () => {
    logout();
    navigateTo("/");
  };

  return (
    <TooltipProvider>
      <Tooltip>
        <TooltipTrigger asChild>
          <Button onClick={handleLogoutClick} variant="ghost" size="icon">
            <LogOut className="h-5 w-5" />
            <span className="sr-only">Sair</span>
          </Button>
        </TooltipTrigger>
        <TooltipContent>
          <p>Sair</p>
        </TooltipContent>
      </Tooltip>
    </TooltipProvider>
  );
}
