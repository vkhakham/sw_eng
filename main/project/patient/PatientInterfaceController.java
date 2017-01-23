package patient;

import common.*;
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
    private Button getAppointmentsBtn;

    @FXML
    private MenuItem doctorTypeGP;

    @FXML
    private MenuItem doctorTypeOrthopede;

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

    private void handlePatientGetFreeAppointmentsForSpecialist(Message msg) {
        ObservableList<String> datesData = FXCollections.observableList(((FreeAppointments)msg.data).getDates());
        Doctor doctor = ((FreeAppointments) msg.data).getDoctor();
        timesList.setItems(datesData);
        timesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            ScheduledAppointment newAppointment  = new ScheduledAppointment();
            newAppointment.setDoctor(doctor);
            newAppointment.setDate(newValue);
            try {
                sendToServer(new Message(Uri.PatientScheduleAppointment, user, sessionId, ClientType.Patient, newAppointment));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Selected item: " + newValue);
        });
    }

    private void handlePatientGetSpecialistDoctorList(Message msg) {
        System.out.println("msg");
        ObservableList<Doctor> doctors = null;
        setBranchColumn.setCellValueFactory(new PropertyValueFactory<Doctor, String>("Branch"));
        setDoctorNameColumn.setCellValueFactory(new PropertyValueFactory<Doctor, String>("Name"));
        doctors = FXCollections.observableList((List<Doctor>) msg.data);
        setAppointmetsDoctorTable.setItems(doctors);

        setAppointmetsDoctorTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Doctor>() {
            @Override
            public void changed(ObservableValue<? extends Doctor> observable, Doctor oldValue, Doctor newValue) {
                try {
                    sendToServer(new Message(Uri.PatientGetFreeAppointmentsForSpecialist, user, sessionId, ClientType.Patient, newValue.getId()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Selected item: " + newValue);
            }
        });
    }

    private void handlePatientGetFreeAppointmentsForGp(Message msg) {
        ObservableList<String> datesData = FXCollections.observableList(((FreeAppointments)msg.data).getDates());
        timesList.setItems(datesData);
        Doctor doctor = ((FreeAppointments) msg.data).getDoctor();
        ArrayList<Doctor> gpDoc = new ArrayList<Doctor>();
        gpDoc.add(doctor);
        setBranchColumn.setCellValueFactory(new PropertyValueFactory<Doctor, String>("Branch"));
        setDoctorNameColumn.setCellValueFactory(new PropertyValueFactory<Doctor, String>("Name"));
        ObservableList<Doctor> obsGpDoc = FXCollections.observableList(gpDoc);
        setAppointmetsDoctorTable.setItems(obsGpDoc);

        timesList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ScheduledAppointment newAppointment  = new ScheduledAppointment();
                newAppointment.setDoctor(doctor);
                newAppointment.setDate(newValue);
                try {
                    sendToServer(new Message(Uri.PatientScheduleAppointment, user, sessionId, ClientType.Patient, newAppointment));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Selected item: " + newValue);
            }
        });
    }

    private void handleFutureScheduledAppointments(Message msg) {
        ObservableList<ScheduledAppointment> tableData = null;
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
        if (!msg.error.equals(ErrorType.NotFound)) {
            tableData = FXCollections.observableList((List<ScheduledAppointment>) msg.data);
        }
        appointments.setItems(tableData);
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
}
