"use client";

// External Library
import Link from "next/link";

// Components
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";

// Types
import type { Genre, Movie } from "@/modules/movie/types";

// Utils
import { getIconForGenre } from "@/modules/movie/utils/genreIcons";
import genreLabelMap from "@/modules/movie/utils/formatterLabel";

interface MovieCardProps {
  movie: Movie;
}

export function MovieCard({ movie }: MovieCardProps) {
  const IconComponent = getIconForGenre(movie.genre);
  const getGenreLabel = genreLabelMap[movie.genre];

  return (
    <Link href={`/movies/${movie.movieId.id}`} passHref>
      <Card className="h-full w-full overflow-hidden transition-transform duration-200 hover:shadow-lg hover:-translate-y-1 flex flex-col justify-between">
        <CardHeader className="pb-4">
          <CardTitle className="text-lg font-bold line-clamp-2 h-[3.25rem]">
            {movie.title}
          </CardTitle>
        </CardHeader>

        <CardContent className="flex-1 flex items-center justify-center text-muted-foreground p-6">
          <IconComponent className="w-20 h-20 text-gray-700" />
        </CardContent>

        <CardFooter className="p-4 pt-2">
          <Badge
            variant="secondary"
            className="capitalize bg-blue-300 text-white"
          >
            {getGenreLabel}
          </Badge>
        </CardFooter>
      </Card>
    </Link>
  );
}
