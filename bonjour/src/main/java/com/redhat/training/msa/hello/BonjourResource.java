package com.redhat.training.msa.hello;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/api")
@Component
public class BonjourResource {

    @Autowired
    private BonjourService service;

    @Context
    private HttpServletRequest servletRequest;

    @GET
    @Path("/bonjour")
    @Produces(MediaType.TEXT_PLAIN)
    public String bonjour() {
        String hostname = servletRequest.getServerName(); 
        return service.bonjour() + hostname + "\n";
    }

}
