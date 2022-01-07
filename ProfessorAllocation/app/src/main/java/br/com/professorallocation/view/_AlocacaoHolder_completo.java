package br.com.professorallocation.view;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.professorallocation.config.RetrofitConfig;
import br.com.professorallocation.databinding.ItemLayoutAlocacaoBinding;
import br.com.professorallocation.model.Alocacao;
import br.com.professorallocation.model.Curso;
import br.com.professorallocation.model.Professor;
import br.com.professorallocation.service.CursoService;
import br.com.professorallocation.service.ProfessorService;

public class _AlocacaoHolder_completo extends RecyclerView.ViewHolder {

    private ItemLayoutAlocacaoBinding viewBind;
    private RetrofitConfig retrofitConfig;
    CursoService service1 = retrofitConfig.getCursoService();
    private List<Curso> listCurso;
    ProfessorService service2 = retrofitConfig.getProfessorService();
    private List<Professor> listProfessor;
    private int i_foreachcrs;
    private int i_foreachprf;



    public final static String KEY_ALOCACAO = "KEY_ALOCACAO";

    public _AlocacaoHolder_completo(@NonNull ItemLayoutAlocacaoBinding itemView) {
        super(itemView.getRoot());
        viewBind = itemView;
    }

    public void bind(Alocacao alocacao, ActionAlocacaoClick itemClick){
        viewBind.tvIdAlocation.setText(String.valueOf(alocacao.getId()));
        viewBind.tvDayAllocation.setText(alocacao.getDay());
        viewBind.tvStartHourAllocation.setText(String.valueOf(alocacao.getStartHour()));
        viewBind.tvEndHourAllocation.setText(String.valueOf(alocacao.getEndHour()));
/*
        i_foreachcrs = 0 ;
        for(Curso curso:this.listCurso){
            if(curso.getId()== alocacao.getCourseId()){
                viewBind.tvItemValue4.setText(curso.getName());
            }
            i_foreachcrs++;
        }

        i_foreachprf = 0 ;
        for(Professor professor:listProfessor){
            if(professor.getId()== alocacao.getProfessorId()){
                viewBind.tvItemValue5.setText(professor.getName());
            }
            i_foreachprf++;
        }
*/
        viewBind.clContainerItem.setOnLongClickListener(view ->{
            itemClick.removeAlocacaoClick(alocacao);
            return true;
        });

        viewBind.clContainerItem.setOnClickListener(view -> {
            Intent intent = new Intent(viewBind.getRoot().getContext(), DetalhesAlocacaoActivity.class);
            intent.putExtra(KEY_ALOCACAO, alocacao.getId());
            viewBind.getRoot().getContext().startActivity(intent);

        });
    }

}
