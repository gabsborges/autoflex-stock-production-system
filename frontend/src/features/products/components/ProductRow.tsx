import { HiOutlinePencil, HiOutlineTrash } from "react-icons/hi";
import { useNavigate } from "react-router-dom";

interface ProductRowProps {
  id: number;
  sku: string;
  name: string;
  price: number;
  quantity: number;
  onDelete: (id: number) => void;
}

export function ProductRow({ id, sku, name, price, quantity, onDelete }: ProductRowProps) {
  const navigate = useNavigate();

  return (
    <tr className="border-b border-gray-200 hover:bg-gray-50">
      <td className="py-3 px-2 text-gray-700">{sku}</td>
      <td className="py-3 px-2">{name}</td>
      <td className="py-3 px-2 text-orange-600 font-semibold">
        {price.toLocaleString("en-US", { style: "currency", currency: "USD" })}
      </td>
      <td className="py-3 px-2 text-gray-600">{quantity}</td>
      <td className="py-3 px-2 flex justify-center gap-3 text-gray-600">
        <button
          aria-label="Edit"
          className="hover:text-blue-600 transition"
          onClick={() => navigate(`/products/${id}/edit`)}
        >
          <HiOutlinePencil className="w-5 h-5" />
        </button>
        <button
          aria-label="Delete"
          className="hover:text-red-600 transition"
          onClick={() => onDelete(id)}
        >
          <HiOutlineTrash className="w-5 h-5" />
        </button>
      </td>
    </tr>
  );
}