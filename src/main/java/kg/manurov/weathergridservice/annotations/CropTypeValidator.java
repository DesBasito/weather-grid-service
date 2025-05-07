package kg.manurov.weathergridservice.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.manurov.weathergridservice.enums.CropType;
import kg.manurov.weathergridservice.enums.EnumInterface;

import static kg.manurov.weathergridservice.enums.EnumInterface.getEnumDescription;

public class CropTypeValidator implements ConstraintValidator<ValidCropType, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = true;

        context.disableDefaultConstraintViolation();

        if (!EnumInterface.isExists(CropType.class, value)) {
            context.buildConstraintViolationWithTemplate(getEnumDescription(CropType.class))
                    .addPropertyNode("cropType")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
