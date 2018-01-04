package com.redhat.training.msa.hola.client;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.redhat.training.msa.hola.rest.AlohaService;
import com.redhat.training.msa.hola.tracing.WithoutTracing;

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
