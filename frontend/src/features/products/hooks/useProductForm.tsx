import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "react-toastify";
import { useGetProductsQuery, useUpdateProductMutation } from "../productsApi";

export function useProductForm() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const { data: products = [], isLoading, isError } = useGetProductsQuery();
  const product = products.find((p) => p.id === id);

  const [updateProduct] = useUpdateProductMutation();

  const [form, setForm] = useState({
    sku: "",
    name: "",
    price: 0,
    quantity: 0,
  });

  useEffect(() => {
    if (product) {
      setForm({
        sku: product.sku,
        name: product.name,
        price: product.price,
        quantity: product.quantity,
      });
    }
  }, [product]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: name === "price" || name === "quantity" ? Number(value) : value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!product) return;
    try {
      await updateProduct({ id: product.id, body: form }).unwrap();
      toast.success("Product updated successfully!");
      navigate("/products");
    } catch (err) {
      console.error("Failed to update product:", err);
      toast.error("Failed to update product.");
    }
  };

  return {
    form,
    handleChange,
    handleSubmit,
    isLoading,
    isError,
  };
}