package cargo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CargoType {

    INTERNATIONAL("International"),
    KYRGYZSTAN("Kyrgyzstan"),
    UZBEKISTAN("Uzbekistan");

    private final String description;
}
