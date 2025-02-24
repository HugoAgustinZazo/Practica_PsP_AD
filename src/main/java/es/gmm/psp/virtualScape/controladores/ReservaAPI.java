package es.gmm.psp.virtualScape.controladores;

import es.gmm.psp.virtualScape.model.Reservas;
import es.gmm.psp.virtualScape.model.RespuestaApi;
import es.gmm.psp.virtualScape.services.ServiceReserva;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/virtual-escape/reservas")
public class ReservaAPI {
    @Autowired
    private ServiceReserva serviceReserva;

    // POST respuestas
    @Operation(summary = "Crear una nueva reserva", description = "Crea una nueva reserva si no hay conflicto de horarios")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Reserva creada",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": true, \"mensajeError\": \"\", \"idGenerado\": \"123456789\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": false, \"mensajeError\": \"Los datos enviados no son válidos: nombre de sala no encontrado\", \"idGenerado\": null}"

                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto: choque de horarios con otra reserva",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": false, \"mensajeError\": \"Conflicto de horarios: ya existe una reserva para ese horario\", \"idGenerado\": null}"
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<RespuestaApi> nuevaReserva(@Valid @RequestBody Reservas reserva) {
        boolean hayConflicto = serviceReserva.verificarConflicto(reserva);
        if (hayConflicto) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new RespuestaApi(false, "Conflicto de horarios con otra reserva", null));
        }

        Reservas nuevaReserva = serviceReserva.save(reserva);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RespuestaApi(true, "Reserva creada con éxito", nuevaReserva.getId()));
    }

}
