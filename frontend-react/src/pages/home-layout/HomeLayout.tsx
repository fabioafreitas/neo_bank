import { Outlet } from "react-router";
import Footer from "../../shared/components/Footer";
import Header from "../../shared/components/Header";

export default function HomeLayout() {
  return (
    <>
      <Header />
      <Outlet />
      <Footer />
    </>
  );
}
