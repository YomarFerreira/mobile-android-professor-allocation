package br.com.professorallocation.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;

import br.com.professorallocation.R;
import br.com.professorallocation.config.RetrofitConfig;
import br.com.professorallocation.databinding.ActivityDetalhesAlocacaoBinding;
import br.com.professorallocation.model.Alocacao;
import br.com.professorallocation.service.AlocacaoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalhesAlocacaoActivity extends AppCompatActivity {

    private ActivityDetalhesAlocacaoBinding binding;

    public final static String KEY_ALLOCATION = "KEY_ALLOCATION";
    private Alocacao alocacao;

    private int idAlocacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalhesAlocacaoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idAlocacao = getIntent().getIntExtra(AlocacaoHolder.KEY_ALLOCATION, -1);
        requestGetById(idAlocacao);
    }

    @Override
    protected void onResume() {
        super.onResume();

        idAlocacao = getIntent().getIntExtra(AlocacaoHolder.KEY_ALLOCATION, -1);
        requestGetById(idAlocacao);
    }

    private void setupView(Alocacao alocacao){
        binding.tvAllocationCourseDetails.setText(alocacao.getCourse().getName());
        binding.tvAllocationDayDetails.setText(alocacao.getDay());

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String startHourFormated = df.format(alocacao.getStartHour());
        String endHourFormated = df.format(alocacao.getEndHour());

        binding.tvAllocationStartHourDetails.setText(startHourFormated);
        binding.tvAllocationEndHourDetails.setText(endHourFormated);
        binding.tvAllocationProfessorDetails.setText(alocacao.getProfessor().getName());
    }
    
    private void requestGetById(int idAlocacao){
        binding.pbLoading.setVisibility(View.VISIBLE);

        RetrofitConfig retrofitConfig = new RetrofitConfig();
        AlocacaoService service = retrofitConfig.getAlocacaoService();

        service.getByID(idAlocacao).enqueue(new Callback<Alocacao>() {
            @Override
            public void onResponse(Call<Alocacao> call, Response<Alocacao> response) {
                binding.pbLoading.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    alocacao = response.body();
                    setupView(alocacao);
                }else{
                    Toast.makeText(getApplicationContext()," Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Alocacao> call, Throwable t) {
                binding.pbLoading.setVisibility(View.GONE);
                Log.e(DetalhesAlocacaoActivity.class.getSimpleName(),"Comunication error details activity, " + t.getMessage());
                Toast.makeText(getApplicationContext()," Communication error with the server! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_item:
                Intent intent = new Intent(getApplicationContext(), CadAlocacaoActivity.class);
                intent.putExtra(KEY_ALLOCATION, alocacao);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}