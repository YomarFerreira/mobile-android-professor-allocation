package br.com.professorallocation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(value = {"course", "id"})
public class Alocacao implements Serializable {

    private int id;
    private String dayOfWeek;
    private String startHour;
    private String endHour;
    private Professor professor;

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getDayOfWeek() {return dayOfWeek;}

    public void setDayOfWeek(String dayOfWeek) {this.dayOfWeek = dayOfWeek;}

    public String getStartHour() {return startHour;}

    public void setStartHour(String startHour) {this.startHour = startHour;}

    public String getEndHour() {return endHour;}

    public void setEndHour(String endHour) {this.endHour = endHour;}

    public Professor getProfessor() {return professor;}

    public void setProfessor(Professor professor) {this.professor = professor;}
}
