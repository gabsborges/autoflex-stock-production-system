import { useState, useEffect } from "react";
import { toast } from "react-toastify";
import { useUpdateRawMaterialMutation, useGetProductByIdQuery } from "../productsApi";
import { useGetRawMaterialsQuery } from "../../rawMaterials/rawMaterialsApi";
import { useNavigate } from "react-router-dom";

export interface RawMaterial {
  id: number;
  name: string;
  quantity: number;
}

export function useEditProduct(productId: number | string) {
  const [form, setForm] = useState({
    sku: "",
    name: "",
    price: 0,
    quantity: 0,
  });

  const [materials, setMaterials] = useState<RawMaterial[]>([]);
  const [materialQuantities, setMaterialQuantities] = useState<Record<string, number>>({});
  const [showMaterialModal, setShowMaterialModal] = useState(false);

  const { data: productData, isLoading: loadingProduct } = useGetProductByIdQuery(productId);
  const { data: rawData, isLoading: loadingRaw } = useGetRawMaterialsQuery();
  const rawMaterials = Array.isArray(rawData)
    ? rawData
    : Array.isArray(rawData?.data)
      ? rawData.data
      : [];

  const [updateRawMaterial] = useUpdateRawMaterialMutation();
  const navigate = useNavigate();


  useEffect(() => {
    if (productData) {
      setForm({
        sku: productData.sku,
        name: productData.name,
        price: productData.price,
        quantity: productData.quantity,
      });

      if (Array.isArray(productData.rawMaterials)) {
        const mapped = productData.rawMaterials.map((rm: any) => ({
          id: rm.rawMaterialId,
          name: rm.name,
          quantity: rm.quantityRequired,
        }));
        setMaterials(mapped);
      }
    }
  }, [productData]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: name === "price" || name === "quantity" ? Number(value) : value,
    }));
  };

  const removeMaterial = (id: number) => {
    setMaterials(materials.filter((m) => m.id !== id));
  };

  const addSelectedMaterials = () => {
    const selected: RawMaterial[] = rawMaterials
      .filter((rm) => materialQuantities[rm.id.toString()] > 0)
      .map((rm) => ({
        id: rm.id,
        name: rm.name,
        quantity: materialQuantities[rm.id.toString()]!,
      }));

    const merged = materials.filter((m) => !selected.find((s) => s.id === m.id));
    setMaterials([...merged, ...selected]);

    setMaterialQuantities({});
    setShowMaterialModal(false);
  };


  const updateOrCreateMaterials = async () => {
    try {
      for (const m of materials) {
        const exists = productData?.rawMaterials?.find((rm: any) => rm.rawMaterialId === m.id);

        if (exists) {

          await updateRawMaterial({
            productId,
            body: { rawMaterialId: m.id, quantityRequired: m.quantity },
          }).unwrap();
        } else {
          // Cria via POST
          const body = { rawMaterialId: m.id, quantityRequired: m.quantity };
          const response = await fetch(`http://localhost:8080/products/${productId}/raw-materials`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body),
          });
          if (!response.ok) {
            let errorMessage = `Failed to link material ${m.name} (status ${response.status})`;
            try {
              const data = await response.json();
              if (data?.message) errorMessage = data.message;
            } catch { }
            throw new Error(errorMessage);
          }
        }
      }
      toast.success("Raw materials updated successfully!");
      navigate("/products");
    } catch (err: any) {
      toast.error(err.data?.description || err.message || "Failed to update materials");
      throw err;
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      await updateOrCreateMaterials();
      toast.success("Product updated successfully!");
    } catch (err) {
      console.error(err);
    }
  };

  return {
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
  };
}