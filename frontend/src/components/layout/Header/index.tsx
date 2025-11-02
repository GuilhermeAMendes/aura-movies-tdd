"use client";

// External Library
import Link from "next/link";
import { usePathname } from "next/navigation";

// Components
import { Button } from "@/components/ui/button";
import { AccessButton } from "@/modules/auth/components/buttons/AccessButton";
import { LogoutButton } from "@/modules/auth/components/buttons/LogoutButton";

// Hooks
import { useNavigationHandler } from "@/shared/hooks/navigation/useNavigation";
import { useAuthStore } from "@/modules/auth/store/authStore";

export function Header() {
  const pathname = usePathname();
  const { navigateTo } = useNavigationHandler();
  const { token } = useAuthStore();

  const isHomePage = pathname === "/";
  const isAuthPage = pathname === "/login";

  if (isAuthPage) {
    return null;
  }

  const handleLoginClick = () => {
    navigateTo("/login");
  };

  const handleRegisterClick = () => {
    navigateTo("/register");
  };

  return (
    <header className="sticky top-0 z-40 w-full border-b bg-background">
      <div className="flex h-16 items-center justify-between px-6">
        <Link href="/" className="flex items-center space-x-2">
          <span className="font-bold text-2xl tracking-tight text-primary">
            Aura
          </span>
        </Link>
        <nav>
          {token ? (
            <>
              {isHomePage && <AccessButton />}
              <LogoutButton />
            </>
          ) : isHomePage ? (
            <div className="flex items-center gap-2">
              <Button
                onClick={handleRegisterClick}
                variant="ghost"
                className="text-lg"
              >
                Registrar
              </Button>
              <Button
                onClick={handleLoginClick}
                className="text-lg bg-black text-white hover:bg-neutral-800"
              >
                Login
              </Button>
            </div>
          ) : null}
        </nav>
      </div>
    </header>
  );
}
