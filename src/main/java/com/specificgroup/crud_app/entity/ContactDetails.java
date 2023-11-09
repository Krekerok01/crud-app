package com.specificgroup.crud_app.entity;

import java.util.Objects;

public class ContactDetails {
    private Long id;
    private String phone;
    private String email;

    public ContactDetails(Long id, String mobilePhone, String email) {
        this.id = id;
        this.phone = mobilePhone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContactDetails)) return false;
        ContactDetails that = (ContactDetails) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(phone, that.phone) &&
               Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phone, email);
    }

    @Override
    public String toString() {
        return "ContactDetails{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
