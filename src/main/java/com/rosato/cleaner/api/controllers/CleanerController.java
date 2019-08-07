package com.rosato.cleaner.api.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.rosato.cleaner.api.models.Cleaner;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/cleaners")
public class CleanerController {

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
    Cleaner cleaner = new Cleaner(seniorCapacity, juniorCapacity);

    List<Map<String, Integer>> response = new ArrayList<>();

    for (Integer room : rooms) {
      Map<String, Integer> mapping = cleaner.optimize(room);

      response.add(mapping);
    }

    return response;
  }

}