// Components
import { AppNavigation } from "@/components/layout/AppNavigation"; // 1. Importe o novo componente

export default function MainAppLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="container mx-auto p-4 md:p-8">
      <AppNavigation />
      {children}
    </div>
  );
}
