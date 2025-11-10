package com.ase.parkingservice.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ase.parkingservice.entities.Usage;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service zum Abrufen und Verarbeiten externer API-Daten.
 */
@Service
public class ExternalApiService {

    private final WebClient webClient;

    public ExternalApiService(WebClient webClient) {
        this.webClient = webClient;
    }
  
    public List<ExternalParkingDto> fetchParkingData() {
        return webClient.get()
            .uri("https://sau-portal.de/team-11-api/api/v1/users?withDetails=false")
            .retrieve()
            .bodyToFlux(EmployeeDto.class)
            .collectList()
            .block(); // block() = synchroner Aufruf (für klassische Spring Services)
    }
}
