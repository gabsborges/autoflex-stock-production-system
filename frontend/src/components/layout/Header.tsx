import { useLocation } from "react-router-dom";
import { HiOutlineMenu } from "react-icons/hi";
import { useMemo, type Dispatch, type SetStateAction } from "react";

interface HeaderProps {
  setSidebarOpen: Dispatch<SetStateAction<boolean>>;
}

// Mapeamento das rotas para título do header
const routeTitles: Record<string, string> = {
  "/": "Dashboard",
  "/dashboard": "Dashboard",
  "/products": "Products",
  "/products/new": "New Product",
  "/products/:id/edit": "Edit Product",
  "/raw-materials": "Raw Materials",
  "/raw-materials/:id/edit": "Edit Raw Material",
  "/production-simulation": "Production Simulation",
};

function getTitle(pathname: string) {

  for (const route in routeTitles) {

    const pattern = "^" + route.replace(/:\w+/g, "[^/]+") + "$";
    const regex = new RegExp(pattern);
    if (regex.test(pathname)) {
      return routeTitles[route];
    }
  }
  return "Dashboard";
}

export default function Header({ setSidebarOpen }: HeaderProps) {
  const location = useLocation();

  const title = useMemo(() => getTitle(location.pathname), [location.pathname]);

  return (
    <header className="flex items-center justify-between bg-white shadow px-6 h-16">
      {/* Botão hamburger mobile */}
      <button
        onClick={() => setSidebarOpen(true)}
        className="md:hidden p-2 rounded-md hover:bg-gray-100 text-gray-700"
        aria-label="Open sidebar"
      >
        <HiOutlineMenu className="w-6 h-6" />
      </button>

      <div className="flex-1 text-lg font-semibold text-gray-800">{title}</div>

      <div className="flex items-center gap-4">
        <div className="flex items-center gap-2 cursor-pointer select-none">
          <div className="w-8 h-8 rounded-full bg-gray-300 flex items-center justify-center text-gray-700 font-bold">
            A
          </div>
          <span className="text-gray-700 font-medium">Admin</span>
        </div>
      </div>
    </header>
  );
}
