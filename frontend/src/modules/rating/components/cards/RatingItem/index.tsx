// External Library
import { StarIcon } from "lucide-react";

// Components
import { RemoveRate } from "../../buttons/RemoveRate";

// Types
import { Rating } from "@/modules/rating/types";

// Utils
import { formatDate } from "@/shared/utils/helper/formatter/dateFormatter";

export default function RatingItem({ rating }: { rating: Rating }) {
  return (
    <div className="flex flex-col sm:flex-row justify-between sm:items-center p-4 border-b last:border-b-0 hover:bg-muted/50 transition-colors">
      <div className="mb-2 sm:mb-0">
        <p className="font-semibold text-lg">{rating.title}</p>
        <p className="text-sm text-muted-foreground font-mono">
          {rating.movieId.id}
        </p>
      </div>
      <div className="flex items-center gap-4">
        <div className="flex flex-col items-start sm:items-end">
          <div className="flex items-center gap-1">
            <StarIcon className="w-5 h-5 text-yellow-500 fill-yellow-500" />
            <span className="font-bold text-lg">{rating.grade}</span>
          </div>
          <p className="text-sm text-muted-foreground">
            Avaliado em: {formatDate(rating.lastGradedAt)}
          </p>
        </div>
        <RemoveRate id={rating.movieId.id} />
      </div>
    </div>
  );
}
