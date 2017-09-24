package com.cus.metime.salon.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A TimePeriod.
 */
@Entity
@Table(name = "time_period")
public class TimePeriod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "end_hour")
    private LocalDate endHour;

    @Column(name = "start_hour")
    private LocalDate startHour;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEndHour() {
        return endHour;
    }

    public TimePeriod endHour(LocalDate endHour) {
        this.endHour = endHour;
        return this;
    }

    public void setEndHour(LocalDate endHour) {
        this.endHour = endHour;
    }

    public LocalDate getStartHour() {
        return startHour;
    }

    public TimePeriod startHour(LocalDate startHour) {
        this.startHour = startHour;
        return this;
    }

    public void setStartHour(LocalDate startHour) {
        this.startHour = startHour;
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
        TimePeriod timePeriod = (TimePeriod) o;
        if (timePeriod.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), timePeriod.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TimePeriod{" +
            "id=" + getId() +
            ", endHour='" + getEndHour() + "'" +
            ", startHour='" + getStartHour() + "'" +
            "}";
    }
}
