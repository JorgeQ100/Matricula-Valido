package com.picoyplaca.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.validacion.services.RestriccionCirculacion;
import com.vehiculo.entities.Vehiculo;

@RestController
@RequestMapping("/api")
public class CirculacionController {
	
	
	@Autowired
	private RestriccionCirculacion restriccionCirculacion;
	
	
	
	@GetMapping("/vehiculo")
	public String puedeCircular(@RequestParam("matricula") String Matricula,
			@RequestParam("fechaActual") String fechaActualString) {
		
		
		if (Matricula == null || fechaActualString == null) {
			return "Falta datos";
		}

		try {
			LocalDate fechaActual = LocalDate.parse(fechaActualString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			LocalDate fechaHoy = LocalDate.now();
			if (!fechaActual.isEqual(fechaHoy)) {
				return "La fecha debe ser la actual";
			}
			Vehiculo vehiculo = new Vehiculo(Matricula, fechaActualString);
			

			if (restriccionCirculacion.puedeCircula(vehiculo, fechaActual)) {
				return "El vehículo puede circular.";
			} else {
				return "El vehículo no puede circular.";
			}
		} catch (DateTimeParseException e) {
			return "Formato Incorrecto. Debe ser yyyy-MM-dd";
		} catch (Exception e) {
			return "Error de datos" + e.getMessage();
		}

	}
}
