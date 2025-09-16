package pe.edu.upeu.asistencia.control;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.asistencia.modelo.Participante;
import pe.edu.upeu.asistencia.servicio.ParticipanteServicioI;

@Controller
public class AsistenciaController {
    @Autowired
    private ParticipanteServicioI participanteServicioI;
    @FXML private Label idMsg;
    @FXML TextField txtdato;
    @FXML Button btnenviar;
    @FXML
    void enviar(){
        System.out.println("KS");
        idMsg.setText(txtdato.getText());

    }
    @FXML
    void regEstudiantes(){
        Participante participante = new Participante();
        participante.setNombre(new SimpleStringProperty(txtdato.getText()));
        participante.setEstado(new SimpleBooleanProperty(true));
        participanteServicioI.save(participante);
        listaEstudiantes();
    }
    void listaEstudiantes(){
        for (Participante e: participanteServicioI.findAll()){
            System.out.println(e.getNombre());
        }
    }
}
