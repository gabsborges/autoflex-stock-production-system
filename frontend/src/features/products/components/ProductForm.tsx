interface ProductFormProps {
  form: {
    sku: string;
    name: string;
    price: number;
    quantity: number;
  };
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onSubmit: (e: React.FormEvent) => void;
  submitLabel?: string;
}

export function ProductForm({ form, onChange, onSubmit, submitLabel = "Save" }: ProductFormProps) {
  return (
    <form onSubmit={onSubmit} className="bg-white p-6 rounded-lg shadow space-y-4 max-w-md">
      <div>
        <label className="block mb-1 font-medium">SKU</label>
        <input
        data-cy="product-sku"
          type="text"
          name="sku"
          value={form.sku}
          onChange={onChange}
          className="w-full border px-3 py-2 rounded"
          required
        />
      </div>

      <div>
        <label className="block mb-1 font-medium">Name</label>
        <input
        data-cy="product-name"
          type="text"
          name="name"
          value={form.name}
          onChange={onChange}
          className="w-full border px-3 py-2 rounded"
          required
        />
      </div>

      <div>
        <label className="block mb-1 font-medium">Price</label>
        <input
        data-cy="product-price"
          type="number"
          name="price"
          value={form.price}
          onChange={onChange}
          className="w-full border px-3 py-2 rounded"
          required
        />
      </div>

      <div>
        <label className="block mb-1 font-medium">Quantity</label>
        <input
        data-cy="product-quantity"
          type="number"
          name="quantity"
          value={form.quantity}
          onChange={onChange}
          className="w-full border px-3 py-2 rounded"
          required
        />
      </div>

      <button
      data-cy="save-product"
        type="submit"
        className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition"
      >
        {submitLabel}
      </button>
    </form>
  );
}