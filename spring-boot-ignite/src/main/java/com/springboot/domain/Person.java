package com.springboot.domain;

public class Person {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Person buildPerson(String personId) {
        Person person = new Person();
        person.setId(personId);
        return person;
    }
}
