/**
 * Copyright 2016, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.training.msa.hola;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.faulttolerance.Fallback;

import io.swagger.annotations.ApiOperation;

@Path("/")
public class HolaResource {

    @Inject
    @WithoutTracing
    private AlohaService alohaService;

    @Context
    private SecurityContext securityContext;

    @Context
    private HttpServletRequest servletRequest;

    @GET
    @Path("/hola")
    @Produces("text/plain")
    @ApiOperation("Returns the greeting in Spanish")
    public String hola() {
        String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
        return String.format("Hola de %s", hostname);

    }

    @GET
    @Path("/hola-chaining")
    @Produces("application/json")
    @ApiOperation("Returns the greeting plus the next service in the chain")
    @Fallback(AlohaServiceFallback.class)
    public List<String> holaChaining() {
        List<String> greetings = new ArrayList<>();
        greetings.add(hola());
        greetings.addAll(alohaService.aloha());
        return greetings;
    }

}
