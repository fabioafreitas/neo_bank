import logoUrl from "../../assets/logo.svg";

export default function Login() {
  return (
    <div className="flex items-center justify-center">
      <div className="rounded-lg shadow-lg p-8 w-full max-w-md">
        <img src={logoUrl} alt="My Brand" className="w-8 h-auto" />
        <header className="mb-4">my header</header>
        <main className="mb-4">my content</main>
        <footer>my footer</footer>
      </div>
    </div>
  );
}
