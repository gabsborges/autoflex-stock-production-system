package com.autoflex.resource;

import java.util.List;

import com.autoflex.dto.ApiResponse;
import com.autoflex.dto.ProductRawMaterialRequestDTO;
import com.autoflex.dto.ProductRawMaterialResponseDTO;
import com.autoflex.service.ProductRawMaterialService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/products/{productId}/raw-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductRawMaterialResource {

    @Inject
    ProductRawMaterialService service;

    @POST
    public ApiResponse<ProductRawMaterialResponseDTO> linkRawMaterial(
            @PathParam("productId") Long productId,
            @Valid ProductRawMaterialRequestDTO request
    ) {
        ProductRawMaterialResponseDTO response = service.create(productId, request);
        return new ApiResponse<>(response);
    }
    
    @GET
public ApiResponse<List<ProductRawMaterialResponseDTO>> listByProduct(@PathParam("productId") Long productId) {
    List<ProductRawMaterialResponseDTO> list = service.listByProduct(productId);
    return new ApiResponse<>(list);
}
}
