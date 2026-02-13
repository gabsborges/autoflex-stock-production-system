package com.autoflex.controller;

import java.util.List;

import com.autoflex.dto.ApiResponse;
import com.autoflex.dto.StockMovementRequestDTO;
import com.autoflex.dto.StockMovementResponseDTO;
import com.autoflex.service.StockMovementService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/stock")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StockMovementController {

    @Inject
    StockMovementService service;

    @GET
    @Path("/product/{productId}")
    public ApiResponse<List<StockMovementResponseDTO>> history(
            @PathParam("productId") Long productId) {

        return new ApiResponse<>(service.listByProduct(productId));
    }

    @POST
    public ApiResponse<String> register(@Valid StockMovementRequestDTO dto) {
        service.registerMovement(dto);
        return new ApiResponse<>("Stock movement registered successfully");
    }

}
