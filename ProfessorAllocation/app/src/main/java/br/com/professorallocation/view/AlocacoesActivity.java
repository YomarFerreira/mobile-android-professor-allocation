package br.com.professorallocation.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;


import br.com.professorallocation.R;
import br.com.professorallocation.config.RetrofitConfig;
import br.com.professorallocation.databinding.ActivityAlocacoesBinding;
import br.com.professorallocation.model.Alocacao;
import br.com.professorallocation.service.AlocacaoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlocacoesActivity extends AppCompatActivity {

    private ActivityAlocacoesBinding viewBinding;
    private RetrofitConfig retrofitConfig;
    private AlocacaoAdapter adapter;

    @Override
    protected void onResume(){
        requestCarregarTodasAsAlocacoes();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityAlocacoesBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        this.adapter = new AlocacaoAdapter();

        this.adapter.setActionAlocacaoClick(new ActionAlocacaoClick() {
            @Override
            public void removeAlocacaoClick(Alocacao alocacao) {
                dialogConfirmation(alocacao);
            }

        });


        viewBinding.rvListaAlocacoes.setAdapter(this.adapter);

        retrofitConfig = new RetrofitConfig();

    }

    private void dialogConfirmation(Alocacao alocacao) {

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");

        AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
        msgBox.setTitle("Delete");
        msgBox.setIcon(android.R.drawable.ic_menu_delete);
        msgBox.setMessage("Are you sure you want to delete the " + alocacao.getCourse().getName() + " course allocation on "  + alocacao.getDay() + " starting at " + df.format(alocacao.getStartHour()) + "hs ?");
        msgBox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestDelete(alocacao);            }
        });
        msgBox.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlocacoesActivity.this, "Delete Canceled.", Toast.LENGTH_SHORT).show();
            }
        });
        msgBox.show();
    }


    private void requestDelete(Alocacao alocacao){

        AlocacaoService service = retrofitConfig.getAlocacaoService();
        service.delete(alocacao.getId()).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    adapter.RemoverAlocacaoDaTela(alocacao);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(AlocacoesActivity.class.getSimpleName(),"Comunication error activity, " + t.getMessage());
                Toast.makeText(getApplicationContext()," Communication error with the server! ", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_item:
                Intent intent = new Intent(getApplicationContext(), CadAlocacaoActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private void requestCarregarTodasAsAlocacoes(){
        viewBinding.pbLoading.setVisibility(View.VISIBLE);

        AlocacaoService service = retrofitConfig.getAlocacaoService();
        service.getAll().enqueue(new Callback<List<Alocacao>>() {
            @Override
            public void onResponse(Call<List<Alocacao>> call, Response<List<Alocacao>> response) {
                viewBinding.pbLoading.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    List<Alocacao> list = response.body();
                    adapter.setAlocacoesList(list);

                    for (Alocacao alocacao: list) {
                        Log.d(AlocacoesActivity.class.getSimpleName(), alocacao.getDay());
                    }
                }else{
                    Toast.makeText(getApplicationContext()," Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Alocacao>> call, Throwable t) {
                viewBinding.pbLoading.setVisibility(View.GONE);

                Log.e(AlocacoesActivity.class.getSimpleName(),"Comunication error, " + t.getMessage());
                Toast.makeText(getApplicationContext()," Communication error with the server! ", Toast.LENGTH_SHORT).show();
            }
        });

    }


}