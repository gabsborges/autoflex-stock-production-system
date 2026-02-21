import { HiOutlinePencil, HiOutlineTrash } from "react-icons/hi";
import { useDeleteProductMutation, useGetProductsQuery } from "../productsApi";
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import { toast, ToastContainer } from "react-toastify";
import { DeleteProductModal } from "../components/DeleteProductModal";

export default function ProductsList() {
  const { data: products = [], isLoading, isError } = useGetProductsQuery();
  const [deleteProduct] = useDeleteProductMutation();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [productIdToDelete, setProductIdToDelete] = useState<string | number | null>(null);
  const navigate = useNavigate();

  const handleDeleteClick = (id: string | number) => {
    setProductIdToDelete(id);
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setProductIdToDelete(null);
  };

  const confirmDelete = async () => {
    try {
      if (productIdToDelete === null) {
        toast.error("No product selected for deletion.");
        return;
      }
      await deleteProduct(productIdToDelete.toString()).unwrap();
      toast.success("Product deleted successfully!");
      closeModal();
    } catch (error) {
      console.error("Failed to delete the product:", error);
      toast.error("Failed to delete the product.");
      closeModal();
    }
  };

  if (isLoading) return <p>Loading...</p>;
  if (isError) return <p>Error loading products</p>;

  return (
    <div className="bg-white rounded-lg shadow p-6">
      <div className="flex justify-end mb-4">
        <Link
          data-cy="create-product"
          to="/products/new"
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition">
          + Create Product
        </Link>
      </div>

      <table className="w-full table-fixed border-collapse">
        <thead>
          <tr className="border-b border-gray-300 text-left text-sm text-gray-600">
            <th className="w-1/6 py-3 px-2 uppercase">SKU</th>
            <th className="w-2/6 py-3 px-2 uppercase">Name</th>
            <th className="w-1/6 py-3 px-2 uppercase">Price</th>
            <th className="w-1/6 py-3 px-2 uppercase">Quantity</th>
            <th className="w-1/6 py-3 px-2 uppercase text-center">Actions</th>
          </tr>
        </thead>
        <tbody>
          {products.map(({ id, sku, name, price, quantity }) => (
            <tr key={id} className="border-b border-gray-200 hover:bg-gray-50">
              <td className="py-3 px-2 text-gray-700">{sku}</td>
              <td className="py-3 px-2">{name}</td>
              <td className="py-3 px-2 text-orange-600 font-semibold">
                {price.toLocaleString("en-US", {
                  style: "currency",
                  currency: "USD",
                })}
              </td>
              <td className="py-3 px-2 text-gray-600">{quantity}</td>
              <td className="py-3 px-2 flex justify-center gap-3 text-gray-600">
                <button
                  aria-label="Edit"
                  data-cy={`edit-product-${id}`}
                  className="hover:text-blue-600 transition"
                  onClick={() => navigate(`/products/${id}/edit`)}
                >
                  <HiOutlinePencil className="w-5 h-5" />
                </button>
                <button
                  aria-label="Delete"
                  data-cy={`delete-product-${id}`}
                  className="hover:text-red-600 transition"
                  onClick={() => handleDeleteClick(id)}
                >
                  <HiOutlineTrash className="w-5 h-5" />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>


      {isModalOpen && (
  <DeleteProductModal
    isOpen={isModalOpen}
    onClose={closeModal}
    onConfirm={confirmDelete}
  />
)}
      <ToastContainer />
    </div>
  );
}
