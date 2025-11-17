package com.ase.parkingservice.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ase.parkingservice.services.ExternalApiService;


@RestController
@RequestMapping("/api/parkingservice/update")
public class updateController {
  private final ExternalApiService externalApiService;

  public RootController(ExternalApiService externalApiService) {
    this.externalApiService = externalApiService;
  }

  @PostMapping
  public Update update() {
    return externalApiService.update();
  }

}
