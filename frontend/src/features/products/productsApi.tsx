import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

// --- Interfaces ---
export interface Product {
  id: string;
  name: string;
  sku: string;
  price: number;
  quantity: number;
}

export interface ProductRequestDTO {
  sku: string;
  name: string;
  price: number;
  quantity: number;
}

export interface ProductUpdateRequestDTO {
  sku: string;
  name: string;
  price: number;
  quantity: number;
}

export interface ProductRawMaterialRequestDTO {
  rawMaterialId: number;
  quantityRequired: number;
}

export interface ProductRawMaterialResponseDTO {
  id: number; // vínculo do material com o produto
  rawMaterialId: number;
  quantityRequired: number;
}

export interface ProductDetail {
  id: string;
  sku: string;
  name: string;
  price: number;
  quantity: number;
  rawMaterials: {
    id: number;
    name: string;
    quantityRequired: number;
  }[];
}

export interface ProductProducibleDTO {
  id: number;
  name: string;
  sku: string;
  quantityProducible: number;
  unitPrice: number;
  totalValue: number;
}

export interface ProductionPlanResponseDTO {
  products: ProductProducibleDTO[];
  grandTotal: number;
}

export const productsApi = createApi({
  reducerPath: "productsApi",
  baseQuery: fetchBaseQuery({ baseUrl: "http://localhost:8080/" }),
  tagTypes: ["Products"],
  endpoints: (builder) => ({

    getProducts: builder.query<Product[], void>({
      query: () => "products",
      transformResponse: (response: { data: Product[] }) => response.data,
      providesTags: (result) =>
        result
          ? [...result.map((p) => ({ type: "Products" as const, id: p.id })), { type: "Products", id: "LIST" }]
          : [{ type: "Products", id: "LIST" }],
    }),


    getProductById: builder.query<ProductDetail, string>({
      query: (id) => `products/${id}`,
      transformResponse: (response: { data: ProductDetail }) => response.data,
      providesTags: (result, error, id) => [{ type: "Products", id }],
    }),


    updateProduct: builder.mutation<ProductDetail, { id: string; body: ProductUpdateRequestDTO }>({
      query: ({ id, body }) => ({
        url: `products/${id}`,
        method: "PUT",
        body,
      }),
      invalidatesTags: (result, error, { id }) => [{ type: "Products", id }],
    }),


    createProduct: builder.mutation<Product, ProductRequestDTO>({
      query: (body) => ({
        url: "products",
        method: "POST",
        body,
      }),
      transformResponse: (response: { data: Product }) => response.data,
      invalidatesTags: [{ type: "Products", id: "LIST" }],
    }),

    getProductRawMaterials: builder.query<ProductRawMaterialResponseDTO[], string>({
      query: (productId) => `products/${productId}/raw-materials`,
      transformResponse: (response: { data: ProductRawMaterialResponseDTO[] }) => response.data,
      providesTags: (result, error, productId) =>
        result ? [...result.map((rm) => ({ type: "Products" as const, id: rm.id })), { type: "Products", id: productId }] : [{ type: "Products", id: productId }],
    }),

    linkRawMaterial: builder.mutation<ProductRawMaterialResponseDTO, { productId: string | number; body: ProductRawMaterialRequestDTO }>({
      query: ({ productId, body }) => ({
        url: `products/${productId}/raw-materials`,
        method: "POST",
        body,
      }),
      invalidatesTags: (result, error, { productId }) => [{ type: "Products", id: productId }],
    }),

    updateRawMaterial: builder.mutation<
      ProductRawMaterialResponseDTO,
      { productId: string | number; body: { rawMaterialId: number; quantityRequired: number } }
    >({
      query: ({ productId, body }) => ({
        url: `products/${productId}/raw-materials`,
        method: "PUT",
        body, 
      }),
      invalidatesTags: (result, error, { productId }) => [{ type: "Products", id: productId }],
    }),

    deleteProduct: builder.mutation<void, string>({
      query: (id) => ({
        url: `products/${id}`,
        method: "DELETE",
      }),
      invalidatesTags: [{ type: "Products", id: "LIST" }],
    }),


getProductionSuggestions: builder.query<ProductionPlanResponseDTO, void>({
  query: () => "products/production-suggestions",
}),
  }),
});


export const {
  useGetProductsQuery,
  useGetProductByIdQuery,
  useUpdateProductMutation,
  useCreateProductMutation,
  useGetProductRawMaterialsQuery,
  useLinkRawMaterialMutation,
  useUpdateRawMaterialMutation,
  useDeleteProductMutation,
  useGetProductionSuggestionsQuery,
} = productsApi;
