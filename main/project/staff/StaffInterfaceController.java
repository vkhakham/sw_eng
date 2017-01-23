package staff;

import common.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import ocsf.client.AbstractClient;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


public class StaffInterfaceController extends AbstractClient {
    private Integer sessionId;
    private Integer user;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<ScheduledAppointment> patientsTable;

    @FXML
    private TableColumn<ScheduledAppointment, String> timeColumn;

    @FXML
    private TableColumn<ScheduledAppointment, String> nameColumn;

    @FXML
    private TableColumn<ScheduledAppointment, Integer> idColumn;

    @FXML
    private Button pickToday;

    @FXML
    private Button showDate;

    /**
     * Constructs the client.
     *
     * @param host the server's host name.
     * @param port the port number.
     */
    StaffInterfaceController(String host, int port, Integer sessionId, Integer user) {
        super(host, port);
        this.user = user;
        this.sessionId = sessionId;
        try {
            openConnection();
            sendToServer(new Message(Uri.EmployeeGetQueue, this.user, this.sessionId, ClientType.Employee, LocalDate.now().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void GetPatientList(Event event) throws Exception {
        LocalDate chosenDate;
        if (!event.getSource().equals(pickToday)) {
            chosenDate = datePicker.getValue();
        } else {
            chosenDate = LocalDate.now();
        }
        if (chosenDate != null) {
            try {
                sendToServer(new Message(Uri.EmployeeGetQueue, this.user, this.sessionId, ClientType.Employee, chosenDate.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void handleMessageFromServer(Object objMsg){
        Message msg = Message.class.cast(objMsg);
        switch (msg.uri) {
            case EmployeeGetQueue:
                handleNewPatientList(msg);
                break;
        }
    }

    private void handleNewPatientList(Message msg) {
        ObservableList<ScheduledAppointment> tableData = null;
        timeColumn.setCellValueFactory(new PropertyValueFactory<ScheduledAppointment, String>("date"));
        nameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ScheduledAppointment, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ScheduledAppointment, String> p) {
                if (p.getValue() != null && p.getValue().getPatient() != null) {
                    return new SimpleStringProperty(p.getValue().getPatient().getName());
                } else {
                    return new SimpleStringProperty("<No name>");
                }
            }
        });
        idColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ScheduledAppointment, Integer>, ObservableValue<Integer>>() {
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ScheduledAppointment, Integer> p) {
                if (p.getValue() != null && p.getValue().getPatient() != null) {
                    return new SimpleIntegerProperty(p.getValue().getPatient().getId()).asObject();
                } else {
                    return new SimpleIntegerProperty(0).asObject();
                }
            }
        });
        if (!msg.error.equals(ErrorType.NotFound)) {
            tableData = FXCollections.observableList((List<ScheduledAppointment>) msg.data);
        }

        ObservableList<ScheduledAppointment> finalTableData = tableData;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                patientsTable.setItems(finalTableData);
            }
        });
    }
}
