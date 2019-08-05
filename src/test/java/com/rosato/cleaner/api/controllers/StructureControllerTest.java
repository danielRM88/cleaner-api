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
import com.rosato.cleaner.api.controllers.StructureController.OptimizationRequest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class StructureControllerTest {
  // bind the above RANDOM_PORT
  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private WebApplicationContext context;

  private MockMvc mvc;

  @BeforeAll
  public void setup() {
    mvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @Test
  public void testOptimize() throws Exception {
    OptimizationRequest req = new OptimizationRequest();
    req.setRooms(Arrays.asList(1, 2, 3));
    req.setSenior(10);
    req.setJunior(6);

    HttpEntity<OptimizationRequest> entity = new HttpEntity<>(req);
    String url = new URL("http://localhost:" + port + "/v1/structures/optimize").toString();

    ObjectMapper objectMapper = new ObjectMapper();
    MockHttpServletResponse mockHttpServletResponse = mvc
        .perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk()).andReturn().getResponse();
    String response = mockHttpServletResponse.getContentAsString();

    List<Map<String, Integer>> result = objectMapper.readValue(response,
        new TypeReference<List<Map<String, Integer>>>() {
        });

    assertEquals(3, result.size());
    assertEquals(Integer.valueOf(2), result.get(0).get("senior"));
  }
}