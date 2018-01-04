package com.redhat.training.msa.hola.client;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.redhat.training.msa.hola.rest.AlohaService;
import com.redhat.training.msa.hola.tracing.WithoutTracing;

public class ClientConfiguration {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientConfiguration.class);
    
	@Inject
	@ConfigProperty(name = "alohaPort", defaultValue="8080")
	private String alohaPort;
	
	@Inject
	@ConfigProperty(name = "alohaHostname", defaultValue="aloha")
	private String alohaHostname;

	@Produces
	@Singleton
	@WithoutTracing
	public AlohaService alohaService() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://" + alohaHostname + ":" + alohaPort + "/api");
		log.info("Aloha service is located at " + target.getUri());
		ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
		AlohaService service = rtarget.proxy(AlohaService.class);
		return service;
	}
}
