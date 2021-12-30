package br.com.professorallocation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Professor implements Serializable {

    private int id;
    private String name;
    private String cpf;
    private int departmentId;
    private List<Departamento> departments;
    private List<Alocacao> allocations;

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getCpf() {return cpf;}

    public void setCpf(String cpf) {this.cpf = cpf;}

    public int getDepartmentId() { return departmentId; }

    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }

    public List<Departamento> getDepartments() { return departments; }

    public void setDepartments(List<Departamento> departments) { this.departments = departments; }

    public List<Alocacao> getAllocations() { return allocations; }

    public void setAllocations(List<Alocacao> allocations) { this.allocations = allocations; }


}
