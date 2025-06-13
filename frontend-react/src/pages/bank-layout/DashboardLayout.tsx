import { Outlet } from "react-router";
import LeftNav from "../../shared/components/LeftNav";

export default function DashboardLayout() {
  return (
    <div className="flex min-h-screen">
      <LeftNav />
      <Outlet></Outlet>
    </div>
  );
}
