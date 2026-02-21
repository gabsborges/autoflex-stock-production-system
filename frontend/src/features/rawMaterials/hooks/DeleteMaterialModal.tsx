interface Props {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void;
}

export default function DeleteRawMaterialModal({ isOpen, onClose, onConfirm }: Props) {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50"
    data-cy="delete-raw-modal">
      <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
        <h2 className="text-lg font-semibold mb-4">Delete Raw Material</h2>
        <p>
          Are you sure you want to delete this raw material? This action
          cannot be undone.
        </p>
        <div className="flex justify-end gap-4 mt-4">
          <button className="bg-gray-300 text-gray-800 px-4 py-2 rounded" onClick={onClose}>
            Cancel
          </button>
          <button 
          data-cy="confirm-delete"
          className="bg-red-600 text-white px-4 py-2 rounded" onClick={onConfirm}>
            Delete
          </button>
        </div>
      </div>
    </div>
  );
}