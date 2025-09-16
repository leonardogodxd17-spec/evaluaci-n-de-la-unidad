package pe.edu.upeu.asistencia.servicio;

import pe.edu.upeu.asistencia.modelo.Participante;

import java.util.List;

public interface ParticipanteServicioI {
    void save(Participante participante); //C

    List<Participante> findAll();//R

    void delete(int index);//U

    void update(Participante participante, int index);//D

    Participante findByid(int index);//Buscar ese



}
