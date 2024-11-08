package com.erskinclinic.app.Enum;

import java.util.Arrays;
import java.util.List;

public enum TreatmentType {
    ACNE_TREATMENT("Acne Treatment", 2750.00),
    SKIN_WHITENING("Skin Whitening", 7650.00),
    MOLE_REMOVAL("Mole Removal", 3850.00),
    LASER_TREATMENT("Laser Treatment", 12500.00);

    private final String treatmentName;
    private final double price;

    TreatmentType(String treatmentName, double price) {
        this.treatmentName = treatmentName;
        this.price = price;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return treatmentName + " - Price: " + price;
    }
    
    public static List<TreatmentType> getAllTreatmentType() {
        return Arrays.asList(TreatmentType.values());
    }
}

