package br.com.professorallocation.service;

import java.util.List;

import br.com.professorallocation.model.Curso;
import br.com.professorallocation.model.DepartamentoPostDto;
import br.com.professorallocation.model.Professor;
import br.com.professorallocation.model.ProfessorPostDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProfessorService {

    @GET("professors")
    Call<List<Professor>> getAll();

    @GET("professors")
    Call<List<Professor>> getByName(@Query("name") String name);

    @POST("/professors")
    Call<Professor> cadProfessor(@Body ProfessorPostDto professor);

    @DELETE("/professors/{professor_id}")
    Call<Boolean> delete(@Path("professor_id")int id);

    @GET("/professors/{professor_id}")
    Call<Professor> getByID(@Path("professor_id")int id);

    @PUT("/professors/{professor_id}")
    Call<Professor> update(@Path("professor_id")int id, @Body ProfessorPostDto professor);

}
