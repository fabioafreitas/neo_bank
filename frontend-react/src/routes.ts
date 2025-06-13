// data structure for defining routes and prevent hardcoding paths in the application

export const ROUTES = {
  // Home-Layout Routes
  HOME: {
    path: "/",
    generate: () => "/",
  },
  ABOUT: {
    path: "about",
    generate: () => "about",
  },
  FAQ: {
    path: "faq",
    generate: () => "faq",
  },
  SERVICES: {
    path: "services",
    generate: () => "services",
  },
  SUPPORT: {
    path: "support",
    generate: () => "support",
  },

  // Auth-Layout Routes
  REGISTER: {
    path: "register",
    generate: () => "register",
  },
  LOGIN: {
    path: "login",
    generate: () => "login",
  },
  FORGOT_PASSWORD: {
    path: "forgot-password",
    generate: () => "forgot-password",
  },

  // Bank-Layout Routes
  DASHBOARD: {
    path: "dashboard",
    generate: () => "dashboard",
  },
  TRANSACTIONS: {
    path: "transactions",
    generate: () => "transactions",
  },
  USER_PROFILE: {
    path: "user-profile",
    generate: () => "user-profile",
  },
};
