package com.cus.metime.salon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Salon.
 */
@Entity
@Table(name = "salon")
public class Salon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "manager")
    private String manager;

    @Column(name = "media_file")
    private String mediaFile;

    @Column(name = "owner")
    private String owner;

    @OneToOne
    @JoinColumn(unique = true)
    private BusinessInformation bussinessInformation;

    @OneToOne
    @JoinColumn(unique = true)
    private CreationalDate creationalDate;

    @OneToOne
    @JoinColumn(unique = true)
    private PlaceInformation placeInformation;

    @OneToMany(mappedBy = "salon")
    @JsonIgnore
    private Set<Service> services = new HashSet<>();

    @OneToMany(mappedBy = "salon")
    @JsonIgnore
    private Set<Stylish> stylishes = new HashSet<>();

    @OneToMany(mappedBy = "salon")
    @JsonIgnore
    private Set<WorkingTime> workingTimes = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Salon creationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Salon isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getManager() {
        return manager;
    }

    public Salon manager(String manager) {
        this.manager = manager;
        return this;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getMediaFile() {
        return mediaFile;
    }

    public Salon mediaFile(String mediaFile) {
        this.mediaFile = mediaFile;
        return this;
    }

    public void setMediaFile(String mediaFile) {
        this.mediaFile = mediaFile;
    }

    public String getOwner() {
        return owner;
    }

    public Salon owner(String owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public BusinessInformation getBussinessInformation() {
        return bussinessInformation;
    }

    public Salon bussinessInformation(BusinessInformation businessInformation) {
        this.bussinessInformation = businessInformation;
        return this;
    }

    public void setBussinessInformation(BusinessInformation businessInformation) {
        this.bussinessInformation = businessInformation;
    }

    public CreationalDate getCreationalDate() {
        return creationalDate;
    }

    public Salon creationalDate(CreationalDate creationalDate) {
        this.creationalDate = creationalDate;
        return this;
    }

    public void setCreationalDate(CreationalDate creationalDate) {
        this.creationalDate = creationalDate;
    }

    public PlaceInformation getPlaceInformation() {
        return placeInformation;
    }

    public Salon placeInformation(PlaceInformation placeInformation) {
        this.placeInformation = placeInformation;
        return this;
    }

    public void setPlaceInformation(PlaceInformation placeInformation) {
        this.placeInformation = placeInformation;
    }

    public Set<Service> getServices() {
        return services;
    }

    public Salon services(Set<Service> services) {
        this.services = services;
        return this;
    }

    public Salon addService(Service service) {
        this.services.add(service);
        service.setSalon(this);
        return this;
    }

    public Salon removeService(Service service) {
        this.services.remove(service);
        service.setSalon(null);
        return this;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }

    public Set<Stylish> getStylishes() {
        return stylishes;
    }

    public Salon stylishes(Set<Stylish> stylishes) {
        this.stylishes = stylishes;
        return this;
    }

    public Salon addStylish(Stylish stylish) {
        this.stylishes.add(stylish);
        stylish.setSalon(this);
        return this;
    }

    public Salon removeStylish(Stylish stylish) {
        this.stylishes.remove(stylish);
        stylish.setSalon(null);
        return this;
    }

    public void setStylishes(Set<Stylish> stylishes) {
        this.stylishes = stylishes;
    }

    public Set<WorkingTime> getWorkingTimes() {
        return workingTimes;
    }

    public Salon workingTimes(Set<WorkingTime> workingTimes) {
        this.workingTimes = workingTimes;
        return this;
    }

    public Salon addWorkingTime(WorkingTime workingTime) {
        this.workingTimes.add(workingTime);
        workingTime.setSalon(this);
        return this;
    }

    public Salon removeWorkingTime(WorkingTime workingTime) {
        this.workingTimes.remove(workingTime);
        workingTime.setSalon(null);
        return this;
    }

    public void setWorkingTimes(Set<WorkingTime> workingTimes) {
        this.workingTimes = workingTimes;
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
        Salon salon = (Salon) o;
        if (salon.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), salon.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Salon{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", manager='" + getManager() + "'" +
            ", mediaFile='" + getMediaFile() + "'" +
            ", owner='" + getOwner() + "'" +
            "}";
    }
}
