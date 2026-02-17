package com.autoflex.resource;

import com.autoflex.dto.ApiResponse;
import com.autoflex.dto.ProductProducibleDTO;
import com.autoflex.dto.ProductRequestDTO;
import com.autoflex.dto.ProductResponseDTO;
import com.autoflex.service.ProductService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService service;

    @POST
    public ApiResponse<ProductResponseDTO> create(@Valid ProductRequestDTO request) {
        ProductResponseDTO created = service.create(request);
        return new ApiResponse<>(created);
    }

    @GET
    public ApiResponse<List<ProductResponseDTO>> listAll() {
        List<ProductResponseDTO> products = service.listAll();
        return new ApiResponse<>(products);
    }

    @GET
    @Path("/paged")
    public ApiResponse<List<ProductResponseDTO>> listPaged(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("sortBy") @DefaultValue("name") String sortBy,
            @QueryParam("direction") @DefaultValue("asc") String direction) {

        if (size <= 0) {
            throw new BadRequestException("Size must be greater than zero");
        }
        if (!"asc".equalsIgnoreCase(direction) && !"desc".equalsIgnoreCase(direction)) {
            throw new BadRequestException("Invalid direction, should be 'asc' or 'desc'");
        }

        List<ProductResponseDTO> products = service.listPaged(page, size, sortBy, direction);
        return new ApiResponse<>(products);
    }

    @GET
    @Path("/sku/{sku}")
    public ApiResponse<ProductResponseDTO> findBySku(@PathParam("sku") String sku) {
        ProductResponseDTO product = service.findBySku(sku);
        return new ApiResponse<>(product);
    }

    @GET
    @Path("/{id}")
    public ApiResponse<ProductResponseDTO> findById(@PathParam("id") Long id) {
        ProductResponseDTO product = service.findById(id);
        return new ApiResponse<>(product);
    }

    @GET
    @Path("/production-suggestions")
    public List<ProductProducibleDTO> getProductionSuggestions() {
        return service.listProducibleProducts();
    }

    @PUT
    @Path("/{id}")
    public ApiResponse<ProductResponseDTO> update(
            @PathParam("id") Long id,
            @Valid ProductRequestDTO request) {

        ProductResponseDTO updated = service.update(id, request);
        return new ApiResponse<>(updated);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.ok().build();
    }
}
