package nl.inholland.universitymanager2.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Student extends Person {
    private Group group;
    private ObservableList<Grade> grades = FXCollections.observableArrayList();

    public Student(String firstName, String lastName, String email, Group group) {
        super(firstName, lastName, email);
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public ObservableList<Grade> getGrades() {
        return grades;
    }
}