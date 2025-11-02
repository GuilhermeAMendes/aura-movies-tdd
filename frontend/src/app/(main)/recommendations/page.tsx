"use client";

// External Library
import { TriangleAlertIcon, InfoIcon } from "lucide-react";

// Hooks
import { useGetRecommendations } from "@/modules/recommendation/hooks/useGetRecommendations";

// Components
import { MovieCard } from "@/modules/movie/components/cards/MovieCard";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { MovieCardSkeleton } from "@/modules/movie/components/cards/MovieCardSkeleton";

export default function RecommendationsPage() {
  const { movies, isLoading, error } = useGetRecommendations();

  const renderContent = () => {
    if (isLoading) {
      return (
        <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4 md:gap-6">
          {Array(10)
            .fill(0)
            .map((_, index) => (
              <MovieCardSkeleton key={index} />
            ))}
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
    <div>
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
