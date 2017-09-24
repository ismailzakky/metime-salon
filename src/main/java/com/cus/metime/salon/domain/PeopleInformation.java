package com.cus.metime.salon.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.cus.metime.salon.domain.enumeration.Gender;

import com.cus.metime.salon.domain.enumeration.IdentityType;

/**
 * A PeopleInformation.
 */
@Entity
@Table(name = "people_information")
public class PeopleInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "contact_number")
    private Long contactNumber;

    @Column(name = "identity_number")
    private String identityNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "identity_type")
    private IdentityType identityType;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public PeopleInformation firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public PeopleInformation lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public PeopleInformation gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Long getContactNumber() {
        return contactNumber;
    }

    public PeopleInformation contactNumber(Long contactNumber) {
        this.contactNumber = contactNumber;
        return this;
    }

    public void setContactNumber(Long contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public PeopleInformation identityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
        return this;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public IdentityType getIdentityType() {
        return identityType;
    }

    public PeopleInformation identityType(IdentityType identityType) {
        this.identityType = identityType;
        return this;
    }

    public void setIdentityType(IdentityType identityType) {
        this.identityType = identityType;
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
        PeopleInformation peopleInformation = (PeopleInformation) o;
        if (peopleInformation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), peopleInformation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PeopleInformation{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", gender='" + getGender() + "'" +
            ", contactNumber='" + getContactNumber() + "'" +
            ", identityNumber='" + getIdentityNumber() + "'" +
            ", identityType='" + getIdentityType() + "'" +
            "}";
    }
}
