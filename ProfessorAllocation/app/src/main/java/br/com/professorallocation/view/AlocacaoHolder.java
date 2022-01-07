package br.com.professorallocation.view;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import br.com.professorallocation.databinding.ItemLayoutAlocacaoBinding;
import br.com.professorallocation.model.Alocacao;

public class AlocacaoHolder extends RecyclerView.ViewHolder {

    private ItemLayoutAlocacaoBinding viewBind;

    public final static String KEY_ALLOCATION = "KEY_ALLOCATION";

    public AlocacaoHolder(@NonNull ItemLayoutAlocacaoBinding itemView) {
        super(itemView.getRoot());
        viewBind = itemView;
    }

    public void bind(Alocacao alocacao, ActionAlocacaoClick itemClick){
        viewBind.tvIdAlocation.setText(String.valueOf(alocacao.getId()));
        viewBind.tvDayAllocation.setText(alocacao.getDay().substring(0,3));

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String startHourFormated = df.format(alocacao.getStartHour());
        String endHourFormated = df.format(alocacao.getEndHour());

        viewBind.tvStartHourAllocation.setText(startHourFormated);
        viewBind.tvEndHourAllocation.setText(endHourFormated);

        viewBind.tvCursoAllocation.setText(alocacao.getCourse().getName());
        viewBind.tvProfessorAllocation.setText(alocacao.getProfessor().getName());

        viewBind.clContainerItem.setOnLongClickListener(view ->{
            itemClick.removeAlocacaoClick(alocacao);
            return true;
        });

        viewBind.clContainerItem.setOnClickListener(view -> {
            Intent intent = new Intent(viewBind.getRoot().getContext(), DetalhesAlocacaoActivity.class);
            intent.putExtra(KEY_ALLOCATION, alocacao.getId());
            viewBind.getRoot().getContext().startActivity(intent);


        });
    }

}
