package es.gmm.psp.virtualScape.controladores;

import es.gmm.psp.virtualScape.model.RespuestaApi;
import es.gmm.psp.virtualScape.model.Sala;
import es.gmm.psp.virtualScape.model.SalasMasReservadas;
import es.gmm.psp.virtualScape.services.ServiceSala;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/virtual-escape/salas")
public class SalaAPI {
    private static final Logger logger = LoggerFactory.getLogger(SalaAPI.class);

    @Autowired
    private ServiceSala serviceSala;

    @Operation(summary = "Añadir una nueva sala", description = "Crea una nueva sala.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Sala creada correctamente",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": true, \"mensajeError\": \"Sala creada con éxito\", \"idGenerado\": \"123456789\"}"))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos no válidos",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": false, \"mensajeError\": \"Datos no válidos en la petición\", \"idGenerado\": null}")))
    })
    @PostMapping
    public ResponseEntity<RespuestaApi> nuevaSala(@RequestParam String nombre,@RequestParam int capacidadMin,@RequestParam int capacidadMax,@RequestParam List<String> tematicas,@Valid @RequestBody Sala sala) {
        if (capacidadMin < 1 || capacidadMax > 8) {
            logger.error("El número de jugadores debe de ser entre 1 y 8");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespuestaApi(false, "El número de jugadores debe ser entre 1 y 8", null));
        }
        int totalJugadores = serviceSala.getTodosJugadores();
        if (totalJugadores + capacidadMax > 30) {
            logger.error("El número total de jugadores no puede superar los 30");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespuestaApi(false, "El número total de jugadores no puede superar los 30", null));
        }
        sala.setId(null);
        sala.setNombre(nombre);
        sala.setCapacidadMin(capacidadMin);
        sala.setCapacidadMax(capacidadMax);
        sala.setTematicas(tematicas);

        Sala salaYaCreada = serviceSala.getByName(sala.getNombre());
        if (salaYaCreada != null) {
            logger.error("Ya existe una sala con ese nombre");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespuestaApi(false, "Ya existe una sala con ese nombre", null));
        }
        Sala newSala = serviceSala.crearSala(sala);
        Sala verificacion = serviceSala.findById(newSala.getId());
        if (verificacion == null) {
            logger.error("Error al verificar la creación de la sala");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RespuestaApi(false, "Error al verificar la creación de la sala", null));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new RespuestaApi(true, "Sala creada con éxito", newSala.getId()));
    }

    @Operation(summary = "Listar todas las salas", description = "Devuelve una lista de todas las salas")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de salas encontrada",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(
                                            example = "[{\"id\": \"1\", \"nombre\": \"Sala 1\", \"capacidad\": 32}, {\"id\": \"2\", \"nombre\": \"Sala 2\", \"capacidad\": 54}]")))),
            @ApiResponse(
                    responseCode = "204",
                    description = "No hay salas registradas",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": false, \"mensajeError\": \"No hay salas registradas\", \"idGenerado\": null}")))
    })
    @GetMapping
    public ResponseEntity<?> getSalas() {
        List<Sala> salas = serviceSala.findAll();
        if (salas == null || salas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new RespuestaApi(false, "No hay salas registradas", null));
        }
        return ResponseEntity.ok(salas);
    }

    @Operation(summary = "Obtener detalles de una sala", description = "Devuelve los detalles de una sala específica dado su identificador")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sala encontrada",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"id\": \"1\", \"nombre\": \"Sala A\", \"capacidad\": 50}"))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sala no encontrada",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": false, \"mensajeError\": \"Sala no encontrada\", \"idGenerado\": null}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getSalaById(@PathVariable String id) {
        Sala sala = serviceSala.findById(id);
        if (sala == null) {
            logger.error("sala no encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RespuestaApi(false, "Sala no encontrada", null));
        }
        return ResponseEntity.ok(sala);
    }

    @Operation(summary = "Modificar una sala", description = "Actualiza por completo una sala existente con los nuevos datos")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sala actualizada correctamente",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": true, \"mensajeError\": \"Sala actualizada con éxito\", \"idGenerado\": null}"))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos en la petición",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": false, \"mensajeError\": \"Datos inválidos en la petición\", \"idGenerado\": null}"))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sala no encontrada",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": false, \"mensajeError\": \"Sala no encontrada\", \"idGenerado\": null}")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<RespuestaApi> actualizarSala(@PathVariable String id,@RequestParam String nombre,@RequestParam int capacidadMin,@RequestParam int capacidadMax,@RequestParam List<String> tematicas, @Valid @RequestBody Sala sala) {
        Sala salaCreada = serviceSala.findById(id);
        if (salaCreada == null) {
            logger.error("Sala no encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RespuestaApi(false, "Sala no encontrada", null));
        }

        if (!salaCreada.getNombre().equals(nombre)) {
            Sala salaConMismoNombre = serviceSala.getByName(nombre);
            if (salaConMismoNombre != null) {
                logger.error("El nombre de la sala ya existe");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespuestaApi(false, "El nombre de la sala ya existe", null));
            }
        }
        int totalJugadoresAntes = serviceSala.getTodosJugadores() - salaCreada.getCapacidadMax();
        if (totalJugadoresAntes + capacidadMax > 30) {
            logger.error("El número total de jugadores no puede ser mayor a 30");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespuestaApi(false, "El número total de jugadores no puede ser mayor a 30", null));
        }
        salaCreada.setNombre(nombre);
        salaCreada.setCapacidadMin(capacidadMin);
        salaCreada.setCapacidadMax(capacidadMax);
        salaCreada.setTematicas(tematicas);
        try {
            Sala salaActualizada = serviceSala.updateSala(salaCreada);
            Sala verificacion = serviceSala.findById(salaActualizada.getId());
            if (verificacion == null) {
                logger.error("Customize Toolbar...");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RespuestaApi(false, "Error al verificar la actualización", null));
            }
            return ResponseEntity.ok(new RespuestaApi(true, "Sala actualizada con éxito", salaActualizada.getId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespuestaApi(false, "Datos inválidos en la petición", null));
        }
    }

    @Operation(summary = "Consultar salas por temática", description = "Consulta las salas que coinciden con el nombre de la temática introducida")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Salas encontradas",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(
                                            example = "[{\"id\": \"1\", \"nombre\": \"Sala 1\", \"capacidadMin\": 2, \"capacidadMax\": 8, \"tematicas\": [\"Ciencia y Ficción\", \"Thriller\"]}, {\"id\": \"2\", \"nombre\": \"Sala 2\", \"capacidadMin\": 4, \"capacidadMax\": 6, \"tematicas\": [\"Romantica\", \"Comédia\"]}]")))),
            @ApiResponse(
                    responseCode = "204",
                    description = "No se encontraron salas con esa temática",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": false, \"mensaje\": \"No se encontraron salas con esa temática\", \"idGenerado\": null}")))
    })
    @GetMapping("/tematica/{nombreTematica}")
    public ResponseEntity<?> getSalasPorTematica(@PathVariable String nameTematica) {
        List<Sala> salas = serviceSala.getByNameTematica(nameTematica);
        if (salas == null || salas.isEmpty()) {
            logger.error("No hay salas");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(salas);
    }

    @Operation(summary = "Listar salas con más reservas", description = "Devuelve las salas que más han sido reservadas")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Datos encontrados",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(
                                            example = "[{\"id\": \"1\", \"nombre\": \"Sala 1\", \"capacidad\": 3}, {\"id\": \"2\", \"nombre\": \"Sala 2\", \"capacidad\": 6}]")))),
            @ApiResponse(
                    responseCode = "204",
                    description = "No se encontraron datos",
                    content = @Content(
                            schema = @Schema(
                                    example = "{\"exito\": false, \"mensaje\": \"No se encontraron datos\", \"idGenerado\": null}")))
    })
    @GetMapping("/mas-reservadas")
    public ResponseEntity<List<SalasMasReservadas>> getSalasMasReservadas() {
        List<SalasMasReservadas> salas = serviceSala.obtenerSalasMasReservadas();
        if (salas == null || salas.isEmpty()) {
            logger.error("No hay salas");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(salas);
    }

}
