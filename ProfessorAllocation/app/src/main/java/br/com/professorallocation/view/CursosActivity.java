package br.com.professorallocation.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;


import br.com.professorallocation.R;
import br.com.professorallocation.config.RetrofitConfig;
import br.com.professorallocation.databinding.ActivityCursosBinding;
import br.com.professorallocation.model.AlocacaoPostDto;
import br.com.professorallocation.model.Curso;
import br.com.professorallocation.model.CursoPostDto;
import br.com.professorallocation.service.CursoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CursosActivity extends AppCompatActivity {

    private ActivityCursosBinding viewBinding;
    private RetrofitConfig retrofitConfig;
    private CursoAdapter adapter;

    @Override
    protected void onResume(){
        requestCarregarTodosOsCursos("");
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityCursosBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        this.adapter = new CursoAdapter();

        this.adapter.setActionCursoClick(new ActionCursoClick() {
            @Override
            public void removeCursoClick(Curso curso) {
                dialogConfirmation(curso);
            }

        });


        viewBinding.rvListaCursos.setAdapter(this.adapter);

        retrofitConfig = new RetrofitConfig();

    }

    private void dialogConfirmation(Curso curso) {

        AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
        msgBox.setTitle("Delete");
        msgBox.setIcon(android.R.drawable.ic_menu_delete);
        msgBox.setMessage("Are you sure you want to delete the course " + curso.getName());
        msgBox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestDelete(curso);            }
        });
        msgBox.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CursosActivity.this, "Delete Canceled.", Toast.LENGTH_SHORT).show();
            }
        });
        msgBox.show();
    }


    private void requestDelete(Curso curso){

        CursoService service = retrofitConfig.getCursoService();
        service.delete(curso.getId()).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    adapter.RemoverCursoDaTela(curso);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(CursosActivity.class.getSimpleName(),"Comunication error, " + t.getMessage());
                Toast.makeText(getApplicationContext()," Communication error with the server! ", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_register_search,menu);
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
                requestCarregarTodosOsCursos(newText.toString());
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_item:
                Intent intent = new Intent(getApplicationContext(), CadCursoActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private void requestCarregarTodosOsCursos(String nomeCurso){
        viewBinding.pbLoading.setVisibility(View.VISIBLE);

        if(nomeCurso!="") {
            CursoService service = retrofitConfig.getCursoService();
            service.getByName(nomeCurso).enqueue(new Callback<List<Curso>>() {

            List<Curso> CoursesFound = new ArrayList<>();
                @Override
                public void onResponse(Call<List<Curso>> call, Response<List<Curso>> response) {
                    viewBinding.pbLoading.setVisibility(View.GONE);

                    if (response.isSuccessful()) {

                        List<Curso> cursosList = response.body();


                        for (Curso searchCourse : cursosList) {
                            int idCursoInt = searchCourse.getId();
                            String nomeCursoStr = searchCourse.getName();
                            if (nomeCursoStr.toLowerCase(Locale.ROOT).contains(nomeCurso.toLowerCase(Locale.ROOT))){
                                Curso courseMount = new Curso();
                                courseMount.setId(idCursoInt);
                                courseMount.setName(nomeCursoStr);

                                CoursesFound.add(courseMount);
                            }
                        }

                        adapter.setCursosList(CoursesFound);

                    } else {
                        Toast.makeText(getApplicationContext(), " Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<List<Curso>> call, Throwable t) {
                    viewBinding.pbLoading.setVisibility(View.GONE);

                    Log.e(CursosActivity.class.getSimpleName(), "Comunication error, " + t.getMessage());
                    Toast.makeText(getApplicationContext(), " Communication error with the server! ", Toast.LENGTH_SHORT).show();

                }
            });

        }else {
            CursoService service = retrofitConfig.getCursoService();
            service.getAll().enqueue(new Callback<List<Curso>>() {
                @Override
                public void onResponse(Call<List<Curso>> call, Response<List<Curso>> response) {
                    viewBinding.pbLoading.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        List<Curso> list = response.body();
                        adapter.setCursosList(list);

                    } else {
                        Toast.makeText(getApplicationContext(), " Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<List<Curso>> call, Throwable t) {
                    viewBinding.pbLoading.setVisibility(View.GONE);
                    Log.e(CursosActivity.class.getSimpleName(), "Comunication error, " + t.getMessage());
                    Toast.makeText(getApplicationContext(), " Communication error with the server! ", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


}