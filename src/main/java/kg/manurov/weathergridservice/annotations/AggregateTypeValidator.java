package kg.manurov.weathergridservice.annotations;

import jakarta.validation.ConstraintValidator;
import kg.manurov.weathergridservice.enums.AggregateType;
import kg.manurov.weathergridservice.enums.EnumInterface;
import kg.manurov.weathergridservice.enums.IrrigationType;

import static kg.manurov.weathergridservice.enums.EnumInterface.getEnumDescription;

public class AggregateTypeValidator implements ConstraintValidator<AgregateType, String> {
    @Override
    public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
        boolean isValid = true;

        context.disableDefaultConstraintViolation();

        if (!EnumInterface.isExists(AggregateType.class, value)) {
            context.buildConstraintViolationWithTemplate(getEnumDescription(AggregateType.class))
                    .addPropertyNode("aggregate")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
