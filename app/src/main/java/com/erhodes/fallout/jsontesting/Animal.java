package com.erhodes.fallout.jsontesting;

public class Animal {
    public String name;
    public String type;

    public Animal(String name) {
        this.name = name;
        type = getClass().getSimpleName();
    }
}
