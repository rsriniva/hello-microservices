package com.redhat.training.msa.aloha.health;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

@ApplicationScoped
@Health
public class AlohaHealth implements HealthCheck {

	@Override
	public HealthCheckResponse call() {
		return HealthCheckResponse.named("aloha service")
				.up().build();
	}

}
