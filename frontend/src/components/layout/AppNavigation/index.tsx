"use client";

// External Library
import Link from "next/link";
import { usePathname } from "next/navigation";
import { cn } from "@/lib/utils";

// Constants
const navLinks = [
  { href: "/recommendations", label: "Recomendações" },
  { href: "/movies", label: "Catálogo" },
  { href: "/profile", label: "Minhas Avaliações" },
];

export function AppNavigation() {
  const pathname = usePathname();

  return (
    <nav className="flex items-center gap-4 sm:gap-8 border-b pb-4 mb-6 md:mb-8">
      {navLinks.map((link) => {
        const isActive = pathname === link.href;
        return (
          <Link
            key={link.href}
            href={link.href}
            className={cn(
              "text-lg font-medium transition-colors hover:text-primary",
              isActive
                ? "text-primary font-semibold border-b-4 border-blue-500"
                : "text-muted-foreground"
            )}
          >
            {link.label}
          </Link>
        );
      })}
    </nav>
  );
}
