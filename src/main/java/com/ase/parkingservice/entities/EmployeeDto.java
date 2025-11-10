package com.ase.parkingservice.entities;

import java.time.LocalDate;
import java.util.UUID;

public class EmployeeDto {
    private UUID id;
    private LocalDate dateOfBirth;
    private String address;
    private String phoneNumber;
    private String employeeNumber;
    private String fieldChair;
    private String title;
    private String employmentStatus;
    private boolean drivesCar;

    // Constructors
    public EmployeeDto() {}

    public EmployeeDto(UUID id, LocalDate dateOfBirth, String address, String phoneNumber,
                       String employeeNumber, String fieldChair, String title,
                       String employmentStatus, boolean drivesCar) {
        this.id = id;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.employeeNumber = employeeNumber;
        this.fieldChair = fieldChair;
        this.title = title;
        this.employmentStatus = employmentStatus;
        this.drivesCar = drivesCar;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getFieldChair() {
        return fieldChair;
    }

    public void setFieldChair(String fieldChair) {
        this.fieldChair = fieldChair;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public boolean isDrivesCar() {
        return drivesCar;
    }

    public void setDrivesCar(boolean drivesCar) {
        this.drivesCar = drivesCar;
    }

    @Override
    public String toString() {
        return "EmployeeDto{" +
                "id=" + id +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", employeeNumber='" + employeeNumber + '\'' +
                ", fieldChair='" + fieldChair + '\'' +
                ", title='" + title + '\'' +
                ", employmentStatus='" + employmentStatus + '\'' +
                ", drivesCar=" + drivesCar +
                '}';
    }
}
