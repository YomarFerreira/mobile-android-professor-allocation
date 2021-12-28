package br.com.professorallocation.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.professorallocation.databinding.ItemLayoutDepartamentoBinding;
import br.com.professorallocation.model.Departamento;

public class DepartamentoAdapter extends RecyclerView.Adapter<DepartamentoHolder> {

    private List<Departamento> departamentosList = new ArrayList<>();
    private ActionDepartamentoClick actionDepartamentoClick;

    public void setActionDepartamentoClick(ActionDepartamentoClick actionDepartamentoClick) {
        this.actionDepartamentoClick = actionDepartamentoClick;
    }

    public void setDepartamentosList(List<Departamento> departamentosList){
        this.departamentosList.clear();
        this.departamentosList.addAll(departamentosList);
        notifyDataSetChanged();
    }

    public void RemoverDepartamentoDaTela(Departamento departamento){
        departamentosList.remove(departamento);
       notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DepartamentoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLayoutDepartamentoBinding binding = ItemLayoutDepartamentoBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        DepartamentoHolder holder = new DepartamentoHolder(binding);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DepartamentoHolder holder, int position) {
        Departamento departamento = departamentosList.get(position);
        holder.bind(departamento,new ActionDepartamentoClick() {
             @Override
            public void removeDepartamentoClick(Departamento departamentoC) {
                actionDepartamentoClick.removeDepartamentoClick(departamentoC);
            }

        });

    }

    @Override
    public int getItemCount() {
        return departamentosList.size();
    }
}
