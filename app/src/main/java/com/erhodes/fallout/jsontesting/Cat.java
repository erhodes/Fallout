package com.erhodes.fallout.jsontesting;

public class Cat extends Animal {
    public String color;

    public Cat(String name, String breed) {
        super(name);
        this.color = breed;
        type = getClass().getSimpleName();
    }
}
