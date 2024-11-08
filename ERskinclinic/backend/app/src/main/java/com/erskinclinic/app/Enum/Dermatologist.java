package com.erskinclinic.app.Enum;

import java.util.Arrays;

import java.util.List;

public enum Dermatologist {
    DR_AMA_WIJERATHNE("Dr. Ama Wijerathne"),
    DR_NIMESH_PERERA("Dr. Nimesh Perera"),
    DR_KASUN_THENUWARA("Dr. Kasun Thenuwara");

    private final String name;

    Dermatologist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
    public static List<Dermatologist> getAllDermatologists() {
        return Arrays.asList(Dermatologist.values());
    }
}

