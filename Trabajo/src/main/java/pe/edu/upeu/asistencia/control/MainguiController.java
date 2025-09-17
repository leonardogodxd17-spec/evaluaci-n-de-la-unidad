package pe.edu.upeu.asistencia.control;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.asistencia.Aplication;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;

@Controller

public class MainguiController {
    @FXML
    private BorderPane bp;
    @FXML
    MenuBar menuBar;
    @FXML
    TabPane tabPane;
    @FXML
    MenuItem MenuItem1, MenuItemC;
    Menu menuEstilos=new Menu("Cambiar estilos");
    ComboBox<String> comboEstilo=new ComboBox<>();
    CustomMenuItem customMenuItem=new CustomMenuItem(comboEstilo);
    @Autowired
    ApplicationContext context;

    public void initialize() {
        comboEstilo.getItems().addAll("Estilo por defecto","Estilo oscuro","Estilo azul","Estilo rosado","Estilo verde");
        comboEstilo.setOnAction(e->cambiarEstilo());
        customMenuItem.setHideOnClick(false);
        menuEstilos.getItems().addAll(customMenuItem);
        menuBar.getMenus().addAll(menuEstilos);
        MenuItemListener mIL=new MenuItemListener();
        MenuItem1.setOnAction(mIL::handle);
        MenuItemC.setOnAction(mIL::handle);
    }
    public void cambiarEstilo(){
        String estilo=comboEstilo.getSelectionModel().getSelectedItem();
        Scene scene=bp.getScene();
        scene.getStylesheets().clear();
        switch(estilo){
            case "Estilo oscuro":scene.getStylesheets().add(getClass().getResource("/css/css/estilo-oscuro.css").toExternalForm());break;
            case "Estilo azul":scene.getStylesheets().add(getClass().getResource("/css/css/estilo-azul.css").toExternalForm());break;
            case "Estilo rosado":scene.getStylesheets().add(getClass().getResource("/css/css/estilo-rosado.css").toExternalForm());break;
            case "Estilo verde":scene.getStylesheets().add(getClass().getResource("/css/css/estilo-verde.css").toExternalForm());break;
            default:break;
        }
    }
    class MenuItemListener{
        Map<String, String[]> menuConfig = Map.of(
                "MenuItem1", new String[]{"/fxml/main_participante.fxml","Movimientos","T"},
                "MenuItemReporte", new String[]{"/fxml/reporte.fxml","Reporte","T"},
                "MenuItemC", new String[]{"/fxml/login.fxml","salir","C"}
        );

        public void handle(ActionEvent e){//STA
                String id=((MenuItem)e.getSource()).getId();
                if (menuConfig.containsKey(id)){
                    String[] items=menuConfig.get(id);
                    if(items[2].equals("C")){
                        Platform.exit();
                        System.exit(0);
                    }else{
                        abrirTabPaneFXML(items[0],items[1]);
                    }
                }
        }
        private void abrirTabPaneFXML(String fxmlPath,String tittle){
            try {
                // 🔎 Primero: si la pestaña ya existe
                for (Tab t : tabPane.getTabs()) {
                    if (t.getText().equals(tittle)) {
                        tabPane.getSelectionModel().select(t); // la activamos

                        // 👇 si guardamos el controller en userData, lo recuperamos aquí
                        Object ctrl = t.getUserData();
                        if (ctrl != null) {
                            if (ctrl instanceof pe.edu.upeu.asistencia.control.ReporteController) {
                                ((pe.edu.upeu.asistencia.control.ReporteController) ctrl).cargarDatos();
                            }
                        }
                        return;
                    }
                }

                // 🚀 Si no existe, la creamos
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
                fxmlLoader.setControllerFactory(context::getBean);
                Parent root = fxmlLoader.load();

                Object controller = fxmlLoader.getController(); // 👈 obtenemos el controller

                ScrollPane scrollPane = new ScrollPane(root);
                scrollPane.setFitToWidth(true);
                scrollPane.setFitToHeight(true);

                Tab newtab = new Tab(tittle, scrollPane);
                newtab.setUserData(controller); // 👈 guardamos el controller en la pestaña
                tabPane.getTabs().add(newtab);
                tabPane.getSelectionModel().select(newtab);

                // 👉 si es el reporte, cargamos datos de inmediato
                if (controller instanceof pe.edu.upeu.asistencia.control.ReporteController) {
                    ((pe.edu.upeu.asistencia.control.ReporteController) controller).cargarDatos();
                }

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
    }


    class MenuListener{
        public void handle(Event e){

        }
    }
}