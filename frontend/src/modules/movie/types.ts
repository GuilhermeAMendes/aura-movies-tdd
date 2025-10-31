export type MovieId = {
  id: string;
};

export type Genre =
  | "ACTION"
  | "ADVENTURE"
  | "COMEDY"
  | "DRAMA"
  | "FANTASY"
  | "HORROR"
  | "ROMANCE"
  | "SCI_FI"
  | "THRILLER"
  | "DOCUMENTARY";

export interface Movie {
  movieId: MovieId;
  title: string;
  genre: Genre;
}

export interface GetMovieByIdPayload {
  id: string;
}

export interface GetMovieByIdResponse {
  movie: Movie;
}

export interface GetMoviesResponse {
  movies: Movie[];
}
