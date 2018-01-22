package com.redhat.training.msa.hola.rest;

import static io.restassured.RestAssured.given;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.arquillian.CreateSwarm;

import com.redhat.training.msa.hola.ArquillianTestUtils;

@RunWith(Arquillian.class)
public class HolaResourceSecurityFallbackTest {


	@Deployment
	public static WebArchive createWebArchive() {
		return ArquillianTestUtils.deploy();
	}

	@CreateSwarm
	public static Swarm newContainer() throws Exception {
		return ArquillianTestUtils.newContainer();
	}


    @Test
    @RunAsClient
    public void testSecureEndpointWithCredentials() throws Exception {
    	given()
    	.when()
    		.auth()
    		.oauth2(ArquillianTestUtils.getValidAccessToken("XXXX"))
    		.get("/api/hola-secure")
    	.then()
    		.statusCode(403);
    }
}
