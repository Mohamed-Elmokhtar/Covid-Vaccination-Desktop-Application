import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class GUIBuilder {

    public static Stage CreateStage(String title) {
        Stage stage = new Stage();
        stage.setTitle(title);

        return stage;
    }

    public static Button CreateButton(String text) {
        Button button = new Button();
        button.setText(text);

        return button;
    }

    public static Label CreateLabel(String text) {
        Label label = new Label();
        label.setText(text);

        return label;
    }

    public static TextField CreateTextField(String text) {
        TextField textField = new TextField();
        textField.setPromptText(text);

        return textField;
    }

    public static <E, T> TableColumn<E, T> CreateTableColumn(String columnTitle, String dataName, int width) {
        TableColumn<E, T> column = new TableColumn<>(columnTitle);
        column.setMinWidth(width);
        column.setCellValueFactory(new PropertyValueFactory<>(dataName));

        return column;
    }

    public static <E> TableView<E> CreateTableView(int maxHeight) {
        TableView<E> table = new TableView<>();
        table.setMaxHeight(maxHeight);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }

    public static Scene CreateScene(Pane root, int width, int height) {
        Scene scene = new Scene(root, width, height);

        return scene;
    }

    public static DatePicker CreateDatePicker(String format) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText(format.toLowerCase());
        datePicker.setConverter( new StringConverter<LocalDate>() {
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty())
                    return LocalDate.parse(string, dateFormatter);
                else
                    return null;
            }

            @Override
            public String toString(LocalDate date) {
                if (date != null)
                    return dateFormatter.format(date);
                else
                    return "";
            }
        });

        return datePicker;
    }

    public static void CreateAlert(AlertType type, String title, String header, String message) {
        Alert alert = new Alert(type);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean CreateConfirmationAlert(AlertType type, String title, String header, String message) {
        Alert alert = new Alert(type);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        boolean answer = true;
        Optional<ButtonType> choice = alert.showAndWait();
        if (choice.get() == ButtonType.CANCEL)
            answer = false;

        return answer;
    }
}