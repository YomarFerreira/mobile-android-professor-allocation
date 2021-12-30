package br.com.professorallocation.service;

import java.util.List;

import br.com.professorallocation.model.Curso;
import br.com.professorallocation.model.CursoPostDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CursoService {

    @GET("courses")
    Call<List<Curso>> getAll();

    @POST("/courses")
    Call<Curso> cadCurso(@Body CursoPostDto curso);

    @DELETE("/courses/{course_id}")
    Call<Boolean> delete(@Path("course_id")int id);

    @GET("/courses/{course_id}")
    Call<Curso> getByID(@Path("course_id")int id);

    @PUT("/courses/{course_id}")
    Call<Curso> update(@Path("course_id")int id, @Body CursoPostDto curso);

}
