package com.redhat.training.msa.hola.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.arquillian.CreateSwarm;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.redhat.training.msa.hola.ArquillianTestUtils;

@RunWith(Arquillian.class)
public class HolaResourceTest {

	
	@Deployment
	public static WebArchive createWebArchive() {
		return ArquillianTestUtils.deploy();
	}
	

	@CreateSwarm
	public static Swarm newContainer() throws Exception {
		return ArquillianTestUtils.newContainer();
	}


    @Rule
    public MockitoRule rule = MockitoJUnit.rule();


 

    @Test
    public void testHola() throws Exception {
    	given().when().get("/api/hola").then().body(containsString("Hola"));
    }


}