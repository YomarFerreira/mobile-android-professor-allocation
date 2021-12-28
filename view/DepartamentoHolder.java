package br.com.professorallocation.view;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.professorallocation.databinding.ItemLayoutDepartamentoBinding;
import br.com.professorallocation.model.Departamento;

public class DepartamentoHolder extends RecyclerView.ViewHolder {

    private ItemLayoutDepartamentoBinding viewBind;

    public final static String KEY_DEPARTMENT = "KEY_DEPARTMENT";

    public DepartamentoHolder(@NonNull ItemLayoutDepartamentoBinding itemView) {
        super(itemView.getRoot());
        viewBind = itemView;
    }

    public void bind(Departamento departamento, ActionDepartamentoClick itemClick){
        viewBind.tvIdValue.setText(String.valueOf(departamento.getId()));
        viewBind.tvItemValue.setText(departamento.getName());

        viewBind.clContainerItem.setOnLongClickListener(view ->{
            itemClick.removeDepartamentoClick(departamento);
            return true;
        });

        viewBind.clContainerItem.setOnClickListener(view -> {
            Intent intent = new Intent(viewBind.getRoot().getContext(), DetalhesDepartamentoActivity.class);
            intent.putExtra(KEY_DEPARTMENT, departamento.getId());
            viewBind.getRoot().getContext().startActivity(intent);


        });
    }

}
