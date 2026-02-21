import { useNavigate, useParams } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useEditProduct } from "../hooks/useEditProduct";

export default function ProductEdit() {
  const { id } = useParams<{ id: string }>();
  const productId = Number(id);
  const navigate = useNavigate();

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
    loadingProduct,
    loadingRaw,
  } = useEditProduct(productId);

  if (loadingProduct || loadingRaw) return <p>Loading...</p>;

  return (
    <div className="max-w-4xl mx-auto p-4 bg-white rounded shadow space-y-8 md:space-y-0 md:flex md:gap-10">
      <ToastContainer />

      {/* Formulário */}
      <form onSubmit={handleSubmit} className="md:flex-1 space-y-4">
        <h2 className="font-bold text-lg mb-4">Edit Product</h2>

        {["sku","name","price","quantity"].map((field) => (
          <div key={field}>
            <label className="block text-sm font-medium mb-1">{field.toUpperCase()}</label>
            <input
            data-cy={`product-${field}`}
              type={field==="price" || field==="quantity" ? "number":"text"}
              name={field}
              value={form[field as keyof typeof form]}
              onChange={handleChange}
              className="w-full border border-gray-300 rounded px-3 py-2"
            />
          </div>
        ))}

        <div className="flex gap-4 mt-4">
          <button data-cy="update-product" type="submit" className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition">
            Update Product
          </button>
          <button type="button" className="bg-gray-300 text-gray-800 px-4 py-2 rounded hover:bg-gray-400 transition"
            onClick={() => navigate("/products")}>
            Cancel
          </button>
        </div>
      </form>

      {/* Matérias-primas vinculadas */}
      <section className="md:flex-1">
        <h2 className="font-bold text-lg mb-4">Raw Materials Composition</h2>

        <table className="w-full border border-gray-200 rounded">
          <thead>
            <tr className="bg-gray-100 text-gray-600 text-left text-sm uppercase">
              <th className="py-2 px-3 border-b border-gray-200">Raw Material</th>
              <th className="py-2 px-3 border-b border-gray-200">Quantity Required</th>
              <th className="py-2 px-3 border-b border-gray-200 text-center">Actions</th>
            </tr>
          </thead>
          <tbody>
            {materials.length === 0 ? (
              <tr>
                <td colSpan={3} className="py-6 text-center text-gray-400">No materials linked yet.</td>
              </tr>
            ) : (
              materials.map(({ id, name, quantity }) => (
                <tr key={id} className="border-b border-gray-200 hover:bg-gray-50">
                  <td className="py-2 px-3">{name}</td>
                  <td className="py-2 px-3">{quantity}</td>
                  <td className="py-2 px-3 text-center">
                    <button type="button" className="text-red-600 hover:text-red-800" onClick={() => removeMaterial(id)}>Remove</button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>

        <button
          type="button"
          onClick={() => setShowMaterialModal(true)}
          className="mt-4 w-full md:w-auto bg-blue-600 text-white px-3 py-2 rounded hover:bg-blue-700 transition"
        >
          + Add Material
        </button>
      </section>

      {/* Modal */}
      {showMaterialModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded shadow-lg w-full max-w-2xl p-6 relative">
            <h3 className="font-bold text-lg mb-4">Select Raw Materials</h3>

            {loadingRaw ? <p>Loading...</p> : rawMaterials.length === 0 ? <p>No materials available.</p> : (
              <div className="overflow-y-auto max-h-96">
                <table className="w-full border border-gray-200 rounded">
                  <thead>
                    <tr className="bg-gray-100 text-gray-600 text-left text-sm uppercase">
                      <th className="py-2 px-3 border-b border-gray-200">Material</th>
                      <th className="py-2 px-3 border-b border-gray-200">Available</th>
                      <th className="py-2 px-3 border-b border-gray-200">Quantity</th>
                    </tr>
                  </thead>
                  <tbody>
                    {rawMaterials.map((rm) => (
                      <tr key={rm.id} className="border-b border-gray-200 hover:bg-gray-50">
                        <td className="py-2 px-3">{rm.name}</td>
                        <td className="py-2 px-3">{rm.quantity}</td>
                        <td className="py-2 px-3">
                          <input
                            type="number"
                            min={0}
                            value={materialQuantities[rm.id.toString()] || ""}
                            onChange={(e) => setMaterialQuantities({...materialQuantities, [rm.id.toString()]: Number(e.target.value)})}
                            className="w-24 border border-gray-300 rounded px-2 py-1"
                          />
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}

            <div className="flex justify-end gap-3 mt-4">
              <button type="button" className="px-4 py-2 rounded bg-gray-300 hover:bg-gray-400" onClick={() => setShowMaterialModal(false)}>Cancel</button>
              <button type="button" className="px-4 py-2 rounded bg-green-600 text-white hover:bg-green-700" onClick={addSelectedMaterials}>Add Selected</button>
            </div>

            <button type="button" className="absolute top-2 right-2 text-gray-500 hover:text-gray-800" onClick={() => setShowMaterialModal(false)}>✕</button>
          </div>
        </div>
      )}
    </div>
  );
}