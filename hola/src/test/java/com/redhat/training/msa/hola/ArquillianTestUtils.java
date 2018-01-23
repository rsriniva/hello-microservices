package com.redhat.training.msa.hola;

import java.io.File;
import java.util.Properties;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.wildfly.swarm.Swarm;

public class ArquillianTestUtils {

	public static WebArchive deploy() {

		File[] deps = Maven.resolver()
				.loadPomFromFile("pom.xml")
				.importDependencies(ScopeType.COMPILE,ScopeType.RUNTIME,ScopeType.TEST).resolve()
				.withTransitivity().asFile();
		WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "test.war")
				.addPackages(true, "com.redhat.training.msa")
				.addAsLibraries(deps)
				.addAsResource("project-defaults.yml","project-defaults.yml")
				.addAsWebInfResource(EmptyAsset.INSTANCE,"beans.xml")
	            .addAsWebInfResource("jwt-roles.properties", "classes/jwt-roles.properties")
                .addAsManifestResource("privateKey.pem","classes/privateKey.pem")
                .addAsWebInfResource("alumni.json","classes/alumni.json")
                .addAsWebInfResource("unregistered.json","classes/unregistered.json")
				.addAsManifestResource("META-INF/microprofile-config.properties","microprofile-config.properties");
		
		System.out.println(webArchive.toString(true));

		return webArchive;
	}

	public static Swarm newContainer() throws Exception {
		Properties properties = new Properties();
		properties.put("swarm.http.port", 8080);
		Swarm swarm = new Swarm(properties);
		return swarm.withProfile("defaults");
	}


}
