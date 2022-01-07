package br.com.professorallocation.config;

import br.com.professorallocation.service.AlocacaoService;
import br.com.professorallocation.service.CursoService;
import br.com.professorallocation.service.DepartamentoService;
import br.com.professorallocation.service.ProfessorService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {

    private Retrofit retrofit;

    public RetrofitConfig(){

        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://recode-6-professor-allocation.herokuapp.com/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

    }

    public CursoService getCursoService() { return this.retrofit.create(CursoService.class); }

    public DepartamentoService getDepartamentoService() { return this.retrofit.create(DepartamentoService.class); }

    public ProfessorService getProfessorService() { return this.retrofit.create(ProfessorService.class); }

    public AlocacaoService getAlocacaoService() { return this.retrofit.create(AlocacaoService.class); }

}
