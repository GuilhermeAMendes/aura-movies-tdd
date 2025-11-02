"use client";

// External Library
import Link from "next/link";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { toast } from "sonner";

// Store
import { useAuthStore } from "@/modules/auth/store/authStore";

// Hooks
import { useNavigationHandler } from "@/shared/hooks/navigation/useNavigation";

// Type Guard
import { isLeft } from "@/shared/patterns/either";

// Components
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";

// Schema
const loginFormSchema = z.object({
  email: z.string().email({
    message: "Por favor, insira um e-mail válido.",
  }),
  password: z
    .string()
    .min(1, {
      message: "A senha é obrigatória.",
    })
    .max(255),
});

// Types
type LoginFormValues = z.infer<typeof loginFormSchema>;

export default function LoginPage() {
  const form = useForm<LoginFormValues>({
    resolver: zodResolver(loginFormSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  });

  const { login, isLoading } = useAuthStore();
  const { navigateTo } = useNavigationHandler();

  const onSubmit = async (payload: LoginFormValues) => {
    const { email, password } = payload;

    const result = await login({
      username: email,
      password,
    });

    if (isLeft(result)) {
      toast.error("Falha no Login", {
        description: result.value.message || "Credenciais inválidas.",
      });
      return;
    }

    toast.success("Login bem-sucedido!", {
      description: "Redirecionando para a página principal...",
    });

    navigateTo("/recommendations");
    return;
  };

  return (
    <>
      {/* Simplesmente não entendi porque a página de registro tem Header e essa não,
      tipo não sei de onde o /register ta tirando o Header, não tá no código, nem no
      layout.tsx, então, pra consistência, vou hardcodar aqui no /login */}
      <header className="sticky top-0 z-40 w-full border-b bg-background">
        <div className="flex h-16 items-center px-6">
          <Link href="/" className="flex items-center space-x-2">
            <span className="font-bold text-2xl tracking-tight text-primary">
              Aura
            </span>
          </Link>
        </div>
      </header>

      <div className="flex min-h-screen items-center justify-center bg-background p-6">
        <div className="flex flex-col lg:flex-row gap-6 w-full max-w-6xl">
          {/* Login Form */}
          <Card className="w-full lg:w-1/2">
            <CardHeader>
              <CardTitle className="text-2xl font-bold">Login</CardTitle>
              <CardDescription>
                Entre com suas credenciais para acessar o sistema de
                recomendação.
              </CardDescription>
            </CardHeader>
            <CardContent>
              <Form {...form}>
                <form
                  onSubmit={form.handleSubmit(onSubmit)}
                  className="space-y-6"
                >
                  <FormField
                    control={form.control}
                    name="email"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Email</FormLabel>
                        <FormControl>
                          <Input
                            placeholder="Entre com seu email"
                            {...field}
                            disabled={isLoading}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="password"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Senha</FormLabel>
                        <FormControl>
                          <Input
                            type="password"
                            placeholder="Entre com sua senha"
                            {...field}
                            disabled={isLoading}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <Button type="submit" className="w-full" disabled={isLoading}>
                    {isLoading ? "Entrando..." : "Entrar"}
                  </Button>

                  <p className="text-sm text-center">
                    Não possui uma conta?{" "}
                    <Link
                      href="/register"
                      className="font-medium text-primary underline"
                    >
                      Clique aqui para registrar-se.
                    </Link>
                  </p>
                </form>
              </Form>
            </CardContent>
          </Card>

          {/* Test Users Credentials */}
          <Card className="w-full lg:w-1/2">
            <CardHeader>
              <CardTitle className="text-2xl font-bold">
                Usuários de Teste
              </CardTitle>
              <CardDescription>
                Credenciais para testar o sistema
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4 max-h-[500px] overflow-y-auto">
                {/* Lucas */}
                <div className="border rounded-lg p-4">
                  <h3 className="font-semibold text-sm mb-2">
                    Lucas (G.O.A.T.)
                  </h3>
                  <p className="text-sm mb-1">
                    <span className="font-medium">Email:</span> lucas@gmail.com
                  </p>
                  <p className="text-sm mb-1">
                    <span className="font-medium">Senha:</span> senha
                  </p>
                  <p className="text-xs text-muted-foreground">
                    Gosta de filmes de Ação e Comédia. Avaliou positivamente The
                    Dark Knight, Mad Max, e The Shawshank Redemption.
                  </p>
                </div>

                {/* Jane */}
                <div className="border rounded-lg p-4">
                  <h3 className="font-semibold text-sm mb-2">Jane Smith</h3>
                  <p className="text-sm mb-1">
                    <span className="font-medium">Email:</span>{" "}
                    jane.smith@email.com
                  </p>
                  <p className="text-sm mb-1">
                    <span className="font-medium">Senha:</span> senha
                  </p>
                  <p className="text-xs text-muted-foreground">
                    Gosta de filmes de Drama e Romance. Adora The Shawshank
                    Redemption, Forrest Gump, The Notebook e Casablanca.
                  </p>
                </div>

                {/* Mike */}
                <div className="border rounded-lg p-4">
                  <h3 className="font-semibold text-sm mb-2">Mike Johnson</h3>
                  <p className="text-sm mb-1">
                    <span className="font-medium">Email:</span>{" "}
                    mike.johnson@email.com
                  </p>
                  <p className="text-sm mb-1">
                    <span className="font-medium">Senha:</span> senha
                  </p>
                  <p className="text-xs text-muted-foreground">
                    Gosta de filmes de Sci-Fi e Thriller. Avaliou positivamente
                    Blade Runner 2049, Interstellar, The Matrix e Gone Girl.
                  </p>
                </div>

                {/* Sarah */}
                <div className="border rounded-lg p-4">
                  <h3 className="font-semibold text-sm mb-2">Sarah Wilson</h3>
                  <p className="text-sm mb-1">
                    <span className="font-medium">Email:</span>{" "}
                    sarah.wilson@email.com
                  </p>
                  <p className="text-sm mb-1">
                    <span className="font-medium">Senha:</span> senha
                  </p>
                  <p className="text-xs text-muted-foreground">
                    Preferências mistas. Gosta de The Dark Knight, Superbad, The
                    Shawshank Redemption, The Conjuring e The Notebook.
                  </p>
                </div>

                {/* Alan Turing */}
                <div className="border rounded-lg p-4">
                  <h3 className="font-semibold text-sm mb-2">Alan Turing</h3>
                  <p className="text-sm mb-1">
                    <span className="font-medium">Email:</span> turing@gmail.com
                  </p>
                  <p className="text-sm mb-1">
                    <span className="font-medium">Senha:</span> senha
                  </p>
                  <p className="text-xs text-muted-foreground">
                    Usuário sem avaliações de filmes ainda.
                  </p>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </>
  );
}
