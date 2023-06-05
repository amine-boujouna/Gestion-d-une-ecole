package com.sip.store.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@PersistenceContext
@JsonInclude(value= JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "FirstName")
    private String FirstName;
    @Column(name = "LastName")
    private String LastName;
    @Column(name = "mobile")
    private int mobile;
    @Column(name = "address")
    private String address;
    @Column(name = "email")
    private String email;
    @Column(name = "fileName")
    private String fileName;
    @Column(name = "Education")
    private String Education;
    @Column(name = "Experience")
    private String Experience;
    @Column(name = "Genre")
    private String Genre;
    @Column(name = "Datedenaissance")
    private String Datedenaissance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getEducation() {
        return Education;
    }

    public void setEducation(String education) {
        Education = education;
    }

    public String getExperience() {
        return Experience;
    }

    public void setExperience(String experience) {
        Experience = experience;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getDatedenaissance() {
        return Datedenaissance;
    }

    public void setDatedenaissance(String datedenaissance) {
        Datedenaissance = datedenaissance;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", mobile=" + mobile +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", fileName='" + fileName + '\'' +
                ", Education='" + Education + '\'' +
                ", Experience='" + Experience + '\'' +
                ", Genre='" + Genre + '\'' +
                ", Datedenaissance='" + Datedenaissance + '\'' +
                '}';
    }
}
