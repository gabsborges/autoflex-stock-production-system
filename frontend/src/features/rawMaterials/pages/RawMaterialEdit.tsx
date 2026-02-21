import { useParams, useNavigate } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useEditRawMaterial } from "../hooks/useEditRawMaterial";
import RawMaterialForm from "../components/rawMaterialForm";

export default function RawMaterialEdit() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const {
    name,
    setName,
    quantity,
    setQuantity,
    handleUpdate,
    isLoading,
    isError,
    isUpdating,
  } = useEditRawMaterial(id);

  if (isLoading) return <p>Loading raw material...</p>;
  if (isError) return <p>Error loading raw material.</p>;

  const onSubmit = async () => {
    const success = await handleUpdate();
    if (success) navigate("/raw-materials");
  };

  return (
    <div className="max-w-4xl mx-auto p-6 bg-white rounded shadow">
      <ToastContainer />
      <RawMaterialForm
        name={name}
        quantity={quantity}
        onNameChange={setName}
        onQuantityChange={setQuantity}
        onSubmit={onSubmit}
        loading={isUpdating}
        title="Edit Raw Material"
        buttonLabel="Save Changes"
      />
    </div>
  );
}