import { HiOutlinePencil, HiOutlineTrash } from "react-icons/hi";

interface RawMaterial {
  id: string;
  name: string;
  quantity: number;
}

interface Props {
  rawMaterials: RawMaterial[];
  onEdit: (id: string) => void;
  onDelete: (id: string) => void;
}

export default function RawMaterialTable({ rawMaterials, onEdit, onDelete }: Props) {
  return (
    <table className="w-full table-fixed border-collapse" data-cy="raw-material-table">
      <thead>
        <tr className="border-b border-gray-300 text-left text-sm text-gray-600">
          <th className="w-1/6 py-3 px-2 uppercase">Code</th>
          <th className="w-3/6 py-3 px-2 uppercase">Name</th>
          <th className="w-1/6 py-3 px-2 uppercase">StockQuantity</th>
          <th className="w-1/6 py-3 px-2 uppercase text-center">Actions</th>
        </tr>
      </thead>
      <tbody>
        {rawMaterials.length === 0 ? (
          <tr>
            <td colSpan={4} className="text-center py-4 text-gray-500">
              No raw materials found.
            </td>
          </tr>
        ) : (
          rawMaterials.map(({ id, name, quantity }) => (
            <tr key={id} className="border-b border-gray-200 hover:bg-gray-50" data-cy={`raw-row-${id}`}>
              <td className="py-3 px-2 text-gray-700">{id}</td>
              <td className="py-3 px-2">{name}</td>
              <td className="py-3 px-2 text-orange-600 font-semibold">{quantity}</td>
              <td className="py-3 px-2 flex justify-center gap-3 text-gray-600">
                <button
                  data-cy={`edit-raw-${id}`}
                  aria-label="Edit"
                  className="hover:text-blue-600 transition"
                  onClick={() => onEdit(id)}
                >
                  <HiOutlinePencil className="w-5 h-5" />
                </button>
                <button
                  data-cy={`delete-raw-${id}`}
                  aria-label="Delete"
                  className="hover:text-red-600 transition"
                  onClick={() => onDelete(id)}
                >
                  <HiOutlineTrash className="w-5 h-5" />
                </button>
              </td>
            </tr>
          ))
        )}
      </tbody>
    </table>
  );
}