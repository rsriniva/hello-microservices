package com.redhat.training.msa.hola.health;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

@ApplicationScoped
@Health
public class HolaHealth implements HealthCheck {

	@Override
	public HealthCheckResponse call() {
		return HealthCheckResponse.named("hola service")
				.up().build();
	}

}
