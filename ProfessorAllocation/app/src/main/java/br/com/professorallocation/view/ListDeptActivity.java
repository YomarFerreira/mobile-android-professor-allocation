package br.com.professorallocation.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.com.professorallocation.R;
import br.com.professorallocation.config.RetrofitConfig;
import br.com.professorallocation.databinding.ActivityCadProfessorBinding;


public class ListDeptActivity extends AppCompatActivity {

    private @NonNull ActivityCadProfessorBinding viewBinding;
    private RetrofitConfig retrofitConfig;
    private ListDeptAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityCadProfessorBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        this.adapter = new ListDeptAdapter();

        viewBinding.rvListaDepartamentos.setAdapter(this.adapter);

        retrofitConfig = new RetrofitConfig();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_register, menu);
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







}