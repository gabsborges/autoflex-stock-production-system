import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useCreateRawMaterial } from "../hooks/useCreateRawMateial";
import RawMaterialForm from "../components/rawMaterialForm";

export default function RawMaterialCreate() {
  const {
    name,
    setName,
    quantity,
    setQuantity,
    handleCreate,
    isLoading,
  } = useCreateRawMaterial();

  return (
    <div className="max-w-4xl mx-auto p-6 bg-white rounded shadow">
      <ToastContainer />
      <RawMaterialForm
        name={name}
        quantity={quantity}
        onNameChange={setName}
        onQuantityChange={setQuantity}
        onSubmit={handleCreate}
        loading={isLoading}
        title="Create Raw Material"
        buttonLabel="Create"
      />
    </div>
  );
}