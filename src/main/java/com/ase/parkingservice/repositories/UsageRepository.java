package com.ase.parkingservice.repositories;

import java.time.LocalDate;
import java.util.Optional;

import com.ase.parkingservice.entities.Usage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsageRepository extends JpaRepository<Usage, Integer> {
  Optional<Usage> findByParkingLotIdAndDate(Integer parkingLotId, LocalDate date);
}
