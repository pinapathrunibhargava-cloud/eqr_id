package com.eqrid.eqrid_website.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "police")
public class Police {

    @Id
    @Column(name = "police_id", unique = true)
    private String policeId;   // used as primary key

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "department")
    private String department;

    @Column(name = "email", unique = true)
    private String officialEmail;

    // Getters and setters
    public String getPoliceId() { return policeId; }
    public void setPoliceId(String policeId) { this.policeId = policeId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getOfficialEmail() { return officialEmail; }
    public void setOfficialEmail(String officialEmail) { this.officialEmail = officialEmail; }
}
