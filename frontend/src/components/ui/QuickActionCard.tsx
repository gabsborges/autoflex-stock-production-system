import type { ReactNode } from "react";
import { Link } from "react-router-dom";

interface QuickActionCardProps {
  to: string;
  icon: ReactNode;
  title: string;
  subtitle: string;
  iconBgColor: string;
  iconColor: string;
}

export default function QuickActionCard({
  to,
  icon,
  title,
  subtitle,
  iconBgColor,
  iconColor,
}: QuickActionCardProps) {
  return (
    <Link
      to={to}
      className="flex items-center justify-between p-4 bg-white rounded-lg shadow-sm border border-gray-200 hover:bg-gray-50 transition"
    >
      <div className="flex items-center gap-4">
        <div className={`p-3 rounded-lg ${iconBgColor} ${iconColor} text-2xl flex items-center justify-center`}>
          {icon}
        </div>
        <div>
          <p className="font-semibold text-gray-900">{title}</p>
          <p className="text-sm text-gray-500">{subtitle}</p>
        </div>
      </div>
      <div className="text-gray-400 text-2xl font-semibold">{`→`}</div>
    </Link>
  );
}
