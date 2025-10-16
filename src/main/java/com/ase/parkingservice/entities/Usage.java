package com.ase.parkingservice.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "usage")
public class Usage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "parking_lot_id", nullable = false)
  private Integer parkingLotId;

  @Column(name = "date", nullable = false)
  private LocalDate date;

  @Column(name = "used_parking_lots", nullable = false)
  private Integer usedParkingLots = 0;

  // Getter und Setter
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getParkingLotId() {
    return parkingLotId;
  }

  public void setParkingLotId(Integer parkingLotId) {
    this.parkingLotId = parkingLotId;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public Integer getUsedParkingLots() {
    return usedParkingLots;
  }

  public void setUsedParkingLots(Integer usedParkingLots) {
    this.usedParkingLots = usedParkingLots;
  }
}
