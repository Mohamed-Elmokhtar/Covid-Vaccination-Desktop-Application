import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MohLaunch extends Application {
    Stage window;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Ministry Of Health");
        window.setResizable(false);
        window.setOnCloseRequest(e -> {
            e.consume();
            quit();
        });

        MinistryOfHealth mohOnSession = new MinistryOfHealth();
        ArrayList<Recipient> recipients = mohOnSession.readRecipientsFromFile("Recipients.csv");
        mohOnSession.setRecipients(recipients);
        ArrayList<VaccineCenter> vaccineCenters = mohOnSession.readVCsFromFile("VaccineCenters.csv");
        mohOnSession.setVaccineCenters(vaccineCenters);
        int vaccineBatch = mohOnSession.readLastBatchNumberFromFile("LastBatchNumber.csv");
        mohOnSession.setCurrentVaccineBatch(vaccineBatch);
        ArrayList<Vaccine> vaccines = mohOnSession.readVaccinesFromFile("Vaccines.csv");
        mohOnSession.setVaccines(vaccines);

        mohOnSession.launchPage(window);
        window.show();
    }

    public void quit() {
        boolean user_reply = GUIBuilder.CreateConfirmationAlert(AlertType.CONFIRMATION, "Quit", null,
                "Are you sure you want to close the " + "application\nany unsaved data will be lost");
        if (user_reply)
            window.close();
    }
}