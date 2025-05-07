package kg.manurov.weathergridservice.enums;

public interface EnumInterface {
    String getDescription();
    static <E extends Enum<E> & EnumInterface> String fromString(Class<E> enumClass, String value) {
        try {
            return Enum.valueOf(enumClass, value.toUpperCase()).getDescription();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    static <E extends Enum<E> & EnumInterface> String getType(Class<E> enumClass, String value) {
        for (E type : enumClass.getEnumConstants()) {
            if (type.getDescription().equalsIgnoreCase(value.strip())) {
                return type.name();
            }
        }
        throw new IllegalArgumentException(String.format("Тип %s не найден", value));
    }
    static <E extends Enum<E> & EnumInterface> Boolean isExists(Class<E> enumClass,String value) {
        for (E type : enumClass.getEnumConstants()) {
            if (type.getDescription().equalsIgnoreCase(value.strip())) {
                return true;
            }
        }
        return false;
    }

    static  <E extends Enum<E> & EnumInterface> String getEnumDescription(Class<E> enumClass) {
        StringBuilder str = new StringBuilder();
        str.append("Указан неверный тип. Укажите одну из: ");
        for (E enumValue : enumClass.getEnumConstants()) {
            str.append(String.format("-> %s ",enumValue.getDescription()));
        }
        return str.toString();
    }
}