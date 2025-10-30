export interface JWTClaims {
  exp: number;
  sub: string;
  iat: number;
}

export interface User {
  name: string;
  lastname: string;
  email: string;
  password: string;
}

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
