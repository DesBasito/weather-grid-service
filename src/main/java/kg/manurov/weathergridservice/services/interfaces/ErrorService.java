package kg.manurov.weathergridservice.services.interfaces;


import kg.manurov.weathergridservice.errors.ErrorResponseBody;
import org.springframework.validation.BindingResult;

public interface ErrorService {
    ErrorResponseBody makeResponse(Exception ex);

    ErrorResponseBody makeResponse(BindingResult result);
}
