package com.redhat.training.msa.hola;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public class ClientConfiguration {

	@Produces
	@Singleton
	@WithoutTracing
	public AlohaService alohaService() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://aloha:8080/api");
		ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
		AlohaService service = rtarget.proxy(AlohaService.class);
		return service;
	}
}
