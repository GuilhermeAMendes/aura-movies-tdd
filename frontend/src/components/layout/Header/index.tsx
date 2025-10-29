"use client";

// External Library
import Link from "next/link";
import { usePathname } from "next/navigation";

// Components
import { Button } from "@/components/ui/button";

// Utils
import { useNavigationHandler } from "@/shared/hooks/navigation/useNavigation";

export function Header() {
  const pathname = usePathname();
  const { navigateTo } = useNavigationHandler();

  const isHomePage = pathname === "/";

  const isAuthPage = pathname === "/login";

  if (isAuthPage) {
    return null;
  }

  const handleLoginClick = () => {
    navigateTo("/login");
  };

  return (
    <header className="sticky top-0 z-40 w-full border-b bg-background">
      <div className="container flex h-16 items-center justify-between py-4 px-6 md:px-8">
        <Link href="/" className="flex items-center space-x-2">
          <span className="font-bold text-2xl tracking-tight text-primary">
            Aura
          </span>
        </Link>

        <nav>
          {isHomePage && (
            <Button
              onClick={handleLoginClick}
              variant="ghost"
              className="text-lg"
            >
              Login
            </Button>
          )}
        </nav>
      </div>
    </header>
  );
}
