package patient.src;

import common.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;
import ocsf.client.AbstractClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PatientInterfaceController extends AbstractClient {
    private Integer sessionId;
    private Integer user;

    private Doctor doctorTimesAreShownFor;

    @FXML
    private Button refreshBtn;

    @FXML
    private Button getTimesBtn;

    @FXML
    private Text timesText;

    @FXML
    private TableView<Doctor> setAppointmetsDoctorTable;

    @FXML
    private TableColumn<Doctor, String> setBranchColumn;

    @FXML
    private TableColumn<Doctor, String> setDoctorNameColumn;

    @FXML
    private MenuButton selectDoctorMenu;

    @FXML
    private Button setAppointmentBtn;

    @FXML
    private Button cancelAppointmentBtn;

    @FXML
    private MenuItem doctorTypeGP;

    @FXML
    private MenuItem doctorTypeOrthopede;

    @FXML
    private MenuItem doctorTypeEye;

    @FXML
    private ListView<String> timesList;

    @FXML
    private TableView<ScheduledAppointment> appointments;

    @FXML
    private TableColumn<ScheduledAppointment, String> futureTimeColumn;

    @FXML
    private TableColumn<ScheduledAppointment, String> futureDoctorNameColumn;

    @FXML
    private TableColumn<ScheduledAppointment, String> futureDoctorRoleColumn;

    /**
     * Constructs the client.
     *
     * @param host the server's host name.
     * @param port the port number.
     */
    PatientInterfaceController(String host, int port, Integer sessionId, Integer user) {
        super(host, port);
        this.user = user;
        this.sessionId = sessionId;
        try {
            openConnection();
            sendToServer(new Message(Uri.PatientGetFutureScheduledAppointments, this.user, this.sessionId, ClientType.Patient, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * handle responses from server
     *
     * @param objMsg Message from server
     */

    @Override
    protected void handleMessageFromServer(Object objMsg){
        Message msg = Message.class.cast(objMsg);
        switch (msg.uri) {
            case PatientGetFutureScheduledAppointments:
                handleFutureScheduledAppointments(msg);
                break;
            case PatientGetFreeAppointmentsForSpecialist:
                handlePatientGetFreeAppointmentsForSpecialist(msg);
                break;
            case PatientGetFreeAppointmentsForGp:
                handlePatientGetFreeAppointmentsForGp(msg);
                break;
            case PatientGetSpecialistDoctorList:
                handlePatientGetSpecialistDoctorList(msg);
                break;
        }
    }

    /**
     * show available Appointments received from server in a listView
     *
     * @param msg Message from server
     */
    private void handlePatientGetFreeAppointmentsForSpecialist(Message msg) {
        ObservableList<String> datesData = FXCollections.observableList(((FreeAppointments)msg.data).getDates());
        Doctor doctor = ((FreeAppointments) msg.data).getDoctor();
        Platform.runLater(() -> timesList.setItems(datesData));
    }

    /**
     * show specialist doctors list from server
     *
     * @param msg Server message
     */
    private void handlePatientGetSpecialistDoctorList(Message msg) {
        ObservableList<Doctor> doctors = null;
        setBranchColumn.setCellValueFactory(new PropertyValueFactory<Doctor, String>("Branch"));
        setDoctorNameColumn.setCellValueFactory(new PropertyValueFactory<Doctor, String>("Name"));
        doctors = FXCollections.observableList((List<Doctor>) msg.data);

        ObservableList<Doctor> finalDoctors = doctors;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setAppointmetsDoctorTable.setItems(finalDoctors);
                timesList.getItems().clear();
            }
        });
    }

    private void handlePatientGetFreeAppointmentsForGp(Message msg) {
        ObservableList<String> datesData = FXCollections.observableList(((FreeAppointments)msg.data).getDates());
        Doctor doctor = ((FreeAppointments) msg.data).getDoctor();
        ArrayList<Doctor> gpDoc = new ArrayList<Doctor>();
        gpDoc.add(doctor);
        setBranchColumn.setCellValueFactory(new PropertyValueFactory<Doctor, String>("Branch"));
        setDoctorNameColumn.setCellValueFactory(new PropertyValueFactory<Doctor, String>("Name"));
        ObservableList<Doctor> obsGpDoc = FXCollections.observableList(gpDoc);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                timesList.setItems(datesData);
                setAppointmetsDoctorTable.setItems(obsGpDoc);
                setAppointmetsDoctorTable.getSelectionModel().selectFirst();
            }
        });
    }

    private void handleFutureScheduledAppointments(Message msg) {
        ObservableList<ScheduledAppointment> tableData = null;
        if (!msg.error.equals(ErrorType.NotFound)) {
            tableData = FXCollections.observableList((List<ScheduledAppointment>) msg.data);
            System.out.println(tableData.size());
        }
        ObservableList<ScheduledAppointment> finalTableData = tableData;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                futureTimeColumn.setCellValueFactory(new PropertyValueFactory<ScheduledAppointment, String>("Date"));
                futureDoctorNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ScheduledAppointment, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ScheduledAppointment, String> p) {
                        if (p.getValue() != null && p.getValue().getDoctor() != null) {
                            return new SimpleStringProperty(p.getValue().getDoctor().getName());
                        } else {
                            return new SimpleStringProperty("<No name>");
                        }
                    }
                });
                futureDoctorRoleColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ScheduledAppointment, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ScheduledAppointment, String> p) {
                        if (p.getValue() != null && p.getValue().getDoctor() != null) {
                            return new SimpleStringProperty(p.getValue().getDoctor().getRole());
                        } else {
                            return new SimpleStringProperty("<No Role>");
                        }
                    }
                });
                appointments.setItems(finalTableData);
            }
        });
    }

    @FXML
    void handleGpMenuChoice(ActionEvent event) {
        try {
            sendToServer(new Message(Uri.PatientGetFreeAppointmentsForGp, this.user, this.sessionId, ClientType.Patient, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleSpecialistMenuChoice(ActionEvent event) {
        String type = "dr_" + ((MenuItem) event.getSource()).getText();
        System.out.println(type);
        try {
            sendToServer(new Message(Uri.PatientGetSpecialistDoctorList, this.user, this.sessionId, ClientType.Patient, type));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleRefreshFutureButton(ActionEvent event) {
        try {
            sendToServer(new Message(Uri.PatientGetFutureScheduledAppointments, this.user, this.sessionId, ClientType.Patient, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleCancelButton(ActionEvent event) {
        ScheduledAppointment newAppointment  = appointments.getSelectionModel().getSelectedItem();
        try {
            sendToServer(new Message(Uri.PatientUnscheduleAppointment, this.user, this.sessionId, ClientType.Patient, newAppointment));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleSetButton(ActionEvent event) {
        ScheduledAppointment newAppointment  = new ScheduledAppointment();
        newAppointment.setDoctor(setAppointmetsDoctorTable.getSelectionModel().getSelectedItem());
        newAppointment.setDate(timesList.getSelectionModel().getSelectedItem());
        System.out.println(newAppointment.getDate());
        try {
            sendToServer(new Message(Uri.PatientScheduleAppointment, user, sessionId, ClientType.Patient, newAppointment));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleGetTimesBtn(ActionEvent event) {
        Message reply;
        switch(setAppointmetsDoctorTable.getSelectionModel().getSelectedItem().getRole()) {
            case "dr_gp":
                reply = new Message(Uri.PatientGetFreeAppointmentsForGp, this.user, this.sessionId, ClientType.Patient, null);
                break;
            default:
                reply = new Message(Uri.PatientGetFreeAppointmentsForSpecialist, user, sessionId, ClientType.Patient, setAppointmetsDoctorTable.getSelectionModel().getSelectedItem().getId());
        };
        try {
            sendToServer(reply);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
