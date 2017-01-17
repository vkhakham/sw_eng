package staff;

import common.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TableColumn<ScheduledAppointment, String> idColumn;

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
            sendToServer(new Message(Uri.EmployeeGetQueue, this.user, this.sessionId, ClientType.Employee, "2017-01-01"));
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
        try {
            sendToServer(new Message(Uri.EmployeeGetQueue, this.user, this.sessionId, ClientType.Employee, chosenDate.toString()));
        } catch (IOException e) {
            e.printStackTrace();
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
        idColumn.setCellValueFactory(new PropertyValueFactory<ScheduledAppointment, String>("patientId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<ScheduledAppointment, String>("patientName"));
        if (!msg.error.equals(ErrorType.NotFound)) {
            tableData = FXCollections.observableList((List<ScheduledAppointment>) msg.data);
        }
        patientsTable.setItems(tableData);
    }
}
