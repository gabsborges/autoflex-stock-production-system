interface DeleteProductModalProps {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void;
}

export function DeleteProductModal({ isOpen, onClose, onConfirm }: DeleteProductModalProps) {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
      <div className="bg-white p-6 rounded-lg shadow-lg w-1/3">
        <h2 className="text-lg font-semibold mb-4">Delete Product</h2>
        <p>Are you sure you want to delete this product? This action cannot be undone.</p>
        <div className="flex justify-end gap-4 mt-4">
          <button className="bg-gray-300 text-gray-800 px-4 py-2 rounded" onClick={onClose}>
            Cancel
          </button>
          <button className="bg-red-600 text-white px-4 py-2 rounded" onClick={onConfirm}>
            Delete
          </button>
        </div>
      </div>
    </div>
  );
}