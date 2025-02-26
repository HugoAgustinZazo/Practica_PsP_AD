package es.gmm.psp.virtualScape.services;

import es.gmm.psp.virtualScape.controladores.ReservaAPI;
import es.gmm.psp.virtualScape.model.Reservas;
import es.gmm.psp.virtualScape.model.Sala;
import es.gmm.psp.virtualScape.model.SalasMasReservadas;
import es.gmm.psp.virtualScape.repositories.RepositoryReserva;
import es.gmm.psp.virtualScape.repositories.RepositorySala;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceSala {
    private static final Logger logger = LoggerFactory.getLogger(ServiceSala.class);

    @Autowired
    private RepositorySala repositorySala;

    @Autowired
    private RepositoryReserva repositoryReserva;


    public Sala crearSala(Sala sala) {
        logger.info("Creando sala...");
        return repositorySala.save(sala);
    }
    public List<SalasMasReservadas> obtenerSalasMasReservadas() {
        List<Reservas> reservas = repositoryReserva.findAll();
        Map<String, Integer> conteoReservas = new HashMap<>();

        for (Reservas reserva : reservas) {
            String nombreSala = reserva.getNombreSala();
            conteoReservas.put(nombreSala, conteoReservas.getOrDefault(nombreSala, 0) + 1);
        }
        List<Map.Entry<String, Integer>> orden = new ArrayList<>(conteoReservas.entrySet());
        orden.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        List<SalasMasReservadas> salas = new ArrayList<>();
        int limite = Math.min(orden.size(), 2);

        for (int i = 0; i < limite; i++) {
            Map.Entry<String, Integer> entrada = orden.get(i);
            salas.add(new SalasMasReservadas(entrada.getKey(), entrada.getValue()));
        }
        logger.info("Devolviendo salas más reservadas");
        return salas;
    }
    public List<Sala> getByNameTematica(String name){
        logger.info("Buscando sala por temática...");
        return repositorySala.findByTematicas(name);
    }
    public Sala getByName(String name){
        logger.info("Buscando sala por nombre...");
        return repositorySala.findByNombre(name);
    }

    public List<Sala> findAll() {
        logger.info("Buscando todas las salas...");
        return repositorySala.findAll();
    }

    public Sala findById(String id) {
        logger.info("Buscando sala por id: "+id+"...");
        return repositorySala.findById(id).orElse(null);
    }

    public Sala updateSala(Sala sala) {
        if (sala == null || sala.getId() == null) {
            logger.error("Sala no válida");
            throw new IllegalArgumentException("Sala no válida");
        }
        logger.info("Updateando sala...");
        return repositorySala.save(sala);
    }
    public int getTodosJugadores() {
        List<Sala> salas = repositorySala.findAll();
        int totalJugadores = 0;
        for (Sala sala : salas) {
            totalJugadores += sala.getCapacidadMax();
        }
        logger.info("Recuperando todos los jugadores...");
        return totalJugadores;
    }
}
