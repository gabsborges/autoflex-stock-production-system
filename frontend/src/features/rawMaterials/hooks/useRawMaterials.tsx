import { toast } from "react-toastify";
import { useDeleteRawMaterialMutation, useGetRawMaterialsQuery } from "../rawMaterialsApi";

export function useRawMaterials() {
  const { data, isLoading, isError } = useGetRawMaterialsQuery();
  const [deleteRawMaterial] = useDeleteRawMaterialMutation();

  const rawMaterials = Array.isArray(data?.data) ? data.data : [];

  const handleDelete = async (id: string) => {
    try {
      await deleteRawMaterial(id).unwrap();
      toast.success("Raw material deleted successfully!");
    } catch (error) {
      console.error(error);
      toast.error("Failed to delete the raw material.");
    }
  };

  return { rawMaterials, isLoading, isError, handleDelete };
}