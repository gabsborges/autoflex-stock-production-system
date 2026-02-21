import React from "react";

interface Props {
  name: string;
  quantity: number;
  onNameChange: (value: string) => void;
  onQuantityChange: (value: number) => void;
  onSubmit: () => Promise<void | boolean>;
  loading?: boolean;
  title: string;
  buttonLabel: string;
}

export default function RawMaterialForm({
  name,
  quantity,
  onNameChange,
  onQuantityChange,
  onSubmit,
  loading = false,
  title,
  buttonLabel,
}: Props) {
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await onSubmit();
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4 w-full">
      <h2 className="font-bold text-lg mb-4">{title}</h2>

      <div>
        <label className="block text-sm font-medium mb-1">Name</label>
        <input
          type="text"
          value={name}
          onChange={(e) => onNameChange(e.target.value)}
          className="w-full border border-gray-300 rounded px-3 py-2"
          required
        />
      </div>

      <div>
        <label className="block text-sm font-medium mb-1">Quantity</label>
        <input
          type="number"
          min="0"
          value={quantity}
          onChange={(e) => onQuantityChange(Number(e.target.value))}
          className="w-full border border-gray-300 rounded px-3 py-2"
          required
        />
      </div>

      <button
        type="submit"
        disabled={loading}
        className="mt-6 bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition disabled:opacity-50"
      >
        {loading ? "Saving..." : buttonLabel}
      </button>
    </form>
  );
}