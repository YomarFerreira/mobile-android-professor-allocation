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
import br.com.professorallocation.databinding.ActivityDepartamentosBinding;
import br.com.professorallocation.model.Curso;
import br.com.professorallocation.model.Departamento;
import br.com.professorallocation.service.CursoService;
import br.com.professorallocation.service.DepartamentoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepartamentosActivity extends AppCompatActivity {

    private ActivityDepartamentosBinding viewBinding;
    private RetrofitConfig retrofitConfig;
    private DepartamentoAdapter adapter;

    @Override
    protected void onResume(){
        requestCarregarTodosOsDepartamentos("");
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityDepartamentosBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        this.adapter = new DepartamentoAdapter();

        this.adapter.setActionDepartamentoClick(new ActionDepartamentoClick() {
            @Override
            public void removeDepartamentoClick(Departamento departamento) {
                dialogConfirmation(departamento);
            }

        });

        viewBinding.spinListDepartments.setAdapter(this.adapter);

        retrofitConfig = new RetrofitConfig();
    }

    private void dialogConfirmation(Departamento departamento) {

        AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
        msgBox.setTitle("Delete");
        msgBox.setIcon(android.R.drawable.ic_menu_delete);
        msgBox.setMessage("Are you sure you want to delete the department " + departamento.getName());
        msgBox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestDelete(departamento);            }
        });
        msgBox.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(DepartamentosActivity.this, "Delete Canceled.", Toast.LENGTH_SHORT).show();
            }
        });
        msgBox.show();
    }


    private void requestDelete(Departamento departamento){

        DepartamentoService service = retrofitConfig.getDepartamentoService();
        service.delete(departamento.getId()).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    adapter.RemoverDepartamentoDaTela(departamento);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(DepartamentosActivity.class.getSimpleName(),"Comunication error, " + t.getMessage());
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
                requestCarregarTodosOsDepartamentos(newText.toString());
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_item:
                Intent intent = new Intent(getApplicationContext(), CadDepartamentoActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private void requestCarregarTodosOsDepartamentos(String nomeDepartamento){
        viewBinding.pbLoading.setVisibility(View.VISIBLE);

        if(nomeDepartamento!="") {
            DepartamentoService service = retrofitConfig.getDepartamentoService();
            service.getByName(nomeDepartamento).enqueue(new Callback<List<Departamento>>() {

                List<Departamento> DepartmentsFound = new ArrayList<>();
                @Override
                public void onResponse(Call<List<Departamento>> call, Response<List<Departamento>> response) {
                    viewBinding.pbLoading.setVisibility(View.GONE);

                    if (response.isSuccessful()) {

                        List<Departamento> departamentosList = response.body();


                        for (Departamento searchDepartment : departamentosList) {
                            int idDepartamentoInt = searchDepartment.getId();
                            String nomeDepartamentoStr = searchDepartment.getName();
                            if (nomeDepartamentoStr.toLowerCase(Locale.ROOT).contains(nomeDepartamento.toLowerCase(Locale.ROOT))){
                                Departamento departmentMount = new Departamento();
                                departmentMount.setId(idDepartamentoInt);
                                departmentMount.setName(nomeDepartamentoStr);

                                DepartmentsFound.add(departmentMount);
                            }
                        }

                        adapter.setDepartamentosList(DepartmentsFound);

                    } else {
                        Toast.makeText(getApplicationContext(), " Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<List<Departamento>> call, Throwable t) {
                    viewBinding.pbLoading.setVisibility(View.GONE);

                    Log.e(DepartamentosActivity.class.getSimpleName(), "Comunication error, " + t.getMessage());
                    Toast.makeText(getApplicationContext(), " Communication error with the server! ", Toast.LENGTH_SHORT).show();

                }
            });

        }else {

            DepartamentoService service = retrofitConfig.getDepartamentoService();
            service.getAll().enqueue(new Callback<List<Departamento>>() {
                @Override
                public void onResponse(Call<List<Departamento>> call, Response<List<Departamento>> response) {
                    viewBinding.pbLoading.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        List<Departamento> list = response.body();
                        adapter.setDepartamentosList(list);

                        for (Departamento departamento: list) {
                            Log.d(DepartamentosActivity.class.getSimpleName(), departamento.getName());
                        }
                    }else{
                        Toast.makeText(getApplicationContext()," Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<List<Departamento>> call, Throwable t) {
                    viewBinding.pbLoading.setVisibility(View.GONE);
                    Log.e(DepartamentosActivity.class.getSimpleName(),"Comunication error, " + t.getMessage());
                    Toast.makeText(getApplicationContext()," Communication error with the server! ", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}