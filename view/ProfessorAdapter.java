package br.com.professorallocation.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.professorallocation.databinding.ItemLayoutProfessorBinding;
import br.com.professorallocation.model.Professor;

public class ProfessorAdapter extends RecyclerView.Adapter<ProfessorHolder> {

    private List<Professor> professorsList = new ArrayList<>();
    private ActionProfessorClick actionProfessorClick;

    public void setActionProfessorClick(ActionProfessorClick actionProfessorClick) {
        this.actionProfessorClick = actionProfessorClick;
    }

    public void setProfessorsList(List<Professor> professorsList){
        this.professorsList.clear();
        this.professorsList.addAll(professorsList);
        notifyDataSetChanged();
    }

    public void RemoverProfessorDaTela(Professor professor){
       professorsList.remove(professor);
       notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProfessorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLayoutProfessorBinding binding = ItemLayoutProfessorBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        ProfessorHolder holder = new ProfessorHolder(binding);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfessorHolder holder, int position) {
        Professor professor = professorsList.get(position);
        holder.bind(professor,new ActionProfessorClick() {
            @Override
            public void removeProfessorClick(Professor professorC) {
                actionProfessorClick.removeProfessorClick(professorC);
            }

        });

    }

    @Override
    public int getItemCount() {
        return professorsList.size();
    }
}
