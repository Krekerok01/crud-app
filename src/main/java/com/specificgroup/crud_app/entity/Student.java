package com.specificgroup.crud_app.entity;

import java.util.Objects;

public class Student {
    private Long id;
    private String name;
    private int age;
    private ContactDetails contactDetails;

    public Student(Long id, String name, int age, ContactDetails contactDetails) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.contactDetails = contactDetails;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public ContactDetails getContactDetails() {
        return contactDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return age == student.age &&
                Objects.equals(id, student.id) &&
                Objects.equals(name, student.name) &&
                Objects.equals(contactDetails, student.contactDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, contactDetails);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", contactDetails=" + contactDetails +
                '}';
    }
}
