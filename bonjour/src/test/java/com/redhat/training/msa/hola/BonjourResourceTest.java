package com.redhat.coolstore.cart.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = com.redhat.training.msa.hello.Application.class
)
public class BonjourResourceTest {

    @Value("${local.server.port}")
    private int port;

    @Before
    public void beforeTest() {
        RestAssured.baseURI = String.format("http://localhost:%d", port);
    }

    @Test
    public void invokeHealthCheck() throws Exception {
        given().get("/health").then().assertThat().statusCode(200).body(containsString("UP"));
    }

    @Test
    public void invokeHello() throws Exception {
        given().get("/api/bonjour").then().assertThat().statusCode(200).body(containsString("Bonjour"));
    }

}
