package com.example.demo.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "pessoa")
public class Person implements Serializable {

    public Person() {
    }

    public Person(String firstNome, String lastNome, String address, String gender, String email) {
        this.firstNome = firstNome;
        this.lastNome = lastNome;
        this.address = address;
        this.gender = gender;
        this.email = email;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name", nullable = false, length = 80)
    private String firstNome;
    @Column(name = "last_name", nullable = false, length = 80)
    private String lastNome;
    @Column(nullable = false, length = 80)
    private String address;
    @Column(nullable = false, length = 10)
    private String gender;
    @Column(nullable = false, length = 100)
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstNome() {
        return firstNome;
    }

    public void setFirstNome(String firstNome) {
        this.firstNome = firstNome;
    }

    public String getLastNome() {
        return lastNome;
    }

    public void setLastNome(String lastNome) {
        this.lastNome = lastNome;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) && Objects.equals(firstNome, person.firstNome) && Objects.equals(lastNome, person.lastNome) && Objects.equals(address, person.address) && Objects.equals(gender, person.gender) && Objects.equals(email, person.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstNome, lastNome, address, gender, email);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstNome='" + firstNome + '\'' +
                ", lastNome='" + lastNome + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
