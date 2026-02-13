package com.autoflex.resource;

import com.autoflex.entity.User;
import com.autoflex.service.UserService;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @POST
    public Response createUser(UserRequest request) {
        User user = userService.createUser(request.username, request.password);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @PATCH
    @Path("/{id}/role")

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateRole(@PathParam("id") Long id, RoleRequest request) {
        if (request == null || request.role == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Role is required").build();
        }

        User user = User.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found").build();
        }
        userService.updateRole(user, request.role);
        return Response.ok(user).build();
    }

    public static class RoleRequest {
        public String role;
    }

    public static class UserRequest {
        public String username;
        public String password;
    }
}
