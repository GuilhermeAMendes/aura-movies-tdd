// Components
import { Skeleton } from "@/components/ui/skeleton";

export function RatingItemSkeleton() {
  return (
    <div className="flex flex-col sm:flex-row justify-between sm:items-center p-4 border-b last:border-b-0">
      <div className="space-y-2 mb-2 sm:mb-0">
        <Skeleton className="h-5 w-20" />
        <Skeleton className="h-4 w-48" />
      </div>
      <div className="flex items-center gap-4">
        <div className="flex flex-col items-start sm:items-end space-y-2">
          <Skeleton className="h-6 w-16" />
          <Skeleton className="h-4 w-28" />
        </div>
        <Skeleton className="h-10 w-10 rounded-md" />
      </div>
    </div>
  );
}
