import type { RawMaterial } from "../hooks/useCreateProduct";


interface MaterialsModalProps {
  isOpen: boolean;
  onClose: () => void;
  onAdd: () => void;
  rawMaterials: RawMaterial[];
  materialQuantities: Record<string, number>;
  setMaterialQuantities: React.Dispatch<React.SetStateAction<Record<string, number>>>;
  isLoading: boolean;
}

export function MaterialsModal({
  isOpen,
  onClose,
  onAdd,
  rawMaterials,
  materialQuantities,
  setMaterialQuantities,
  isLoading,
}: MaterialsModalProps) {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded shadow-lg w-full max-w-2xl p-6 relative">
        <h3 className="font-bold text-lg mb-4">Select Raw Materials</h3>

        {isLoading ? (
          <p>Loading...</p>
        ) : rawMaterials.length === 0 ? (
          <p>No materials available.</p>
        ) : (
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
                        onChange={(e) =>
                          setMaterialQuantities({
                            ...materialQuantities,
                            [rm.id.toString()]: Number(e.target.value),
                          })
                        }
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
          <button
            type="button"
            className="px-4 py-2 rounded bg-gray-300 hover:bg-gray-400"
            onClick={onClose}
          >
            Cancel
          </button>
          <button
            type="button"
            className="px-4 py-2 rounded bg-green-600 text-white hover:bg-green-700"
            onClick={onAdd}
          >
            Add Selected
          </button>
        </div>

        <button
          type="button"
          className="absolute top-2 right-2 text-gray-500 hover:text-gray-800"
          onClick={onClose}
        >
          ✕
        </button>
      </div>
    </div>
  );
}