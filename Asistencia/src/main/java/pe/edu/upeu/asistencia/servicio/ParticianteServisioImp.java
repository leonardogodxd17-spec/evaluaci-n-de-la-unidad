package pe.edu.upeu.asistencia.servicio;

import org.springframework.stereotype.Service;
import pe.edu.upeu.asistencia.modelo.Participante;
import pe.edu.upeu.asistencia.repositorio.ParticianteRepositorio;

import java.util.ArrayList;
import java.util.List;
@Service
public class ParticianteServisioImp extends ParticianteRepositorio implements ParticipanteServicioI {

    @Override
    public void save(Participante participante) {
        listaParticipantes.add(participante);
    }

    @Override
    public List<Participante> findAll() {
        if (listaParticipantes.isEmpty()) {
            return super.findAll();
        }
        return listaParticipantes;
    }

    @Override
    public void delete( int index) {
        listaParticipantes.remove(index);
    }

    @Override
    public void update(Participante participante, int index) {
        listaParticipantes.set(index, participante);
    }

    @Override
    public Participante findByid(int index) {
        return listaParticipantes.get(index);
    }
}
