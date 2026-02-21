import { useState } from "react";
import { toast } from "react-toastify";
import { useCreateProductMutation } from "../productsApi";
import { useGetRawMaterialsQuery } from "../../rawMaterials/rawMaterialsApi";

export interface RawMaterial {
  id: number;
  name: string;
  quantity: number; // quantidade usada no produto
}

export function useCreateProduct() {
  const [form, setForm] = useState({
    sku: "PRD-001",
    name: "",
    price: 0,
    quantity: 0,
  });

  const [materials, setMaterials] = useState<RawMaterial[]>([]);
  const [materialQuantities, setMaterialQuantities] = useState<Record<string, number>>({});
  const [showMaterialModal, setShowMaterialModal] = useState(false);

  const [createProduct] = useCreateProductMutation();
  const { data, isLoading, isError } = useGetRawMaterialsQuery();
  const rawMaterials = Array.isArray(data)
    ? data
    : Array.isArray(data?.data)
    ? data.data
    : [];

  // Atualiza campos do formulário
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: name === "price" || name === "quantity" ? Number(value) : value,
    }));
  };

  // Remove material da lista
  const removeMaterial = (id: number) => {
    setMaterials(materials.filter((m) => m.id !== id));
  };

  // Adiciona materiais selecionados no modal
  const addSelectedMaterials = () => {
    const selected: RawMaterial[] = rawMaterials
      .filter((rm) => materialQuantities[rm.id.toString()] > 0)
      .map((rm) => ({
        id: rm.id,
        name: rm.name,
        quantity: materialQuantities[rm.id.toString()]!,
      }));

    setMaterials([...materials, ...selected]);
    setMaterialQuantities({});
    setShowMaterialModal(false);
  };

  // Faz POST de cada material
  const linkMaterialsToProduct = async (productId: number, materialsToLink: RawMaterial[]) => {
    for (const m of materialsToLink) {
      const body = {
        rawMaterialId: m.id,
        quantityRequired: m.quantity,
      };

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
        } catch {}
        throw new Error(errorMessage);
      }
    }
  };

  // Submit do formulário
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const productDTO = {
        ...form,
        price: Number(form.price),
        quantity: Number(form.quantity),
      };

      // 1️⃣ Cria o produto
      const createdProduct = await createProduct(productDTO).unwrap();
      const productId = Number(createdProduct.id);

      // 2️⃣ Vincula matérias-primas, se houver
      if (materials.length > 0) {
        await linkMaterialsToProduct(productId, materials);
      }

      toast.success(`Product "${createdProduct.name}" created successfully!`);

      // 3️⃣ Reset do form
      setForm({ sku: "", name: "", price: 0, quantity: 0 });
      setMaterials([]);
    } catch (err: any) {
      toast.error(err.data?.description || err.message || "Unknown error");
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
    isLoading,
    isError,
  };
}