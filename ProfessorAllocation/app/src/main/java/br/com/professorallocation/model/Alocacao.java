package br.com.professorallocation.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class Alocacao implements Serializable {

    private int id;
    private String day;
    @JsonFormat(pattern = "HH:mmZ", shape = JsonFormat.Shape.STRING)
    private Date startHour;
    @JsonFormat(pattern = "HH:mmZ", shape = JsonFormat.Shape.STRING)
    private Date endHour;

    private int courseId;
    private int professorId;
    private Curso course;
    private Professor professor;

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getDay() {return day;}

    public void setDay(String day) {this.day = day;}

    public Date getStartHour() { return startHour; }

    public void setStartHour(Date startHour) { this.startHour = startHour; }

    public Date getEndHour() { return endHour; }

    public void setEndHour(Date endHour) { this.endHour = endHour; }

    public int getCourseId() { return courseId; }

    public void setCourseId(int courseId) { this.courseId = courseId; }

    public int getProfessorId() { return professorId; }

    public void setProfessorId(int professorId) { this.professorId = professorId; }

    public Curso getCourse() { return course; }

    public void setCourse(Curso course) { this.course = course; }

    public Professor getProfessor() { return professor; }

    public void setProfessor(Professor professor) { this.professor = professor; }



}

