package com.eqrid.eqrid_website.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "qr_details")
public class QrDetails {

    @Id
    private String uniqueId;

    private String fullName;
    private int age;
    private String gender;
    private String bloodGroup;
    private String nearestPoliceStation;
    private String emergencyContact;
    private String aadhaar;
    private String vehicleNo;

    @Column(length = 500)
    private String address;

    @Column(length = 5000)
private String qrImageBase64;


    // Getters & Setters
    public String getUniqueId() { return uniqueId; }
    public void setUniqueId(String uniqueId) { this.uniqueId = uniqueId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getNearestPoliceStation() { return nearestPoliceStation; }
    public void setNearestPoliceStation(String nearestPoliceStation) { this.nearestPoliceStation = nearestPoliceStation; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public String getAadhaar() { return aadhaar; }
    public void setAadhaar(String aadhaar) { this.aadhaar = aadhaar; }

    public String getVehicleNo() { return vehicleNo; }
    public void setVehicleNo(String vehicleNo) { this.vehicleNo = vehicleNo; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getQrImageBase64() { return qrImageBase64; }
public void setQrImageBase64(String qrImageBase64) { this.qrImageBase64 = qrImageBase64; }
}