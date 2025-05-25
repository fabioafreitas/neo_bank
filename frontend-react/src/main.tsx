import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter, Route, Routes } from "react-router";
import "./global.css";

import Dashboard from "./pages/Dashboard";
import Transactions from "./pages/Transactions";
import UserProfile from "./pages/UserProfile";
import About from "./pages/About";
import Faq from "./pages/Faq";
import Home from "./pages/Home";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />}>
          <Route element={<div>main</div>} />
          <Route path="about" element={<About />} />
          <Route path="faq" element={<Faq />} />
        </Route>
        <Route path="dashboard" element={<Dashboard />}>
          <Route element={<div>main</div>} />
          <Route path="transactions" element={<Transactions />} />
          <Route path="user-profile" element={<UserProfile />} />
        </Route>
      </Routes>
    </BrowserRouter>
  </StrictMode>
);
