/**
 * Copyright 2017 Pivotal Software, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micrometer.jersey2.server.resources;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.micrometer.jersey2.server.exception.ResourceGoneException;

/**
 * @author Michael Weirauch
 */
@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class TestResource {

    @Produces(MediaType.TEXT_PLAIN)
    public static class SubResource {

        @GET
        @Path("sub-hello/{name}")
        public String hello(@PathParam("name") String name) {
            return "hello " + name;
        }

    }

    @GET
    public String index() {
        return "index";
    }

    @GET
    @Path("hello")
    public String hello() {
        return "hello";
    }

    @GET
    @Path("hello/{name}")
    public String hello(@PathParam("name") String name) {
        return "hello " + name;
    }

    @GET
    @Path("throws-not-found-exception")
    public String throwsNotFoundException() {
        throw new NotFoundException();
    }

    @GET
    @Path("throws-exception")
    public String throwsException() {
        throw new IllegalArgumentException();
    }

    @GET
    @Path("throws-webapplication-exception")
    public String throwsWebApplicationException() {
        throw new NotAuthorizedException("notauth", Response.status(Status.UNAUTHORIZED).build());
    }

    @GET
    @Path("throws-mappable-exception")
    public String throwsMappableException() {
        throw new ResourceGoneException("Resource has been permanently removed.");
    }

    @GET
    @Path("redirect/{status}")
    public Response redirect(@PathParam("status") int status) {
        if (status == 307) {
            throw new RedirectionException(status, URI.create("hello"));
        }
        return Response.status(status).header("Location", "/hello").build();
    }

    @Path("/sub-resource")
    public SubResource subResource() {
        return new SubResource();
    }

}
