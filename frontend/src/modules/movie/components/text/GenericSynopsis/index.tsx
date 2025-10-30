// Utils
import genreLabelMap from "@/modules/movie/utils/formatterLabel";

// Types
import type { Movie } from "@/modules/movie/types";

export const GenericSynopsis = ({ movie }: { movie: Movie }) => {
  const { title, genre } = movie;

  const formattedGenre = genreLabelMap[genre];

  return (
    <div className="border-l-4 border-primary pl-4 space-y-3">
      <p className="text-lg text-muted-foreground italic">
        Embarque em uma jornada inesquecível com <strong>"{title}"</strong>.
        Este filme, uma obra-prima do gênero de {formattedGenre}, explora temas
        profundos e personagens cativantes.
      </p>
      <p className="text-lg text-muted-foreground italic">
        Prepare-se para ser transportado para um mundo de emoções, onde cada
        cena é cuidadosamente elaborada para prender sua atenção. De{" "}
        <strong>"{title}"</strong>, espere nada menos que uma experiência
        cinematográfica que ficará com você por muito tempo.
      </p>
    </div>
  );
};
