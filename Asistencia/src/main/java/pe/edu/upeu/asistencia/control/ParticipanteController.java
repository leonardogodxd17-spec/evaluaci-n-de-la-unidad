package pe.edu.upeu.asistencia.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.asistencia.enums.Carrera;
import pe.edu.upeu.asistencia.enums.TipoParticipante;
import pe.edu.upeu.asistencia.modelo.Participante;
import pe.edu.upeu.asistencia.servicio.ParticipanteServicioI;

@Controller
public class ParticipanteController {

    @FXML
    private TextField txtNombres, txtIngreso, txtApellidos;
    @FXML
    private ComboBox<Carrera> cbxCarrera;
    @FXML
    private ComboBox<TipoParticipante> cbxTipoParticipante;

    @FXML
    private TableView<Participante> tableView;
    ObservableList<Participante> listaParticipantes;
    @FXML
    private TableColumn<Participante, String> ingresoColum, nombreColum, apellidoColum, carraraColum, tipoPartColum;
    private TableColumn<Participante, Void> opcColum;

    @Autowired
    ParticipanteServicioI ps;
    int indexE=-1;

    @FXML
    public void initialize(){
        cbxCarrera.getItems().setAll(Carrera.values());
        cbxTipoParticipante.getItems().setAll(TipoParticipante.values());
        definirColumnas();
        listarParticipantes();
    }

    public void limpiarFormulario(){
        txtNombres.setText("");
        txtIngreso.setText("");
        txtApellidos.setText("");
        cbxCarrera.setValue(null);
        cbxTipoParticipante.setValue(null);
    }

    @FXML
    public void registrarParticipante(){
        Participante p = new Participante();
        p.setDni(new SimpleStringProperty(txtIngreso.getText()));
        p.setNombre(new SimpleStringProperty(txtNombres.getText()));
        p.setApellidos(new SimpleStringProperty(txtApellidos.getText()));
        p.setCarrera(cbxCarrera.getSelectionModel().getSelectedItem());
        p.setTipoParticipante(cbxTipoParticipante.getSelectionModel().getSelectedItem());
        if(indexE==-1){
            ps.save(p);
        }else{
            ps.update(p,  indexE);
            indexE=-1;
        }
        limpiarFormulario();
        listarParticipantes();
    }

    public void definirColumnas(){
        ingresoColum=new TableColumn("INGRESO");
        nombreColum=new TableColumn("Nombres");
        apellidoColum=new TableColumn("Apellidos");
        carraraColum=new TableColumn("Carrera");
        tipoPartColum=new TableColumn("Tipo Participante");
        opcColum=new TableColumn("Opciones");
        opcColum.setPrefWidth(200);
        tableView.getColumns().addAll(ingresoColum, nombreColum, apellidoColum, carraraColum, tipoPartColum, opcColum);
    }

    public void agregarAccionBotones(){
        Callback<TableColumn<Participante, Void> , TableCell<Participante, Void> > cellFactory =
                param->new  TableCell<>(){
                private final Button editarBtn = new Button("Editar");
                private final Button eliminarBtn = new Button("Eliminar");
                    {
                        editarBtn.setOnAction(event -> {
                            Participante p=getTableView().getItems().get(getIndex());
                            editarDatos(p, getIndex());
                        });
                        eliminarBtn.setOnAction(event -> {
                            eliminarParticipante(getIndex());
                        });
                    }
                @Override
                public void updateItem(Void item, boolean empty){
                    super.updateItem(item, empty);
                    if(empty){
                        setGraphic(null);
                    }else {
                        HBox hbox = new HBox(editarBtn, eliminarBtn);
                        hbox.setSpacing(10);
                        setGraphic(hbox);
                    }
                }
                };
            opcColum.setCellFactory(cellFactory);
    }
    public void listarParticipantes(){
        ingresoColum.setCellValueFactory(cellData->cellData.getValue().getDni());
        nombreColum.setCellValueFactory(cellData->cellData.getValue().getNombre());
        apellidoColum.setCellValueFactory(cellData->cellData.getValue().getApellidos());
        carraraColum.setCellValueFactory(
                cellData->new SimpleStringProperty(cellData.getValue().getCarrera().toString()));
        agregarAccionBotones();
        listaParticipantes=FXCollections.observableArrayList(ps.findAll());
        tableView.setItems(listaParticipantes);
    }
    public void eliminarParticipante(int index){
        ps.delete(index);
        listarParticipantes();
    }

    public void editarDatos(Participante p, int index){
        txtIngreso.setText(p.getDni().getValue());
        txtNombres.setText(p.getNombre().getValue());
        txtApellidos.setText(p.getApellidos().getValue());
        cbxCarrera.setValue(p.getCarrera());
        cbxTipoParticipante.setValue(p.getTipoParticipante());
        indexE=index;
    }




}
