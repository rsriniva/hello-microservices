package com.redhat.training.msa.hola;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashSet;
import java.util.Properties;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.keycloak.jose.jws.Algorithm;
import org.keycloak.jose.jws.JWSBuilder;
import org.keycloak.representations.AccessToken;
import org.keycloak.util.TokenUtil;
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
	            .addAsWebInfResource("keycloak.json", "keycloak.json")
	            .addAsWebInfResource("private.pem", "classes/private.pem")
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

    public static String getValidAccessToken(String role) throws Exception {
        return createAccessToken(role, (int) (System.currentTimeMillis() / 1000));
    }

    
 

    private static String createAccessToken(String role, int issuedAt) throws Exception {
        AccessToken token = new AccessToken();
        token.type(TokenUtil.TOKEN_TYPE_BEARER);
        token.subject("testuser");
        token.issuedAt(issuedAt);
        token.issuer("https://rhsso:8443/auth/realms/coolstore-test");
        token.expiration(issuedAt + 300);
        token.setAllowedOrigins(new HashSet<>());

        AccessToken.Access access = new AccessToken.Access();
        token.setRealmAccess(access);
        access.addRole(role);

        Algorithm jwsAlgorithm = Algorithm.RS256;
        PrivateKey privateKey = readPrivateKey();
        String encodedToken = new JWSBuilder().type("JWT").jsonContent(token).sign(jwsAlgorithm, privateKey);
        return encodedToken;
    }
    
    private static PrivateKey readPrivateKey() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyFactory factory = KeyFactory.getInstance("RSA", "BC");
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("private.pem");
        PemReader privateKeyReader = new PemReader(new InputStreamReader(is));
        try {
            PemObject privObject = privateKeyReader.readPemObject();
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privObject.getContent());
            PrivateKey privateKey = factory.generatePrivate(privKeySpec);
            return privateKey;
        } finally {
            privateKeyReader.close();
        }
    }


}
