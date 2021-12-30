package br.com.professorallocation.model;


//DTO - data transfer object
public class ProfessorPostDto {

    private String name;
    private String cpf;
    private int departmentId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() { return cpf; }

    public void setCpf(String cpf) { this.cpf = cpf; }

    public int getDepartmentId() { return departmentId; }

    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }
}
