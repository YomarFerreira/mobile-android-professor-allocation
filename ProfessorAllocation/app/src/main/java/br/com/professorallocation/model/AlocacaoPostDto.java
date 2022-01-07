package br.com.professorallocation.model;

import java.sql.Time;

//DTO - data transfer object
public class AlocacaoPostDto {

    private String day;
    private String startHour;
    private String endHour;
    private int courseId;
    private int professorId;


    public String getDay() { return day; }

    public void setDay(String day) { this.day = day; }

    public String getStartHour() { return startHour; }

    public void setStartHour(String startHour) { this.startHour = startHour; }

    public String getEndHour() { return endHour; }

    public void setEndHour(String endHour) { this.endHour = endHour; }

    public int getCourseId() { return courseId; }

    public void setCourseId(int courseId) { this.courseId = courseId; }

    public int getProfessorId() { return professorId; }

    public void setProfessorId(int professorId) { this.professorId = professorId; }

}
