package kg.manurov.weathergridservice.errors;

import kg.manurov.weathergridservice.services.interfaces.ErrorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ErrorService errorService;

    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResponse noSuchElementException(NoSuchElementException e){
        log.error(e.getMessage());
        return ErrorResponse.builder(e, HttpStatus.NOT_FOUND,e.getMessage()).build();
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponseBody> nullPointerExceptions(NullPointerException e){
        log.error(e.getMessage());
        return new ResponseEntity<>(errorService.makeResponse(e), HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseBody> validationHandler(MethodArgumentNotValidException ex){
        log.error(ex.getMessage());
        return new ResponseEntity<>(errorService.makeResponse(ex.getBindingResult()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseBody> throwIllegalException(IllegalArgumentException ex){
        log.error(ex.getMessage());
        return new ResponseEntity<>(errorService.makeResponse(ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseBody> httpMethodException(HttpMessageNotReadableException ex){
        log.error(ex.getMessage());
        return new ResponseEntity<>(errorService.makeResponse(ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseBody> httpMethodException(RuntimeException ex){
        log.error(ex.getMessage());
        return new ResponseEntity<>(errorService.makeResponse(ex), HttpStatus.CONFLICT);
    }
}

