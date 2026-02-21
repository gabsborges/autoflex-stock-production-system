
import { Link, useLocation } from "react-router-dom";
import {
  HiCube,
  HiOutlineBeaker,
  HiOutlineClipboardList,
  HiOutlineLogout,
  HiOutlineViewGrid,
} from "react-icons/hi";
import type { Dispatch, SetStateAction } from "react";

interface SidebarProps {
  sidebarOpen: boolean;
  setSidebarOpen: Dispatch<SetStateAction<boolean>>;
}

const menuItems = [
  { name: "Dashboard", path: "/dashboard", icon: HiOutlineViewGrid },
  { name: "Products", path: "/products", icon: HiCube },
  { name: "Raw Materials", path: "/raw-materials", icon: HiOutlineClipboardList },
  { name: "Production Suggestion", path: "/production-suggestion", icon: HiOutlineBeaker },
];

export default function Sidebar({ sidebarOpen, setSidebarOpen }: SidebarProps) {
  const location = useLocation();

  return (
    <>

      {sidebarOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 z-30 md:hidden"
          onClick={() => setSidebarOpen(false)}
        />
      )}


      <aside
        className={`fixed inset-y-0 left-0 w-64 bg-gray-900 text-gray-200 flex flex-col justify-between shadow-lg z-40 transform transition-transform duration-300
          ${sidebarOpen ? "translate-x-0" : "-translate-x-full"} md:translate-x-0 md:static md:inset-auto`}
      >
        <div>
          <div className="h-16 flex items-center px-6 border-b border-gray-700">
            <h1 className="text-xl font-bold text-white">Autoflex</h1>
          </div>

          <nav className="mt-6 px-4 space-y-1">
            {menuItems.map(({ name, path, icon: Icon }) => {
              const active = location.pathname === path;
              return (
                <Link
                  key={path}
                  to={path}
                  onClick={() => setSidebarOpen(false)}
                  className={`flex items-center gap-3 px-3 py-2 rounded-md text-sm font-medium transition-colors
                    ${active ? "bg-gray-700 text-white" : "hover:bg-gray-800 hover:text-white"}`}
                >
                  <Icon className="w-5 h-5" />
                  {name}
                </Link>
              );
            })}
          </nav>
        </div>

        <div className="px-4 py-4 border-t border-gray-700">
          <button
            onClick={() => alert("Logout")}
            className="flex items-center gap-3 px-3 py-2 rounded-md text-sm font-medium text-gray-400 hover:bg-gray-800 hover:text-white w-full"
          >
            <HiOutlineLogout className="w-5 h-5" />
            Sign Out
          </button>
        </div>
      </aside>
    </>
  );
}
