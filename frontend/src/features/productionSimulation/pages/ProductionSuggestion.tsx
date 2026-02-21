import { useGetProductionSuggestionsQuery } from "../../products/productsApi";

export default function ProductionSuggestion() {
  const { data, isLoading, isError } = useGetProductionSuggestionsQuery();

  const products = data?.products ?? [];
  const grandTotal = data?.grandTotal ?? 0;

  if (isLoading) return <p>Loading production plan...</p>;
  if (isError) return <p>Error loading production plan.</p>;

  return (
    <div className="max-w-6xl mx-auto p-6 bg-white rounded shadow">
      <h1 className="text-xl font-bold mb-6">
        Production Plan (Priority by Highest Value)
      </h1>

      <div className="mb-6 text-lg font-bold text-green-700">
        Total Production Value: ${grandTotal}
      </div>

      <table className="w-full border border-gray-200 rounded">
        <thead>
          <tr className="bg-gray-100 text-gray-600 text-left text-sm uppercase">
            <th className="py-3 px-4 border-b">Product</th>
            <th className="py-3 px-4 border-b">SKU</th>
            <th className="py-3 px-4 border-b">Quantity Producible</th>
            <th className="py-3 px-4 border-b">Unit Price</th>
            <th className="py-3 px-4 border-b">Total Value</th>
          </tr>
        </thead>
        <tbody>
          {products.length === 0 ? (
            <tr>
              <td colSpan={5} className="py-6 text-center text-gray-400">
                No products can be produced with current stock.
              </td>
            </tr>
          ) : (
            products.map((item) => (
              <tr
                key={item.id}
                className="border-b border-gray-200 hover:bg-gray-50"
              >
                <td className="py-3 px-4 font-medium">{item.name}</td>
                <td className="py-3 px-4">{item.sku}</td>
                <td className="py-3 px-4 font-semibold">
                  {item.quantityProducible}
                </td>
                <td className="py-3 px-4">
                  ${item.price}
                </td>
                <td className="py-3 px-4 text-green-700 font-bold">
                  ${item.totalValue}
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}