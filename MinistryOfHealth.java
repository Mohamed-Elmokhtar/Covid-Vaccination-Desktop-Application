import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MinistryOfHealth {
    private ArrayList<Recipient> recipients;
    private ArrayList<VaccineCenter> vaccineCenters;
    private ArrayList<Vaccine> vaccines;
    private int currentVaccineBatch;
    private ArrayList<Appointment> appointments;

    /**
     * Constructs a default Ministry Of Health with recipients, vaccineCenters, vaccines
     * vaccineBatches and appointments set to null
     */
    public MinistryOfHealth() {}

    /**
     * Mutator to set ministry of health recipients
     * 
     * @param recipients    The recipients to set to be under this ministry of health
     */
    public void setRecipients(ArrayList<Recipient> recipients) {
        this.recipients = recipients;
    }

    /**
     * Mutator to set ministry of health vaccine centers
     * 
     * @param vaccineCenters    The vaccine centers to set to be under this ministry of health
     */
    public void setVaccineCenters(ArrayList<VaccineCenter> vaccineCenters) {
        this.vaccineCenters = vaccineCenters;
    }

    /**
     * Mutator to set the vaccine batch of the upcoming dose to distribute
     * 
     * @param currentVaccineBatch   The batch number of the last vaccine ditributed incremented by one
     */
    public void setCurrentVaccineBatch(int currentVaccineBatch) {
        this.currentVaccineBatch = currentVaccineBatch;
    }

    /**
     * Mutator to set the vaccines to vaccines that has been already distributed by ministry of health
     * 
     * @param vaccines  Contains each vaccine batch and the vaccined center it dot distributed to it
     */
    public void setVaccines(ArrayList<Vaccine> vaccines) {
        this.vaccines = vaccines;
    }

    /**
     * The method that will instantiate the program for ministry of health by displaying
     * a page that consists of ministry of health role main features
     * 
     * @param window    The window on which the page should be displayed
     */
    public void launchPage(Stage window) {
        Label mohMainPageTitle = GUIBuilder.CreateLabel("Ministry Of Health");
        mohMainPageTitle.getStyleClass().add("title");
        HBox pageTitle = new HBox();
        pageTitle.setPadding(new Insets(10));
        pageTitle.setAlignment(Pos.CENTER);
        pageTitle.getChildren().add(mohMainPageTitle);

        Button showRecipientsButton = GUIBuilder.CreateButton("RECIPIENTS INFORMATION");
        showRecipientsButton.getStyleClass().add("main_buttons");
        showRecipientsButton.setOnAction(e -> recipientsInformation(window));
        showRecipientsButton.setMinWidth(350);
        showRecipientsButton.setMinHeight(75);
        GridPane.setConstraints(showRecipientsButton, 0, 0);

        Button recipientsDistributionButton = GUIBuilder.CreateButton("DISTRIBUTE RECIPIENTS");
        recipientsDistributionButton.getStyleClass().add("main_buttons");
        recipientsDistributionButton.setOnAction(e -> recipientsDistribution(window));
        recipientsDistributionButton.setMinWidth(350);
        recipientsDistributionButton.setMinHeight(75);
        GridPane.setConstraints(recipientsDistributionButton, 1, 0);

        Button vaccineDistributionButton = GUIBuilder.CreateButton("DISTRIBUTE VACCINES");
        vaccineDistributionButton.getStyleClass().add("main_buttons");
        vaccineDistributionButton.setOnAction(e -> vaccineDistribution(window));
        vaccineDistributionButton.setMinWidth(350);
        vaccineDistributionButton.setMinHeight(75);
        GridPane.setConstraints(vaccineDistributionButton, 0, 1);

        Button statisticsButton = GUIBuilder.CreateButton("VACCINATION STATISTICS");
        statisticsButton.setOnAction(e -> statisticsTable(window));
        statisticsButton.getStyleClass().add("main_buttons");
        statisticsButton.setMinWidth(350);
        statisticsButton.setMinHeight(75);
        GridPane.setConstraints(statisticsButton, 1, 1);

        GridPane mohMainPageOptions = new GridPane();
        mohMainPageOptions.setVgap(50);
        mohMainPageOptions.setHgap(50);
        mohMainPageOptions.setAlignment(Pos.CENTER);
        mohMainPageOptions.getChildren().addAll(showRecipientsButton, recipientsDistributionButton,
                vaccineDistributionButton, statisticsButton);

        BorderPane mohMainPage = new BorderPane();
        mohMainPage.setTop(pageTitle);
        mohMainPage.setCenter(mohMainPageOptions);

        Scene scene = GUIBuilder.CreateScene(mohMainPage, 800, 600);
        scene.getStylesheets().add("Style.css");
        window.setScene(scene);
    }

    /**
     * The method that will display the information of all the currently registed
     * recipients in a table format
     * 
     * @param window    The window on which the table page should be displayed
     */
    public void recipientsInformation(Stage window) {
        TableColumn<Recipient, String> recipIdColumn = GUIBuilder.CreateTableColumn("ID", "ID", 50);
        TableColumn<Recipient, String> recipNameColumn = GUIBuilder.CreateTableColumn("Name", "name", 200);
        TableColumn<Recipient, String> recipPhoneColumn = GUIBuilder.CreateTableColumn("Phone Number", "phone", 100);
        TableColumn<Recipient, Integer> recipAgeColumn = GUIBuilder.CreateTableColumn("Age", "age", 50);
        TableColumn<Recipient, Integer> recipDoseColumn = GUIBuilder.CreateTableColumn("Doses Recieved", "dose", 100);
        TableColumn<Recipient, String> recipStatusColumn = GUIBuilder.CreateTableColumn("Status", "status", 150);
        TableColumn<Recipient, String> recipVCNameColumn = GUIBuilder.CreateTableColumn("Assigned Vaccine Center", "VCName", 250);
        TableColumn<Recipient, String> recipApp1Column = GUIBuilder.CreateTableColumn("Appointment 1", "appointment1", 150);
        TableColumn<Recipient, String> recipApp2Column = GUIBuilder.CreateTableColumn("Appointment 2", "appointment2", 150);

        TableView<Recipient> recipientsTable = GUIBuilder.CreateTableView(500);
        recipientsTable.setItems(getRecipientsList());
        recipientsTable.getColumns().addAll(recipIdColumn, recipNameColumn, recipPhoneColumn, recipAgeColumn,
                                            recipDoseColumn, recipStatusColumn, recipVCNameColumn, recipApp1Column, recipApp2Column);

        Label pageTitle = GUIBuilder.CreateLabel("Recipients Information");
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
     * The method that will allow ministry of health to distribute recipients over 
     * vaccine centers
     * 
     * @param window    The window on which the page should be displayed
     */
    public void recipientsDistribution(Stage window) {
        Label recipientsDistributionPageTitle = GUIBuilder.CreateLabel("Recipients Distribution");
        recipientsDistributionPageTitle.getStyleClass().add("title");
        HBox pageTitle = new HBox();
        pageTitle.setPadding(new Insets(10));
        pageTitle.setAlignment(Pos.CENTER);
        pageTitle.getChildren().add(recipientsDistributionPageTitle);

        ComboBox<String> recipientsList = new ComboBox<>();
        GridPane.setConstraints(recipientsList, 0, 0);
        for (Recipient recipient: recipients) {
            if (recipient.getVCName().equals("None")) {
                recipientsList.getItems().add(recipient.getID());
            }
        }

        if (!recipientsList.getItems().equals(FXCollections.emptyObservableList())) {

            ComboBox<String> VCsList = new ComboBox<>();
            GridPane.setConstraints(VCsList, 1, 0);
            for (VaccineCenter vaccineCenter: vaccineCenters) {
                VCsList.getItems().add(vaccineCenter.getID());
            }

            Button assigningButton = GUIBuilder.CreateButton("Assign Recipient To VC");
            assigningButton.getStyleClass().add("green_button");
            assigningButton.setDisable(true);
            GridPane.setConstraints(assigningButton, 1, 2);

            VBox recipientDetails = new VBox(8);
            recipientDetails.setPadding(new Insets(20));
            GridPane.setConstraints(recipientDetails, 0, 1);
            recipientsList.setPromptText("Select Recipient ID");
            recipientsList.setOnAction(e -> {
                recipientDetails.getChildren().clear();
                if (recipientsList.getValue() != null) {
                    LinkedList<Label> labels = getRecipientDetials(getSelectedRecipient(recipientsList.getValue()));
                    recipientDetails.getChildren().addAll(labels);
                    assigningButton.setText("Assign " + recipientsList.getValue() + " To " + VCsList.getValue());
                }
                if (VCsList.getValue() != null)
                    assigningButton.setDisable(false);
            });

            assigningButton.setOnAction(e -> {
                Recipient recipient = getSelectedRecipient(recipientsList.getValue());
                VaccineCenter vaccineCenter = getSelectedVC(VCsList.getValue());
                recipient.setVCName(vaccineCenter.getName());
                UpdateRecipientData(recipient);
                recipientsList.getItems().remove(recipientsList.getValue());
                recipientsList.getSelectionModel().select(null);
                assigningButton.setText("Assign Recipient To " + VCsList.getValue());
                assigningButton.setDisable(true);
            });

            VBox VCDetails = new VBox(8);
            VCDetails.setPadding(new Insets(20));
            GridPane.setConstraints(VCDetails, 1, 1);
            VCsList.setPromptText("Select VC ID");
            VCsList.setOnAction(e -> {
                LinkedList<Label> labels = getVCDetials(getSelectedVC(VCsList.getValue()));
                VCDetails.getChildren().clear();
                VCDetails.getChildren().addAll(labels);
                assigningButton.setText("Assign " + recipientsList.getValue() + " To " + VCsList.getValue());
                if (recipientsList.getValue() != null)
                    assigningButton.setDisable(false);
            });

            Button backButton = GUIBuilder.CreateButton("BACK");
            backButton.getStyleClass().add("red_button");
            backButton.setOnAction(e -> launchPage(window));
            HBox pageBottom = new HBox(10);
            pageBottom.setPadding(new Insets(10));
            pageBottom.setAlignment(Pos.CENTER);
            pageBottom.getChildren().addAll(backButton);
            
            GridPane recipientsDistribution = new GridPane();
            recipientsDistribution.setPadding(new Insets(40));
            recipientsDistribution.setVgap(10);
            recipientsDistribution.setHgap(50);
            recipientsDistribution.setAlignment(Pos.CENTER);
            recipientsDistribution.getChildren().addAll(recipientsList, recipientDetails, VCsList, VCDetails, assigningButton);

            BorderPane recipientsDistributionPage = new BorderPane();
            recipientsDistributionPage.setTop(pageTitle);
            recipientsDistributionPage.setBottom(pageBottom);
            recipientsDistributionPage.setCenter(recipientsDistribution);

            Scene scene = new Scene(recipientsDistributionPage, 800, 600);
            scene.getStylesheets().add("Style.css");
            window.setScene(scene);
        }
        else {
            GUIBuilder.CreateAlert(AlertType.WARNING, "WARNING", "All Registered Recipeints Are Assigned To Vaccine Centers", null);        }
    }

    /**
     * The method that will allow ministry of health to distribute vaccines over 
     * vaccine centers where each vaccine will have a unique batch number
     * 
     * @param window    The window on which the page should be displayed
     */
    public void vaccineDistribution(Stage window) {
        Button assigningButton = GUIBuilder.CreateButton("Assign n Doses To VC");
        assigningButton.getStyleClass().add("green_button");
        assigningButton.setDisable(true);
        GridPane.setConstraints(assigningButton, 0, 4);

        ComboBox<String> VCsList = new ComboBox<>();
        GridPane.setConstraints(VCsList, 0, 2);
        for (VaccineCenter vaccineCenter: vaccineCenters)
            VCsList.getItems().add(vaccineCenter.getID());

        Label numberOfDosesLabel = GUIBuilder.CreateLabel("Number of doses");
        GridPane.setConstraints(numberOfDosesLabel, 0, 0);

        Spinner<Integer> numberOfDosesInput = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(5, Integer.MAX_VALUE, 5);
        numberOfDosesInput.setValueFactory(valueFactory);
        numberOfDosesInput.valueProperty().addListener((v, oldValue, newValue) -> {
            if (VCsList.getValue() == null)
                assigningButton.setText("Assign " + newValue + " Doses To VC");
            else
                assigningButton.setText("Assign " + newValue + " Doses To " + VCsList.getValue());
        });
        GridPane.setConstraints(numberOfDosesInput, 0, 1);

        assigningButton.setOnAction(e -> {
            VaccineCenter vaccineCenter = getSelectedVC(VCsList.getValue());
            for (int i = 0; i < numberOfDosesInput.getValue(); i++)
                vaccines.add(new Vaccine(currentVaccineBatch++, vaccineCenter.getID()));
            
            vaccineCenter.setVaccineDosesAvailable(vaccineCenter.getVaccineDosesAvailable() + numberOfDosesInput.getValue());
            UpdateVCData(vaccineCenter);
            storeLastBatchNumberToFile("LastBatchNumber.csv");
            storeVaccinesToFile("Vaccines.csv");
        });

        Label vaccineDistributionPageTitle = GUIBuilder.CreateLabel("Vaccine Distribution");
        vaccineDistributionPageTitle.getStyleClass().add("title");
        HBox pageTitle = new HBox();
        pageTitle.setPadding(new Insets(10));
        pageTitle.setAlignment(Pos.CENTER);
        pageTitle.getChildren().add(vaccineDistributionPageTitle);

        VBox VCDetails = new VBox(8);
        VCDetails.setPadding(new Insets(20));
        GridPane.setConstraints(VCDetails, 0, 3);
        VCsList.setPromptText("Select VC ID");
        VCsList.setOnAction(e -> {
            LinkedList<Label> labels = getVCDetials(getSelectedVC(VCsList.getValue()));
            VCDetails.getChildren().clear();
            VCDetails.getChildren().addAll(labels);
            assigningButton.setText("Assign " + numberOfDosesInput.getValue() + " Doses To " + VCsList.getValue());
            assigningButton.setDisable(false);
        });

        Button backButton = GUIBuilder.CreateButton("BACK");
        backButton.getStyleClass().add("red_button");
        backButton.setOnAction(e -> launchPage(window));
        HBox pageBottom = new HBox(10);
        pageBottom.setPadding(new Insets(10));
        pageBottom.setAlignment(Pos.CENTER);
        pageBottom.getChildren().addAll(backButton);
        
        GridPane vaccineDistribution = new GridPane();
        vaccineDistribution.setPadding(new Insets(40));
        vaccineDistribution.setVgap(10);
        vaccineDistribution.setHgap(50);
        vaccineDistribution.setAlignment(Pos.CENTER);
        vaccineDistribution.getChildren().addAll(numberOfDosesLabel, numberOfDosesInput, VCsList, VCDetails, assigningButton);

        BorderPane vaccineDistributionPage = new BorderPane();
        vaccineDistributionPage.setTop(pageTitle);
        vaccineDistributionPage.setBottom(pageBottom);
        vaccineDistributionPage.setCenter(vaccineDistribution);

        Scene scene = new Scene(vaccineDistributionPage, 800, 600);
        scene.getStylesheets().add("Style.css");
        window.setScene(scene);
    }

    /**
     * The method that will display the information related to vaccination
     * statistics from all vaccine centers in a table format
     * 
     * @param window    The window on which the table page should be displayed
     */
    public void statisticsTable(Stage window) {
        countAppointmentsPerDate();
        TableColumn<Appointment, String> dateColumn = GUIBuilder.CreateTableColumn("Date", "date", 50);
        TableColumn<Appointment, String> appPerdateColumn = GUIBuilder.CreateTableColumn("Number of Doses", "appointmentsPerDate", 100);
        
        TableView<Appointment> appointmentsTable = GUIBuilder.CreateTableView(500);
        appointmentsTable.setItems(getAppointmentsList());
        appointmentsTable.getColumns().addAll(dateColumn, appPerdateColumn);
        
        long totalVCDoses = 0;
        for (VaccineCenter vaccineCenter: vaccineCenters)
            totalVCDoses += vaccineCenter.getTotalVCDoses();

        Label totalVCDoseseFromAllVCs = GUIBuilder.CreateLabel("Total Doses From All Vaccine Centers: " + totalVCDoses);
        totalVCDoseseFromAllVCs.getStyleClass().add("statistics_label");
        VBox statistics = new VBox(5);
        statistics.setPadding(new Insets(5));
        statistics.setAlignment(Pos.CENTER);
        statistics.getChildren().addAll(appointmentsTable, totalVCDoseseFromAllVCs);

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
     * @return  Observable list consisting of all appointments
     */
    public ObservableList<Appointment> getAppointmentsList() {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList(appointments);
        return appointmentsList;
    }

    /**
     * Gets the recipeints and convert them to observable list
     * to be used in the table
     * 
     * @return  Observable list consisting of all recipients
     */
    public ObservableList<Recipient> getRecipientsList() {
        ObservableList<Recipient> recipientsList = FXCollections.observableArrayList(recipients);
        return recipientsList;
    }

    /**
     * Searches for a recipient using his ID
     * 
     * @param recipientID   The target recipient ID
     * @return  Recipient if ID equals any of the IDs in recipient list or null if not found
     */
    public Recipient getSelectedRecipient(String recipientID) {
        for (Recipient recipient: recipients) {
            if (recipient.getID().equals(recipientID))
                return recipient;
        }
        return null;
    }

    /**
     * Prepares target recipient details to be displayed on screen
     * 
     * @param recipient Target recipient to fetch his details
     * @return  list of labels where each label holds one detail about the recipient
     */
    public LinkedList<Label> getRecipientDetials(Recipient recipient) {
        LinkedList<Label> labels = new LinkedList<>();
        
        Label id = GUIBuilder.CreateLabel("Recipient ID: " + recipient.getID());
        labels.add(id);    
        Label name = GUIBuilder.CreateLabel("Recipient Name: " + recipient.getName());
        labels.add(name);
        Label phone = GUIBuilder.CreateLabel("Recipient Phone: " + recipient.getPhone());
        labels.add(phone);
        Label age = GUIBuilder.CreateLabel("Recipient Age: " + recipient.getAge() + " years");
        labels.add(age);
        Label dose = GUIBuilder.CreateLabel("Doses Recieved: " + recipient.getDose());
        labels.add(dose);
        Label status;
        if (recipient.getStatus().equals("null"))
            status = GUIBuilder.CreateLabel("Recipient Status: Pending");
        else 
            status = GUIBuilder.CreateLabel("Recipient Status: " + recipient.getStatus());
        labels.add(status);
        
        for (Label label: labels)
            label.getStyleClass().add("details_label");

        return labels;
    }

    /**
     * Searches for a vaccine center using its ID
     * 
     * @param VCID   The target vaccine center ID
     * @return  Vaccine center if ID equals any of the IDs in vaccine centers list or null if not found
     */
    public VaccineCenter getSelectedVC(String VCID) {
        for (VaccineCenter vaccineCenter: vaccineCenters) {
            if (vaccineCenter.getID().equals(VCID))
                return vaccineCenter;
        }
        return null;
    }

    /**
     * Prepares target vaccine center details to be displayed on screen
     * 
     * @param vaccineCenter Target vaccine center to fetch his details
     * @return  list of labels where each label holds one detail about the vaccine center
     */
    public LinkedList<Label> getVCDetials(VaccineCenter vaccineCenter) {
        LinkedList<Label> labels = new LinkedList<>();
        
        Label id = GUIBuilder.CreateLabel("Vaccine Center ID: " + vaccineCenter.getID());
        labels.add(id);    
        Label name = GUIBuilder.CreateLabel("Vaccine Center Name: " + vaccineCenter.getName());
        labels.add(name);
        Label dosesAvailable = GUIBuilder.CreateLabel("Vaccine Doses Available: " + vaccineCenter.getVaccineDosesAvailable());
        labels.add(dosesAvailable);
        Label capacity = GUIBuilder.CreateLabel("Vaccine Center Capacity: " + vaccineCenter.getCapacityPerDay() + " per day");
        labels.add(capacity);
        
        for (Label label: labels)
            label.getStyleClass().add("details_label");

        return labels;
    }

    /**
     * Count the number of doses given by all vaccine centers
     * on each day
     */
    public void countAppointmentsPerDate() {
        ArrayList<String> dates = new ArrayList<>();
        LinkedHashMap<String, Integer> appointmentsMap = new LinkedHashMap<>();
        appointments = new ArrayList<>();
        
        for (Recipient recipient: recipients) {
            if (recipient.getStatus().equals("1st Dose Completed"))
                dates.add(recipient.getAppointment1());

            else if (recipient.getStatus().equals("2nd Dose Completed")) {
                dates.add(recipient.getAppointment1());
                dates.add(recipient.getAppointment2());
            }
        }
        
        for (String date: dates) {
            if (appointmentsMap.get(date) == null)
                appointmentsMap.put(date, 1);
            else {
                int value = appointmentsMap.get(date);
                appointmentsMap.put(date, value+1);
            }
        }

        for (var entry: appointmentsMap.entrySet()) {
            appointments.add(new Appointment(entry.getKey(), entry.getValue()));
        }
    }

    /**
     * Creates a file with the given name if it does not exist
     * 
     * @param fileName  the file name required
     */
    public void createFile(String fileName) {
        try {
            File targetFile = new File(fileName);
            if (targetFile.createNewFile())
                throw new IOException();
        } catch (IOException e) {}
    }

    /**
     * Reads recipient date from the file containing recipeints details
     * 
     * @param fileName  Target file where recipients details are stored
     * @return  List of recipients read from the file
     */
    public ArrayList<Recipient> readRecipientsFromFile(String fileName) {
        ArrayList<Recipient> fileData = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            for (int i = 0; i < lines.size(); i++) {
                String[] line = lines.get(i).split(",");
                int age = Integer.parseInt(line[3]);
                int dose = Integer.parseInt(line[4]);
                fileData.add(new Recipient(line[0], line[1], line[2], age, dose, line[5], line[6], line[7], line[8], line[9], line[10]));
            }
            return fileData;
        } catch (IOException e) {
            createFile(fileName);
            return readRecipientsFromFile(fileName);
        }
    }

    /**
     * Updates the target recipient details by removing recipient with old data
     * and replacing him with the new recipient with update data
     * 
     * @param recipient The new recipient with updated data
     */
    public void UpdateRecipientData(Recipient recipient) {
        for (int i = 0; i < recipients.size(); i++) {
            if (recipients.get(i).getID().equals(recipient.getID())) {
                recipients.set(i, recipient);
                break;
            }
        }
        storeRecipientsDataToFile("Recipients.csv");
    }

    /**
     * Stores back the recipients and their details to the terget
     * file to keep the data in file up to date
     * 
     * @param fileName  the target file where recipients details should be stored
     */
    public void storeRecipientsDataToFile(String fileName) {
        StringBuilder recipBuilder = new StringBuilder();

        for (Recipient recipient : recipients)
            recipBuilder.append(recipient.toCSVString() + "\n");
        try {
            Files.write(Paths.get(fileName), recipBuilder.toString().getBytes());
        } catch (IOException e) {
            createFile(fileName);
        }
    }

    /**
     * Reads vaccine center date from the file containing vaccine centers details
     * 
     * @param fileName  Target file where vaccine center details are stored
     * @return  List of vaccine center read from the file
     */
    public ArrayList<VaccineCenter> readVCsFromFile(String fileName) {
        ArrayList<VaccineCenter> fileData = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            for (int i = 0; i < lines.size(); i++) {
                String[] line = lines.get(i).split(",");
                int capacityPerDay = Integer.parseInt(line[2]);
                int vaccineDosesAvailable = Integer.parseInt(line[3]);
                long totalVCDoses = Long.parseLong(line[4]);
                fileData.add(new VaccineCenter(line[0], line[1], capacityPerDay, vaccineDosesAvailable, totalVCDoses, recipients));
            }
            return fileData;
        } catch (IOException e) {
            createFile(fileName);
            return readVCsFromFile(fileName);
        }
    }

    /**
     * Updates the target vaccine center details by removing vaccine center with old data
     * and replacing him with the new vaccine center with update data
     * 
     * @param vaccineCenter The new vaccine center with updated data
     */
    public void UpdateVCData(VaccineCenter vaccineCenter) {
        for (int i = 0; i < vaccineCenters.size(); i++) {
            if (vaccineCenters.get(i).getID().equals(vaccineCenter.getID())) {
                vaccineCenters.set(i, vaccineCenter);
                break;
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

        for (VaccineCenter vaccineCenter : vaccineCenters)
            VCBuilder.append(vaccineCenter.toCSVString() + "\n");
        try {
            Files.write(Paths.get(fileName), VCBuilder.toString().getBytes());
        } catch (IOException e) {
            createFile(fileName);
        }
    }

    /**
     * Reads the last batch number from the file containing the batch number
     * of the dose laslty distributed by ministry of health
     * 
     * @param fileName  Target file where batch number is stored
     * @return  Batch number of the dose laslty distributed by ministry of health
     */
    public int readLastBatchNumberFromFile(String fileName) {
        int fileData;
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            if (!lines.isEmpty()) {
                fileData = Integer.parseInt(lines.get(0));
            }
            else
                fileData = 1;
            return fileData;
        } catch (IOException e) {
            createFile(fileName);
            return readLastBatchNumberFromFile(fileName);
        }
    }

    /**
     * Stores back the batch number of the last dose distributed
     * in the file to keep the data in file up to date
     * 
     * @param fileName  the target file where batch number should be stored
     */
    public void storeLastBatchNumberToFile(String fileName) {
        StringBuilder batchNumberBuilder = new StringBuilder();
        batchNumberBuilder.append(currentVaccineBatch);

        try {
            Files.write(Paths.get(fileName), batchNumberBuilder.toString().getBytes());
        } catch (IOException e) {
            createFile(fileName);
        }
    }

    /**
     * Reads the vaccines from the file containing vaccine doses details
     * 
     * @param fileName  Target file where vaccines details are stored
     * @return  List of the vaccines stored in the file
     */
    public ArrayList<Vaccine> readVaccinesFromFile(String fileName) {
        ArrayList<Vaccine> fileData = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            for (int i = 0; i < lines.size(); i++) {
                String[] line = lines.get(i).split(",");
                int batchNumber = Integer.parseInt(line[0]);
                fileData.add(new Vaccine(batchNumber, line[1]));
            }
            return fileData;
        } catch (IOException e) {
            createFile(fileName);
            return readVaccinesFromFile(fileName);
        }
    }

    /**
     * Stores back the vaccines after vaccine distribution or recipient vaccination
     * in the file to keep the data in file up to date
     * 
     * @param fileName  The target file where vaccines details should be stored
     */
    public void storeVaccinesToFile(String fileName) {
        StringBuilder vaccinesBuilder = new StringBuilder();

        for (Vaccine vaccine: vaccines)
            vaccinesBuilder.append(vaccine.toCSVString() + "\n");
            
        try {
            Files.write(Paths.get(fileName), vaccinesBuilder.toString().getBytes());
        } catch (IOException e) {
            createFile(fileName);
        }
    }
}