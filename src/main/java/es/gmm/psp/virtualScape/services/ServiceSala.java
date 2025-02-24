package es.gmm.psp.virtualScape.services;

import es.gmm.psp.virtualScape.model.Sala;
import es.gmm.psp.virtualScape.repositories.RepositorySala;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceSala {

    @Autowired
    private RepositorySala repositorySala;


    public Sala crearSala(Sala sala) {
        return repositorySala.save(sala);
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
}
