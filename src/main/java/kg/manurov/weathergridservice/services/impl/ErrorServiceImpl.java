package kg.manurov.weathergridservice.services.impl;

import kg.manurov.weathergridservice.errors.ErrorResponseBody;
import kg.manurov.weathergridservice.services.interfaces.ErrorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ErrorServiceImpl implements ErrorService {
    @Override
    public ErrorResponseBody makeResponse(Exception ex){
        String msg = ex.getMessage();
        return ErrorResponseBody.builder()
                .title(ex.getMessage())
                .response(Map.of("errors", List.of(msg)))
                .build();
    }

    @Override
    public ErrorResponseBody makeResponse(BindingResult result){
        Map<String,List<String>> reasons = new HashMap<>();
        result.getFieldErrors().stream()
                .filter(err -> err.getDefaultMessage() != null)
                .forEach(e -> {
                    List<String> errors = new ArrayList<>();
                    errors.add(e.getDefaultMessage());
                    if (!reasons.containsKey(e.getField())){
                        reasons.put(e.getField(),errors);
                    }
                });
        log.error("Errors while adding data to db {}", reasons);
        return ErrorResponseBody.builder()
                .title("Valid errors")
                .response(reasons)
                .build();
    }
}
