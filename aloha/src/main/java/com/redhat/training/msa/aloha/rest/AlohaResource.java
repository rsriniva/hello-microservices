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
package com.redhat.training.msa.aloha.rest;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/")
@Api("aloha")
public class AlohaResource {

	private final Logger log = LoggerFactory.getLogger(AlohaResource.class);
	
	@Inject
	@ConfigProperty(name = "pauseTime", defaultValue = "0")
	private Integer pauseTime;
	
    @Context
    private SecurityContext securityContext;

    @Context
    private HttpServletRequest servletRequest;

    @PostConstruct
    private void init() {
    		log.info(String.format("Aloha will pause for %d milliseconds in its endpoints", pauseTime));
    }
    
    @GET
    @Path("/aloha")
    @Produces("text/plain")
    @ApiOperation("Returns the greeting in Hawaiian")
    public String hola() {
    		if (pauseTime > 0) {
    			try { Thread.sleep(pauseTime); } catch(Exception e) {};
    		}
        String hostname = servletRequest.getServerName(); 
        return String.format("Aloha mai %s", hostname);

    }

}
