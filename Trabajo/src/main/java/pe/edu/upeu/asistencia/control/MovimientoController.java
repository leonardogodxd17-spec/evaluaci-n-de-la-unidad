package pe.edu.upeu.asistencia.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.asistencia.enums.TipoMovimiento;
import pe.edu.upeu.asistencia.modelo.Movimiento;
import pe.edu.upeu.asistencia.servicio.MovimientoService;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;

@Controller
public class MovimientoController {
    @FXML private TableView<Movimiento> tablaMovimientos;
    @FXML private Label saldoLabel;
    @FXML private TextField descripcionField;
    @FXML
    private TextField montoField;
    @FXML private ComboBox<TipoMovimiento> tipoCombo;
    @FXML private Button guardarBtn;
    @FXML private Button reporteBtn;

    @Autowired
    private MovimientoService service;
    @Autowired
    private ApplicationContext context;

    @FXML
    public void initialize() {

        tipoCombo.setItems(FXCollections.observableArrayList(TipoMovimiento.values()));
        tipoCombo.getSelectionModel().selectFirst();
        tablaMovimientos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaMovimientos.getColumns().clear();

        // Crear columnas de la tabla
        TableColumn<Movimiento, String> colDescripcion = new TableColumn<>("Descripción");
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        TableColumn<Movimiento, Double> colMonto = new TableColumn<>("Monto");
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));

        TableColumn<Movimiento, TipoMovimiento> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        // Agregar columnas a la tabla
        tablaMovimientos.getColumns().addAll(colDescripcion, colMonto, colTipo);

        // Inicializar items
        tablaMovimientos.setItems(service.listarMovimientos()); // 👈 observable compartido
        actualizarSaldo();
    }

    @FXML
    private void onGuardar(ActionEvent event) {
        String desc = descripcionField.getText().trim();
        String montoTxt = montoField.getText().trim();
        if (desc.isEmpty() || montoTxt.isEmpty()) {
            showAlert("Faltan datos", "Complete descripción y monto.");
            return;
        }
        double monto;
        try {
            monto = Double.parseDouble(montoTxt);
        } catch (NumberFormatException ex) {
            showAlert("Monto inválido", "Ingrese un número válido para monto.");
            return;
        }
        Movimiento m = new Movimiento(desc, monto, tipoCombo.getValue());
        service.registrarMovimiento(m);


        // recargo la tabla desde la lista del service

        actualizarSaldo();
        clearFields();
        showInfo("Guardado", "Movimiento registrado correctamente.");
    }
    private void actualizarSaldo() {
        double balance = service.calcularBalance();
        saldoLabel.setText(String.format("Saldo: %.2f", balance));
    }
    private void showError(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText(title);
        a.showAndWait();
    }
    @FXML
    private void onReporte(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reporte.fxml"));
            loader.setControllerFactory(context::getBean); // importante para que Spring inyecte beans
            Parent root = loader.load();

            // pedir el controlador y recargar datos
            pe.edu.upeu.asistencia.control.ReporteController reporteCtrl = loader.getController();
            reporteCtrl.cargarDatos();

            // agregar/seleccionar pestaña en el MainguiController (obtenido desde Spring)
            pe.edu.upeu.asistencia.control.MainguiController maingui = context.getBean(pe.edu.upeu.asistencia.control.MainguiController.class);

            // si ya existe la pestaña, seleccionar y actualizar
            for (Tab t : maingui.tabPane.getTabs()) {
                if ("Reporte".equals(t.getText())) {
                    maingui.tabPane.getSelectionModel().select(t);
                    // opcional: forzar recarga cada vez que se selecciona
                    reporteCtrl.cargarDatos();
                    return;
                }
            }

            ScrollPane scroll = new ScrollPane(root);
            scroll.setFitToWidth(true);
            scroll.setFitToHeight(true);
            Tab newTab = new Tab("Reporte", scroll);
            newTab.setUserData(reporteCtrl); // <-- asigna el controller que obtuviste antes: reporteCtrl
            maingui.tabPane.getTabs().add(newTab);
            maingui.tabPane.getSelectionModel().select(newTab);

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error", "No se pudo generar la pestaña de reporte.");
        }

    }

    private void clearFields() {
        descripcionField.clear();
        montoField.clear();
        tipoCombo.getSelectionModel().selectFirst();
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.setHeaderText(title);
        a.showAndWait();
    }

    private void showInfo(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(title);
        a.showAndWait();
    }
}
