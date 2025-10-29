// External Library
import type { LucideIcon } from "lucide-react";
import {
  Bomb,
  Compass,
  Smile,
  VenetianMask,
  Wand2,
  Ghost,
  Heart,
  Rocket,
  Crosshair,
  Camera,
  FilmIcon,
} from "lucide-react";

// Types
import type { Genre } from "../types";

const genreIconMap: Record<Genre, LucideIcon> = {
  ACTION: Bomb,
  ADVENTURE: Compass,
  COMEDY: Smile,
  DRAMA: VenetianMask,
  FANTASY: Wand2,
  HORROR: Ghost,
  ROMANCE: Heart,
  SCI_FI: Rocket,
  THRILLER: Crosshair,
  DOCUMENTARY: Camera,
};

export const getIconForGenre = (genre: Genre): LucideIcon => {
  return genreIconMap[genre] || FilmIcon;
};
