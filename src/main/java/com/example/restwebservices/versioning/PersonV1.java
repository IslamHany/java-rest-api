package com.example.restwebservices.versioning;

public class PersonV1 {
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public PersonV1() {
    }

    public PersonV1(String name) {
        this.name = name;
    }

    private String name;

}
