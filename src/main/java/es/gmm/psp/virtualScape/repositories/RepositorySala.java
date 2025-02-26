package es.gmm.psp.virtualScape.repositories;

import es.gmm.psp.virtualScape.model.Sala;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RepositorySala extends MongoRepository<Sala,String> {
    Sala findByNombre(String nombre);
    List<Sala> findByTematicas(String name);
}
