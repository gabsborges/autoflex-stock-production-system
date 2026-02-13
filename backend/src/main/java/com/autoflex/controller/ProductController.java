package com.autoflex.controller;

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
import java.util.stream.Collectors;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    ProductService service;

    @POST
    public ProductResponseDTO create(@Valid ProductRequestDTO request) {
        Product product = ProductMapper.toEntity(request);
        Product saved = service.create(product);
        return ProductMapper.toResponse(saved);
    }

    @GET
    public List<ProductResponseDTO> list() {
        return service.listAll()
                .stream()
                .map(ProductMapper::toResponse)
                .collect(Collectors.toList());
    }
}
