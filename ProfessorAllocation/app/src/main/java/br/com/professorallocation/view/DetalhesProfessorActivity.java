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
import br.com.professorallocation.databinding.ActivityDetalhesProfessorBinding;
import br.com.professorallocation.model.Alocacao;
import br.com.professorallocation.model.Departamento;
import br.com.professorallocation.model.Professor;
import br.com.professorallocation.service.ProfessorService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalhesProfessorActivity extends AppCompatActivity {

    private ActivityDetalhesProfessorBinding binding;

    public final static String KEY_PROFESSOR = "KEY_PROFESSOR";
    private Professor professor;

    private int idProfessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalhesProfessorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idProfessor = getIntent().getIntExtra(ProfessorHolder.KEY_PROFESSOR, -1);
        requestGetById(idProfessor);
    }

    @Override
    protected void onResume() {
        super.onResume();

        idProfessor = getIntent().getIntExtra(ProfessorHolder.KEY_PROFESSOR, -1);
        requestGetById(idProfessor);
    }

    private void setupView(Professor professor){

        binding.tvIdProfessor.setText(String.valueOf(professor.getId()));
        binding.tvNameProfessor.setText(professor.getName());
        String cpfstr =(professor.getCpf().substring(0,3) + "." + professor.getCpf().substring(3,6) + "." + professor.getCpf().substring(6,9) + "-" + professor.getCpf().substring(9,11));
        binding.tvCpfProfessor.setText(cpfstr);
        binding.tvDepartmentProfessor.setText(professor.getDepartment().getName());

    }
    
    private void requestGetById(int idProfessor){
        binding.pbLoading.setVisibility(View.VISIBLE);

        RetrofitConfig retrofitConfig = new RetrofitConfig();
        ProfessorService service = retrofitConfig.getProfessorService();

        service.getByID(idProfessor).enqueue(new Callback<Professor>() {
            @Override
            public void onResponse(Call<Professor> call, Response<Professor> response) {
                binding.pbLoading.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    professor = response.body();
                    setupView(professor);
                }else{
                    Toast.makeText(getApplicationContext()," Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Professor> call, Throwable t) {
                binding.pbLoading.setVisibility(View.GONE);
                Log.e(DetalhesProfessorActivity.class.getSimpleName(),"Comunication error, " + t.getMessage());
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
                Intent intent = new Intent(getApplicationContext(), CadProfessorActivity.class);
                intent.putExtra(KEY_PROFESSOR, professor);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}