"use client";

// External Library
import { useState, useEffect } from "react";
import { StarIcon, PencilIcon, SaveIcon, XIcon, Loader2 } from "lucide-react";

// Components
import { Button } from "@/components/ui/button";

// Hooks
import { usePostRate } from "../../../hooks/usePostRate";
import { usePatchRate } from "../../../hooks/usePatchRate";

// Types
import { Rating } from "@/modules/rating/types";
import { RemoveRate } from "../../buttons/RemoveRate";

interface MovieRatingFormProps {
  movieId: string;
  existingRating: Rating | null;
  onSuccess: () => void;
}

const GRADES = ["0", "1", "2", "3", "4", "5"];

export function MovieRatingForm({
  movieId,
  existingRating,
  onSuccess,
}: MovieRatingFormProps) {
  const [isEditing, setIsEditing] = useState(!existingRating);
  const [selectedGrade, setSelectedGrade] = useState<string>(
    existingRating?.grade ?? "0"
  );

  const { postRate, isLoading: isPosting } = usePostRate();
  const { patchRate, isLoading: isPatching } = usePatchRate();
  const isLoading = isPosting || isPatching;

  useEffect(() => {
    setIsEditing(!existingRating);
    setSelectedGrade(existingRating?.grade ?? "0");
  }, [existingRating]);

  const handleSave = async () => {
    if (existingRating) {
      if (selectedGrade === existingRating.grade) {
        setIsEditing(false);
        return;
      }

      const saveSucceeded = await patchRate({
        movieId: movieId,
        grade: selectedGrade,
      });

      if (saveSucceeded) {
        onSuccess();
        setIsEditing(false);
      }
    } else {
      const saveSucceeded = await postRate({
        movieId: { id: movieId },
        grade: selectedGrade,
      });

      if (saveSucceeded) {
        onSuccess();
        setIsEditing(false);
      }
    }
  };

  const handleCancel = () => {
    setSelectedGrade(existingRating?.grade ?? "0");
    setIsEditing(false);
  };

  if (isEditing) {
    return (
      <div className="flex flex-col items-center gap-4 p-6 bg-muted/50 rounded-lg shadow-inner">
        <h3 className="text-xl font-semibold text-center">
          {existingRating ? "Altere sua nota" : "Avalie este filme"}
        </h3>
        <div className="flex flex-wrap justify-center gap-2">
          {GRADES.map((grade) => (
            <Button
              key={grade}
              variant={selectedGrade === grade ? "default" : "outline"}
              size="icon"
              onClick={() => setSelectedGrade(grade)}
              disabled={isLoading}
              className="text-lg font-bold w-12 h-12"
            >
              {grade}
            </Button>
          ))}
        </div>
        <div className="flex gap-4 mt-4 w-full justify-center">
          {!!existingRating && (
            <Button variant="ghost" onClick={handleCancel} disabled={isLoading}>
              <XIcon className="mr-2 h-4 w-4" /> Cancelar
            </Button>
          )}
          <Button
            onClick={handleSave}
            disabled={isLoading}
            className="w-full max-w-xs"
          >
            {isLoading ? (
              <Loader2 className="mr-2 h-4 w-4 animate-spin" />
            ) : (
              <SaveIcon className="mr-2 h-4 w-4" />
            )}
            Salvar
          </Button>
        </div>
      </div>
    );
  }

  return (
    <div className="flex items-center justify-between p-4 bg-muted/50 rounded-lg">
      <div className="flex items-center gap-2">
        <StarIcon className="w-6 h-6 text-yellow-500 fill-yellow-500" />
        <span className="font-bold text-xl">
          Sua nota: {existingRating?.grade}
        </span>
      </div>
      <div>
        <Button
          variant="outline"
          size="icon"
          onClick={() => setIsEditing(true)}
        >
          <PencilIcon className="h-4 w-4" />
        </Button>
        <RemoveRate id={movieId} onSuccess={onSuccess} />
      </div>
    </div>
  );
}
