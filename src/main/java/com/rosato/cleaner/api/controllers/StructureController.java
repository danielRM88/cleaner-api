package com.rosato.cleaner.api.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/structures")
public class StructureController {

  public static class OptimizationRequest {
    List<Integer> rooms;
    Integer senior;
    Integer junior;

    public List<Integer> getRooms() {
      return this.rooms;
    }

    public void setRooms(List<Integer> rooms) {
      this.rooms = rooms;
    }

    public Integer getSenior() {
      return this.senior;
    }

    public void setSenior(Integer senior) {
      this.senior = senior;
    }

    public Integer getJunior() {
      return this.junior;
    }

    public void setJunior(Integer junior) {
      this.junior = junior;
    }
  }

  @PostMapping("/optimize")
  @ResponseStatus(HttpStatus.OK)
  public List<Map<String, Integer>> optimize(@Valid @RequestBody OptimizationRequest req) {
    List<Integer> rooms = req.rooms;
    Integer seniorCapacity = req.senior;
    Integer juniorCapacity = req.junior;

    List<Map<String, Integer>> response = new ArrayList<>();

    for (Integer room : rooms) {
      Integer minDifference = Integer.MAX_VALUE;
      Integer seniors = 1;
      Integer juniors = 0;
      Map<String, Integer> mapping = new HashMap<>();

      Integer selectedJuniors = juniors;
      Integer selectedSeniors = seniors;
      do {
        juniors = (int) Math.ceil((room - (seniorCapacity * seniors)) / juniorCapacity.doubleValue());

        if (juniors < 0) {
          juniors = 0;
        }

        Integer difference = ((seniorCapacity * seniors) + (juniorCapacity * juniors) - room);
        if (difference < minDifference) {
          minDifference = difference;
          selectedJuniors = juniors;
          selectedSeniors = seniors;

          if (difference == 0) {
            break;
          }
        }

        seniors++;
      } while (juniors > 0);

      mapping.put("senior", selectedSeniors);
      mapping.put("junior", selectedJuniors);

      response.add(mapping);
    }

    return response;
  }

}