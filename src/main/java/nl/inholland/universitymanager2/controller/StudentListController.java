package nl.inholland.universitymanager2.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nl.inholland.universitymanager2.Application;
import nl.inholland.universitymanager2.data.Database;
import nl.inholland.universitymanager2.model.Grade;
import nl.inholland.universitymanager2.model.Student;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentListController implements Initializable {

    @FXML
    TableView<Student> studentTableView;
    @FXML
    TableView<Grade> gradeTableView;
    @FXML
    TableColumn<Student, String> coachColumn;

    private Database database;
    private ObservableList<Student> students;
    private Student selectedStudent;

    public StudentListController(Database database) {
        this.database = database;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        students = FXCollections.observableArrayList(database.getStudents());
        studentTableView.setItems(students);

        coachColumn.setCellValueFactory(cellData -> {
            Student student = cellData.getValue();
            if (student != null && student.getGroup() != null && student.getGroup().getCoach() != null) {
                return new SimpleStringProperty(student.getGroup().getCoach().toString());
            }
            return new SimpleStringProperty("");
        });

        studentTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    selectedStudent = newValue;
                    if (newValue != null) {
                        gradeTableView.setItems(newValue.getGrades());
                    } else {
                        gradeTableView.setItems(FXCollections.observableArrayList());
                    }
                });
    }

    public void onAddStudentClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("student-dialog-view.fxml"));
            StudentDialogController studentDialogController = new StudentDialogController(database);
            fxmlLoader.setController(studentDialogController);
            Scene scene = new Scene(fxmlLoader.load());

            Stage dialog = new Stage();
            dialog.setScene(scene);
            dialog.setTitle("Add student");
            dialog.initModality(Modality.APPLICATION_MODAL); // 1.5
            dialog.showAndWait();

            if (studentDialogController.getStudent() != null) {
                students.add(studentDialogController.getStudent());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onAddGradeClick(ActionEvent event) {
        if (selectedStudent == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No student selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a student first.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("grade-dialog-view.fxml"));
            GradeDialogController gradeDialogController = new GradeDialogController();
            fxmlLoader.setController(gradeDialogController);
            Scene scene = new Scene(fxmlLoader.load());

            Stage dialog = new Stage();
            dialog.setScene(scene);
            dialog.setTitle("Add grade");
            dialog.initModality(Modality.APPLICATION_MODAL); // 1.5
            dialog.showAndWait();

            Grade newGrade = gradeDialogController.getGrade();
            if (newGrade != null) {
                selectedStudent.getGrades().add(newGrade);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}