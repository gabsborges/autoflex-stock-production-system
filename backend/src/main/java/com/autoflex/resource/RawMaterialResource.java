package com.autoflex.resource;

import com.autoflex.dto.ApiResponse;
import com.autoflex.dto.RawMaterialRequestDTO;
import com.autoflex.dto.RawMaterialResponseDTO;
import com.autoflex.entity.RawMaterial;
import com.autoflex.service.RawMaterialService;
import com.autoflex.mapper.RawMaterialMapper;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
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
        RawMaterial rm = service.findEntityById(id);
        RawMaterialResponseDTO dto = RawMaterialMapper.toResponse(rm);
        return new ApiResponse<>(dto);
    }
}
