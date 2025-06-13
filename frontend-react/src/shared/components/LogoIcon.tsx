import { NavLink } from "react-router";
import logoUrl from "../../assets/logo.svg";
import { ROUTES } from "../../routes";

export default function LogoIcon() {
  return (
    <NavLink to={ROUTES.HOME.path} end className="flex items-center">
      <img src={logoUrl} alt="My Brand" className="w-8 h-auto" />
    </NavLink>
  );
}
