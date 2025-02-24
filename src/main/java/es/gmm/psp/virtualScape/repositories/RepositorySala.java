package es.gmm.psp.virtualScape.repositories;

import es.gmm.psp.virtualScape.model.Sala;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepositorySala extends MongoRepository<Sala,String> {
}
