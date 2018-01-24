package com.redhat.training.msa.hola.rest;


import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.arquillian.CreateSwarm;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.redhat.training.msa.hola.ArquillianTestUtils;
@RunWith(Arquillian.class)
public class HolaResourceNoFallbackTest {
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(options().port(7070));

		
	@Deployment
	public static WebArchive createWebArchive() {
		return ArquillianTestUtils.deploy().addAsManifestResource("config-test.properties","microprofile-config.properties");
	}
	
	@CreateSwarm
	public static Swarm newContainer() throws Exception {
		return ArquillianTestUtils.newContainer();
	}
	@Test
	public void testNonFallback() {
		wireMockRule.stubFor(get(urlMatching("/api/aloha"))
		        .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody("Aloha [MOCK]")));
		given().when().get("/api/hola-chaining").then().body(containsString("Aloha [MOCK]"));
	
	}

}
