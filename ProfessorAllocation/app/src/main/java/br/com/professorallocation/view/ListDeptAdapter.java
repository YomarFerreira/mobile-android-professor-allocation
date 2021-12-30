package br.com.professorallocation.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.professorallocation.databinding.ItemLayoutDepartamentoWhiteBinding;
import br.com.professorallocation.model.Departamento;

public class ListDeptAdapter extends RecyclerView.Adapter<ListDeptHolder> {

    private List<Departamento> departamentosList = new ArrayList<>();
    private ActionListDeptClick actionListDeptClick;

    public void setActionListDeptClick(ActionListDeptClick actionListDeptClick) {
        this.actionListDeptClick = actionListDeptClick;
    }


    public void setDeptList(@NonNull List<Departamento> departamentosList){
        this.departamentosList.clear();
        this.departamentosList.addAll(departamentosList);

    }

    @NonNull
    @Override
    public ListDeptHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLayoutDepartamentoWhiteBinding binding = ItemLayoutDepartamentoWhiteBinding.inflate(
             LayoutInflater.from(parent.getContext()),
             parent,
            false
        );

        ListDeptHolder holder = new ListDeptHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListDeptHolder holder, int position) {
        Departamento departamento = departamentosList.get(position);
        holder.bind(departamento);
    };

    @Override
    public int getItemCount() {
        return departamentosList.size();
    }






}
