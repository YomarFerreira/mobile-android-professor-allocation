package br.com.professorallocation.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.professorallocation.databinding.ItemLayoutCursoBinding;
import br.com.professorallocation.model.Curso;
import br.com.professorallocation.model.Departamento;

public class CursoAdapter extends RecyclerView.Adapter<CursoHolder> {

    private List<Curso> cursosList = new ArrayList<>();
    private ActionCursoClick actionCursoClick;

    public void setActionCursoClick(ActionCursoClick actionCursoClick) {
        this.actionCursoClick = actionCursoClick;
    }

    public void setCursosList(List<Curso> cursosList){
        this.cursosList.clear();
        this.cursosList.addAll(cursosList);
        notifyDataSetChanged();
    }

    public void RemoverCursoDaTela(Curso curso){
       cursosList.remove(curso);
       notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CursoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLayoutCursoBinding binding = ItemLayoutCursoBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        CursoHolder holder = new CursoHolder(binding);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CursoHolder holder, int position) {
        Curso curso = cursosList.get(position);
        holder.bind(curso,new ActionCursoClick() {
            @Override
            public void removeCursoClick(Curso cursoC) {
                actionCursoClick.removeCursoClick(cursoC);
            }

        });

    }

    @Override
    public int getItemCount() {
        return cursosList.size();
    }
}
