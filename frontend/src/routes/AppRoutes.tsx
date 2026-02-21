import Layout from "../components/layout/Layout";
import Dashboard from "../pages/Dashboard";
import ProductList from "../features/products/pages/ProductList";
import ProductCreate from "../features/products/pages/ProductCreate";
import ProductEdit from "../features/products/pages/ProductEdit";
import RawMaterialList from "../features/rawMaterials/pages/RawMaterialList";
import RawMaterialEdit from "../features/rawMaterials/pages/RawMaterialEdit";
import { createBrowserRouter } from "react-router-dom";
import RawMaterialCreate from "../features/rawMaterials/pages/RawMaterialCreate";
import ProductionSuggestion from "../features/productionSimulation/pages/ProductionSuggestion";

export const router = createBrowserRouter([
  {
    path: "/",
    element: (
      <Layout>
        <Dashboard />
      </Layout>
    ),
  },
  {
    path: "/dashboard",
    element: (
      <Layout>
        <Dashboard />
      </Layout>
    ),
  },
  {
    path: "/products",
    element: (
      <Layout>
        <ProductList />
      </Layout>
    ),
  },
  {
    path: "/products/new",
    element: (
      <Layout>
        <ProductCreate />
      </Layout>
    ),
  },
  {
    path: "/products/:id/edit",
    element: (
      <Layout>
        <ProductEdit />
      </Layout>
    ),
  },
  {
    path: "/raw-materials",
    element: (
      <Layout>
        <RawMaterialList />
      </Layout>
    ),
  },
  {
    path: "/raw-materials/new",
    element: (
      <Layout>
        <RawMaterialCreate />
      </Layout>
    ),
  },
  {
    path: "/raw-materials/:id/edit",
    element: (
      <Layout>
        <RawMaterialEdit />
      </Layout>
    ),
  },
  {
    path: "/production-suggestion",
    element: (
      <Layout>
        <ProductionSuggestion />
      </Layout>
    ),
  },
]);