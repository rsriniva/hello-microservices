package com.redhat.training.msa.hola.rest;

import static org.junit.Assert.fail;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.arquillian.CreateSwarm;

import com.redhat.training.msa.hola.TestUtils;

@RunWith(Arquillian.class)
public class RestApiTest {

	
	@Deployment
	public static WebArchive createWebArchive() {
		return TestUtils.deploy();
	}
	

	@CreateSwarm
	public static Swarm newContainer() throws Exception {
		return TestUtils.newContainer();
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}