package kg.manurov.weathergridservice.dto;

import jakarta.validation.constraints.*;
import kg.manurov.weathergridservice.annotations.ValidCropType;
import kg.manurov.weathergridservice.annotations.ValidIrrigationType;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FieldDto {

    @NotNull(message = "Владелец поля обязателен для заполнения")
    Long userId;

    @NotBlank(message = "Имя поля не должно быть пустым")
    @Size(max = 100, message = "Имя поля не должно превышать 100 символов")
    String name;

    @DecimalMin(value = "0.10", message = "Площадь должна быть положительной")
    Double areaHa;

    @ValidCropType
    String cropType;

    @ValidIrrigationType
    String irrigationType;

    @DecimalMin(value = "-180.0", message = "Долгота не может быть меньше -180")
    @DecimalMax(value = "180.0", message = "Долгота не может быть больше 180")
    @NotNull(message = "Геометрия (долгота) обязательна")
    Double longitude;

    @DecimalMin(value = "-90.0", message = "Широта не может быть меньше -90")
    @DecimalMax(value = "90.0", message = "Широта не может быть больше 90")
    @NotNull(message = "Геометрия (широта) обязательна")
    Double latitude;
}
