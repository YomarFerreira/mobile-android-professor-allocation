package br.com.professorallocation.view;

import static java.lang.Math.toIntExact;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.timepicker.TimeFormat;

import org.w3c.dom.Entity;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import br.com.professorallocation.R;
import br.com.professorallocation.config.RetrofitConfig;
import br.com.professorallocation.databinding.ActivityCadAlocacaoBinding;
import br.com.professorallocation.model.AlocacaoPostDto;
import br.com.professorallocation.model.Curso;
import br.com.professorallocation.model.Alocacao;
import br.com.professorallocation.model.Departamento;
import br.com.professorallocation.model.Professor;
import br.com.professorallocation.model.ProfessorPostDto;
import br.com.professorallocation.service.AlocacaoService;
import br.com.professorallocation.service.CursoService;
import br.com.professorallocation.service.DepartamentoService;
import br.com.professorallocation.service.ProfessorService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadAlocacaoActivity extends AppCompatActivity {

    private ActivityCadAlocacaoBinding viewBinding;
    private RetrofitConfig retrofitConfig;
    private int setProfsAloc, setCoursAloc, daySelSpin;
    private String daySelBD = "";
    private Professor setProf;
    private Curso setCurs;
    @Override
    protected void onResume() {
        requestCarregarlistaDias();
        requestCarregarlistaProfs();
        requestCarregarlistaCours();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityCadAlocacaoBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        retrofitConfig = new RetrofitConfig();

        if (getIntent().hasExtra(DetalhesAlocacaoActivity.KEY_ALLOCATION)) {

            getSupportActionBar().setTitle(getResources().getString(R.string.update_title_allocation));
            viewBinding.btCadAlocacao.setText(getResources().getString(R.string.update_button));


            Alocacao alocacao = (Alocacao) getIntent().getSerializableExtra(DetalhesAlocacaoActivity.KEY_ALLOCATION);

            if (alocacao != null) {

                this.daySelBD = alocacao.getDay();

                getSetTimePicker(alocacao.getStartHour(), viewBinding.tpStartHourAllocationActivity);
                getSetTimePicker(alocacao.getEndHour(), viewBinding.tpEndHourAllocationActivity);

                this.setProfsAloc = alocacao.getProfessor().getId();
                this.setCoursAloc = alocacao.getCourse().getId();

                viewBinding.btCadAlocacao.setOnClickListener(view-> {
                    AlocacaoPostDto alocacaoPut = new AlocacaoPostDto();

                    alocacaoPut.setDay(viewBinding.slDaysAllocationActivity.getSelectedItem().toString());
                    String hourStart = String.format(Locale.getDefault(),"%02d:%02d", viewBinding.tpStartHourAllocationActivity.getHour(), viewBinding.tpStartHourAllocationActivity.getMinute());
                    alocacaoPut.setStartHour(hourStart+"+0000");
                    String hourEnd = String.format(Locale.getDefault(),"%02d:%02d", viewBinding.tpEndHourAllocationActivity.getHour(), viewBinding.tpEndHourAllocationActivity.getMinute());
                    alocacaoPut.setEndHour(hourEnd+"+0000");
                    setProfsAloc = toIntExact((viewBinding.slProfessorAllocationActivity.getSelectedItemId()+1));
                    alocacaoPut.setProfessorId(setProfsAloc);
                    setCoursAloc = toIntExact((viewBinding.slCourseAllocationActivity.getSelectedItemId()+1));
                    alocacaoPut.setCourseId(setCoursAloc);

                    requestUpdateAlocacao(alocacao.getId(), alocacaoPut);
                });

            }
        }else{
            viewBinding.tpStartHourAllocationActivity.setIs24HourView(true);
            viewBinding.tpEndHourAllocationActivity.setIs24HourView(true);
            viewBinding.btCadAlocacao.setOnClickListener(view -> registerAlocacao());
        }

    }


    private void  getSetTimePicker(Date dateGet, TimePicker tpkrGet) {
        String hString,mString;
        int hInteger, mInteger;

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String startHourFormated = df.format(dateGet);
        hString = startHourFormated.substring(0,2);
        mString = startHourFormated.substring(3,5);
        hInteger = Integer.parseInt(hString);
        mInteger = Integer.parseInt(mString);

        tpkrGet.setIs24HourView(true);
        tpkrGet.setHour(hInteger);
        tpkrGet.setMinute(mInteger);
    }

    private void requestCarregarlistaProfs(){
        ProfessorService service = retrofitConfig.getProfessorService();
        service.getAll().enqueue(new Callback<List<Professor>>() {
            @Override
            public void onResponse(Call<List<Professor>> call, Response<List<Professor>> response) {

                if (response.isSuccessful()) {
                    List<Professor> listProf = response.body();

                    List<String> nomeProfessores = new ArrayList<>();
                    for (Professor prof : listProf) {
                        nomeProfessores.add(prof.getName());
                    }

                    setupSpnProf(nomeProfessores, listProf);


                    int i_foreach = 0;
                    for (Professor professor : listProf) {
                        Log.d(CadAlocacaoActivity.class.getSimpleName(), professor.getName());
                        if (professor.getId() == setProfsAloc) {
                            viewBinding.slProfessorAllocationActivity.setSelection(i_foreach);
                        }
                        i_foreach++;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), " Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Professor>> call, Throwable t) {
                Log.e(CadAlocacaoActivity.class.getSimpleName(), "Comunication error, " + t.getMessage());
                Toast.makeText(getApplicationContext(), " Communication error with the server! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpnProf(List<String> names, List<Professor> professores){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(this, R.layout.layout_spinner_list_item, names);
        spinnerAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown_item);
        viewBinding.slProfessorAllocationActivity.setAdapter(spinnerAdapter);

        viewBinding.slProfessorAllocationActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setProf = professores.get(position);
                Log.d(CadAlocacaoActivity.class.getSimpleName(), "selecionado " + setProf.getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void requestCarregarlistaCours(){
        CursoService service = retrofitConfig.getCursoService();
        service.getAll().enqueue(new Callback<List<Curso>>() {
            @Override
            public void onResponse(Call<List<Curso>> call, Response<List<Curso>> response) {

                if (response.isSuccessful()) {
                    List<Curso> listCours = response.body();

                    List<String> nomeCursos = new ArrayList<>();
                    for (Curso curs : listCours) {
                        nomeCursos.add(curs.getName());
                    }

                    setupSpnCours(nomeCursos, listCours);

                    int i_foreach = 0;
                    for (Curso curso : listCours) {
                        Log.d(CadAlocacaoActivity.class.getSimpleName(), curso.getName());
                        if (curso.getId() == setCoursAloc) {
                            viewBinding.slCourseAllocationActivity.setSelection(i_foreach);
                        }
                        i_foreach++;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), " Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Curso>> call, Throwable t) {
                Log.e(CadAlocacaoActivity.class.getSimpleName(), "Comunication error, " + t.getMessage());
                Toast.makeText(getApplicationContext(), " Communication error with the server! ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupSpnCours(List<String> names, List<Curso> cursos){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(this, R.layout.layout_spinner_list_item, names);
        spinnerAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown_item);
        viewBinding.slCourseAllocationActivity.setAdapter(spinnerAdapter);

        viewBinding.slCourseAllocationActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setCurs = cursos.get(position);
                Log.d(CadAlocacaoActivity.class.getSimpleName(), "selecionado " + setCurs.getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void requestCarregarlistaDias(){
        String[] lsDayOfWeek = getResources().getStringArray(R.array.ListaDiasSemana);
        viewBinding.slDaysAllocationActivity.setAdapter(new ArrayAdapter<String>(getBaseContext(), R.layout.layout_spinner_list_item,  lsDayOfWeek));
        viewBinding.slDaysAllocationActivity.setLayoutMode(R.layout.layout_spinner_dropdown_item);

             if (daySelBD.equals("SUNDAY")) { daySelSpin = 0; }
        else if (daySelBD.equals("MONDAY")) { daySelSpin = 1; }
        else if (daySelBD.equals("TUESDAY")) { daySelSpin = 2; }
        else if (daySelBD.equals("WEDNESDAY")) { daySelSpin = 3; }
        else if (daySelBD.equals("THURSDAY")) { daySelSpin = 4; }
        else if (daySelBD.equals("FRIDAY")) { daySelSpin = 5; }
        else if (daySelBD.equals("SATURDAY")){ daySelSpin = 6; }
        else { daySelSpin = 0; };

        viewBinding.slDaysAllocationActivity.setSelection(daySelSpin);

    }

    private void requestUpdateAlocacao(int idAlocacao, AlocacaoPostDto alocacao){

        RetrofitConfig config = new RetrofitConfig();
        AlocacaoService service = config.getAlocacaoService();

        service.update(idAlocacao, alocacao).enqueue(new Callback<Alocacao>() {
            @Override
            public void onResponse(Call<Alocacao> call, Response<Alocacao> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Edit performed successfully", Toast.LENGTH_SHORT).show();
                    finish();

                }else{
                    Toast.makeText(getApplicationContext()," Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Alocacao> call, Throwable t) {
                Log.e(CadAlocacaoActivity.class.getSimpleName(),"Comunication error, " + t.getMessage());
                Toast.makeText(getApplicationContext()," Communication error with the server! ", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void registerAlocacao() {
        String alocacaoDay = viewBinding.slDaysAllocationActivity.getSelectedItem().toString();

        String hourStart = String.format(Locale.getDefault(),"%02d:%02d", viewBinding.tpStartHourAllocationActivity.getHour(), viewBinding.tpStartHourAllocationActivity.getMinute());
        String alocacaoStart = hourStart+"+0000";

        String hourEnd = String.format(Locale.getDefault(),"%02d:%02d", viewBinding.tpEndHourAllocationActivity.getHour(), viewBinding.tpEndHourAllocationActivity.getMinute());
        String alocacaoEnd = hourEnd+"+0000";

        int alocacaoProf = toIntExact((viewBinding.slProfessorAllocationActivity.getSelectedItemId()+1));

        int alocacaoCurs = toIntExact((viewBinding.slCourseAllocationActivity.getSelectedItemId()+1));

        AlocacaoPostDto alocacao = new AlocacaoPostDto();
        alocacao.setDay(alocacaoDay);
        alocacao.setStartHour(alocacaoStart);
        alocacao.setEndHour(alocacaoEnd);
        alocacao.setProfessorId(alocacaoProf);
        alocacao.setCourseId(alocacaoCurs);

        RetrofitConfig config = new RetrofitConfig();
        AlocacaoService service = config.getAlocacaoService();

        service.cadAlocacao(alocacao).enqueue(new Callback<Alocacao>() {
            @Override
            public void onResponse(Call<Alocacao> call, Response<Alocacao> response) {

                Alocacao alocacaoIdShow = response.body();

                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "The request was successful - ID. " + alocacaoIdShow.getId(), Toast.LENGTH_SHORT).show();
                    viewBinding.slDaysAllocationActivity.setSelection(0);
                    viewBinding.tpStartHourAllocationActivity.setHour(0);
                    viewBinding.tpStartHourAllocationActivity.setMinute(0);
                    viewBinding.tpEndHourAllocationActivity.setHour(0);
                    viewBinding.tpEndHourAllocationActivity.setMinute(0);
                    viewBinding.slProfessorAllocationActivity.setSelection(0);
                    viewBinding.slCourseAllocationActivity.setSelection(0);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext()," Error: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Alocacao> call, Throwable t) {
                Log.e(CadAlocacaoActivity.class.getSimpleName(),"Comunication error, " + t.getMessage());
                Toast.makeText(getApplicationContext()," Communication error with the server! ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}