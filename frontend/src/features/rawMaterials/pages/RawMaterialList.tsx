import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import { useRawMaterials } from "../hooks/useRawMaterials";
import RawMaterialTable from "../components/rawMaterialTable";
import DeleteRawMaterialModal from "../hooks/DeleteMaterialModal";

export default function RawMaterialList() {
  const navigate = useNavigate();
  const { rawMaterials, isLoading, isError, handleDelete } = useRawMaterials();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedId, setSelectedId] = useState<string | null>(null);

  const openModal = (id: string) => {
    setSelectedId(id);
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setSelectedId(null);
    setIsModalOpen(false);
  };

  const confirmDelete = async () => {
    if (!selectedId) return;
    await handleDelete(selectedId);
    closeModal();
  };

  const handleEdit = (id: string) => navigate(`/raw-materials/${id}/edit`);

  if (isLoading) return <p>Loading...</p>;
  if (isError) return <p>Error loading raw materials.</p>;

  return (
    <div className="bg-white rounded-lg shadow p-6">
      <div className="flex justify-end mb-4">
        <Link
          to="/raw-materials/new"
          data-cy="create-raw"
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition"
        >
          + Create Raw Material
        </Link>
      </div>

      <RawMaterialTable rawMaterials={rawMaterials} onEdit={handleEdit} onDelete={openModal} />

      <DeleteRawMaterialModal
        isOpen={isModalOpen}
        onClose={closeModal}
        onConfirm={confirmDelete}
      />

      <ToastContainer position="top-right" autoClose={3000} />
    </div>
  );
}