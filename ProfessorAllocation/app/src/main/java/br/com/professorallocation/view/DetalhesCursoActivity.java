package br.com.professorallocation.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import br.com.professorallocation.R;
import br.com.professorallocation.config.RetrofitConfig;
import br.com.professorallocation.databinding.ActivityDetalhesCursoBinding;
import br.com.professorallocation.model.Alocacao;
import br.com.professorallocation.model.Curso;
import br.com.professorallocation.service.CursoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalhesCursoActivity extends AppCompatActivity {

    private ActivityDetalhesCursoBinding binding;

    public final static String KEY_COURSE = "KEY_COURSE";
    private Curso curso;

    private int idCurso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalhesCursoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idCurso = getIntent().getIntExtra(CursoHolder.KEY_COURSE, -1);
        requestGetById(idCurso);
    }

    @Override
    protected void onResume() {
        super.onResume();

        idCurso = getIntent().getIntExtra(CursoHolder.KEY_COURSE, -1);
        requestGetById(idCurso);
    }

    private void setupView(Curso curso){

        binding.tvIdCourse.setText(String.valueOf(curso.getId()));
        binding.tvNameCourse.setText(curso.getName());


    }
    
    private void requestGetById(int idCurso){
        binding.pbLoading.setVisibility(View.VISIBLE);

        RetrofitConfig retrofitConfig = new RetrofitConfig();
        CursoService service = retrofitConfig.getCursoService();

        service.getByID(idCurso).enqueue(new Callback<Curso>() {
            @Override
            public void onResponse(Call<Curso> call, Response<Curso> response) {
                binding.pbLoading.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    curso = response.body();
                    setupView(curso);
                }else{
                    Toast.makeText(getApplicationContext()," Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Curso> call, Throwable t) {
                binding.pbLoading.setVisibility(View.GONE);
                Log.e(DetalhesCursoActivity.class.getSimpleName(),"Comunication error, " + t.getMessage());
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
                Intent intent = new Intent(getApplicationContext(), CadCursoActivity.class);
                intent.putExtra(KEY_COURSE, curso);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }





}