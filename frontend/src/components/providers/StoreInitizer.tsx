"use client";

// External Library
import { useEffect, useRef } from "react";

// Store
import { useAuthStore } from "@/modules/auth/store/authStore";

export function StoreInitializer() {
  const initialized = useRef(false);

  const { restoredSession } = useAuthStore();

  useEffect(() => {
    if (!initialized.current) {
      restoredSession();
      initialized.current = true;
    }
  }, []);

  return null;
}
