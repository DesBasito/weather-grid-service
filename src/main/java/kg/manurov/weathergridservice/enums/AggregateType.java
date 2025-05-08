package kg.manurov.weathergridservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AggregateType implements EnumInterface{
    DAILY("day"),
    MONTHLY("month"),
    YEARLY("year");

    private final String description;
}
