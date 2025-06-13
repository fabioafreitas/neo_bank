import LogoIcon from "./LogoIcon";

export default function Footer() {
  return (
    <footer className="flex bg-slate-900 text-white text-center justify-between px-6 py-4">
      <LogoIcon />
      <span>Desenvolvido por Fábio Alves</span>
      <div className="flex gap-4">
        <a href="https://www.linkedin.com/in/fabioalvesfrei/">
          <img
            className="max-w-6"
            src="/icons/linkedin.png"
            alt="linkedin icon"
          />
        </a>{" "}
        <a href="https://github.com/fabioafreitas/">
          <img className="max-w-6" src="/icons/github.png" alt="github icon" />
        </a>
      </div>
    </footer>
  );
}
