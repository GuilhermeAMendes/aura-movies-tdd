// Local: src/app/(main)/recommendations/page.tsx
"use client";

// Hooks
import { useGetRecommendations } from "@/modules/recommendation/hooks/useGetRecommendations";

// Components
import { MovieCard } from "@/modules/movie/components/cards/MovieCard";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { Loader2, TriangleAlertIcon, InfoIcon } from "lucide-react";

export default function RecommendationsPage() {
  const { movies, isLoading, error } = useGetRecommendations();

  const renderContent = () => {
    if (isLoading) {
      return (
        <div className="flex justify-center items-center h-64">
          <Loader2 className="h-10 w-10 animate-spin text-primary" />
          <p className="ml-3 text-muted-foreground">
            Buscando suas recomendações...
          </p>
        </div>
      );
    }

    if (error) {
      return (
        <Alert variant="destructive">
          <TriangleAlertIcon className="h-4 w-4" />
          <AlertTitle>Erro ao Carregar Recomendações</AlertTitle>
          <AlertDescription>
            {error.message || "Não foi possível buscar suas recomendações."}
          </AlertDescription>
        </Alert>
      );
    }

    if (movies.length === 0) {
      return (
        <Alert>
          <InfoIcon className="h-4 w-4" />
          <AlertTitle>Nenhuma recomendação encontrada</AlertTitle>
          <AlertDescription>
            Parece que ainda não temos recomendações para você. Tente avaliar
            mais filmes na sua tela de perfil!
          </AlertDescription>
        </Alert>
      );
    }

    return (
      <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4 md:gap-6">
        {movies.map((movie) => (
          <MovieCard key={movie.movieId.id} movie={movie} />
        ))}
      </div>
    );
  };

  return (
    <div className="container mx-auto p-4 md:p-8">
      <div className="mb-6">
        <h1 className="text-3xl font-bold tracking-tight">
          Recomendado para Você
        </h1>
        <p className="text-muted-foreground">
          Filmes que selecionamos com base nas suas avaliações.
        </p>
      </div>

      <div>{renderContent()}</div>
    </div>
  );
}
