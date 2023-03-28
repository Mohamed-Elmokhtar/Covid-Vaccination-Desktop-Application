import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class RecipientLaunch extends Application {
    Stage window;
    ArrayList<Recipient> recipients = readRecipientsFromFile("Recipients.csv");
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Recipient");
        window.setResizable(false);
        window.setOnCloseRequest(e -> {
            e.consume();
            quit();
        });
        launchPage();
        window.show();
    }

    public void launchPage() {
        Label recipientPageTitle = GUIBuilder.CreateLabel("Recipient");
        recipientPageTitle.getStyleClass().add("title");
        HBox pageTitle = new HBox();
        pageTitle.setPadding(new Insets(10));
        pageTitle.setAlignment(Pos.CENTER);
        pageTitle.getChildren().add(recipientPageTitle);
    
        Label nameLabel = GUIBuilder.CreateLabel("Name: ");
        nameLabel.getStyleClass().add("loginregister_details");
        GridPane.setConstraints(nameLabel, 0, 0);
        TextField nameInput = GUIBuilder.CreateTextField("eg. Ali");
        nameInput.getStyleClass().add("loginregister_details");
        GridPane.setConstraints(nameInput, 1, 0);
        
        Label phoneLabel = GUIBuilder.CreateLabel("Phone Number: ");
        phoneLabel.getStyleClass().add("loginregister_details");
        GridPane.setConstraints(phoneLabel, 0, 1);
        TextField phoneInput = GUIBuilder.CreateTextField("eg. 60138714531");
        phoneInput.getStyleClass().add("loginregister_details");
        GridPane.setConstraints(phoneInput, 1, 1);

        Button registerButton = GUIBuilder.CreateButton("REGISTER");
        registerButton.getStyleClass().add("black_button");
        registerButton.setOnAction(e -> registerationPage());
        registerButton.setMinWidth(80);
        GridPane.setConstraints(registerButton, 1, 2);

        Button loginButton = GUIBuilder.CreateButton("LOGIN");
        loginButton.getStyleClass().add("blue_button");
        loginButton.setMinWidth(80);
        GridPane.setConstraints(loginButton, 1, 3);
        loginButton.setOnAction(e-> validateLoginCredentials(nameInput.getText(), phoneInput.getText()));          
                    
        GridPane recipientPageOptions = new GridPane();
        recipientPageOptions.setVgap(10);
        recipientPageOptions.setHgap(10);
        recipientPageOptions.setAlignment(Pos.CENTER);
        recipientPageOptions.getChildren().addAll(nameLabel, nameInput, phoneLabel, phoneInput, registerButton, loginButton);

        BorderPane recipientMainPage = new BorderPane();
        recipientMainPage.setTop(pageTitle);
        recipientMainPage.setCenter(recipientPageOptions);

        Scene scene = GUIBuilder.CreateScene(recipientMainPage, 800, 600);
        scene.getStylesheets().add("Style.css");
        window.setScene(scene);
    }

    public void validateLoginCredentials(String name, String phone) {
        if(validName(name) && validPhone(phone))
            login(name, phone);
        else if (!validName(name) && !validAge(phone))
            GUIBuilder.CreateAlert(AlertType.ERROR, "Invalid Login Details", "Invalid name and phone",
                        "numbers and symbols are not allowed in name\nonly digits (0-9) are allowed in phone number");
        else if (!validName(name))
            GUIBuilder.CreateAlert(AlertType.ERROR, "Invalid Login Details", "Invalid name",
                        "numbers and symbols are not allowed in name");
        else if (!validAge(phone))
            GUIBuilder.CreateAlert(AlertType.ERROR, "Invalid Login Details", "Invalid phone",
                        "only digits (0-9) are allowed in phone number");
    }

    public void registerationPage() {
        Label RegistrationPageTitle = GUIBuilder.CreateLabel("New Recipient Registration");
        RegistrationPageTitle.getStyleClass().add("sub_title");
        HBox pageTitle = new HBox();
        pageTitle.setPadding(new Insets(10));
        pageTitle.setAlignment(Pos.CENTER);
        pageTitle.getChildren().add(RegistrationPageTitle);

        Button backButton = GUIBuilder.CreateButton("BACK");
        backButton.setOnAction(e -> launchPage());
        backButton.getStyleClass().add("red_button");
        HBox pageBottom = new HBox(10);
        pageBottom.setPadding(new Insets(10));
        pageBottom.setAlignment(Pos.CENTER);
        pageBottom.getChildren().addAll(backButton);

        Label nameLabel = GUIBuilder.CreateLabel("Name: ");
        nameLabel.getStyleClass().add("loginregister_details");
        GridPane.setConstraints(nameLabel, 0, 0);
        TextField registerName = GUIBuilder.CreateTextField("eg. Ali");
        registerName.getStyleClass().add("loginregister_details");
        GridPane.setConstraints(registerName, 1, 0);
        Label phoneLabel = GUIBuilder.CreateLabel("Phone: ");
        phoneLabel.getStyleClass().add("loginregister_details");
        GridPane.setConstraints(phoneLabel, 0, 1);
        TextField registerPhone = GUIBuilder.CreateTextField("eg. 60138715423");
        registerPhone.getStyleClass().add("loginregister_details");
        GridPane.setConstraints(registerPhone, 1, 1);
        Label ageLabel = GUIBuilder.CreateLabel("Age: ");
        ageLabel.getStyleClass().add("loginregister_details");
        GridPane.setConstraints(ageLabel, 0, 2);
        TextField registerAge = GUIBuilder.CreateTextField("eg. 25");
        registerAge.getStyleClass().add("loginregister_details");
        GridPane.setConstraints(registerAge, 1, 2);
        Button registerButton = GUIBuilder.CreateButton("REGISTER");
        registerButton.getStyleClass().add("black_button");
        GridPane.setConstraints(registerButton, 1, 3);

        GridPane registerDetails = new GridPane();
        registerDetails.setHgap(10);
        registerDetails.setVgap(10);
        registerDetails.setAlignment(Pos.CENTER);
        registerDetails.getChildren().addAll(nameLabel, registerName, phoneLabel, registerPhone, ageLabel,
                                            registerAge, registerButton);

        BorderPane registrationPage = new BorderPane();
        registrationPage.setTop(pageTitle);
        registrationPage.setBottom(pageBottom);
        registrationPage.setCenter(registerDetails);

        registerButton.setOnAction(e -> validateRegisterationCredentials(registerName.getText(), registerPhone.getText(), registerAge.getText()));
        Scene scene = new Scene(registrationPage, 800, 600);
        scene.getStylesheets().add("Style.css");
        window.setScene(scene);
    }

    public void validateRegisterationCredentials(String name, String phone, String age) {
        if (validName(name) && validPhone(phone) && validAge(age)) {
            String ID;
            if (recipients.isEmpty())
                ID = "R-0";
            else
                ID = generateID(recipients.get(recipients.size()-1).getID());
            
            recipients.add(new Recipient(ID, name, phone, Integer.parseInt(age)));
            storeRecipientsDataToFile("Recipients.csv");
            GUIBuilder.CreateAlert(AlertType.INFORMATION, "Loged In Successfully", null, 
                            "you can now proceed to view your personal details");
            launchPage();
        }
        else if (!validName(name) && !validPhone(phone) && !validAge(age))  
            GUIBuilder.CreateAlert(AlertType.ERROR, "Invalid Details", "Invalid name, phone and age",
                        "numbers and symbols are not allowed in name\nonly digits (0-9) are allowed in phone number" +
                        "\nonly digits (0-9) are allowed in age");
        else if (!validName(name) && !validAge(age))                                
            GUIBuilder.CreateAlert(AlertType.ERROR, "Invalid Details", "Invalid name and age",
                        "numbers and symbols are not allowed in name\nonly digits (0-9) are allowed in age");
        else if (!validName(name) && !validPhone(phone))                             
            GUIBuilder.CreateAlert(AlertType.ERROR, "Invalid Details", "Invalid name and phone",
                        "numbers and symbols are not allowed in name\nonly digits (0-9) are allowed in phone number");
        else if (!validPhone(phone) && !validAge(age))                               
            GUIBuilder.CreateAlert(AlertType.ERROR, "Invalid Details", "Invalid phone and age",
                        "only digits (0-9) are allowed in phone number\nonly digits (0-9) are allowed in age");
        else if (!validName(name))                                                          
            GUIBuilder.CreateAlert(AlertType.ERROR, "Invalid Details", "Invalid name",
                        "numbers and symbols are not allowed in name");   
        else if (!validPhone(phone))                                                        
            GUIBuilder.CreateAlert(AlertType.ERROR, "Invalid Details", "Invalid phone",
                        "only digits (0-9) are allowed in phone number");
        else if (!validAge(age))                                                            
            GUIBuilder.CreateAlert(AlertType.ERROR, "Invalid Details", "Invalid age",
                        "only digits (0-9) are allowed in age");
    }

    public void login(String name, String phone) {
        boolean recipFound = false;
        for(Recipient recipient: recipients) {
            if ((name + "," + phone).equals(recipient.searchString())) {
                recipFound = true;
                showRecipInfo(recipient);
                break;
            }
        }
        if (!recipFound)
            GUIBuilder.CreateAlert(AlertType.ERROR, "Login Details Does Not Exist", null,
                        "Please make sure you have an account registered with given details\nif not, register a new account");
    }
    
    public void showRecipInfo(Recipient recipient) {
        Label recipInfoPageTitle = GUIBuilder.CreateLabel("Personal Details");
        recipInfoPageTitle.getStyleClass().add("sub_title");
        HBox pageTitle = new HBox();
        pageTitle.setPadding(new Insets(10));
        pageTitle.setAlignment(Pos.CENTER);
        pageTitle.getChildren().add(recipInfoPageTitle);

        Label id = GUIBuilder.CreateLabel("Recipient ID\t\t:  " + recipient.getID());  
        GridPane.setConstraints(id, 0, 0);
        id.getStyleClass().add("loginregister_details");

        Label name = GUIBuilder.CreateLabel("Recipient Name\t:  " + recipient.getName());
        GridPane.setConstraints(name, 0, 1);
        name.getStyleClass().add("loginregister_details");

        Label phone = GUIBuilder.CreateLabel("Recipient Phone\t:  " + recipient.getPhone());
        GridPane.setConstraints(phone, 0, 2);
        phone.getStyleClass().add("loginregister_details");

        Label age = GUIBuilder.CreateLabel("Recipient Age\t\t:  " + recipient.getAge() + " years");
        GridPane.setConstraints(age, 0, 3);
        age.getStyleClass().add("loginregister_details");

        Label dose = GUIBuilder.CreateLabel("Doses Recieved\t:  " + recipient.getDose());
        GridPane.setConstraints(dose, 0, 4);
        dose.getStyleClass().add("loginregister_details");

        Label status;
        if (recipient.getStatus().equals("Pending"))
            status = GUIBuilder.CreateLabel("Status\t\t\t:  Pending");
        else if (recipient.getStatus().equals("1st Dose Appointment"))
            status = GUIBuilder.CreateLabel("Status\t\t\t:  1st Dose Appointment - " + recipient.getAppointment1());
        else if (recipient.getStatus().equals("1st Dose Completed"))
            status = GUIBuilder.CreateLabel("Status\t\t\t:  1st Dose Completed - " + recipient.getAppointment1() + "\n" +
                                            "\t\t\t\t   Batch: " + recipient.getDose1Batch());
        else if (recipient.getStatus().equals("2nd Dose Appointment"))
            status = GUIBuilder.CreateLabel("Status\t\t\t:  1st Dose Completed - " + recipient.getAppointment1() + "\n" +
                                            "\t\t\t\t   Batch: " + recipient.getDose1Batch() + "\n" +
                                            "\t\t\t\t   2nd Dose Appointment - " + recipient.getAppointment2());
        else
            status = GUIBuilder.CreateLabel("Status\t\t\t:  1st Dose Completed - " + recipient.getAppointment1() + "\n" +
                                            "\t\t\t\t   Batch: " + recipient.getDose1Batch() + "\n" +
                                            "\t\t\t\t   2nd Dose Completed - " + recipient.getAppointment2() + "\n" + 
                                            "\t\t\t\t   Batch: " + recipient.getDose2Batch());
        GridPane.setConstraints(status, 0, 5);
        status.getStyleClass().add("loginregister_details");
        
        GridPane recipDetails = new GridPane();
        recipDetails.setVgap(10);
        recipDetails.setAlignment(Pos.CENTER);
        recipDetails.getChildren().addAll(id, name, phone, age, dose, status); 

        Button backButton = GUIBuilder.CreateButton("LOGOUT");
        backButton.setOnAction(e -> launchPage());
        backButton.getStyleClass().add("red_button");
        HBox pageBottom = new HBox(10);
        pageBottom.setPadding(new Insets(10));
        pageBottom.setAlignment(Pos.CENTER);
        pageBottom.getChildren().addAll(backButton);

        BorderPane recipientDetailsPage = new BorderPane();
        recipientDetailsPage.setTop(pageTitle);
        recipientDetailsPage.setCenter(recipDetails);
        recipientDetailsPage.setBottom(pageBottom);

        Scene scene = GUIBuilder.CreateScene(recipientDetailsPage, 800, 600);
        scene.getStylesheets().add("Style.css");
        window.setScene(scene);
    }

    private boolean validName(String nameInput){
        Pattern p = Pattern.compile("[a-zA-Z' ']+");
        Matcher m = p.matcher(nameInput);
        if(m.find() && m.group().equals(nameInput))
            return true;
        
        return false;
    }

    private boolean validPhone(String phoneInput){
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(phoneInput);
        if(m.find() && m.group().equals(phoneInput))
            return true;

        return false;
    }
    
    private boolean validAge(String ageInput){
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(ageInput);
        if(m.find() && m.group().equals(ageInput))
            return true;
        
        return false;
    }

    public String generateID(String currentID) {
        if (currentID == null)
            return "R-0";
        else {
            String IDNum = "";
            for (int i = 2; i < currentID.length(); i++)
                IDNum += currentID.charAt(i);

            int oldIDNum = Integer.parseInt(IDNum);
            String newID = "R-" + String.valueOf(oldIDNum+1);
            return newID;
        }
    }

    public void quit() {
        boolean user_reply = GUIBuilder.CreateConfirmationAlert(AlertType.CONFIRMATION, "Quit", null,
                "Are you sure you want to close the " + "application\nany unsaved data will be lost");
        if (user_reply)
            window.close();
    }

    public void createFile(String fileName) {
        try {
            File targetFile = new File(fileName);
            if (targetFile.createNewFile())
                throw new IOException();
        } catch (IOException e) {}
    }

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
}