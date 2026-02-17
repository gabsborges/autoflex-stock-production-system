package com.autoflex.resource;

import com.autoflex.dto.ApiResponse;
import com.autoflex.dto.RawMaterialRequestDTO;
import com.autoflex.dto.RawMaterialResponseDTO;
import com.autoflex.service.RawMaterialService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/raw-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RawMaterialResource {

    @Inject
    RawMaterialService service;

    @POST
    public ApiResponse<RawMaterialResponseDTO> create(@Valid RawMaterialRequestDTO dto) {
        return new ApiResponse<>(service.create(dto));
    }

    @GET
    public ApiResponse<List<RawMaterialResponseDTO>> list() {
        return new ApiResponse<>(service.listAll());
    }

    @GET
    @Path("/{id}")
    public ApiResponse<RawMaterialResponseDTO> findById(@PathParam("id") Long id) {
        return new ApiResponse<>(service.findById(id));
    }

    @PUT
    @Path("/{id}")
    public ApiResponse<RawMaterialResponseDTO> update(
            @PathParam("id") Long id,
            @Valid RawMaterialRequestDTO dto) {

        return new ApiResponse<>(service.update(id, dto));
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.ok().build();
    }
}
