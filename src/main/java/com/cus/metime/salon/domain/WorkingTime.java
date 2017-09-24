package com.cus.metime.salon.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.cus.metime.salon.domain.enumeration.WorkingDay;

/**
 * A WorkingTime.
 */
@Entity
@Table(name = "working_time")
public class WorkingTime implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "is_active")
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "working_day")
    private WorkingDay workingDay;

    @ManyToOne
    private Salon salon;

    @OneToOne
    @JoinColumn(unique = true)
    private TimePeriod timePeriod;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public WorkingTime isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public WorkingDay getWorkingDay() {
        return workingDay;
    }

    public WorkingTime workingDay(WorkingDay workingDay) {
        this.workingDay = workingDay;
        return this;
    }

    public void setWorkingDay(WorkingDay workingDay) {
        this.workingDay = workingDay;
    }

    public Salon getSalon() {
        return salon;
    }

    public WorkingTime salon(Salon salon) {
        this.salon = salon;
        return this;
    }

    public void setSalon(Salon salon) {
        this.salon = salon;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public WorkingTime timePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
        return this;
    }

    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
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
        WorkingTime workingTime = (WorkingTime) o;
        if (workingTime.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), workingTime.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WorkingTime{" +
            "id=" + getId() +
            ", isActive='" + isIsActive() + "'" +
            ", workingDay='" + getWorkingDay() + "'" +
            "}";
    }
}
