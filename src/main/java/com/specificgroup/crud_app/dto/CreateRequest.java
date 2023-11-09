package com.specificgroup.crud_app.dto;


import java.util.Objects;

public class CreateRequest {
    private final String name;
    private final String age;
    private final String specialization;
    private final String phone;
    private final String email;

    public CreateRequest(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.specialization = builder.specialization;
        this.phone = builder.phone;
        this.email = builder.email;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getSpecialization() {
        return specialization;
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
        if (!(o instanceof CreateRequest that)) return false;
        return Objects.equals(name, that.name) &&
               Objects.equals(age, that.age) &&
               Objects.equals(specialization, that.specialization) &&
               Objects.equals(phone, that.phone) &&
               Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, specialization, phone, email);
    }

    @Override
    public String toString() {
        return "CreateRequest{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", specialization='" + specialization + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public final static class Builder {
        private String name;
        private String age;
        private String specialization;
        private String phone;
        private String email;

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder age(String age){
            this.age = age;
            return this;
        }

        public Builder specialization(String specialization){
            this.specialization = specialization;
            return this;
        }

        public Builder phone(String phone){
            this.phone = phone;
            return this;
        }

        public Builder email(String email){
            this.email = email;
            return this;
        }

        public CreateRequest build() {
            return new CreateRequest(this);
        }
    }
}
