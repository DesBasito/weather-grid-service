package kg.manurov.weathergridservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CropType implements EnumInterface {
    WHEAT("Пшеница"),
    BARLEY("Ячмень"),
    CORN("Кукуруза"),
    COTTON("Хлопок"),
    SUGAR_BEET("Сахарная свекла"),
    POTATO("Картофель"),
    RICE("Рис"),
    SUNFLOWER("Подсолнечник"),
    APPLE("Яблоня"),
    APRICOT("Абрикос"),
    GRAPE("Виноград"),
    TOMATO("Томат"),
    CUCUMBER("Огурец"),
    ONION("Лук"),
    ALFALFA("Люцерна"),
    OTHER("Другое");

    private final String description;
}
