package kg.manurov.weathergridservice.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.manurov.weathergridservice.enums.EnumInterface;
import kg.manurov.weathergridservice.enums.IrrigationType;

import static kg.manurov.weathergridservice.enums.EnumInterface.getEnumDescription;

public class IrrigationTypeValidator implements ConstraintValidator<ValidIrrigationType, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = true;

        context.disableDefaultConstraintViolation();

        if (!EnumInterface.isExists(IrrigationType.class, value)) {
            context.buildConstraintViolationWithTemplate(getEnumDescription(IrrigationType.class))
                    .addPropertyNode("irrigationType")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
