import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export interface RawMaterial {
  id: string;
  name: string;
  quantity: number;
}

export const rawMaterialsApi = createApi({
  reducerPath: "rawMaterialsApi",
  baseQuery: fetchBaseQuery({ baseUrl: "http://localhost:8080/" }), // Ajuste o endpoint da API
  tagTypes: ["RawMaterials"],
  endpoints: (builder) => ({
    // POST - Criar material
    createRawMaterial: builder.mutation({
      query: (rawMaterial) => ({
        url: "raw-materials",
        method: "POST",
        body: rawMaterial,
      }),
      // Invalidates the list when a new material is created
      invalidatesTags: ["RawMaterials"],
    }),


    getRawMaterials: builder.query<RawMaterial[], void>({
      query: () => "raw-materials",
      providesTags: ["RawMaterials"],
    }),


    getRawMaterialsByID: builder.query<RawMaterial, string>({
      query: (id) => `raw-materials/${id}`,
      providesTags: ["RawMaterials"],
    }),

    updateRawMaterial: builder.mutation({
      query: ({ id, body }) => ({
        url: `raw-materials/${id}`,
        method: "PUT",
        body,
      }),
      invalidatesTags: ["RawMaterials"],
    }),
    deleteRawMaterial: builder.mutation<void, string>({
      query: (id) => ({
        url: `raw-materials/${id}`,
        method: "DELETE",
      }),
      invalidatesTags: ["RawMaterials"],
    }),
  }),
});

export const { useCreateRawMaterialMutation, useGetRawMaterialsQuery, useUpdateRawMaterialMutation, useDeleteRawMaterialMutation, useGetRawMaterialsByIDQuery } = rawMaterialsApi;