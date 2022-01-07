package br.com.professorallocation.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import br.com.professorallocation.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        setUpViewCurso();
        setUpViewDepartamento();
        setUpViewProfessor();
        setUpViewAlocacao();

    }

    private void setUpViewCurso() {
        viewBinding.llContentCourse.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), CursosActivity.class);
            startActivity(intent);

        });
    }

    private void setUpViewDepartamento() {
        viewBinding.llContentDepartment.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), DepartamentosActivity.class);
            startActivity(intent);

        });
    }

    private void setUpViewProfessor() {
        viewBinding.llContentProfessor.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), ProfessoresActivity.class);
            startActivity(intent);

        });
    }

    private void setUpViewAlocacao() {
        viewBinding.llContentAllocation.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), AlocacoesActivity.class);
            startActivity(intent);

        });
    }

}