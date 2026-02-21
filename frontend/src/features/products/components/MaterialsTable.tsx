import type { RawMaterial } from "../hooks/useCreateProduct";

interface MaterialsTableProps {
  materials: RawMaterial[];
  onRemove: (id: number) => void;
}

export function MaterialsTable({ materials, onRemove }: MaterialsTableProps) {
  if (materials.length === 0)
    return (
      <p className="py-6 text-center text-gray-400">No materials added yet.</p>
    );

  return (
    <table className="w-full border border-gray-200 rounded">
      <thead>
        <tr className="bg-gray-100 text-gray-600 text-left text-sm uppercase">
          <th className="py-2 px-3 border-b border-gray-200">Raw Material</th>
          <th className="py-2 px-3 border-b border-gray-200">Quantity Required</th>
          <th className="py-2 px-3 border-b border-gray-200 text-center">Actions</th>
        </tr>
      </thead>
      <tbody>
        {materials.map(({ id, name, quantity }) => (
          <tr key={id} className="border-b border-gray-200 hover:bg-gray-50">
            <td className="py-2 px-3">{name}</td>
            <td className="py-2 px-3">{quantity}</td>
            <td className="py-2 px-3 text-center">
              <button
                type="button"
                className="text-red-600 hover:text-red-800"
                onClick={() => onRemove(id)}
                aria-label="Remove material"
              >
                Remove
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}