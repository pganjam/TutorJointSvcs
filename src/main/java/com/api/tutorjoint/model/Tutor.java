package com.api.tutorjoint.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tutor")
public class Tutor implements Serializable {
    @Id
    @Column(name = "party_id")
    private Long id;

    @OneToMany(
            mappedBy = "tutor",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Course> courses = new ArrayList<Course>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    @MapsId
    private Party party;

    @Column(columnDefinition="VARCHAR(512)")
    private String description;

    @Column
    private String education;

    @Column
    private String educationTxt;

    @Column
    private String occupation;

    @Column
    private String occupationTxt;

    @Column
    private String experience;

    @Column
    private String experienceTxt;

    @Column
    private String preference;

    @Column
    private Long imageId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEducationTxt() {
        return educationTxt;
    }

    public void setEducationTxt(String educationTxt) {
        this.educationTxt = educationTxt;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOccupationTxt() {
        return occupationTxt;
    }

    public void setOccupationTxt(String occupationTxt) {
        this.occupationTxt = occupationTxt;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getExperienceTxt() {
        return experienceTxt;
    }

    public void setExperienceTxt(String experienceTxt) {
        this.experienceTxt = experienceTxt;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }
}
