import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class VCLaunch extends Application {
    Stage window;
    static ArrayList<Recipient> recipients = readRecipientsFromFile("Recipients.csv");
    static ArrayList<VaccineCenter> vaccineCenters = readVCsFromFile("VaccineCenters.csv");
    static ArrayList<Vaccine> vaccines = readVCVaccinesFromFile("Vaccines.csv");
    VaccineCenter vaccineCenterOnSession;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Vaccine Center");
        window.setResizable(false);
        window.setOnCloseRequest(e -> {
            e.consume();
            quit();
        });
        sessionVCSelection();
        window.show();
    }

    public void sessionVCSelection() {
        Label pageTitle = GUIBuilder.CreateLabel("Vaccine Centers");
        pageTitle.getStyleClass().add("title");
        ComboBox<String> VCsList = new ComboBox<>();
        VCsList.setPromptText("Select Vaccine Center");
        for (VaccineCenter vaccineCenter: vaccineCenters) {
            VCsList.getItems().addAll(vaccineCenter.getID() + "\t" + vaccineCenter.getName());
        }

        VCsList.setOnAction(e -> {
            String VCID = VCsList.getValue().split("\t")[0];
            vaccineCenterOnSession = getSelectedVC(VCID);
            vaccineCenterOnSession.setVaccineBatches(vaccines);
            vaccineCenterOnSession.launchPage(window);
        });

        BorderPane vcSelectionPage = new BorderPane();
        vcSelectionPage.setTop(pageTitle);
        BorderPane.setAlignment(pageTitle, Pos.CENTER);
        vcSelectionPage.setCenter(VCsList);
        BorderPane.setAlignment(VCsList, Pos.TOP_CENTER);

        Scene scene = GUIBuilder.CreateScene(vcSelectionPage, 800, 600);
        scene.getStylesheets().add("Style.css");
        window.setScene(scene);
    }

    public VaccineCenter getSelectedVC(String VCID) {
        for (VaccineCenter vaccineCenter: vaccineCenters) {
            if (vaccineCenter.getID().equals(VCID))
                return vaccineCenter;
        }
        return null;
    }

    public void quit() {
        boolean user_reply = GUIBuilder.CreateConfirmationAlert(AlertType.CONFIRMATION, "Quit", null,
                "Are you sure you want to close the " + "application\nany unsaved data will be lost");
        if (user_reply)
            window.close();
    }

    public static void createFile(String fileName) {
        try {
            File targetFile = new File(fileName);
            if (targetFile.createNewFile())
                throw new IOException();
        } catch (IOException e) {}
    }

    public static ArrayList<Recipient> readRecipientsFromFile(String fileName) {
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

    public static void UpdateRecipientData(Recipient recipient) {
        for (int i = 0; i < recipients.size(); i++) {
            if (recipients.get(i).getID().equals(recipient.getID()))
                recipients.set(i, recipient);
        }
        storeRecipientsDataToFile("Recipients.csv");
    }

    public static void storeRecipientsDataToFile(String fileName) {
        StringBuilder recipBuilder = new StringBuilder();

        for (Recipient recipient : recipients)
            recipBuilder.append(recipient.toCSVString() + "\n");
        try {
            Files.write(Paths.get(fileName), recipBuilder.toString().getBytes());
        } catch (IOException e) {
            createFile(fileName);
        }
    }

    public static ArrayList<VaccineCenter> readVCsFromFile(String fileName) {
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

    public static void storeVaccinesToFile(String fileName) {
        StringBuilder vaccinesBuilder = new StringBuilder();
        
        for (Vaccine vaccine: vaccines)
            vaccinesBuilder.append(vaccine.toCSVString() + "\n");
        try {
            Files.write(Paths.get(fileName), vaccinesBuilder.toString().getBytes());
        } catch (IOException e) {
            VCLaunch.createFile(fileName);
        }
    }

    public static ArrayList<Vaccine> readVCVaccinesFromFile(String fileName) {
        ArrayList<Vaccine> fileData = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            for (int i = 0; i < lines.size(); i++) {
                String[] line = lines.get(i).split(",");
                Integer batch = Integer.parseInt(line[0]);
                fileData.add(new Vaccine(batch, line[1]));
            }
            return fileData;
        } catch (IOException e) {
            VCLaunch.createFile(fileName);
            return readVCVaccinesFromFile(fileName);
        }
    }

    public static void updateVaccinesInFile(Integer batchNumber) {
        for (Vaccine vaccine: vaccines) {
            if (vaccine.getBatchNumber() == batchNumber) {
                vaccines.remove(vaccine);
                break;
            }
        }
        storeVaccinesToFile("Vaccines.csv");
    }
}
