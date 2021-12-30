package br.com.professorallocation.view;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.professorallocation.R;
import br.com.professorallocation.databinding.ItemLayoutDepartamentoWhiteBinding;
import br.com.professorallocation.model.Departamento;

public class ListDeptHolder extends RecyclerView.ViewHolder {

    private ItemLayoutDepartamentoWhiteBinding viewBind;

    public final static String KEY_DEPARTMENT = "KEY_DEPARTMENT";

    public ListDeptHolder(@NonNull ItemLayoutDepartamentoWhiteBinding itemView) {
        super(itemView.getRoot());
        viewBind = itemView;

        TextView tvIdValue;
        TextView tvItemValue;

    }

    public void bind(Departamento departamento){
        viewBind.tvIdValue.setText(String.valueOf(departamento.getId()));
        viewBind.tvItemValue.setText(departamento.getName());

    }

}
