"use client";

// External Library
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
    <div className="flex min-h-screen items-center justify-center bg-background">
      <Card className="w-full max-w-md">
        <CardHeader>
          <CardTitle className="text-2xl font-bold">Login</CardTitle>
          <CardDescription>
            Entre com suas credenciais para acessar o sistema de recomendação.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
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
            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  );
}
