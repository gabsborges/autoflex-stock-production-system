import { configureStore } from "@reduxjs/toolkit";
import { productsApi } from "../features/products/productsApi";
import { rawMaterialsApi } from "../features/rawMaterials/rawMaterialsApi";

export const store = configureStore({
  reducer: {
    [productsApi.reducerPath]: productsApi.reducer,
    [rawMaterialsApi.reducerPath]: rawMaterialsApi.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(productsApi.middleware, rawMaterialsApi.middleware),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
