import { Outlet } from "react-router";

export default function AuthLayout() {
  return (
    <div className="min-h-screen flex bg-slate-900">
      {/* Left side banner image */}
      <div className="hidden md:flex w-1/2 items-center justify-center bg-gradient-to-br from-blue-800 to-blue-400">
        <img
          src="/banner-image.png"
          alt="Banner"
          className="max-w-full max-h-[80vh] object-contain"
        />
      </div>
      {/* Right side outlet */}
      <div className="flex flex-1 items-center justify-center">
        <div className="bg-white p-8 rounded shadow-md w-full max-w-md">
          <Outlet />
        </div>
      </div>
    </div>
  );
}

// {/* <div className="bg-slate-900">a</div>
//       <Outlet /> */}
