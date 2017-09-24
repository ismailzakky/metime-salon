package com.cus.metime.salon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Stylish.
 */
@Entity
@Table(name = "stylish")
public class Stylish implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "media_file")
    private String mediaFile;

    @ManyToOne
    private Salon salon;

    @OneToOne
    @JoinColumn(unique = true)
    private CreationalDate creationalDate;

    @OneToOne
    @JoinColumn(unique = true)
    private PeopleInformation peopleInformation;

    @OneToOne
    @JoinColumn(unique = true)
    private PlaceInformation placeInformation;

    @OneToOne
    @JoinColumn(unique = true)
    private WorkingTime workingTime;

    @OneToMany(mappedBy = "stylish")
    @JsonIgnore
    private Set<MasterPiece> masterpieces = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMediaFile() {
        return mediaFile;
    }

    public Stylish mediaFile(String mediaFile) {
        this.mediaFile = mediaFile;
        return this;
    }

    public void setMediaFile(String mediaFile) {
        this.mediaFile = mediaFile;
    }

    public Salon getSalon() {
        return salon;
    }

    public Stylish salon(Salon salon) {
        this.salon = salon;
        return this;
    }

    public void setSalon(Salon salon) {
        this.salon = salon;
    }

    public CreationalDate getCreationalDate() {
        return creationalDate;
    }

    public Stylish creationalDate(CreationalDate creationalDate) {
        this.creationalDate = creationalDate;
        return this;
    }

    public void setCreationalDate(CreationalDate creationalDate) {
        this.creationalDate = creationalDate;
    }

    public PeopleInformation getPeopleInformation() {
        return peopleInformation;
    }

    public Stylish peopleInformation(PeopleInformation peopleInformation) {
        this.peopleInformation = peopleInformation;
        return this;
    }

    public void setPeopleInformation(PeopleInformation peopleInformation) {
        this.peopleInformation = peopleInformation;
    }

    public PlaceInformation getPlaceInformation() {
        return placeInformation;
    }

    public Stylish placeInformation(PlaceInformation placeInformation) {
        this.placeInformation = placeInformation;
        return this;
    }

    public void setPlaceInformation(PlaceInformation placeInformation) {
        this.placeInformation = placeInformation;
    }

    public WorkingTime getWorkingTime() {
        return workingTime;
    }

    public Stylish workingTime(WorkingTime workingTime) {
        this.workingTime = workingTime;
        return this;
    }

    public void setWorkingTime(WorkingTime workingTime) {
        this.workingTime = workingTime;
    }

    public Set<MasterPiece> getMasterpieces() {
        return masterpieces;
    }

    public Stylish masterpieces(Set<MasterPiece> masterPieces) {
        this.masterpieces = masterPieces;
        return this;
    }

    public Stylish addMasterpiece(MasterPiece masterPiece) {
        this.masterpieces.add(masterPiece);
        masterPiece.setStylish(this);
        return this;
    }

    public Stylish removeMasterpiece(MasterPiece masterPiece) {
        this.masterpieces.remove(masterPiece);
        masterPiece.setStylish(null);
        return this;
    }

    public void setMasterpieces(Set<MasterPiece> masterPieces) {
        this.masterpieces = masterPieces;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stylish stylish = (Stylish) o;
        if (stylish.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stylish.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Stylish{" +
            "id=" + getId() +
            ", mediaFile='" + getMediaFile() + "'" +
            "}";
    }
}
