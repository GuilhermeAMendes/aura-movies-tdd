"use client";

// External Library
import { TriangleAlertIcon, InfoIcon } from "lucide-react";

// Hooks
import { useGetMovies } from "@/modules/movie/hooks/useGetMovies";

// Components
import { MovieCard } from "@/modules/movie/components/cards/MovieCard";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { MovieCardSkeleton } from "@/modules/movie/components/cards/MovieCardSkeleton";

export default function CatalogPage() {
  const { movies, isLoading, error } = useGetMovies();

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
          <AlertTitle>Erro ao Carregar Catálogo</AlertTitle>
          <AlertDescription>
            {error.message || "Não foi possível buscar os filmes."}
          </AlertDescription>
        </Alert>
      );
    }

    if (movies.length === 0) {
      return (
        <Alert>
          <InfoIcon className="h-4 w-4" />
          <AlertTitle>Catálogo Vazio</AlertTitle>
          <AlertDescription>
            Não há filmes no catálogo no momento.
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
        <h1 className="text-3xl font-bold tracking-tight">Catálogo Completo</h1>
        <p className="text-muted-foreground">
          Explore todos os filmes disponíveis no Aura.
        </p>
      </div>

      <div>{renderContent()}</div>
    </div>
  );
}
