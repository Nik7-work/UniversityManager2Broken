package nl.inholland.universitymanager2.model;

public class Grade {
    private String course;
    private double grade;

    public Grade(String course, double grade) {
        this.course = course;
        setGrade(grade);
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        if (grade < 1 || grade > 10) {
            throw new InvalidGradeException("Grade must be between 1 and 10 (got " + grade + ")");
        }
        this.grade = grade;
    }
}