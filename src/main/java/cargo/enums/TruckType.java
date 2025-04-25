package cargo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TruckType {
    SMALL("0.5–2 тонн"),
    MEDIUM("2–4 тонн"),
    LARGE("4–16 тонн"),
    REFRIGERATOR("Охлаждаемый");

    private final String description;
}
