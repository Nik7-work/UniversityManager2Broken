package nl.inholland.universitymanager2.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import nl.inholland.universitymanager2.model.Grade;
import nl.inholland.universitymanager2.model.InvalidGradeException;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class GradeDialogController implements Initializable {

    @FXML
    TextField courseField;

    @FXML
    TextField gradeField;

    private Grade grade;

    public Grade getGrade() {
        return grade;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 2.4 – only allow digits and one optional decimal point while typing
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                return change;
            }
            return null;
        };
        gradeField.setTextFormatter(new TextFormatter<>(filter));
    }

    public void onCancelClick(ActionEvent event) {
        grade = null;
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void onAddClick(ActionEvent event) {
        try {
            String course = courseField.getText().trim();
            if (course.isEmpty()) {
                showError("Course name cannot be empty.");
                return;
            }
            double value = Double.parseDouble(gradeField.getText().trim());
            grade = new Grade(course, value); // may throw InvalidGradeException
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            showError("Please enter a valid number for the grade.");
        } catch (InvalidGradeException e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid input");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}