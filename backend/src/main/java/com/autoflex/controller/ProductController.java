package com.autoflex.controller;

import com.autoflex.entity.Product;
import com.autoflex.service.ProductService;
import jakarta.inject.Inject;
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
    public Product create(Product product) {
        return service.create(product);
    }

    @GET
    public List<Product> list() {
        return service.listAll();
    }
}
