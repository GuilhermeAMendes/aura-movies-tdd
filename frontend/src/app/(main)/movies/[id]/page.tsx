"use client";

import { useParams } from "next/navigation";
import { Loader2, StarIcon, TriangleAlertIcon } from "lucide-react";

// Components
import { Badge } from "@/components/ui/badge";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { GenericSynopsis } from "@/modules/movie/components/text/GenericSynopsis";

// Hooks
import { useGetMovieById } from "@/modules/movie/hooks/useGetMovieById";

// Utils
import { getIconForGenre } from "@/modules/movie/utils/genreIcons";
import genreLabelMap from "@/modules/movie/utils/formatterLabel";

export default function MovieDetailPage() {
  const params = useParams();
  const id = params.id as string;

  const { movie, rating, isLoading, error } = useGetMovieById({ id });

  const renderContent = () => {
    if (isLoading) {
      return (
        <div className="flex-1 flex justify-center items-center">
          <Loader2 className="h-12 w-12 animate-spin text-primary" />
        </div>
      );
    }

    if (error) {
      return (
        <div className="flex-1 flex justify-center items-center p-4">
          <Alert variant="destructive" className="max-w-lg">
            <TriangleAlertIcon className="h-4 w-4" />
            <AlertTitle>Erro ao Carregar Filme</AlertTitle>
            <AlertDescription>
              {error.message ||
                "Não foi possível buscar os detalhes deste filme."}
            </AlertDescription>
          </Alert>
        </div>
      );
    }

    if (!movie) {
      return (
        <div className="flex-1 flex justify-center items-center p-4">
          <Alert variant="destructive" className="max-w-lg">
            <TriangleAlertIcon className="h-4 w-4" />
            <AlertTitle>Filme não encontrado</AlertTitle>
          </Alert>
        </div>
      );
    }

    const IconComponent = getIconForGenre(movie.genre);
    const labelFormatter = genreLabelMap[movie.genre];

    return (
      <div>
        <div className="flex flex-col md:flex-row gap-8 md:gap-12">
          <div className="flex-none w-full md:w-1/3 flex items-center justify-center p-8 bg-muted rounded-lg aspect-square md:aspect-[3/4] shadow-inner">
            <IconComponent className="w-48 h-48 text-gray-700" />
          </div>
          <div className="flex-1 flex flex-col gap-8">
            <div className="space-y-4">
              <Badge
                variant="secondary"
                className="capitalize bg-blue-300 text-white"
              >
                {labelFormatter}
              </Badge>
              <h1 className="text-4xl md:text-5xl font-bold tracking-tight">
                {movie.title}
              </h1>
              <GenericSynopsis movie={movie} />
              {rating ? (
                <div className="flex items-center gap-1">
                  <StarIcon className="w-5 h-5 text-yellow-500 fill-yellow-500" />
                  <span className="font-bold text-lg">{rating.grade}</span>
                </div>
              ) : (
                <div className="flex items-center gap-1">
                  <StarIcon className="w-5 h-5 text-gray-400 fill-gratext-gray-400" />
                  <span className="font-bold text-lg">Filme não avaliado</span>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    );
  };

  return <div className="flex-1 flex flex-col">{renderContent()}</div>;
}
