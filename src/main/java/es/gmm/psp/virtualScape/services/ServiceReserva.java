package es.gmm.psp.virtualScape.services;

import es.gmm.psp.virtualScape.model.Reservas;
import es.gmm.psp.virtualScape.repositories.RepositoryReserva;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceReserva {
    private static final Logger logger = LoggerFactory.getLogger(ServiceReserva.class);


    @Autowired
    private RepositoryReserva reservaRepository;

    public List<Reservas> findAll(){
        logger.info("Buscando todas las reservas...");
        return reservaRepository.findAll();
    }

    public Reservas findById(String id){
        logger.info("Buscando reserva por id...");
        return reservaRepository.findById(id).orElse(null);
    }

    public Reservas save(Reservas reserva){
        logger.info("Guardando Reserva");
        return reservaRepository.save(reserva);
    }

    public boolean verificarConflicto(Reservas reserva){
        List<Reservas> reservas = findAll();
        for(Reservas r : reservas){
            if(r.getFecha().getDiaReserva() == reserva.getFecha().getDiaReserva() && r.getFecha().getHoraReserva() == reserva.getFecha().getHoraReserva()){
                return true;
            }else{
                logger.error("Conflicto encontrado");
            }
        }
        return false;
    }
    public List<Reservas> obtenerReservasPorDia(int dia) {
        logger.info("Obteniendo reserva por dia...");
        return reservaRepository.findByFechaDiaReserva(dia);
    }
    public Reservas actualizarReserva(Reservas reserva) {
        if (reserva == null || reserva.getId() == null) {
            logger.info("Actualizando reserva");
            return null;
        }
        return reservaRepository.save(reserva);
    }

    public void eliminarReserva(String id){
        logger.info("Eliminando reserva...");
        reservaRepository.deleteById(id);
    }

}
