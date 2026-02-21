import { ToastContainer } from "react-toastify";
import { ProductForm } from "../components/ProductForm";
import { MaterialsTable } from "../components/MaterialsTable";
import { MaterialsModal } from "../components/MaterialsModal";
import { useCreateProduct } from "../hooks/useCreateProduct";

export default function ProductCreate() {
  const {
    form,
    handleChange,
    handleSubmit,
    materials,
    removeMaterial,
    showMaterialModal,
    setShowMaterialModal,
    materialQuantities,
    setMaterialQuantities,
    addSelectedMaterials,
    rawMaterials,
    isLoading,
    isError,
  } = useCreateProduct();

  if (isError) return <p>Error loading materials</p>;

  return (
    <div className="max-w-4xl mx-auto p-4 bg-white rounded shadow space-y-8 md:space-y-0 md:flex md:gap-10">
      <ToastContainer />

      {/* Formulário de produto */}
      <ProductForm form={form} onChange={handleChange} onSubmit={handleSubmit} submitLabel="Create Product" />

      {/* Tabela de materiais adicionados */}
      <section className="md:flex-1">
        <h2 className="font-bold text-lg mb-4">Raw Materials Composition</h2>
        <MaterialsTable materials={materials} onRemove={removeMaterial} />
        <button
          type="button"
          onClick={() => setShowMaterialModal(true)}
          className="mt-4 w-full md:w-auto bg-blue-600 text-white px-3 py-2 rounded hover:bg-blue-700 transition"
        >
          + Add Material
        </button>
      </section>

      {/* Modal de seleção de materiais */}
      <MaterialsModal
        isOpen={showMaterialModal}
        onClose={() => setShowMaterialModal(false)}
        onAdd={addSelectedMaterials}
        rawMaterials={rawMaterials}
        materialQuantities={materialQuantities}
        setMaterialQuantities={setMaterialQuantities}
        isLoading={isLoading}
      />
    </div>
  );
}