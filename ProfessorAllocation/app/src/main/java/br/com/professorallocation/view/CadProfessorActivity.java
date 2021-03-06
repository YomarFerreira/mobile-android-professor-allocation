package br.com.professorallocation.view;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import br.com.professorallocation.R;
import br.com.professorallocation.config.RetrofitConfig;
import br.com.professorallocation.databinding.ActivityCadProfessorBinding;
import br.com.professorallocation.model.Departamento;
import br.com.professorallocation.model.Professor;
import br.com.professorallocation.model.ProfessorPostDto;
import br.com.professorallocation.service.DepartamentoService;
import br.com.professorallocation.service.ProfessorService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadProfessorActivity extends AppCompatActivity {

    private ActivityCadProfessorBinding viewBinding;
    private RetrofitConfig retrofitConfig;
    public int setDeptProf;
    public Departamento setDp;

    @Override
    protected void onResume() {
        requestCarregarlistaDepts();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityCadProfessorBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        retrofitConfig = new RetrofitConfig();

        if (getIntent().hasExtra(DetalhesProfessorActivity.KEY_PROFESSOR)) {

            getSupportActionBar().setTitle(getResources().getString(R.string.update_title_professor));
            viewBinding.btCadProfessor.setText(getResources().getString(R.string.update_button));

            Professor professor = (Professor) getIntent().getSerializableExtra(DetalhesProfessorActivity.KEY_PROFESSOR);
            if (professor != null) {
                viewBinding.edProfessorName.setText(professor.getName());
                viewBinding.edProfessorCpf.setText(professor.getCpf());
                this.setDeptProf = professor.getDepartment().getId();

                viewBinding.btCadProfessor.setOnClickListener(view-> {
                    ProfessorPostDto professorPut = new ProfessorPostDto();
                    professorPut.setName(viewBinding.edProfessorName.getText().toString());
                    professorPut.setCpf(viewBinding.edProfessorCpf.getText().toString());
                    professorPut.setDepartmentId(setDp.getId());

                    requestUpdateProfessor(professor.getId(), professorPut);
                });

            }
        }else{
            viewBinding.btCadProfessor.setOnClickListener(view -> registerProfessor());
        }

     }

    private void requestUpdateProfessor(int idProfessor, ProfessorPostDto professor){

        RetrofitConfig config = new RetrofitConfig();
        ProfessorService service = config.getProfessorService();

        service.update(idProfessor, professor).enqueue(new Callback<Professor>() {
            @Override
            public void onResponse(Call<Professor> call, Response<Professor> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Edit performed successfully", Toast.LENGTH_SHORT).show();
                    finish();

                }else{
                    Toast.makeText(getApplicationContext()," Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Professor> call, Throwable t) {
                Log.e(CadProfessorActivity.class.getSimpleName(),"Comunication error, " + t.getMessage());
                Toast.makeText(getApplicationContext()," Communication error with the server! ", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void registerProfessor() {

        String nomeProfessor = viewBinding.edProfessorName.getText().toString();
        String numCpf = viewBinding.edProfessorCpf.getText().toString();
        int idDepartment = setDp.getId();


        ProfessorPostDto professor = new ProfessorPostDto();
        professor.setName(nomeProfessor);
        professor.setCpf(numCpf);
        professor.setDepartmentId(idDepartment);

        RetrofitConfig config = new RetrofitConfig();
        ProfessorService service = config.getProfessorService();
        service.cadProfessor(professor).enqueue(new Callback<Professor>() {
            @Override
            public void onResponse(Call<Professor> call, Response<Professor> response) {

                Professor professorIdShow = response.body();

                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "The request was successful - ID. " + professorIdShow.getId(), Toast.LENGTH_SHORT).show();
                    viewBinding.edProfessorName.setText("");
                    viewBinding.edProfessorCpf.setText("");
                    viewBinding.spinListDepartments.setSelection(0);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext()," Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Professor> call, Throwable t) {
                Log.e(CadProfessorActivity.class.getSimpleName(),"Comunication error, " + t.getMessage());
                Toast.makeText(getApplicationContext()," Communication error with the server! ", Toast.LENGTH_SHORT).show();
            }
        });
     }


    private void requestCarregarlistaDepts() {
        DepartamentoService service = retrofitConfig.getDepartamentoService();
        service.getAll().enqueue(new Callback<List<Departamento>>() {
            @Override
            public void onResponse(Call<List<Departamento>> call, Response<List<Departamento>> response) {
                viewBinding.pbLoading.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    List<Departamento> list = response.body();

                    List<String> nomeDepartamentos = new ArrayList<>();
                    for (Departamento dp : list) {
                        nomeDepartamentos.add(dp.getName());
                    }

                    setupSpinner(nomeDepartamentos, list);

                    int i_foreach = 0;
                    for(Departamento departamento:list){
                        Log.d(CadProfessorActivity.class.getSimpleName(), departamento.getName());
                        if(departamento.getId()== setDeptProf){
                            viewBinding.spinListDepartments.setSelection(i_foreach);
                        }
                        i_foreach++;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), " Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Departamento>> call, Throwable t) {
                Log.e(CadProfessorActivity.class.getSimpleName(), "Comunication error, " + t.getMessage());
                Toast.makeText(getApplicationContext(), " Communication error with the server! ", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void setupSpinner(List<String> nomes, List<Departamento> departamentos){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(this, R.layout.layout_spinner_list_item, nomes);
        spinnerAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown_item);
        viewBinding.spinListDepartments.setAdapter(spinnerAdapter);

        viewBinding.spinListDepartments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setDp = departamentos.get(position);
                Log.d(CadProfessorActivity.class.getSimpleName(), "selecionado " + setDp.getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

}