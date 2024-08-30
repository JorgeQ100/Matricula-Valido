package com.picoyplaca.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.validacion.services.RestriccionCirculacion;
import com.vehiculo.entities.Vehiculo;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class CirculacionController {
    
    @Autowired
    private RestriccionCirculacion restriccionCirculacion;
    
    @GetMapping("/vehiculo")
    public ResponseEntity<?> puedeCircular(@RequestParam("matricula") String matricula,
                                            @RequestParam("fechaActual") String fechaActualString) {
        
        if (matricula == null || fechaActualString == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Falta datos"));
        }

        try {
            LocalDate fechaActual = LocalDate.parse(fechaActualString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate fechaHoy = LocalDate.now();
            if (!fechaActual.isEqual(fechaHoy)) {
                return ResponseEntity.badRequest().body(new ErrorResponse("La fecha debe ser la actual"));
            }
            Vehiculo vehiculo = new Vehiculo(matricula, fechaActualString);

            if (restriccionCirculacion.puedeCircula(vehiculo, fechaActual)) {
                return ResponseEntity.ok(new SuccessResponse("El vehículo puede circular."));
            } else {
                return ResponseEntity.ok(new SuccessResponse("El vehículo no puede circular."));
            }
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Formato Incorrecto. Debe ser yyyy-MM-dd"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ErrorResponse("Error datos imcompletos" ));
        }
    }
    
    // Clases internas para respuestas JSON
    public static class SuccessResponse {
        private String message;
        
        public SuccessResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
    }
    
    public static class ErrorResponse {
        private String error;
        
        public ErrorResponse(String error) {
            this.error = error;
        }
        
        public String getError() {
            return error;
        }
    }
}
