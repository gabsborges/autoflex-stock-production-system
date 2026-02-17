package com.autoflex.resource;

import com.autoflex.dto.ApiResponse;
import com.autoflex.dto.StockMovementRequestDTO;
import com.autoflex.dto.StockMovementResponseDTO;
import com.autoflex.service.StockMovementService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/stock-movements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StockMovementResource {

    @Inject
    StockMovementService service;

    @POST
    public ApiResponse<StockMovementResponseDTO> create(@Valid StockMovementRequestDTO request) {
        return new ApiResponse<>(service.create(request));
    }

    @GET
    @Path("/product/{productId}")
    public ApiResponse<List<StockMovementResponseDTO>> listByProduct(@PathParam("productId") Long productId) {
        return new ApiResponse<>(service.listByProduct(productId));
    }

    @GET
    @Path("/product/{productId}/current-stock")
    public ApiResponse<Integer> getCurrentStock(@PathParam("productId") Long productId) {
        return new ApiResponse<>(service.getCurrentStock(productId));
    }
}
