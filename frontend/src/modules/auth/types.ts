// Types
import { User } from "@/types/user";

export interface LoginPayload {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
}

export interface RegisterPayload extends User {}

export interface RegisterResponse {
  id: string;
}
