import { Outlet } from "react-router";
export default function Dashboard() {
  return (
    <div className="flex min-h-screen">
      <nav className="w-64 bg-gray-800 text-white p-4 space-y-4">
        <p>link 1</p>
        <p>link 2</p>
        <p>link 3</p>
      </nav>
      <Outlet></Outlet>
    </div>
  );
}
