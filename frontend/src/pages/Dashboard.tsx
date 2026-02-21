import { HiCube, HiOutlineBeaker, HiOutlineChartBar, HiOutlineCube } from "react-icons/hi";
import StatCard from "../components/ui/StatCard";
import QuickActionCard from "../components/ui/QuickActionCard";
import { useGetProductsQuery } from "../features/products/productsApi";
import { useGetRawMaterialsQuery } from "../features/rawMaterials/rawMaterialsApi";

export default function Dashboard() {
  const { data: products, isLoading, isError } = useGetProductsQuery();
  const { data: rawMaterials, isLoading: isRawMaterialsLoading, isError: isRawMaterialsError } = useGetRawMaterialsQuery();
  
  if (isLoading || isRawMaterialsLoading) return <p>Loading...</p>;
  if (isError || isRawMaterialsError) return <p>Error loading data</p>;

const rawMaterialsList = rawMaterials?.data ?? [];

const totalProducts = products?.length ?? 0;
const totalRawMaterials = rawMaterialsList.length;

const totalStockItems = rawMaterialsList.reduce(
  (acc: number, item: any) => acc + (item.quantity ?? 0),
  0
);

  return (
    <div>
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-6 mb-8">
        <StatCard
          icon={<HiOutlineCube />}
          title="Total Products"
          value={totalProducts}
          iconBgColor="bg-blue-100"
          iconColor="text-blue-600"
        />
        <StatCard
          icon={<HiCube />}
          title="Total Raw Materials"
          value={totalRawMaterials}
          iconBgColor="bg-green-100"
          iconColor="text-green-600"
        />
        <StatCard
          icon={<HiOutlineChartBar />}
          title="Total Stock Items"
          value={totalStockItems.toLocaleString()}
          iconBgColor="bg-yellow-100"
          iconColor="text-yellow-600"
        />
      </div>

      <section>
        <h2 className="text-lg font-semibold mb-4">Quick Actions</h2>
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
          <QuickActionCard
            to="/products/new"
            icon={<HiOutlineCube />}
            title="New Product"
            subtitle="Register a new product"
            iconBgColor="bg-blue-100"
            iconColor="text-blue-600"
          />
          <QuickActionCard
            to="/raw-materials/new"
            icon={<HiCube />}
            title="New Raw Material"
            subtitle="Add raw material to stock"
            iconBgColor="bg-green-100"
            iconColor="text-green-600"
          />
          <QuickActionCard
            to="/production-suggestion"
            icon={<HiOutlineBeaker />}
            title="Production suggestion"
            subtitle="Calculate optimal production plan"
            iconBgColor="bg-yellow-100"
            iconColor="text-yellow-600"
          />
        </div>
      </section>
    </div>
  );
}
