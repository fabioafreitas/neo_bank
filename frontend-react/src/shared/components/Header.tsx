import { NavLink } from "react-router";
import LogoIcon from "./LogoIcon";
import { ButtonPrimary } from "./ButtonPrimary";
import { ButtonSecondary } from "./ButtonSecondary";
import { LanguageSelector } from "./LanguageSelector";
import { ROUTES } from "../../routes";

export default function Header() {
  return (
    <header className="flex bg-slate-900 text-white text-center justify-between px-6">
      <LogoIcon />
      <nav className="flex justify-center items-center p-4 gap-16">
        <NavLink
          to={ROUTES.ABOUT.path}
          className={({ isActive }) => (isActive ? "active" : "")}
        >
          Sobre Nós
        </NavLink>
        <NavLink
          to={ROUTES.SERVICES.path}
          className={({ isActive }) => (isActive ? "active" : "")}
        >
          Nossos Serviços
        </NavLink>
        <NavLink
          to={ROUTES.SUPPORT.path}
          className={({ isActive }) => (isActive ? "active" : "")}
        >
          Suporte
        </NavLink>
        <NavLink
          to={ROUTES.FAQ.path}
          className={({ isActive }) => (isActive ? "active" : "")}
        >
          Dúvidas
        </NavLink>
      </nav>
      <div className="flex items-center">
        <ButtonPrimary title="Abra sua conta" />
        <ButtonSecondary title="Abra sua conta" />
        <LanguageSelector />
      </div>
    </header>
  );
}
