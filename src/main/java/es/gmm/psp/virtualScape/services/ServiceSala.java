package es.gmm.psp.virtualScape.services;

import es.gmm.psp.virtualScape.model.Reservas;
import es.gmm.psp.virtualScape.model.Sala;
import es.gmm.psp.virtualScape.model.SalasMasReservadas;
import es.gmm.psp.virtualScape.repositories.RepositoryReserva;
import es.gmm.psp.virtualScape.repositories.RepositorySala;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceSala {

    @Autowired
    private RepositorySala repositorySala;

    @Autowired
    private RepositoryReserva repositoryReserva;


    public Sala crearSala(Sala sala) {
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

        return salas;
    }
    public List<Sala> getByNameTematica(String name){
        return repositorySala.findByTematicas(name);
    }
    public Sala getByName(String name){
        return repositorySala.findByNombre(name);
    }

    public List<Sala> findAll() {
        return repositorySala.findAll();
    }

    public Sala findById(String id) {
        return repositorySala.findById(id).orElse(null);
    }

    public Sala updateSala(Sala sala) {
        if (sala == null || sala.getId() == null) {
            throw new IllegalArgumentException("Sala no válida");
        }
        return repositorySala.save(sala);
    }
    public int getTodosJugadores() {
        List<Sala> salas = repositorySala.findAll();
        int totalJugadores = 0;
        for (Sala sala : salas) {
            totalJugadores += sala.getCapacidadMax();
        }
        return totalJugadores;
    }
}
