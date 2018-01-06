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
package com.redhat.training.msa.hola.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Metric;
import org.eclipse.microprofile.metrics.annotation.Timed;

import com.redhat.training.msa.hola.tracing.WithoutTracing;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/")
@Api("hola")
@ApplicationScoped
public class HolaResource {

    @Inject
    @WithoutTracing
    private AlohaService alohaService;

    @Context
    private SecurityContext securityContext;

    @Context
    private HttpServletRequest servletRequest;

    @Inject
    @Metric(name = "requestCount", description = "Total endpoint requests made to the Hola microservice",
    		displayName="HolaResource#requestCount", absolute=true)
    private Counter requestCounter;

	@Inject
	@Metric(name = "failureCount", description = "Total chained endpoint failures encountered",
    		displayName="HolaResource#failureCount", absolute=true)
	private Counter failedCount;

    @GET
    @Path("/hola")
    @Produces("text/plain")
    @ApiOperation("Returns the greeting in Spanish")
    @Timed
    @PermitAll
    public String hola() {
    		requestCounter.inc();
        String hostname = servletRequest.getServerName();
        return String.format("Hola de %s", hostname);
    }

    @GET
    @Path("/hola-chaining")
    @Produces("application/json")
    @ApiOperation("Returns the greeting plus the next service in the chain")
    @Timed(absolute=true, unit = MetricUnits.MILLISECONDS, name = "holaChainingTimer",
    		displayName = "holaChainingTimer", description = "Invocation time for the holaChaining endpoint")
    @Fallback(fallbackMethod="alohaFallback")
    @CircuitBreaker(successThreshold = 4, requestVolumeThreshold = 3,
    		failureRatio = 0.50, delay = 1000)
    @Timeout(1000)
    @PermitAll
    public List<String> holaChaining() {
    		requestCounter.inc();
        List<String> greetings = new ArrayList<>();
        greetings.add(hola());
        greetings.add(alohaService.aloha());
        return greetings;
    }

    @GET
    @Path("/hola-secure")
    @Produces("application/json")
    @RolesAllowed({"VIP", "Voter"})
    public SecurePackage secureHola() {
    		boolean isVIP = securityContext.isUserInRole("VIP");
    		JsonWebToken token = (JsonWebToken) securityContext.getUserPrincipal();
    		return new SecurePackage(token.getName(), new Date(token.getExpirationTime() * 1000).toString(), isVIP);
    }

    @SuppressWarnings("unused")
	private List<String> alohaFallback() {
		failedCount.inc();
	    List<String> greetings = new ArrayList<>();
	    greetings.add(hola());
	    greetings.add("Aloha fallback");
	    return greetings;
	}
}
