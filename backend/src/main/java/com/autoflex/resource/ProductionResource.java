package com.autoflex.resource;

import com.autoflex.dto.ApiResponse;
import com.autoflex.dto.ProductResponseDTO;
import com.autoflex.service.ProductionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

@Path("/production-suggestion")
@Produces(MediaType.APPLICATION_JSON)
public class ProductionResource {

    @Inject
    ProductionService service;

    @GET
    @Path("/suggest")
    public ApiResponse<Map<ProductResponseDTO, Integer>> suggestProductionAndConsume() {
        Map<ProductResponseDTO, Integer> suggestion = service.suggestProduction();
        return new ApiResponse<>(suggestion);
    }
}
