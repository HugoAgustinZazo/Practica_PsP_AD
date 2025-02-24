package es.gmm.psp.virtualScape.controladores;

import es.gmm.psp.virtualScape.model.Reservas;
import es.gmm.psp.virtualScape.services.ServiceReserva;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/virtual-escape/especiales")
public class EspecialesApi {

    @Autowired
    private ServiceReserva serviceReserva;

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
