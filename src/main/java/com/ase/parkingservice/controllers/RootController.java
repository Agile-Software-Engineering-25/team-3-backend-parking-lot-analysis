package com.ase.parkingservice.controllers;

import com.ase.parkingservice.entities.Usage;
import com.ase.parkingservice.services.UsageService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/parkservice")
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
