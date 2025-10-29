"use client";

// Hooks
import { useGetRatings } from "@/modules/rating/hooks/useGetRatings";

// Types
import type { Rating } from "@/modules/rating/types";

// Helper
import { formatDate } from "@/shared/utils/helper/formatter/dateFormatter";

// Components
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardDescription,
} from "@/components/ui/card";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { Loader2, TriangleAlertIcon, StarIcon, InfoIcon } from "lucide-react";

function RatingItem({ rating }: { rating: Rating }) {
  return (
    <div className="flex flex-col sm:flex-row justify-between sm:items-center p-4 border-b last:border-b-0 hover:bg-muted/50 transition-colors">
      <div className="mb-2 sm:mb-0">
        <p className="font-semibold text-lg">Filme ID:</p>
        <p className="text-sm text-muted-foreground font-mono">
          {rating.movieId.id}
        </p>
      </div>
      <div className="flex flex-col items-start sm:items-end">
        <div className="flex items-center gap-1">
          <StarIcon className="w-5 h-5 text-yellow-500 fill-yellow-500" />
          <span className="font-bold text-lg">{rating.grade}</span>
        </div>
        <p className="text-sm text-muted-foreground">
          Avaliado em: {formatDate(rating.lastGradedAt)}
        </p>
      </div>
    </div>
  );
}

export default function ProfilePage() {
  const { ratings, isLoading, error } = useGetRatings();

  const renderContent = () => {
    if (isLoading) {
      return (
        <div className="flex justify-center items-center h-48">
          <Loader2 className="h-10 w-10 animate-spin text-primary" />
          <p className="ml-3 text-muted-foreground">Buscando avaliações...</p>
        </div>
      );
    }

    if (error) {
      return (
        <Alert variant="destructive">
          <TriangleAlertIcon className="h-4 w-4" />
          <AlertTitle>Erro ao Carregar</AlertTitle>
          <AlertDescription>
            {error.message || "Não foi possível buscar suas avaliações."}
          </AlertDescription>
        </Alert>
      );
    }

    // Estado Vazio (sem avaliações)
    if (ratings.length === 0) {
      return (
        <Alert>
          <InfoIcon className="h-4 w-4" />
          <AlertTitle>Nenhuma avaliação encontrada</AlertTitle>
          <AlertDescription>
            Parece que você ainda não avaliou nenhum filme. Comece a avaliar
            para ver seu histórico aqui!
          </AlertDescription>
        </Alert>
      );
    }

    return (
      <div className="flex flex-col">
        {ratings.map((rating) => (
          <RatingItem key={rating.movieId.id} rating={rating} />
        ))}
      </div>
    );
  };

  return (
    <div className="container mx-auto p-4 md:p-8">
      <Card className="w-full max-w-4xl mx-auto">
        <CardHeader>
          <CardTitle className="text-3xl font-bold">
            Minhas Avaliações
          </CardTitle>
          <CardDescription>
            Aqui está o histórico de todos os filmes que você já avaliou.
          </CardDescription>
        </CardHeader>
        <CardContent>{renderContent()}</CardContent>
      </Card>
    </div>
  );
}
