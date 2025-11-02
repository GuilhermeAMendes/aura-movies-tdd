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
const registerFormSchema = z.object({
  name: z
    .string()
    .min(1, {
      message: "O nome é obrigatório",
    })
    .max(255),
  lastname: z
    .string()
    .min(1, {
      message: "O sobrenome é obrigatório",
    })
    .max(255),
  email: z.string().email({
    message: "Por favor, insira um e-mail válido.",
  }),
  password: z
    .string()
    .min(1, {
      message: "A senha é obrigatória",
    })
    .max(255),
});

type RegisterFormValues = z.infer<typeof registerFormSchema>;

export default function RegisterPage() {
  const form = useForm<RegisterFormValues>({
    resolver: zodResolver(registerFormSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  });

  const { register, isLoading } = useAuthStore();
  const { navigateTo } = useNavigationHandler();

  const onSubmit = async (payload: RegisterFormValues) => {
    const { name, lastname, email, password } = payload;

    const result = await register({
      name: name,
      lastname: lastname,
      email: email,
      password,
    });

    if (isLeft(result)) {
      toast.error("Falha ao registrar usuário", {
        description: result.value.message || "Erro interno no servidor.",
      });
      return;
    }

    toast.success("Login bem-sucedido!", {
      description: "Redirecionando para a página de login...",
    });

    navigateTo("/login");
    return;
  };

  return <div>PLACEHOLDER</div>;
}
