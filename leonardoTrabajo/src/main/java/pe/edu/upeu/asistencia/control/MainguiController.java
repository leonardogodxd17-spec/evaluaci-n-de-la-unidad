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
    MenuItem menuItem1, menuItemC, MenuItemReporte;

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

        menuItem1.setOnAction(mIL::handle);
        menuItemC.setOnAction(mIL::handle);

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
    //clase interna

    class MenuItemListener{
        Map<String, String[]> menuConfig = Map.of(
                "menuItem1", new String[]{"/fxml/main_participante.fxml","Movimientos","M"},
                "menuItemC", new String[]{"/fxml/login.fxml","salir","C"}
        );

        public void handle(ActionEvent e) {
            String id = ((MenuItem)e.getSource()).getId();
            System.out.println("ID del menú clickeado: " + id); // Debug
            
            if (menuConfig.containsKey(id)) {
                String[] items = menuConfig.get(id);
                System.out.println("Configuración encontrada: " + java.util.Arrays.toString(items)); // Debug
                
                if (items[2].equals("C")) {
                    // Confirmar antes de cerrar
                    Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmDialog.setTitle("Confirmar Salida");
                    confirmDialog.setHeaderText("¿Está seguro que desea cerrar la aplicación?");
                    confirmDialog.setContentText("Se perderán todos los datos no guardados.");
                    
                    confirmDialog.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                    confirmDialog.setResultConverter(buttonType -> {
                        if (buttonType == ButtonType.YES) {
                            Platform.exit();
                            System.exit(0);
                        }
                        return null;
                    });
                    confirmDialog.showAndWait();
                } else {
                    System.out.println("Abriendo pestaña: " + items[0] + " - " + items[1]); // Debug
                    abrirTabPaneFXML(items[0], items[1]);
                }
            } else {
                System.out.println("No se encontró configuración para ID: " + id); // Debug
            }
        }
        private void abrirTabPaneFXML(String fxmlPath, String tittle) {
            try {
                // Verificar si la pestaña ya existe
                for (Tab t : tabPane.getTabs()) {
                    if (t.getText().equals(tittle)) {
                        tabPane.getSelectionModel().select(t);
                        return;
                    }
                }

                // Crear nueva pestaña
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
                fxmlLoader.setControllerFactory(context::getBean);
                Parent root = fxmlLoader.load();

                ScrollPane scrollPane = new ScrollPane(root);
                scrollPane.setFitToWidth(true);
                scrollPane.setFitToHeight(true);

                Tab newtab = new Tab(tittle, scrollPane);
                tabPane.getTabs().clear();
                tabPane.getTabs().add(newtab);

            } catch (IOException ex) {
                throw new RuntimeException("Error al cargar la vista: " + ex.getMessage(), ex);
            }
        }
    }


    class MenuListener{
        public void handle(Event e){

        }
    }
}
