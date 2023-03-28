import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VaccineCenter {
    private String ID;
    private String name;
    private int capacityPerDay;
    private long totalVCDoses;
    private int vaccineDosesAvailable;
    private ArrayList<Recipient> VCrecipients;
    private ArrayList<Integer> vaccineBatches;
    private ArrayList<Appointment> appointments;
    private String hallSimulatorDate;

    /**
     * Constructs a default vaccine center with all data fields set to default values
     */
    public VaccineCenter() {
    }

    /**
     * Constructs a vaccine center with the specified ID, name, capacity per day,
     * name, vaccineDosesAvailable, total VC doses, recipients, sets VC recipients 
     * to recipients that only belong to that vaccine center, and sets vaccine batches 
     * to batches that only belongs to that vaccine center
     * 
     * @param ID                    vaccine center ID
     * @param name                  vaccine center name
     * @param capacityPerDay        vaccine center capacity per day
     * @param vaccineDosesAvailable vaccine center available doses
     * @param totalVCDoses          vaccine center total vaccine doses given to recipients
     * @param recipients            vaccine center recipients
     */
    public VaccineCenter(String ID, String name, int capacityPerDay, int vaccineDosesAvailable, long totalVCDoses,
            ArrayList<Recipient> recipients) {
        this.ID = ID;
        this.name = name;
        this.capacityPerDay = capacityPerDay;
        this.vaccineDosesAvailable = vaccineDosesAvailable;
        this.totalVCDoses = totalVCDoses;
        VCrecipients = getVCRecipients(recipients);
    }

    public void setVaccineBatches(ArrayList<Vaccine> vaccines) {
        vaccineBatches = new ArrayList<>();
        for (Vaccine vaccine: vaccines) {
            if (vaccine.getAssignedVC().equals(this.ID))
                this.vaccineBatches.add(vaccine.getBatchNumber());
        }
    }

    /**
     * Mutator to set the number of vaccine doses available in that vaccine center
     * 
     * @param vaccineDosesAvailable Vaccine center available number of doses
     */
    public void setVaccineDosesAvailable(int vaccineDosesAvailable) {
        this.vaccineDosesAvailable = vaccineDosesAvailable;
    }

    /**
     * Accessor to get the appointments made on that vaccine center
     * 
     * @return List containing all appointments made on that vaccine center
     */
    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    /**
     * Accessor to get the total doses given to recipients
     * by that vaccine center
     * 
     * @return Total doses given to recipients
     */
    public long getTotalVCDoses() {
        return totalVCDoses;
    }

    /**
     * Mutator to alter the total number of doses given to
     * recipients in the vaccine center
     * 
     * @param totalVCDoses The new total number of doses
     */
    public void setTotalVCDoses(long totalVCDoses) {
        this.totalVCDoses = totalVCDoses;
    }

    /**
     * Accessor to return the vaccine center ID
     * 
     * @return vaccine center ID
     */
    public String getID() {
        return ID;
    }

    /**
     * Accessor for getting the vaccine centre name
     * 
     * @return vaccine centre name
     */
    public String getName() {
        return name;
    }

    /**
     * Accessor for getting vaccine centre capacity per day
     * 
     * @return vaccine center capacity per day
     */
    public int getCapacityPerDay() {
        return capacityPerDay;
    }

    /**
     * Accessor for getting the number of vaccine doses that are available
     * 
     * @return the number of vaccine doses that are available
     */
    public int getVaccineDosesAvailable() {
        return vaccineDosesAvailable;
    }

    /**
     * Accessor for getting the recipients array list
     * 
     * @return array list of all recipients registered
     */
    public ArrayList<Recipient> getRecipients() {
        return VCrecipients;
    }

    /**
     * Accessor getting an array list of recipients that only belong to that
     * specific vaccine center
     * 
     * @return List of recipients that are assigned to taht vaccine center
     */
    public ArrayList<Recipient> getVCRecipients(ArrayList<Recipient> recipients) {
        ArrayList<Recipient> VCRecipients = new ArrayList<>();
        for (Recipient recipient : recipients) {
            if (recipient.getVCName().equals(name))
                VCRecipients.add(recipient);
        }
        return VCRecipients;
    }

    /**
     * Sets the appointment date for the a recipient
     * 
     * @param recipientID   The ID of the recipiend to set appointment for
     * @param date          The date to assign to the target recipient
     * @return              The recipient after updating his appointment and status
     *                      or null if not recipient with given ID was found
     */
    public Recipient setAppointmentDate(String recipientID, String date) {
        for (Recipient recipient : VCrecipients) {
            if (recipient.getID().equals(recipientID)) {
                if (recipient.getStatus().equals("Pending") && recipient.getDose() == 0) {
                    recipient.setAppointment1(date);
                    recipient.setStatus("1st Dose Appointment");
                } else if (recipient.getStatus().equals("1st Dose Completed") && recipient.getDose() == 1) {
                    recipient.setAppointment2(date);
                    recipient.setStatus("2nd Dose Appointment");
                }
                return recipient;
            }
        }
        return null;
    }

    /**
     * Updates the recipient status based on crrent dose number
     * 
     * @param recipient The recipient to update his status
     */
    public void updateRecipientsStatus(Recipient recipient) {
        if (recipient.getDose() == 1)
            recipient.setStatus("1st Dose Completed");
        else if (recipient.getDose() == 2)
            recipient.setStatus("2nd Dose Completed");
    }

    /**
     * Updates the recipient dose number and dose bactch number
     * based on current status and dose and updates doses available
     * at the vaccine center and total doses given by the vaccine center
     * 
     * @param recipientID The ID of recipient to update his dose
     */
    public void updateRecipeientDose(String recipientID) {
        for (Recipient recipient : VCrecipients) {
            if (recipient.getID().equals(recipientID)) {
                if (recipient.getDose() == 0 && recipient.getStatus().equals("1st Dose Appointment")) {
                    recipient.setDose(1);
                    recipient.setDose1Batch(String.valueOf(vaccineBatches.get(0)));
                }
                else if (recipient.getDose() == 1 && recipient.getStatus().equals("2nd Dose Appointment")) {
                    recipient.setDose(2);
                    recipient.setDose2Batch(String.valueOf(vaccineBatches.get(0)));
                }
                VCLaunch.updateVaccinesInFile(vaccineBatches.remove(0));
                updateRecipientsStatus(recipient);
                totalVCDoses++;
                vaccineDosesAvailable--;
                break;
            }
        }
        UpdateVCData(ID);
    }

    /**
     * Count the number of doses given by the vaccine center
     * on each day
     */
    public void countAppointmentsPerDate() {
        ArrayList<String> dates = new ArrayList<>();
        LinkedHashMap<String, Integer> appointmentsMap = new LinkedHashMap<>();
        appointments = new ArrayList<>();

        for (Recipient recipient : VCrecipients) {
            if (recipient.getStatus().equals("1st Dose Completed"))
                dates.add(recipient.getAppointment1());

            else if (recipient.getStatus().equals("2nd Dose Completed")) {
                dates.add(recipient.getAppointment1());
                dates.add(recipient.getAppointment2());
            }
        }

        for (String date : dates) {
            if (appointmentsMap.get(date) == null)
                appointmentsMap.put(date, 1);
            else {
                int value = appointmentsMap.get(date);
                appointmentsMap.put(date, value + 1);
            }
        }

        for (var entry : appointmentsMap.entrySet()) {
            appointments.add(new Appointment(entry.getKey(), entry.getValue()));
        }
    }

    /**
     * Returns a string representation of the vaccine center details to the csv file
     * 
     * @return a string represenation of this vaccine centre to the csv file
     */
    public String toCSVString() {
        return ID + "," + name + "," + capacityPerDay + "," + vaccineDosesAvailable + "," + totalVCDoses;
    }

    /**
     * The page where the main options for a vaccine center
     * will be displayed
     * 
     * @param window The window where the page should be displayed
     */
    public void launchPage(Stage window) {

        Button appointmentsButton = GUIBuilder.CreateButton("SET APPOINTMENTS");
        appointmentsButton.getStyleClass().add("main_buttons");
        Button vaccinationButton = GUIBuilder.CreateButton("VACCINATE RECIPIENTS");
        vaccinationButton.getStyleClass().add("main_buttons");
        Button statisticsButton = GUIBuilder.CreateButton("CENTER STATISTICS");
        statisticsButton.getStyleClass().add("main_buttons");
        Button recipientInfoButton = GUIBuilder.CreateButton("RECIPIENTS INFORMATION");
        recipientInfoButton.getStyleClass().add("main_buttons");

        Label pageTitle = GUIBuilder.CreateLabel(name);
        pageTitle.getStyleClass().add("center_title");
        appointmentsButton.setOnAction(e -> setAppointmentDates(window));
        appointmentsButton.setMinWidth(350);
        appointmentsButton.setMinHeight(75);
        GridPane.setConstraints(appointmentsButton, 0, 0);

        vaccinationButton.setOnAction(e -> vaccinationSimulatorDatePicker(window));
        vaccinationButton.setMinWidth(350);
        vaccinationButton.setMinHeight(75);
        GridPane.setConstraints(vaccinationButton, 0, 1);

        statisticsButton.setOnAction(e -> statisticsTable(window));
        statisticsButton.setMinWidth(350);
        statisticsButton.setMinHeight(75);
        GridPane.setConstraints(statisticsButton, 1, 0);

        recipientInfoButton.setMinWidth(350);
        recipientInfoButton.setOnAction(e -> recipientsInformation(window));
        recipientInfoButton.setMinHeight(75);
        GridPane.setConstraints(recipientInfoButton, 1, 1);

        GridPane vcPageOptions = new GridPane();
        vcPageOptions.setVgap(50);
        vcPageOptions.setHgap(50);
        vcPageOptions.setAlignment(Pos.CENTER);
        vcPageOptions.getChildren().addAll(appointmentsButton, vaccinationButton, statisticsButton,
                recipientInfoButton);

        BorderPane vcMainPage = new BorderPane();
        vcMainPage.setTop(pageTitle);
        BorderPane.setAlignment(pageTitle, Pos.CENTER);
        vcMainPage.setCenter(vcPageOptions);

        Scene scene = GUIBuilder.CreateScene(vcMainPage, 800, 600);
        scene.getStylesheets().add("Style.css");
        window.setScene(scene);
    }

    /**
     * The page where vaccine center can pick recipients
     * and apointments to set the recipients appointment dates
     * 
     * @param window The window where the page should be displayed
     */
    public void setAppointmentDates(Stage window) {
        Label setAppointmentPageTitle = GUIBuilder.CreateLabel("Set Appointment");
        HBox pageTitle = new HBox();
        pageTitle.setPadding(new Insets(10));
        pageTitle.setAlignment(Pos.CENTER);
        pageTitle.getChildren().add(setAppointmentPageTitle);

        ListView<String> recipientsList = new ListView<>();
        recipientsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        recipientsList.setMaxHeight(500);
        recipientsList.setMaxWidth(300);
        for (Recipient recipient : VCrecipients) {
            if (recipient.getStatus().equals("Pending") || recipient.getStatus().equals("1st Dose Completed"))
                recipientsList.getItems().add(recipient.getID());
        }

        if (!recipientsList.getItems().equals(FXCollections.emptyObservableList())) {
            String format = "dd-MMM-yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
            DatePicker calendar = GUIBuilder.CreateDatePicker(format);

            VBox dateSelection = new VBox(8);
            dateSelection.setPadding(new Insets(10));
            dateSelection.getChildren().addAll(calendar);

            Button backButton = GUIBuilder.CreateButton("BACK");
            backButton.setOnAction(e -> launchPage(window));
            backButton.getStyleClass().add("red_button");

            Button confirmButton = GUIBuilder.CreateButton("CONFIRM");
            confirmButton.getStyleClass().add("green_button");

            confirmButton.setOnAction(e -> {
                ArrayList<String> selectedRecipsID = new ArrayList<>();
                ArrayList<String> unSelectedRecipsID = new ArrayList<>();
                
                if (recipientsList.getItems().isEmpty()) {
                    GUIBuilder.CreateAlert(AlertType.INFORMATION, "NOTICE", null,
                            "No more recipients waiting for appointment\nclick BACK to go to home page");
                }
                try {       
                    String date = calendar.getValue().format(dateFormatter);

                    ObservableList<String> recipientsID = recipientsList.getSelectionModel().getSelectedItems();
                    if (!recipientsID.isEmpty()) {
                        for (String recipID : recipientsID) {
                            if (checkCapacityPerday(date))
                                    unSelectedRecipsID.add(recipID);
                            else {
                                Recipient recipient = setAppointmentDate(recipID, date);
                                VCLaunch.UpdateRecipientData(recipient);
                                selectedRecipsID.add(recipID);
                            }
                        }
                        if (unSelectedRecipsID.size() != 0) {
                            GUIBuilder.CreateAlert(AlertType.WARNING, "WARNING", "Exceeded Maximum Capacity Per Day", 
                                "capacity per day: " + capacityPerDay + "\n" +
                                "Could not update recipients appointment for recipients: \n" + unSelectedRecipsID.toString());
                        }
                        for (String recipID : selectedRecipsID)
                            recipientsList.getItems().remove(recipID);
                    }
                    else
                        GUIBuilder.CreateAlert(AlertType.WARNING, "No Recipients Selected", null, 
                        "Please select recipients to set appointment for");
                } catch (NullPointerException ex) {
                    GUIBuilder.CreateAlert(AlertType.WARNING, "WARNING", "No Date Selected Please Select a Date", null);
                }
            });

            HBox pageBottom = new HBox(10);
            pageBottom.setPadding(new Insets(10));
            pageBottom.setAlignment(Pos.CENTER);
            pageBottom.getChildren().addAll(backButton, confirmButton);

            BorderPane setAppointmentPage = new BorderPane();
            setAppointmentPage.setTop(pageTitle);
            setAppointmentPage.setLeft(dateSelection);
            setAppointmentPage.setCenter(recipientsList);
            setAppointmentPage.setBottom(pageBottom);

            Scene scene = GUIBuilder.CreateScene(setAppointmentPage, 800, 600);
            scene.getStylesheets().add("Style.css");
            window.setScene(scene);
        }
        else
            GUIBuilder.CreateAlert(AlertType.INFORMATION, "NOTICE", "No Recipients Waiting For Appointment", null);
    }

    /**
     * The method that will display the information of the currently registed
     * recipients who are assigned to this specific vaccine center in a table format
     * 
     * @param window    The window on which the table page should be displayed
     */
    public void recipientsInformation(Stage window) {
        TableColumn<Recipient, String> recipIdColumn = GUIBuilder.CreateTableColumn("ID", "ID", 50);
        TableColumn<Recipient, String> recipNameColumn = GUIBuilder.CreateTableColumn("Name", "name", 200);
        TableColumn<Recipient, String> recipPhoneColumn = GUIBuilder.CreateTableColumn("Phone Number", "phone", 100);
        TableColumn<Recipient, Integer> recipAgeColumn = GUIBuilder.CreateTableColumn("Age", "age", 50);
        TableColumn<Recipient, Integer> recipDoseColumn = GUIBuilder.CreateTableColumn("Doses Recieved", "dose", 100);
        TableColumn<Recipient, String> recipStatusColumn = GUIBuilder.CreateTableColumn("Status", "status", 200);
        TableColumn<Recipient, String> recipVCNameColumn = GUIBuilder.CreateTableColumn("Assigned Vaccine Center",
                "VCName", 250);
        TableColumn<Recipient, String> recipApp1Column = GUIBuilder.CreateTableColumn("Appointment 1", "appointment1",
                150);
        TableColumn<Recipient, String> recipApp2Column = GUIBuilder.CreateTableColumn("Appointment 2", "appointment2",
                150);

        TableView<Recipient> recipientsTable = GUIBuilder.CreateTableView(500);
        recipientsTable.setItems(getRecipientsList(VCrecipients));
        recipientsTable.getColumns().addAll(recipIdColumn, recipNameColumn, recipPhoneColumn, recipAgeColumn,
                recipDoseColumn, recipStatusColumn, recipVCNameColumn, recipApp1Column, recipApp2Column);

        Label pageTitle = GUIBuilder.CreateLabel("Recipients Information Table");
        pageTitle.getStyleClass().add("table_title");

        Button backButton = GUIBuilder.CreateButton("BACK");
        backButton.getStyleClass().add("red_button");
        backButton.setOnAction(e -> launchPage(window));

        BorderPane recipInfoPage = new BorderPane();
        recipInfoPage.setPadding(new Insets(10));
        recipInfoPage.setTop(pageTitle);
        BorderPane.setAlignment(pageTitle, Pos.CENTER);
        recipInfoPage.setCenter(recipientsTable);
        recipInfoPage.setBottom(backButton);
        BorderPane.setAlignment(backButton, Pos.CENTER);

        Scene scene = GUIBuilder.CreateScene(recipInfoPage, 800, 600);
        scene.getStylesheets().add("Style.css");
        window.setScene(scene);
    }

    /**
     * Gets the recipeints and convert them to observable list
     * to be used in the table
     * 
     * @return  Observable list consisting of all recipients
     */
    public ObservableList<Recipient> getRecipientsList(ArrayList<Recipient> VCRecipients) {
        ObservableList<Recipient> recipientsList = FXCollections.observableArrayList(VCRecipients);
        return recipientsList;
    }

    /**
     * Start a simulation for how recipients vaccination would got in
     * the vaccine center vaccination hall
     * 
     * @param window    The window on which the table page should be displayed
     */
    public void recipientVaccinationSimulator(Stage window) {
        Label pageTitle = GUIBuilder.CreateLabel("Vaccination Hall Simulator: " + hallSimulatorDate);
        pageTitle.setAlignment(Pos.TOP_CENTER);
        pageTitle.getStyleClass().add("sub_title");
        Queue<Recipient> seniorQueue = new LinkedList<>();
        Queue<Recipient> normalQueue = new LinkedList<>();

        Label assignedRecipients = GUIBuilder.CreateLabel("Assigned Recipients");
        GridPane.setConstraints(assignedRecipients, 0, 0);
        assignedRecipients.setMinWidth(100);
        assignedRecipients.getStyleClass().add("queue_label");

        Label seniorQueueLabel = GUIBuilder.CreateLabel("Senior Queue");
        GridPane.setConstraints(seniorQueueLabel, 2, 0);
        seniorQueueLabel.setMinWidth(100);
        seniorQueueLabel.getStyleClass().add("queue_label");
        seniorQueueLabel.setVisible(false);

        Label normalQueueLabel = GUIBuilder.CreateLabel("Normal Queue");
        GridPane.setConstraints(normalQueueLabel, 3, 0);
        normalQueueLabel.setMinWidth(100);
        normalQueueLabel.getStyleClass().add("queue_label");
        normalQueueLabel.setVisible(false);

        Label stackOfVaccines = GUIBuilder.CreateLabel("Stack Of Vaccines");
        GridPane.setConstraints(stackOfVaccines, 1, 0);
        stackOfVaccines.getStyleClass().add("queue_label");
        stackOfVaccines.setMinWidth(100);

        Label vaccinatedRecipientsLabel = GUIBuilder.CreateLabel("Vaccinated Recipients");
        GridPane.setConstraints(vaccinatedRecipientsLabel, 4, 0);
        vaccinatedRecipientsLabel.setMinWidth(100);
        vaccinatedRecipientsLabel.getStyleClass().add("queue_label");
        vaccinatedRecipientsLabel.setVisible(false);

        ListView<String> recipientsList = new ListView<>();
        recipientsList.setSelectionModel(new NoSelectionMode<>());
        GridPane.setConstraints(recipientsList, 0, 1);
        recipientsList.setMaxHeight(500);
        recipientsList.setMaxWidth(120);

        ListView<Integer> batchList = new ListView<>();
        batchList.setSelectionModel(new NoSelectionMode<>());
        GridPane.setConstraints(batchList, 1, 1);
        batchList.setMaxHeight(500);
        batchList.setMaxWidth(120);

        ListView<String> seniorList = new ListView<>();
        seniorList.setSelectionModel(new NoSelectionMode<>());
        GridPane.setConstraints(seniorList, 2, 1);
        seniorList.setVisible(false);
        seniorList.setMaxHeight(500);
        seniorList.setMaxWidth(120);

        ListView<String> normalList = new ListView<>();
        normalList.setSelectionModel(new NoSelectionMode<>());
        GridPane.setConstraints(normalList, 3, 1);
        normalList.setVisible(false);
        normalList.setMaxHeight(500);
        normalList.setMaxWidth(120);

        for (Recipient recipient : VCrecipients) {
            if ((recipient.getStatus().equals("1st Dose Appointment")
                    && recipient.getAppointment1().equals(hallSimulatorDate))
                    || (recipient.getStatus().equals("2nd Dose Appointment")
                            && recipient.getAppointment2().equals(hallSimulatorDate))) {
                recipientsList.getItems().add(recipient.getName() + "  " + recipient.getAge());
                if (recipient.getAge() >= 60)
                    seniorQueue.add(recipient);
                else
                    normalQueue.add(recipient);
            }
        }

        if (recipientsList.getItems().equals(FXCollections.emptyObservableList())) {
            GUIBuilder.CreateAlert(AlertType.WARNING, "WARNING", "No Recipients Assigned On Selected Date",
            "Please change the simulator date");

        }
        else {
            for (Integer batch : vaccineBatches)
                batchList.getItems().add(batch);

            Button nextButton = GUIBuilder.CreateButton("Next");
            nextButton.getStyleClass().add("green_button");
            nextButton.setDisable(true);

            Button startButton = GUIBuilder.CreateButton("Start");
            startButton.getStyleClass().add("blue_button");
            startButton.setOnAction(e -> {
                for (Recipient recipient : seniorQueue)
                    seniorList.getItems().add(recipient.getName() + "  " + recipient.getAge());
            
                for (Recipient recipient : normalQueue)
                    normalList.getItems().add(recipient.getName() + "  " + recipient.getAge());
                
                startButton.setDisable(true);
                nextButton.setDisable(false);
                seniorList.setVisible(true);
                seniorQueueLabel.setVisible(true);
                normalList.setVisible(true);
                normalQueueLabel.setVisible(true);
            });

            ListView<String> vaccinatedRecipients = new ListView<>();
            vaccinatedRecipients.setSelectionModel(new NoSelectionMode<>());
            GridPane.setConstraints(vaccinatedRecipients, 4, 1);
            vaccinatedRecipients.setVisible(false);
            vaccinatedRecipients.setMaxHeight(500);
            vaccinatedRecipients.setMaxWidth(120);

            Button backButton = GUIBuilder.CreateButton("BACK");
            backButton.getStyleClass().add("red_button");
            backButton.setOnAction(e -> vaccinationSimulatorDatePicker(window));

            nextButton.setOnAction(e -> {
                if (!vaccineBatches.isEmpty()) {
                    if (!seniorQueue.isEmpty()) {
                        vaccinatedRecipients.getItems().add(seniorQueue.peek().getName() + "  " + seniorQueue.peek().getAge()
                                + "  " + batchList.getItems().remove(0));
                        updateRecipeientDose(seniorQueue.peek().getID());
                        VCLaunch.UpdateRecipientData(seniorQueue.poll());
                        seniorList.getItems().remove(0);
                        vaccinatedRecipients.setVisible(true);
                    }
                    if (!normalQueue.isEmpty()) {
                        vaccinatedRecipients.getItems().add(normalQueue.peek().getName() + "  " + normalQueue.peek().getAge()
                                + "  " + batchList.getItems().remove(0));
                        updateRecipeientDose(normalQueue.peek().getID());
                        VCLaunch.UpdateRecipientData(normalQueue.poll());
                        normalList.getItems().remove(0);
                        vaccinatedRecipients.setVisible(true);
                        vaccinatedRecipientsLabel.setVisible(true);
                    }
                    if (normalQueue.isEmpty() && seniorQueue.isEmpty())
                        GUIBuilder.CreateAlert(AlertType.INFORMATION, "NOTICE", "All Recipients Have Been Successfully Vaccinated", null);
                
                    VCLaunch.storeRecipientsDataToFile("Recipients.csv");
                }
                else
                    GUIBuilder.CreateAlert(AlertType.WARNING, "WARNING", "No Vaccine Doses Available", 
                    "available doses all distributed\nwait or ask MOH to distribute more vaccines");
        });

        HBox pageBottom = new HBox(10);
        pageBottom.setPadding(new Insets(10));
        pageBottom.setAlignment(Pos.CENTER);
        pageBottom.getChildren().addAll(backButton, startButton, nextButton);

        GridPane vcSimulator = new GridPane();
        vcSimulator.setPadding(new Insets(5));
        vcSimulator.setHgap(5);
        vcSimulator.setAlignment(Pos.CENTER);
        vcSimulator.getChildren().addAll(assignedRecipients, stackOfVaccines, seniorQueueLabel, normalQueueLabel,
                    vaccinatedRecipientsLabel, recipientsList, batchList, seniorList, normalList, vaccinatedRecipients);

        BorderPane hallSimulatorPage = new BorderPane();
        hallSimulatorPage.setTop(pageTitle);
        BorderPane.setAlignment(pageTitle, Pos.CENTER);
        hallSimulatorPage.setCenter(vcSimulator);
        hallSimulatorPage.setBottom(pageBottom);

        Scene scene = GUIBuilder.CreateScene(hallSimulatorPage, 800, 600);
        scene.getStylesheets().add("Style.css");
        window.setScene(scene);
        }
    }

    /**
     * Let the vaccine center choose the date for which simulation
     * of recipient vaccination is to be shown
     * 
     * @param window The window on which the table page should be displayed
     */
    public void vaccinationSimulatorDatePicker(Stage window) {
        String format = "dd-MMM-yyyy";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
        DatePicker calendar = GUIBuilder.CreateDatePicker(format);

        Label pageTitle = GUIBuilder.CreateLabel("VC Simulator Date Picker");
        pageTitle.getStyleClass().add("sub_title");
        VBox dateSelection = new VBox(8);
        dateSelection.setPadding(new Insets(10));
        dateSelection.getChildren().addAll(pageTitle, calendar);
        dateSelection.setAlignment(Pos.TOP_CENTER);

        Button backButton = new Button("BACK");
        backButton.setOnAction(e -> launchPage(window));
        backButton.getStyleClass().add("red_button");

        Button confirmButton = new Button("CONFIRM");
        confirmButton.getStyleClass().add("green_button");
        confirmButton.setOnAction(e -> {
            try {
                hallSimulatorDate = calendar.getValue().format(dateFormatter);
                recipientVaccinationSimulator(window);
            } catch (NullPointerException ex) {
                GUIBuilder.CreateAlert(AlertType.WARNING, "WARNING", "No Date Selected\nPlease Select a Date", null);
            }
        });

        HBox pageBottom = new HBox(10);
        pageBottom.getChildren().addAll(backButton, confirmButton);
        pageBottom.setAlignment(Pos.CENTER);
        pageBottom.setPadding(new Insets(10));

        BorderPane datePickerPage = new BorderPane();
        datePickerPage.setCenter(dateSelection);
        datePickerPage.setBottom(pageBottom);

        Scene scene = GUIBuilder.CreateScene(datePickerPage, 800, 600);
        scene.getStylesheets().add("Style.css");
        window.setScene(scene);

    }

    /**
     * The method that will display the information related to vaccination
     * statistics from this vaccine center in a table format
     * 
     * @param window    The window on which the table page should be displayed
     */
    public void statisticsTable(Stage window) {
        countAppointmentsPerDate();
        TableColumn<Appointment, String> dateColumn = GUIBuilder.CreateTableColumn("Date", "date", 50);
        TableColumn<Appointment, String> appPerdateColumn = GUIBuilder.CreateTableColumn("Number of Doses",
                "appointmentsPerDate", 100);

        TableView<Appointment> appointmentsTable = GUIBuilder.CreateTableView(500);
        appointmentsTable.setItems(getAppointmentsList(appointments));
        appointmentsTable.getColumns().addAll(dateColumn, appPerdateColumn);

        Label totalVCDoseseFromVC = GUIBuilder.CreateLabel("Total Doses Given: " + totalVCDoses);
        totalVCDoseseFromVC.getStyleClass().add("statistics_label");

        Label dosesAvailabelForVC = GUIBuilder.CreateLabel("Total Available Doses: " + vaccineDosesAvailable);
        dosesAvailabelForVC.getStyleClass().add("statistics_label");
        
        VBox statistics = new VBox(5);
        statistics.setPadding(new Insets(5));
        statistics.setAlignment(Pos.CENTER);
        statistics.getChildren().addAll(appointmentsTable, totalVCDoseseFromVC, dosesAvailabelForVC);

        Label pageTitle = GUIBuilder.CreateLabel("Doses By Day");
        pageTitle.getStyleClass().add("table_title");

        Button backButton = GUIBuilder.CreateButton("BACK");
        backButton.getStyleClass().add("red_button");
        backButton.setOnAction(e -> launchPage(window));

        BorderPane statisticsPage = new BorderPane();
        statisticsPage.setPadding(new Insets(10));
        statisticsPage.setTop(pageTitle);
        BorderPane.setAlignment(pageTitle, Pos.CENTER);
        statisticsPage.setCenter(statistics);
        statisticsPage.setBottom(backButton);
        BorderPane.setAlignment(backButton, Pos.CENTER);

        Scene scene = GUIBuilder.CreateScene(statisticsPage, 800, 600);
        scene.getStylesheets().add("Style.css");
        window.setScene(scene);
    }

    /**
     * Gets the appointsments and convert them to observable list
     * to be used in the table
     * 
     * @param appointments  The appointments made on that vaccine center
     * @return  Observable list consisting of all appointments
     */
    public ObservableList<Appointment> getAppointmentsList(ArrayList<Appointment> appointments) {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList(appointments);
        return appointmentsList;
    }

    /**
     * Checks if a vaccine center has exceeded or reached maximum 
     * capacity per day for a specific date
     * 
     * @param date  the date to check on
     * @return  wheather capacity per day reached or exceeded or not
     */
    public boolean checkCapacityPerday(String date) {
        int appointmentPerDay = 0;
        for (Recipient recipient : VCrecipients) {
            if ((recipient.getAppointment1().equals(date) && recipient.getStatus().equals("1st Dose Appointment")) ||
                (recipient.getAppointment2().equals(date) && recipient.getStatus().equals("2nd Dose Appointment")))
                appointmentPerDay++;
        }

        if (appointmentPerDay == capacityPerDay)
            return true;
        
        return false;
    }

    /**
     * Updates the target vaccine center details by modifying the vaccine center
     * date for vaccine center with same ID passed
     * 
     * @param VCID The ID of the target vaccine center to be updated
     */
    public void UpdateVCData(String VCID) {
        for (VaccineCenter vaccineCenter: VCLaunch.vaccineCenters) {
            if (vaccineCenter.getID().equals(this.ID)) {
                vaccineCenter.setVaccineDosesAvailable(this.vaccineDosesAvailable);
                vaccineCenter.setTotalVCDoses(this.totalVCDoses);
            }
        }        
        storeVCsDataToFile("VaccineCenters.csv");
    }

    /**
     * Stores back the vaccine centers and their details to the terget
     * file to keep the data in file up to date
     * 
     * @param fileName  The target file where vaccine centers details should be stored
     */
    public void storeVCsDataToFile(String fileName) {
        StringBuilder VCBuilder = new StringBuilder();

        for (VaccineCenter vaccineCenter : VCLaunch.vaccineCenters)
            VCBuilder.append(vaccineCenter.toCSVString() + "\n");
        try {
            Files.write(Paths.get(fileName), VCBuilder.toString().getBytes());
        } catch (IOException e) {
            VCLaunch.createFile(fileName);
        }
    }
}