import type { ReactNode } from "react";

interface StatCardProps {
  icon: ReactNode;
  title: string;
  value: string | number;
  iconBgColor: string; // ex: "bg-blue-100"
  iconColor: string; // ex: "text-blue-600"
}

export default function StatCard({ icon, title, value, iconBgColor, iconColor }: StatCardProps) {
  return (
    <div className="flex items-center p-4 bg-white rounded-lg shadow-sm border border-gray-200 hover:bg-gray-50 transition">
      <div className={`p-3 rounded-lg ${iconBgColor} ${iconColor} mr-4 flex items-center justify-center text-2xl`}>
        {icon}
      </div>
      <div>
        <p className="text-sm font-medium text-gray-600">{title}</p>
        <p className="text-lg font-bold text-gray-900">{value}</p>
      </div>
    </div>
  );
}
