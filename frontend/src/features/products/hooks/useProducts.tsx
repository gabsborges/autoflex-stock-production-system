import { useState } from "react";
import { toast } from "react-toastify";
import { useDeleteProductMutation, useGetProductsQuery } from "../productsApi";


export function useProducts() {
  const { data: products = [], isLoading, isError } = useGetProductsQuery();
  const [deleteProduct] = useDeleteProductMutation();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [productIdToDelete, setProductIdToDelete] = useState<number | null>(null);

  const openModal = (id: number) => {
    setProductIdToDelete(id);
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setProductIdToDelete(null);
    setIsModalOpen(false);
  };

  const confirmDelete = async () => {
    if (!productIdToDelete) return;
    try {
      await deleteProduct(productIdToDelete.toString()).unwrap();
      toast.success("Product deleted successfully!");
    } catch (error) {
      console.error("Failed to delete the product:", error);
      toast.error("Failed to delete the product.");
    } finally {
      closeModal();
    }
  };

  return {
    products,
    isLoading,
    isError,
    isModalOpen,
    openModal,
    closeModal,
    confirmDelete,
  };
}