package com.redhat.training.msa.hola.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

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
public class HolaResourceFallbackTest {

	
	@Deployment
	public static WebArchive createWebArchive() {
		return ArquillianTestUtils.deploy().addAsManifestResource("META-INF/microprofile-config.properties","microprofile-config.properties");
	}
	

	@CreateSwarm
	public static Swarm newContainer() throws Exception {
		return ArquillianTestUtils.newContainer();
	}


    @Test
    @RunAsClient
    public void testHola() throws Exception {
    	given().when().get("/api/hola").then().body(containsString("Hola de localhost"));
    }

    @Test
    @RunAsClient
    public void testFallbackMethodCall() throws InterruptedException {
    	//By default the method calls the fallback because the aloha microservice is not working
    	given().when().get("/api/hola-chaining").then().body(containsString("Aloha fallback"));
    }


    @Test
    @RunAsClient
    public void testSecureEndpointWithoutCredentials() throws InterruptedException {
    	given().when().get("/api/hola-secure").then().statusCode(401);
    }


}