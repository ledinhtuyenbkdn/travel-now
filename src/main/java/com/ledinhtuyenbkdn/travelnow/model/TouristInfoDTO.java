package com.ledinhtuyenbkdn.travelnow.model;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class TouristInfoDTO {

    @Size(min = 8)
    private String password;

    @Size(min = 2)
    private String fullName;

    private Boolean gender;
    @Past
    private LocalDate birthDate;

    private String role;
    private String nationality;

    public TouristInfoDTO() {
    }

    public TouristInfoDTO(@Size(min = 8) String password, @Size(min = 2) String fullName, Boolean gender, @Past LocalDate birthDate, String role, String nationality) {
        this.password = password;
        this.fullName = fullName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.role = role;
        this.nationality = nationality;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
