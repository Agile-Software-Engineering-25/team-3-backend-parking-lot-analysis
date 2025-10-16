package com.ase.parkingservice.services;

import com.ase.parkingservice.entities.Usage;
import com.ase.parkingservice.repositories.UsageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
public class UsageService {

  private final UsageRepository repository;

  public UsageService(UsageRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public Usage incrementUsage(Integer parkingLotId, LocalDate date) {
    Usage usage = repository.findByParkingLotIdAndDate(parkingLotId, date)
        .orElseGet(() -> {
          Usage u = new Usage();
          u.setParkingLotId(parkingLotId);
          u.setDate(date);
          u.setUsedParkingLots(0);
          return u;
        });
    usage.setUsedParkingLots(usage.getUsedParkingLots() + 1);
    return repository.save(usage);
  }

  @Transactional
  public Usage decrementUsage(Integer parkingLotId, LocalDate date) {
    Usage usage = repository.findByParkingLotIdAndDate(parkingLotId, date)
        .orElseThrow(() -> new IllegalArgumentException("Eintrag nicht gefunden"));
    int current = usage.getUsedParkingLots();
    if (current > 0) {
      usage.setUsedParkingLots(current - 1);
      return repository.save(usage);
    }
    throw new IllegalStateException("Kann nicht weiter dekrementieren");
  }
}
