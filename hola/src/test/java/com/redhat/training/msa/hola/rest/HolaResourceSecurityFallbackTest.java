package com.redhat.training.msa.hola.rest;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.UUID;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.arquillian.CreateSwarm;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.redhat.training.msa.hola.ArquillianTestUtils;

@RunWith(Arquillian.class)
public class HolaResourceSecurityFallbackTest {
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

	@Rule
	public MockitoRule rule = MockitoJUnit.rule();

	@Inject
	private HolaResource resource;

	private String bearerTokenString = UUID.randomUUID().toString();

	@Deployment
	public static WebArchive createWebArchive() {
		return ArquillianTestUtils.deploy();
	}

	@CreateSwarm
	public static Swarm newContainer() throws Exception {
		return ArquillianTestUtils.newContainer();
	}

	@Test
	public void testSecureEndpointWithCredentials() throws Exception {
		
		stubFor(get(urlEqualTo("/hola-secure"))
				.withHeader("Authorization", WireMock.equalTo("Bearer " + bearerTokenString))
				.willReturn(aResponse().withStatus(200).withBody("{}")));
		SecurePackage secureHola = resource.secureHola();
		assertThat(secureHola, notNullValue());

		verify(getRequestedFor(urlEqualTo("/hola-secure")));

	}
}
