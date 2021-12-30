package br.com.professorallocation.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.com.professorallocation.R;
import br.com.professorallocation.config.RetrofitConfig;
import br.com.professorallocation.databinding.ActivityDetalhesDepartamentoBinding;
import br.com.professorallocation.model.Alocacao;
import br.com.professorallocation.model.Departamento;
import br.com.professorallocation.service.DepartamentoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalhesDepartamentoActivity extends AppCompatActivity {

    private ActivityDetalhesDepartamentoBinding binding;

    public final static String KEY_DEPARTMENT = "KEY_DEPARTMENT";
    private Departamento departamento;

    private int idDepartamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalhesDepartamentoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idDepartamento = getIntent().getIntExtra(DepartamentoHolder.KEY_DEPARTMENT, -1);
        requestGetById(idDepartamento);
    }

    @Override
    protected void onResume() {
        super.onResume();

        idDepartamento = getIntent().getIntExtra(DepartamentoHolder.KEY_DEPARTMENT, -1);
        requestGetById(idDepartamento);
    }

    private void setupView(Departamento departamento){
        if(departamento.getAllocations() != null && !departamento.getAllocations().isEmpty()){
            binding.tvDepartment.setText(departamento.getName());
            Alocacao alocacao = departamento.getAllocations().get(0);
            binding.tvAllocationDayOfWeek.setText(alocacao.getDayOfWeek());
            binding.tvAllocationStart.setText(alocacao.getStartHour());
            binding.tvAllocationEnd.setText(alocacao.getEndHour());
            binding.tvAllocationProfessor.setText(alocacao.getProfessor().getName());
        }else{
            binding.tvDepartment.setText(departamento.getName());
            binding.tvAllocationDayOfWeek.setText("No allocation");
            binding.tvAllocationStart.setText("NA");
            binding.tvAllocationEnd.setText("NA");
            binding.tvAllocationProfessor.setText("NA");
        }
    }
    
    private void requestGetById(int idDepartamento){
        binding.pbLoading.setVisibility(View.VISIBLE);

        RetrofitConfig retrofitConfig = new RetrofitConfig();
        DepartamentoService service = retrofitConfig.getDepartamentoService();

        service.getByID(idDepartamento).enqueue(new Callback<Departamento>() {
            @Override
            public void onResponse(Call<Departamento> call, Response<Departamento> response) {
                binding.pbLoading.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    departamento = response.body();
                    setupView(departamento);
                }else{
                    Toast.makeText(getApplicationContext()," Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Departamento> call, Throwable t) {
                binding.pbLoading.setVisibility(View.GONE);
                Log.e(DetalhesDepartamentoActivity.class.getSimpleName(),"Comunication error, " + t.getMessage());
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
                Intent intent = new Intent(getApplicationContext(), CadDepartamentoActivity.class);
                intent.putExtra(KEY_DEPARTMENT, departamento);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }





}