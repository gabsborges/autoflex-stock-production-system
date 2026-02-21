import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { useGetRawMaterialsByIDQuery, useUpdateRawMaterialMutation } from "../rawMaterialsApi";

export function useEditRawMaterial(id: string | undefined) {
  const { data, isLoading, isError } = useGetRawMaterialsByIDQuery(id!, {
    skip: !id,
  });

  const [updateRawMaterial, { isLoading: isUpdating }] =
    useUpdateRawMaterialMutation();

  const [name, setName] = useState("");
  const [quantity, setQuantity] = useState(0);

  useEffect(() => {
    if (data?.data) {
      setName(data.data.name ?? "");
      setQuantity(data.data.quantity ?? 0);
    }
  }, [data]);

  const handleUpdate = async () => {
    if (!id) return false;

    try {
      await updateRawMaterial({
        id,
        body: {
          name: name.trim(),
          quantity,
        },
      }).unwrap();

      toast.success("Raw Material updated successfully!");
      return true;
    } catch (err: any) {
      toast.error(err?.data?.description || "Update failed");
      return false;
    }
  };

  return {
    name,
    setName,
    quantity,
    setQuantity,
    handleUpdate,
    isLoading,
    isError,
    isUpdating,
  };
}