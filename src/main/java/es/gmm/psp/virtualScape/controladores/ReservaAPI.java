package es.gmm.psp.virtualScape.controladores;

import es.gmm.psp.virtualScape.model.*;
import es.gmm.psp.virtualScape.services.ServiceReserva;
import es.gmm.psp.virtualScape.services.ServiceSala;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/virtual-escape/reservas")
public class ReservaAPI {
    @Autowired
    private ServiceReserva serviceReserva;

    @Autowired
    private ServiceSala serviceSala;


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
    public ResponseEntity<RespuestaApi> nuevaReserva(@RequestParam String nombreSala,@RequestParam int diaReserva,@RequestParam int horaReserva,@RequestParam String titular,@RequestParam int telefono,@RequestParam int jugadores,@Valid @RequestBody Reservas reserva) {
        Reservas newReserva = new Reservas(nombreSala,new Fecha(horaReserva,diaReserva),new Contacto(titular,telefono),jugadores);
        boolean conflicto = serviceReserva.verificarConflicto(newReserva);
        if (conflicto) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new RespuestaApi(false, "Conflicto de horarios con otra reserva", null));
        }

        newReserva = serviceReserva.save(newReserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RespuestaApi(true, "Reserva creada con exito", newReserva.getId()));
    }



    @Operation(summary = "Obtener todas las reservas", description = "Devuelve una lista de todas las reservas")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de reservas encontrada con exito",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(
                                            example = "[{\"id\": \"1\", \"nombreSala\": \"Sala 1\", \"fecha\": \"2025-05-22T12:00:00\", \"horaInicio\": \"12:00\", \"horaFin\": \"14:00\"}, {\"id\": \"2\", \"nombreSala\": \"Sala 2\", \"fecha\": \"2025-05-22T14:00:00\", \"horaInicio\": \"14:00\", \"horaFin\": \"16:00\"}]"
                                    )
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "No hay reservas registradas",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": false, \"mensajeError\": \"No hay reservas registradas\", \"idGenerado\": null}"
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<?> getReservas() {
        List<Reservas> reservas = serviceReserva.findAll();
        if (reservas == null || reservas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new RespuestaApi(false, "No hay reservas registradas", null));
        }
        return ResponseEntity.ok(reservas);
    }


    @Operation(summary = "Obtener detalles de una reserva", description = "Devuelve los detalles de una reserva específica por su identificador/id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva encontrada",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"id\": \"1\", \"nombreSala\": \"Sala 1\", \"fecha\": \"2025-01-13T11:00:00\", \"horaInicio\": \"11:00\", \"horaFin\": \"13:00\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva no encontrada",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": false, \"mensajeError\": \"Reserva no encontrada\", \"idGenerado\": null}"
                            )
                    )
            )

    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservaById(@PathVariable String id) {
        Reservas reserva = serviceReserva.findById(id);
        if (reserva != null) {
            return ResponseEntity.ok(reserva);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RespuestaApi(false, "Reserva no encontrada", null));
    }

    // PUT reservas/id
    @Operation(summary = "Actualizar una reserva", description = "Actualiza una reserva existente con nuevos datos")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva actualizada correctamente",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": true, \"mensajeError\": \"\", \"idGenerado\": null}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos en la petición",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": false, \"mensajeError\": \"Datos no válidos en la petición\", \"idGenerado\": null}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva no encontrada",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": false, \"mensajeError\": \"Reserva no encontrada\", \"idGenerado\": null}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto: choque de horarios con otra reserva",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": false, \"mensajeError\": \"Conflicto de horarios con otra reserva\", \"idGenerado\": null}"

                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<RespuestaApi> actualizarReserva(@PathVariable String id,@RequestParam String nombreSala,@RequestParam int diaReserva,@RequestParam int horaReserva, @RequestParam String titular,@RequestParam int telefono,@RequestParam int jugadores,@Valid @RequestBody Reservas reserva) {
        Reservas reservaYaCreada = serviceReserva.findById(id);
        if (reservaYaCreada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RespuestaApi(false, "Reserva no encontrada", null));
        }
        Sala salaYacreada = serviceSala.getByName(nombreSala);
        if(salaYacreada==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespuestaApi(false, "La sala no existe", null));
        }
        reserva.setId(id);
        boolean conflicto = serviceReserva.verificarConflicto(reserva);
        if (conflicto) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new RespuestaApi(false, "Conflicto de horarios con otra reserva", null));
        }
        reserva.setJugadores(jugadores);
        reserva.setNombreSala(nombreSala);
        reserva.setContacto(new Contacto(titular, telefono));
        reserva.setFecha(new Fecha(diaReserva, horaReserva));
        try {
            Reservas reservaConDatosNuevos = serviceReserva.actualizarReserva(reserva);
            Reservas compribacion = serviceReserva.findById(reservaConDatosNuevos.getId());
            if (compribacion == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RespuestaApi(false, "Error al verificar la actualización", null));
            }
            return ResponseEntity.ok(new RespuestaApi(true, "Reserva actualizada con éxito",reservaConDatosNuevos.getId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespuestaApi(false, "Datos inválidos en la petición", null));
        }
    }

    @Operation(summary = "Eliminar una reserva", description = "Elimina una reserva por su identificador/id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Reserva eliminada con éxito",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": true, \"mensajeError\": \"\", \"idGenerado\": null}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva no encontrada",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": false, \"mensajeError\": \"Reserva no encontrada\", \"idGenerado\": null}"
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaApi> eliminarReserva(@PathVariable String id) {
        Reservas reserva = serviceReserva.findById(id);
        if (reserva == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RespuestaApi(false, "Reserva no encontrada", null));
        }
        serviceReserva.eliminarReserva(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RespuestaApi(true, "Reserva eliminada con éxito", id));
    }

    @Operation(summary = "Obtener reservas por día", description = "Devuelve todas las reservas para un día específico.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reservas encontradas",
                    content = @Content(
                            schema = @Schema(
                                    example = "[ { \"sala\": \"La Casa del Terror\", \"dia\": 3, \"hora\": 18, \"contacto\": { \"titular\": \"Diego\", \"telefono\": 685414005 }, \"jugadores\": 4 } ]"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "No hay reservas para ese día",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": false, \"mensajeError\": \"Reserva no encontrada\", \"idGenerado\": null}"
                            )
                    )
            )
    })
    @GetMapping("/dia/{dia}")
    public ResponseEntity<List<Reservas>> obtenerReservasPorDia(@PathVariable int dia) {
        List<Reservas> reservas = serviceReserva.obtenerReservasPorDia(dia);
        return reservas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(reservas);
    }
}
