package com.rosato.cleaner.api.models;

import java.util.HashMap;
import java.util.Map;

public class Cleaner {
  private Integer seniorCapacity;
  private Integer juniorCapacity;

  public Cleaner(Integer seniorCapacity, Integer juniorCapacity) {
    this.seniorCapacity = seniorCapacity;
    this.juniorCapacity = juniorCapacity;
  }

  public Map<String, Integer> optimize(Integer roomCapacity) {
    Map<String, Integer> mapping = new HashMap<>();

    Integer minDifference = Integer.MAX_VALUE;
    Integer seniors = 1;
    Integer juniors = 0;
    Integer selectedJuniors = juniors;
    Integer selectedSeniors = seniors;

    do {
      juniors = (int) Math.ceil((roomCapacity - (this.seniorCapacity * seniors)) / this.juniorCapacity.doubleValue());

      if (juniors < 0) {
        juniors = 0;
      }

      Integer difference = ((this.seniorCapacity * seniors) + (this.juniorCapacity * juniors) - roomCapacity);
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

    return mapping;
  }
}