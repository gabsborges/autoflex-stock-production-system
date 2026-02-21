import { useState } from "react";
import { toast } from "react-toastify";
import { useCreateRawMaterialMutation } from "../rawMaterialsApi";

export function useCreateRawMaterial() {
  const [createRawMaterial, { isLoading }] = useCreateRawMaterialMutation();

  const [name, setName] = useState("");
  const [quantity, setQuantity] = useState(0);

  const handleCreate = async () => {
    try {
      await createRawMaterial({
        name: name.trim(),
        quantity,
      }).unwrap();

      toast.success("Material criado com sucesso!");
      setName("");
      setQuantity(0);
      return true;
    } catch (err: any) {
      toast.error("Erro ao criar material.");
      return false;
    }
  };

  return {
    name,
    setName,
    quantity,
    setQuantity,
    handleCreate,
    isLoading,
  };
}