package br.com.professorallocation.view;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.professorallocation.databinding.ItemLayoutCursoBinding;
import br.com.professorallocation.model.Curso;

public class CursoHolder extends RecyclerView.ViewHolder {

    private ItemLayoutCursoBinding viewBind;

    public final static String KEY_COURSE = "KEY_COURSE";

    public CursoHolder(@NonNull ItemLayoutCursoBinding itemView) {
        super(itemView.getRoot());
        viewBind = itemView;
    }

    public void bind(Curso curso, ActionCursoClick itemClick){
        viewBind.tvIdValue.setText(String.valueOf(curso.getId()));
        viewBind.tvItemValue.setText(curso.getName());

        viewBind.clContainerItem.setOnLongClickListener(view ->{
            itemClick.removeCursoClick(curso);
            return true;
        });

        viewBind.clContainerItem.setOnClickListener(view -> {
            Intent intent = new Intent(viewBind.getRoot().getContext(), DetalhesCursoActivity.class);
            intent.putExtra(KEY_COURSE, curso.getId());
            viewBind.getRoot().getContext().startActivity(intent);


        });
    }

}
