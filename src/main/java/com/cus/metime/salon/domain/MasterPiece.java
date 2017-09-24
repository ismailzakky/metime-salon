package com.cus.metime.salon.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.cus.metime.salon.domain.enumeration.Gender;

/**
 * A MasterPiece.
 */
@Entity
@Table(name = "master_piece")
public class MasterPiece implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "media_file")
    private String mediaFile;

    @Column(name = "property_name")
    private String propertyName;

    @ManyToOne
    private Stylish stylish;

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

    public Gender getGender() {
        return gender;
    }

    public MasterPiece gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getMediaFile() {
        return mediaFile;
    }

    public MasterPiece mediaFile(String mediaFile) {
        this.mediaFile = mediaFile;
        return this;
    }

    public void setMediaFile(String mediaFile) {
        this.mediaFile = mediaFile;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public MasterPiece propertyName(String propertyName) {
        this.propertyName = propertyName;
        return this;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Stylish getStylish() {
        return stylish;
    }

    public MasterPiece stylish(Stylish stylish) {
        this.stylish = stylish;
        return this;
    }

    public void setStylish(Stylish stylish) {
        this.stylish = stylish;
    }

    public CreationalDate getCreationalDate() {
        return creationalDate;
    }

    public MasterPiece creationalDate(CreationalDate creationalDate) {
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
        MasterPiece masterPiece = (MasterPiece) o;
        if (masterPiece.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), masterPiece.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MasterPiece{" +
            "id=" + getId() +
            ", gender='" + getGender() + "'" +
            ", mediaFile='" + getMediaFile() + "'" +
            ", propertyName='" + getPropertyName() + "'" +
            "}";
    }
}
