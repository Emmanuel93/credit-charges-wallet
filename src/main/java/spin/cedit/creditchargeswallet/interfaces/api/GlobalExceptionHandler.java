package spin.cedit.creditchargeswallet.interfaces.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import spin.cedit.creditchargeswallet.domain.exception.DomainException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public Map<String, String> handleDomainException(DomainException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public Map<String, String> handleAnyException(Exception ex) {
        return Map.of("error", "Servicio temporalmente fuera de servicio. Volvemos en unos momentos.");
    }
}
