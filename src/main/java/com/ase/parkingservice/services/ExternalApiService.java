package com.ase.parkingservice.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ase.parkingservice.entities.EmployeeDto;
import org.springframework.web.client.RestTemplate;

/**
 * Service zum Abrufen und Verarbeiten externer API-Daten.
 */
@Service
public class ExternalApiService {

    private final RestTemplate restTemplate;

    public ExternalApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<EmployeeDTO> fetchEmployeeData() {
          String url = "https://sau-portal.de/team-11-api/api/v1/users?withDetails=false";
          ResponseEntity<EmployeeDTO[]> response =
              restTemplate.getForEntity(url, EmployeeDto[].class);
  
          return List.of(response.getBody());
      }
}
