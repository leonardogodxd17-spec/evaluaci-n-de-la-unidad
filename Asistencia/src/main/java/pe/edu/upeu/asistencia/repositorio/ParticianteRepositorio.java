package pe.edu.upeu.asistencia.repositorio;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import pe.edu.upeu.asistencia.enums.Carrera;
import pe.edu.upeu.asistencia.enums.TipoParticipante;
import pe.edu.upeu.asistencia.modelo.Participante;

import java.util.ArrayList;
import java.util.List;

public abstract class ParticianteRepositorio {
    public List<Participante> listaParticipantes =new ArrayList<>();

    public List<Participante> findAll(){
        listaParticipantes.add(
                new Participante(
                        new SimpleStringProperty("62181001"),
                        new SimpleStringProperty("Zaggi"),
                        new SimpleStringProperty("Morales"),
                        new SimpleBooleanProperty(true), Carrera.ARQUITECTURA,
                        TipoParticipante.ASISTENTE
                )
        );
        listaParticipantes.add(
                new Participante(
                        new SimpleStringProperty("123456789"),
                        new SimpleStringProperty("pepe"),
                        new SimpleStringProperty("dula"),
                        new SimpleBooleanProperty(true), Carrera.CIVIL,
                        TipoParticipante.ORGANIZADOR
                )
        );
        return listaParticipantes;

    }
}
