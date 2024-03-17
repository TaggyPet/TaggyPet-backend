package ru.nsu.sberlab.model.enums;

public enum Sex {
    FEMALE("Самка"),
    MALE("Самец");

    private final String sex;

    Sex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return sex;
    }
}
