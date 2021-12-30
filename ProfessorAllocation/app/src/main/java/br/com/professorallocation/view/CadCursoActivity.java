package br.com.professorallocation.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import br.com.professorallocation.R;
import br.com.professorallocation.config.RetrofitConfig;
import br.com.professorallocation.databinding.ActivityCadCursoBinding;
import br.com.professorallocation.model.Curso;
import br.com.professorallocation.model.CursoPostDto;
import br.com.professorallocation.service.CursoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadCursoActivity extends AppCompatActivity {

    private ActivityCadCursoBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityCadCursoBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        if (getIntent().hasExtra(DetalhesCursoActivity.KEY_COURSE)) {

            getSupportActionBar().setTitle(getResources().getString(R.string.update_title_course));
            viewBinding.btCadCurso.setText(getResources().getString(R.string.update_button));

            Curso curso = (Curso) getIntent().getSerializableExtra(DetalhesCursoActivity.KEY_COURSE);
            if (curso != null) {
                viewBinding.edCursoName.setText(curso.getName());

                viewBinding.btCadCurso.setOnClickListener(view-> {
                    CursoPostDto cursoPut = new CursoPostDto();
                    cursoPut.setName(viewBinding.edCursoName.getText().toString());
                    requestUpdateCourse(curso.getId(), cursoPut);
                });
            }
        }else{
            viewBinding.btCadCurso.setOnClickListener(view -> registerCourse());
        }
    }

    private void requestUpdateCourse(int idCurso, CursoPostDto curso){

        RetrofitConfig config = new RetrofitConfig();
        CursoService service = config.getCursoService();

        service.update(idCurso, curso).enqueue(new Callback<Curso>() {
            @Override
            public void onResponse(Call<Curso> call, Response<Curso> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Edit performed successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext()," Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Curso> call, Throwable t) {
                Log.e(CadCursoActivity.class.getSimpleName(),"Comunication error, " + t.getMessage());
                Toast.makeText(getApplicationContext()," Communication error with the server! ", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void registerCourse() {

        String nomeCurso = viewBinding.edCursoName.getText().toString();

        CursoPostDto curso = new CursoPostDto();
        curso.setName(nomeCurso);

        RetrofitConfig config = new RetrofitConfig();
        CursoService service = config.getCursoService();
        service.cadCurso(curso).enqueue(new Callback<Curso>() {
            @Override
            public void onResponse(Call<Curso> call, Response<Curso> response) {

                Curso cursoIdShow = response.body();

                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "The request was successful - ID. " + cursoIdShow.getId(), Toast.LENGTH_SHORT).show();
                    viewBinding.edCursoName.setText("");
                    finish();
                }else{
                    Toast.makeText(getApplicationContext()," Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Curso> call, Throwable t) {
                Log.e(CadCursoActivity.class.getSimpleName(),"Comunication error, " + t.getMessage());
                Toast.makeText(getApplicationContext()," Communication error with the server! ", Toast.LENGTH_SHORT).show();
            }
        });
     }
}