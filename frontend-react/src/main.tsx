import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter, Route, Routes } from "react-router";
import "./global.css";

import { ROUTES } from "./routes";

import HomeLayout from "./pages/home-layout/HomeLayout";
import AboutUs from "./pages/home-layout/AboutUs";
import Faq from "./pages/home-layout/Faq";
import Dashboard from "./pages/bank-layout/Dashboard";
import Transactions from "./pages/bank-layout/Transactions";
import UserProfile from "./pages/bank-layout/UserProfile";
import Home from "./pages/home-layout/Home";
import Support from "./pages/home-layout/Support";
import Services from "./pages/home-layout/Services";
import DashboardLayout from "./pages/bank-layout/DashboardLayout";
import AuthLayout from "./pages/auth-layout/AuthLayout";
import Login from "./pages/auth-layout/Login";
import Register from "./pages/auth-layout/Register";
import ForgotPassword from "./pages/auth-layout/ForgotPassword";
import ColorPalettePage from "./pages/ColorPalettePage";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/colors" element={<ColorPalettePage />} />

        <Route path={ROUTES.HOME.path} element={<HomeLayout />}>
          <Route index element={<Home />} />
          <Route path={ROUTES.ABOUT.path} element={<AboutUs />} />
          <Route path={ROUTES.SERVICES.path} element={<Services />} />
          <Route path={ROUTES.SUPPORT.path} element={<Support />} />
          <Route path={ROUTES.FAQ.path} element={<Faq />} />
        </Route>

        <Route path={ROUTES.DASHBOARD.path} element={<DashboardLayout />}>
          <Route index element={<Dashboard />} />
          <Route path={ROUTES.TRANSACTIONS.path} element={<Transactions />} />
          <Route path={ROUTES.USER_PROFILE.path} element={<UserProfile />} />
        </Route>

        <Route element={<AuthLayout />}>
          <Route path={ROUTES.LOGIN.path} element={<Login />} />
          <Route path={ROUTES.REGISTER.path} element={<Register />} />
          <Route
            path={ROUTES.FORGOT_PASSWORD.path}
            element={<ForgotPassword />}
          />
        </Route>
      </Routes>
    </BrowserRouter>
  </StrictMode>
);
