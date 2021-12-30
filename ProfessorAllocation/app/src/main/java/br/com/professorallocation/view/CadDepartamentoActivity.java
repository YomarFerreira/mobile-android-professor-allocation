package br.com.professorallocation.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.professorallocation.R;
import br.com.professorallocation.config.RetrofitConfig;
import br.com.professorallocation.databinding.ActivityCadDepartamentoBinding;
import br.com.professorallocation.model.Departamento;
import br.com.professorallocation.model.DepartamentoPostDto;
import br.com.professorallocation.service.DepartamentoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadDepartamentoActivity extends AppCompatActivity {

    private ActivityCadDepartamentoBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityCadDepartamentoBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        if (getIntent().hasExtra(DetalhesDepartamentoActivity.KEY_DEPARTMENT)) {

            getSupportActionBar().setTitle(getResources().getString(R.string.update_title_department));
            viewBinding.btCadDepartamento.setText(getResources().getString(R.string.update_button));

            Departamento departamento = (Departamento) getIntent().getSerializableExtra(DetalhesDepartamentoActivity.KEY_DEPARTMENT);
            if (departamento != null) {
                viewBinding.edDepartamentoName.setText(departamento.getName());

                viewBinding.btCadDepartamento.setOnClickListener(view-> {
                    DepartamentoPostDto departamentoPut = new DepartamentoPostDto();
                    departamentoPut.setName(viewBinding.edDepartamentoName.getText().toString());
                    requestUpdateCourse(departamento.getId(), departamentoPut);
                });
            }
        }else{
            viewBinding.btCadDepartamento.setOnClickListener(view -> registerCourse());
        }
    }

    private void requestUpdateCourse(int idDepartamento, DepartamentoPostDto departamento){

        RetrofitConfig config = new RetrofitConfig();
        DepartamentoService service = config.getDepartamentoService();

        service.update(idDepartamento, departamento).enqueue(new Callback<Departamento>() {
            @Override
            public void onResponse(Call<Departamento> call, Response<Departamento> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Edit performed successfully", Toast.LENGTH_SHORT).show();
                    finish();

                }else{
                    Toast.makeText(getApplicationContext()," Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Departamento> call, Throwable t) {
                Log.e(CadDepartamentoActivity.class.getSimpleName(),"Comunication error, " + t.getMessage());
                Toast.makeText(getApplicationContext()," Communication error with the server! ", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void registerCourse() {

        String nomeDepartamento = viewBinding.edDepartamentoName.getText().toString();

        DepartamentoPostDto departamento = new DepartamentoPostDto();
        departamento.setName(nomeDepartamento);

        RetrofitConfig config = new RetrofitConfig();
        DepartamentoService service = config.getDepartamentoService();
        service.cadDepartamento(departamento).enqueue(new Callback<Departamento>() {
            @Override
            public void onResponse(Call<Departamento> call, Response<Departamento> response) {

                Departamento departamentoIdShow = response.body();

                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "The request was successful - ID. " + departamentoIdShow.getId(), Toast.LENGTH_SHORT).show();
                    viewBinding.edDepartamentoName.setText("");
                    finish();
                }else{
                    Toast.makeText(getApplicationContext()," Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Departamento> call, Throwable t) {
                Log.e(CadDepartamentoActivity.class.getSimpleName(),"Comunication error, " + t.getMessage());
                Toast.makeText(getApplicationContext()," Communication error with the server! ", Toast.LENGTH_SHORT).show();
            }
        });
     }
}