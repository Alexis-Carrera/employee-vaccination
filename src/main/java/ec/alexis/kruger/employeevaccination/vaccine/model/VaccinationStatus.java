package ec.alexis.kruger.employeevaccination.vaccine.model;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum VaccinationStatus {
    VACCINATED("Vacunado"),
    PARTIAL_VACCINATED("Parcialmente Vacunado"),
    NO_VACCINATED("No Vacunado");

    private static final Map<String, VaccinationStatus> BY_DESCRIPTION;

    static {
        Map<String, VaccinationStatus> byDescription = new HashMap<>();

        for (VaccinationStatus e : values()) {
            byDescription.put(e.description, e);
        }
        BY_DESCRIPTION = Collections.unmodifiableMap(byDescription);
    }

    private final String description;

    VaccinationStatus(String description) {
        this.description = description;
    }

    public static VaccinationStatus valueOfDescription(String description) {
        return BY_DESCRIPTION.get(description);
    }
}
