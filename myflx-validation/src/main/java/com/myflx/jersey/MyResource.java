package com.myflx.jersey;

import javax.websocket.server.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;


@Path("service")
public class MyResource {
    @Path("{sub_path}")
    @GET
    public String getResource(@PathParam("sub_path") String resourceName) {
        return "out:" + resourceName;
    }

}
