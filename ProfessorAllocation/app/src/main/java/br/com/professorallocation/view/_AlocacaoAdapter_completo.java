package br.com.professorallocation.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.professorallocation.databinding.ItemLayoutAlocacaoBinding;
import br.com.professorallocation.model.Alocacao;

public class _AlocacaoAdapter_completo extends RecyclerView.Adapter<AlocacaoHolder> {

    private List<Alocacao> alocacoesList = new ArrayList<>();
    private ActionAlocacaoClick actionAlocacaoClick;

    public void setActionAlocacaoClick(ActionAlocacaoClick actionAlocacaoClick) {
        this.actionAlocacaoClick = actionAlocacaoClick;
    }

    public void setAlocacoesList(List<Alocacao> alocacaosList){
        this.alocacoesList.clear();

        Collections.sort(alocacoesList, new Comparator<Alocacao>() {
            @Override
            public int compare(Alocacao item1, Alocacao item2) {
                //Alfabetico
                // return item1.getName().compareToIgnoreCase(item2.getName());

                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return item1.getId() < item2.getId() ? -1 : (item1.getCourseId() < item2.getCourseId()) ? 1 : 0;
            }
        });

        this.alocacoesList.addAll(alocacoesList);
        notifyDataSetChanged();
    }

    public void RemoverAlocacaoDaTela(Alocacao alocacao){
        alocacoesList.remove(alocacao);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlocacaoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLayoutAlocacaoBinding binding = ItemLayoutAlocacaoBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        AlocacaoHolder holder = new AlocacaoHolder(binding);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlocacaoHolder holder, int position) {
        Alocacao alocacao = alocacoesList.get(position);
        holder.bind(alocacao,new ActionAlocacaoClick() {
            @Override
            public void removeAlocacaoClick(Alocacao alocacaoC) {
                actionAlocacaoClick.removeAlocacaoClick(alocacaoC);
            }

        });

    }

    @Override
    public int getItemCount() {
        return alocacoesList.size();
    }
}
