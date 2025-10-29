"use client";

import Image from "next/image";
import Link from "next/link";

import { Button } from "@/components/ui/button";
import { useNavigationHandler } from "@/shared/hooks/navigation/useNavigation";
import { Header } from "@/components/layout/Header";

export default function HomePage() {
  const { navigateTo } = useNavigationHandler();

  const handleLoginClick = () => {
    navigateTo("/login");
  };

  return (
    <div className="flex flex-col min-h-screen bg-background text-foreground">
      <Header />
      <main className="flex-1 flex items-center justify-center p-4 md:p-8">
        <section className="flex flex-col md:flex-row items-center justify-center gap-12 md:gap-16 max-w-6xl w-full">
          <div className="relative w-full max-w-md h-64 md:h-96 flex-1">
            <Image
              src="/movie_recommendation.jpg"
              alt="Filmes e Recomendações"
              layout="fill"
              objectFit="cover"
              priority
              className="rounded-lg shadow-lg"
            />
          </div>
          <div className="flex flex-1 flex-col space-y-6 text-center md:text-left">
            <h1 className="text-5xl md:text-6xl font-extrabold tracking-tight text-primary">
              Bem-vindo ao Aura
            </h1>
            <p className="text-xl md:text-2xl text-muted-foreground">
              Seu guia definitivo para o mundo do cinema. Descubra filmes
              baseados nos seus gostos, explore avaliações e encontre sua
              próxima obra-prima.
            </p>

            <Button
              size="lg"
              onClick={handleLoginClick}
              className="text-lg px-8 py-6 mt-4 md:self-start self-center" // Alinhamento do botão
            >
              Comece a Avaliar Agora!
            </Button>
          </div>
        </section>
      </main>

      <footer className="w-full border-t bg-background text-center py-4 text-sm text-muted-foreground">
        © {new Date().getFullYear()} Aura. Todos os direitos reservados.
      </footer>
    </div>
  );
}
