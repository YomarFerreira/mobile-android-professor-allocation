package br.com.professorallocation.view;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.professorallocation.databinding.ItemLayoutProfessorBinding;
import br.com.professorallocation.model.Professor;

public class ProfessorHolder extends RecyclerView.ViewHolder {

    private ItemLayoutProfessorBinding viewBind;

    public final static String KEY_PROFESSOR = "KEY_PROFESSOR";

    public ProfessorHolder(@NonNull ItemLayoutProfessorBinding itemView) {
        super(itemView.getRoot());
        viewBind = itemView;
    }

    public void bind(Professor professor, ActionProfessorClick itemClick){
        viewBind.tvIdProfessor.setText(String.valueOf(professor.getId()));
        viewBind.tvProfessorName.setText(professor.getName());
        String cpfstr =(professor.getCpf().substring(0,3) + "." + professor.getCpf().substring(3,6) + "." + professor.getCpf().substring(6,9) + "-" + professor.getCpf().substring(9,11));
        viewBind.tvProfessorCpf.setText(cpfstr);
        viewBind.tvDepartmentProfessor.setText(professor.getDepartment().getName());

        viewBind.clContainerItem.setOnLongClickListener(view ->{
            itemClick.removeProfessorClick(professor);
            return true;
        });

        viewBind.clContainerItem.setOnClickListener(view -> {
            Intent intent = new Intent(viewBind.getRoot().getContext(), DetalhesProfessorActivity.class);
            intent.putExtra(KEY_PROFESSOR, professor.getId());
            viewBind.getRoot().getContext().startActivity(intent);


        });
    }

}
