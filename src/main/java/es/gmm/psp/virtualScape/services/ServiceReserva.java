package es.gmm.psp.virtualScape.services;

import es.gmm.psp.virtualScape.model.Reservas;
import es.gmm.psp.virtualScape.repositories.RepositoryReserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceReserva {


    @Autowired
    private RepositoryReserva reservaRepository;

    public List<Reservas> findAll(){
        return reservaRepository.findAll();
    }

    public Reservas findById(String id){
        return reservaRepository.findById(id).orElse(null);
    }

    public Reservas save(Reservas reserva){
        return reservaRepository.save(reserva);
    }

    public boolean verificarConflicto(Reservas reserva){
        List<Reservas> reservas = findAll();
        for(Reservas r : reservas){
            if(r.getFecha().getDiaReserva() == reserva.getFecha().getDiaReserva() &&
                    r.getFecha().getHoraReserva() == reserva.getFecha().getHoraReserva()){
                return true;
            }
        }
        return false;
    }

    public Reservas actualizarReserva(Reservas reserva) {
        if (reserva == null || reserva.getId() == null) {
            return null;
        }

        return reservaRepository.save(reserva);
    }

    public void eliminarReserva(String id){
        reservaRepository.deleteById(id);
    }

}
