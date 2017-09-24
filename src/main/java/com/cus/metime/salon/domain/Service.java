package com.cus.metime.salon.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Service.
 */
@Entity
@Table(name = "service")
public class Service implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "media_file")
    private String mediaFile;

    @Column(name = "price")
    private Long price;

    @Column(name = "service_name")
    private String serviceName;

    @ManyToOne
    private Salon salon;

    @OneToOne
    @JoinColumn(unique = true)
    private CreationalDate creationalDate;

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

    public Service isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getMediaFile() {
        return mediaFile;
    }

    public Service mediaFile(String mediaFile) {
        this.mediaFile = mediaFile;
        return this;
    }

    public void setMediaFile(String mediaFile) {
        this.mediaFile = mediaFile;
    }

    public Long getPrice() {
        return price;
    }

    public Service price(Long price) {
        this.price = price;
        return this;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Service serviceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Salon getSalon() {
        return salon;
    }

    public Service salon(Salon salon) {
        this.salon = salon;
        return this;
    }

    public void setSalon(Salon salon) {
        this.salon = salon;
    }

    public CreationalDate getCreationalDate() {
        return creationalDate;
    }

    public Service creationalDate(CreationalDate creationalDate) {
        this.creationalDate = creationalDate;
        return this;
    }

    public void setCreationalDate(CreationalDate creationalDate) {
        this.creationalDate = creationalDate;
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
        Service service = (Service) o;
        if (service.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), service.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Service{" +
            "id=" + getId() +
            ", isActive='" + isIsActive() + "'" +
            ", mediaFile='" + getMediaFile() + "'" +
            ", price='" + getPrice() + "'" +
            ", serviceName='" + getServiceName() + "'" +
            "}";
    }
}
