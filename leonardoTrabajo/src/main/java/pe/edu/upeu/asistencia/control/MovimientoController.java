package pe.edu.upeu.asistencia.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.asistencia.enums.Moneda;
import pe.edu.upeu.asistencia.enums.TipoMovimiento;
import pe.edu.upeu.asistencia.modelo.Movimiento;
import pe.edu.upeu.asistencia.servicio.MonedaService;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

@Controller
public class MovimientoController {
    @FXML private TableView<Movimiento> tablaMovimientos;
    @FXML private TableColumn<Movimiento, String> colDescripcion;
    @FXML private TableColumn<Movimiento, Double> colMonto;
    @FXML private TableColumn<Movimiento, TipoMovimiento> colTipo;
    @FXML private TableColumn<Movimiento, Moneda> colMoneda;
    @FXML private Label saldoLabel;
    @FXML private TextField descripcionField;
    @FXML private TextField montoField;
    @FXML private ComboBox<TipoMovimiento> tipoCombo;
    @FXML private ComboBox<Moneda> monedaCombo;
    @FXML private ComboBox<Moneda> monedaVistaCombo;
    @FXML private Button guardarBtn;
    @FXML private Button reporteBtn;
    @FXML private Button limpiarBtn;

    @Autowired
    private MonedaService service;
    @Autowired
    private ApplicationContext context;

    @FXML
    public void initialize() {
        System.out.println("Inicializando MovimientoController");
        tipoCombo.setItems(FXCollections.observableArrayList(TipoMovimiento.values()));
        tipoCombo.getSelectionModel().selectFirst();

        monedaCombo.setItems(FXCollections.observableArrayList(Moneda.values()));
        monedaCombo.getSelectionModel().selectFirst();

        monedaVistaCombo.setItems(FXCollections.observableArrayList(Moneda.values()));
        monedaVistaCombo.getSelectionModel().selectFirst();
        monedaVistaCombo.setOnAction(e -> actualizarSaldo());
        
        // Configurar botón de limpiar
        if (limpiarBtn != null) {
            limpiarBtn.setOnAction(e -> limpiarFormulario());
        }

        // Configurar tabla
        tablaMovimientos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Configurar columnas del FXML
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colMoneda.setCellValueFactory(new PropertyValueFactory<>("moneda"));
        
        // Asegurar que la tabla sea visible
        tablaMovimientos.setVisible(true);
        tablaMovimientos.setPlaceholder(new Label("No hay movimientos registrados"));

        // Inicializar items
        tablaMovimientos.setItems(service.listarMovimientos()); // 👈 observable compartido
        actualizarSaldo();
    }

    @FXML
    private void onGuardar(ActionEvent event) {
        // Validar campos obligatorios
        String desc = descripcionField.getText().trim();
        String montoTxt = montoField.getText().trim();
        
        if (desc.isEmpty() || montoTxt.isEmpty()) {
            showAlert("Faltan datos", "Complete descripción y monto.");
            return;
        }
        
        // Validar longitud de descripción
        if (desc.length() < 3) {
            showAlert("Descripción muy corta", "La descripción debe tener al menos 3 caracteres.");
            return;
        }
        
        if (desc.length() > 100) {
            showAlert("Descripción muy larga", "La descripción no puede exceder 100 caracteres.");
            return;
        }
        
        // Validar monto
        double monto;
        try {
            monto = Double.parseDouble(montoTxt);
        } catch (NumberFormatException ex) {
            showAlert("Monto inválido", "Ingrese un número válido para monto.");
            return;
        }
        
        // Validar que el monto sea positivo
        if (monto <= 0) {
            showAlert("Monto inválido", "El monto debe ser mayor a 0.");
            return;
        }
        
        // Validar que el monto no sea excesivamente grande
        if (monto > 999999999.99) {
            showAlert("Monto muy grande", "El monto no puede exceder 999,999,999.99.");
            return;
        }
        
        // Validar que se haya seleccionado tipo y moneda
        if (tipoCombo.getValue() == null) {
            showAlert("Tipo requerido", "Seleccione un tipo de movimiento.");
            return;
        }
        
        if (monedaCombo.getValue() == null) {
            showAlert("Moneda requerida", "Seleccione una moneda.");
            return;
        }
        
        try {
            Movimiento m = new Movimiento(desc, monto, tipoCombo.getValue(), monedaCombo.getValue());
            service.registrarMovimiento(m);
            
            actualizarSaldo();
            clearFields();
            showInfo("Guardado", "Movimiento registrado correctamente.");
        } catch (Exception ex) {
            showError("Error al guardar", "No se pudo registrar el movimiento: " + ex.getMessage());
        }
    }
    private void actualizarSaldo() {
        try {
            Moneda monedaVista = monedaVistaCombo.getValue();
            if (monedaVista == null) {
                saldoLabel.setText("Saldo: 0.00");
                return;
            }
            
            double balance = service.calcularBalance(monedaVista);
            String simboloMoneda = getSimboloMoneda(monedaVista);
            saldoLabel.setText(String.format("Saldo: %s %.2f", simboloMoneda, balance));
        } catch (Exception ex) {
            saldoLabel.setText("Error al calcular saldo");
            showError("Error", "No se pudo calcular el saldo: " + ex.getMessage());
        }
    }
    
    private String getSimboloMoneda(Moneda moneda) {
        switch (moneda) {
            case PEN: return "S/";
            case USD: return "$";
            case EUR: return "€";
            default: return "";
        }
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
        monedaCombo.getSelectionModel().selectFirst();
    }
    
    @FXML
    private void limpiarFormulario() {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Limpiar Formulario");
        confirmDialog.setHeaderText("¿Está seguro que desea limpiar el formulario?");
        confirmDialog.setContentText("Se perderán todos los datos ingresados.");
        
        confirmDialog.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        confirmDialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.YES) {
                clearFields();
                showInfo("Formulario Limpiado", "Los campos han sido limpiados correctamente.");
            }
            return null;
        });
        confirmDialog.showAndWait();
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
