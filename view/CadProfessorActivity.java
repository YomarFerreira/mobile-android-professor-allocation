package br.com.professorallocation.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.professorallocation.R;
import br.com.professorallocation.config.RetrofitConfig;
import br.com.professorallocation.databinding.ActivityCadProfessorBinding;
import br.com.professorallocation.model.Professor;
import br.com.professorallocation.model.ProfessorPostDto;
import br.com.professorallocation.service.ProfessorService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadProfessorActivity extends AppCompatActivity {

    private ActivityCadProfessorBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityCadProfessorBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        if (getIntent().hasExtra(DetalhesProfessorActivity.KEY_PROFESSOR)) {

            getSupportActionBar().setTitle(getResources().getString(R.string.update_title_professor));
            viewBinding.btCadProfessor.setText(getResources().getString(R.string.update_button));

            Professor professor = (Professor) getIntent().getSerializableExtra(DetalhesProfessorActivity.KEY_PROFESSOR);
            if (professor != null) {
                viewBinding.edProfessorName.setText(professor.getName());
                viewBinding.edProfessorCpf.setText(professor.getCpf());
               // viewBinding.edDepartmentProf.setText(professor.getDepartmentId());

                viewBinding.btCadProfessor.setOnClickListener(view-> {
                    ProfessorPostDto professorPut = new ProfessorPostDto();
                    professorPut.setName(viewBinding.edProfessorName.getText().toString());
                    professorPut.setCpf(viewBinding.edProfessorCpf.getText().toString());
                 //   professorPut.setDepartmentId(viewBinding.edDepartmentProf.getId());

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
    //    int idDepartment = viewBinding.edDepartmentProf.getId();

        ProfessorPostDto professor = new ProfessorPostDto();
        professor.setName(nomeProfessor);
        professor.setCpf(numCpf);
   //     professor.setDepartmentId(idDepartment);

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
                //    viewBinding.edDepartmentProf.setText("");
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
}