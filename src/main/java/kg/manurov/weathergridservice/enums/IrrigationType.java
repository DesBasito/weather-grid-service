package kg.manurov.weathergridservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IrrigationType implements EnumInterface {
    DRIP("Капельное орошение"),
    SPRINKLER("Дождевание"),
    FURROW("Орошение по бороздам"),
    BASIN("Бассейновое орошение"),
    MANUAL("Ручной полив"),
    RAINFED("Бессистемное (зависит от осадков)"),
    NONE("Не используется орошение");

    private final String description;
}
