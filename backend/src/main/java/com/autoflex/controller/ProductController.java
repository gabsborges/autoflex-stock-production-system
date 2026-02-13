package com.autoflex.controller;

import com.autoflex.dto.ApiResponse;
import com.autoflex.dto.ProductRequestDTO;
import com.autoflex.dto.ProductResponseDTO;
import com.autoflex.entity.Product;
import com.autoflex.mapper.ProductMapper;
import com.autoflex.service.ProductService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    ProductService service;

    @POST
    public ApiResponse<ProductResponseDTO> create(@Valid ProductRequestDTO request) {
        Product product = ProductMapper.toEntity(request);
        Product saved = service.create(product);
        return new ApiResponse<>(ProductMapper.toResponse(saved));
    }

    @GET
    public ApiResponse<List<ProductResponseDTO>> list() {
        List<ProductResponseDTO> products = service.listAll()
                .stream()
                .map(ProductMapper::toResponse)
                .toList();

        return new ApiResponse<>(products);
    }

    @GET
    @Path("/paged")
    public ApiResponse<List<ProductResponseDTO>> listPaged(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {

        List<ProductResponseDTO> products = service.listPaged(page, size)
                .stream()
                .map(ProductMapper::toResponse)
                .toList();

        return new ApiResponse<>(products);
    }

    @GET
    @Path("/sku/{sku}")
    public ApiResponse<ProductResponseDTO> findBySku(@PathParam("sku") String sku) {
        Product product = service.findBySku(sku);
        return new ApiResponse<>(ProductMapper.toResponse(product));
    }
}
