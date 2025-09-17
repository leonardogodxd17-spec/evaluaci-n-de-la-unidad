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
    MenuItem menuItem1, menuItemC;

    Menu menuEstilos=new Menu("Cambiar Estilos");
    ComboBox<String> comboEstilo=new ComboBox<>();
    CustomMenuItem customMenuItem=new CustomMenuItem(comboEstilo);

    @Autowired
    ApplicationContext context;

    @FXML
    public void initialize(){
        comboEstilo.getItems().addAll("Estilo por defecto","Estilo Oscuro"
                ,"Estilo Azul","Estilo Rosado", "Estilo Verde");
        comboEstilo.setOnAction(e->cambiarEstilo());
        customMenuItem.setHideOnClick(false);
        menuEstilos.getItems().add(customMenuItem);
        menuBar.getMenus().addAll(menuEstilos);

        MenuItemListener mIL=new MenuItemListener();
        menuItem1.setOnAction(mIL::handle);
        menuItemC.setOnAction(mIL::handle);
    }

    public void cambiarEstilo(){
        String estilo=comboEstilo.getSelectionModel().getSelectedItem();
        Scene scene=bp.getScene();
        scene.getStylesheets().clear();
        switch (estilo){
            case "Estilo Oscuro":
                scene.getStylesheets().add(getClass().getResource("/css/estilo-oscuro.css").toExternalForm()); break;
            case "Estilo Azul":
                scene.getStylesheets().add(getClass().getResource("/css/estilo-azul.css").toExternalForm()); break;
            case "Estilo Rosado":
                scene.getStylesheets().add(getClass().getResource("/css/estilo-rosado.css").toExternalForm()); break;
            case "Estilo Verde":
                scene.getStylesheets().add(getClass().getResource("/css/estilo-verde.css").toExternalForm()); break;
            default: break;
        }
    }

    class MenuItemListener{
        Map<String, String[]> menuConfig=Map.of(
            "menuItem1", new  String[]{"/fxml/main_participante.fxml", "Reg.ingreso", "T"},
            "menuItemC", new  String[]{"/fxml/login.fxml", "Salir", "C"}
        );

        public void handle(ActionEvent e){
                String id=((MenuItem)e.getSource()).getId();
                if(menuConfig.containsKey(id)){
                    String[] items=menuConfig.get(id);
                    if(items[2].equals("C")){
                        Platform.exit();
                        System.exit(0);
                    }else{
                        abrirTabPaneFXML(items[0],items[1]);
                    }
                }
        }
        private void abrirTabPaneFXML(String fxmlPath, String tittle){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
                fxmlLoader.setControllerFactory(context::getBean);
                Parent root = fxmlLoader.load();

                ScrollPane  scrollPane = new  ScrollPane(root);
                scrollPane.setFitToWidth(true);
                scrollPane.setFitToHeight(true);

                Tab newTab = new Tab(tittle, scrollPane);
                tabPane.getTabs().clear();
                tabPane.getTabs().add(newTab);

            }catch (IOException ex){
                throw new RuntimeException(ex);
            }
        }
    }

    class MenuListener{
        public void handle(Event e){

        }
    }

}
