package com.specificgroup.crud_app.entity;

import java.util.Objects;

public class Tutor {
    private Long id;
    private String name;
    private String specialization;
    private ContactDetails contactDetails;

    public Tutor(Long id, String name, String specialization, ContactDetails contactDetails) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.contactDetails = contactDetails;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public ContactDetails getContactDetails() {
        return contactDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tutor)) return false;
        Tutor tutor = (Tutor) o;
        return Objects.equals(id, tutor.id) &&
               Objects.equals(name, tutor.name) &&
               Objects.equals(specialization, tutor.specialization) &&
               Objects.equals(contactDetails, tutor.contactDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, specialization, contactDetails);
    }

    @Override
    public String toString() {
        return "Tutor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specialization='" + specialization + '\'' +
                ", contactDetails=" + contactDetails +
                '}';
    }
}
