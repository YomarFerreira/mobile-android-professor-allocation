package br.com.professorallocation.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.professorallocation.R;
import br.com.professorallocation.config.RetrofitConfig;
import br.com.professorallocation.databinding.ActivityProfessoresBinding;
import br.com.professorallocation.model.Curso;
import br.com.professorallocation.model.Departamento;
import br.com.professorallocation.model.Professor;
import br.com.professorallocation.service.CursoService;
import br.com.professorallocation.service.DepartamentoService;
import br.com.professorallocation.service.ProfessorService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfessoresActivity extends AppCompatActivity {

    private ActivityProfessoresBinding viewBinding;
    private RetrofitConfig retrofitConfig;
    private ProfessorAdapter adapter;

    @Override
    protected void onResume(){
        requestCarregarListaDosProfessores("");
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityProfessoresBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        this.adapter = new ProfessorAdapter();

        this.adapter.setActionProfessorClick(new ActionProfessorClick() {
            @Override
            public void removeProfessorClick(Professor professor) {
                dialogConfirmation(professor);
            }

        });

        viewBinding.rvListaProfessores.setAdapter(this.adapter);

        retrofitConfig = new RetrofitConfig();

    }

    private void dialogConfirmation(Professor professor) {

        AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
        msgBox.setTitle("Delete");
        msgBox.setIcon(android.R.drawable.ic_menu_delete);
        msgBox.setMessage("Are you sure you want to delete the professor " + professor.getName());
        msgBox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestDelete(professor);            }
        });
        msgBox.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ProfessoresActivity.this, "Delete Canceled.", Toast.LENGTH_SHORT).show();
            }
        });
        msgBox.show();
    }


    private void requestDelete(Professor professor){

        ProfessorService service = retrofitConfig.getProfessorService();
        service.delete(professor.getId()).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    adapter.RemoverProfessorDaTela(professor);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(ProfessoresActivity.class.getSimpleName(),"Comunication error, " + t.getMessage());
                Toast.makeText(getApplicationContext()," Communication error with the server! ", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_register_search, menu);

        MenuItem search = menu.findItem(R.id.search_item);
        SearchView editTextDeBusca = (SearchView)search.getActionView();
        editTextDeBusca.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Estado", "submit");

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("Estado", "digitando");
                requestCarregarListaDosProfessores(newText.toString());
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_item:
                Intent intent = new Intent(getApplicationContext(), CadProfessorActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private void requestCarregarListaDosProfessores(String nomeProfessor){
        viewBinding.pbLoading.setVisibility(View.VISIBLE);

        if(nomeProfessor!="") {
            ProfessorService service = retrofitConfig.getProfessorService();
            service.getByName(nomeProfessor).enqueue(new Callback<List<Professor>>() {

                List<Professor> ProfessorsFound = new ArrayList<>();
                @Override
                public void onResponse(Call<List<Professor>> call, Response<List<Professor>> response) {
                    viewBinding.pbLoading.setVisibility(View.GONE);

                    if (response.isSuccessful()) {

                        List<Professor> professorsList = response.body();
                        Departamento deptoProf = new Departamento();

                        for (Professor searchProfessor : professorsList) {
                            int idProfessorInt = searchProfessor.getId();
                            String nomeProfessorStr = searchProfessor.getName();
                            String cpfProfessorStr = searchProfessor.getCpf();
                            int deptoProfessorInt = searchProfessor.getDepartmentId();
                            String departmentProfessorStr = searchProfessor.getDepartment().getName();

                            if (nomeProfessorStr.toLowerCase(Locale.ROOT).contains(nomeProfessor.toLowerCase(Locale.ROOT))){
                                Professor professorMount = new Professor();
                                professorMount.setId(idProfessorInt);
                                professorMount.setName(nomeProfessorStr);
                                professorMount.setCpf(cpfProfessorStr);

                                deptoProf.setId(deptoProfessorInt);
                                deptoProf.setName(departmentProfessorStr);

                                professorMount.setDepartment(deptoProf);

                                ProfessorsFound.add(professorMount);
                            }
                        }

                        adapter.setProfessorsList(ProfessorsFound);

                    } else {
                        Toast.makeText(getApplicationContext(), " Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<List<Professor>> call, Throwable t) {
                    viewBinding.pbLoading.setVisibility(View.GONE);

                    Log.e(ProfessoresActivity.class.getSimpleName(), "Comunication error, " + t.getMessage());
                    Toast.makeText(getApplicationContext(), " Communication error with the server! ", Toast.LENGTH_SHORT).show();

                }
            });

        }else {

            ProfessorService service = retrofitConfig.getProfessorService();
            service.getAll().enqueue(new Callback<List<Professor>>() {
                @Override
                public void onResponse(Call<List<Professor>> call, Response<List<Professor>> response) {
                    viewBinding.pbLoading.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        List<Professor> list = response.body();
                        adapter.setProfessorsList(list);

                    } else {
                        Toast.makeText(getApplicationContext(), " Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<List<Professor>> call, Throwable t) {
                    viewBinding.pbLoading.setVisibility(View.GONE);
                    Log.e(ProfessoresActivity.class.getSimpleName(), "Comunication error, " + t.getMessage());
                    Toast.makeText(getApplicationContext(), " Communication error with the server! ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}