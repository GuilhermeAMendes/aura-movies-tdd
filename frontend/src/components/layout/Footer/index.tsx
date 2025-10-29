export function Footer() {
  return (
    <footer className="w-full border-t bg-background text-center py-4 text-sm text-muted-foreground">
      © {new Date().getFullYear()} Aura. Todos os direitos reservados.
    </footer>
  );
}
