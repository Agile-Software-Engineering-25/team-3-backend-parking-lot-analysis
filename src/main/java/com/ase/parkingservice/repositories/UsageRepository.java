package com.ase.parkingservice.repositories;

import com.ase.parkingservice.entities.Usage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface UsageRepository extends JpaRepository<Usage, Integer> {
  Optional<Usage> findByParkingLotIdAndDate(Integer parkingLotId, LocalDate date);
}
