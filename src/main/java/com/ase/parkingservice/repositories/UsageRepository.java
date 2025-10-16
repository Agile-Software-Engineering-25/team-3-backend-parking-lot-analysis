package com.ase.parkingservice.repositories;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ase.parkingservice.entities.Usage;

public interface UsageRepository extends JpaRepository<Usage, Integer> {
  Optional<Usage> findByParkingLotIdAndDate(Integer parkingLotId, LocalDate date);
}
