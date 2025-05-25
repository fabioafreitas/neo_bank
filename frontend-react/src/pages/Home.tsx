import { Outlet } from "react-router";

export default function Home() {
  return (
    <div>
      <header className="flex bg-slate-900 text-white text-center justify-between px-6">
        <div className="flex items-center">
          <p>Logo</p>
        </div>
        <nav className="flex justify-center items-center p-4 gap-4">
          <p>Item 1</p>
          <p>Item 2</p>
          <p>Item 3</p>
        </nav>
        <div className="flex items-center">
          <p>Login</p>
        </div>
      </header>
      <div className="grid grid-cols-3 gap-4">
        <div className="bg-gray-200 p-4">1</div>
        <div className="bg-gray-200 p-4">2</div>
        <div className="bg-gray-200 p-4">3</div>
        <div className="bg-gray-200 p-4">4</div>
      </div>

      <Outlet />
      <footer></footer>
    </div>
  );
}
