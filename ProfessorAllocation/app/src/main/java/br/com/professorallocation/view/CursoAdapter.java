package br.com.professorallocation.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.professorallocation.databinding.ItemLayoutCursoBinding;
import br.com.professorallocation.model.Curso;

public class CursoAdapter extends RecyclerView.Adapter<CursoHolder> {

    private List<Curso> cursosList = new ArrayList<>();
    private ActionCursoClick actionCursoClick;

    public void setActionCursoClick(ActionCursoClick actionCursoClick) {
        this.actionCursoClick = actionCursoClick;
    }

    public void setCursosList(List<Curso> cursosList){
        this.cursosList.clear();

        Collections.sort(cursosList, new Comparator<Curso>() {
            @Override
            public int compare(Curso item1, Curso item2) {
                //Alfabetico
                // return item1.getName().compareToIgnoreCase(item2.getName());

                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return item1.getId() < item2.getId() ? -1 : (item1.getId() < item2.getId()) ? 1 : 0;
            }
        });

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
