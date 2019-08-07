package com.rosato.cleaner.api.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rosato.cleaner.api.controllers.CleanerController.OptimizationRequest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class CleanerControllerTest {
  // bind the above RANDOM_PORT
  @LocalServerPort
  private int port;

  @Autowired
  private WebApplicationContext context;

  private MockMvc mvc;

  @BeforeAll
  public void setup() {
    mvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @Test
  public void testOptimizeFirstCase() throws Exception {
    OptimizationRequest req = new OptimizationRequest();
    req.setRooms(Arrays.asList(35, 21, 17, 28));
    req.setSenior(10);
    req.setJunior(6);

    String expectedResponse = "[{\"senior\":3,\"junior\":1},{\"senior\":1,\"junior\":2},{\"senior\":2,\"junior\":0},{\"senior\":1,\"junior\":3}]";

    String url = new URL("http://localhost:" + port + "/v1/cleaners/optimize").toString();

    ObjectMapper objectMapper = new ObjectMapper();
    MockHttpServletResponse mockHttpServletResponse = mvc
        .perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk()).andReturn().getResponse();
    String response = mockHttpServletResponse.getContentAsString();

    System.out.println(response);
    List<Map<String, Integer>> result = objectMapper.readValue(response,
        new TypeReference<List<Map<String, Integer>>>() {
        });

    assertEquals(4, result.size());
    assertEquals(expectedResponse, response);
  }

  @Test
  public void testOptimizeSecondCase() throws Exception {
    OptimizationRequest req = new OptimizationRequest();
    req.setRooms(Arrays.asList(24, 28));
    req.setSenior(11);
    req.setJunior(6);

    String expectedResponse = "[{\"senior\":2,\"junior\":1},{\"senior\":2,\"junior\":1}]";

    String url = new URL("http://localhost:" + port + "/v1/cleaners/optimize").toString();

    ObjectMapper objectMapper = new ObjectMapper();
    MockHttpServletResponse mockHttpServletResponse = mvc
        .perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk()).andReturn().getResponse();
    String response = mockHttpServletResponse.getContentAsString();

    System.out.println(response);
    List<Map<String, Integer>> result = objectMapper.readValue(response,
        new TypeReference<List<Map<String, Integer>>>() {
        });

    assertEquals(2, result.size());
    assertEquals(expectedResponse, response);
  }
}