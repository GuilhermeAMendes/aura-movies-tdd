// Components
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
} from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";

export function MovieCardSkeleton() {
  return (
    <Card className="h-full w-full overflow-hidden flex flex-col justify-between">
      <CardHeader className="pb-4">
        <Skeleton className="h-6 w-3/4" />
        <Skeleton className="h-6 w-1/2" />
      </CardHeader>

      <CardContent className="flex-1 flex items-center justify-center p-6">
        <Skeleton className="h-32 w-32 rounded-full" />
      </CardContent>

      <CardFooter className="p-4 pt-2">
        <Skeleton className="h-6 w-1/4" />
      </CardFooter>
    </Card>
  );
}
