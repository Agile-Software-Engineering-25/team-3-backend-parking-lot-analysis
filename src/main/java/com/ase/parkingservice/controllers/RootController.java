package com.ase.parkingservice.controllers;

import java.time.LocalDate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ase.parkingservice.entities.Usage;
import com.ase.parkingservice.services.UsageService;

@RestController
@RequestMapping("/parkservice")
public class RootController {
  private final UsageService usageService;

  public RootController(UsageService usageService) {
    this.usageService = usageService;
  }

  @PostMapping
  public Usage park(@RequestParam Integer parkingLotId, @RequestParam String date) {
    LocalDate localDate = LocalDate.parse(date);
    return usageService.incrementUsage(parkingLotId, localDate);
  }

  @DeleteMapping
  public Usage leave(@RequestParam Integer parkingLotId, @RequestParam String date) {
    LocalDate localDate = LocalDate.parse(date);
    return usageService.decrementUsage(parkingLotId, localDate);
  }
}
