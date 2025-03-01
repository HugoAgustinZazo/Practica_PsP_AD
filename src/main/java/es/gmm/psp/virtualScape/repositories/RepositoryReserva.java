package es.gmm.psp.virtualScape.repositories;

import es.gmm.psp.virtualScape.model.Reservas;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RepositoryReserva extends MongoRepository<Reservas,String> {
    List<Reservas> findByFechaDiaReserva(int diaReserva);
}
