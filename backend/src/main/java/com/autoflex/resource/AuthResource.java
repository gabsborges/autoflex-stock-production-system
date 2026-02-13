package com.autoflex.resource;

import com.autoflex.service.AuthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    public Response login(Map<String, String> body) {

        String username = body.get("username");
        String password = body.get("password");

        String token = authService.login(username, password);

        return Response.ok(Map.of("token", token)).build();
    }
}
