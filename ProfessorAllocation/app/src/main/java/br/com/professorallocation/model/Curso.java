package br.com.professorallocation.model;

import java.io.Serializable;
import java.util.List;

public class Curso implements Serializable {

    private int id;
    private String name;
    private List<Alocacao> allocations;

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Alocacao> getAllocations() { return allocations; }

    public void setAllocations(List<Alocacao> allocations) { this.allocations = allocations; }
}

