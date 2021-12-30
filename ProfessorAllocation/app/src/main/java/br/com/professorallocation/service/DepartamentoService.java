package br.com.professorallocation.service;

import java.util.List;

import br.com.professorallocation.model.Departamento;
import br.com.professorallocation.model.DepartamentoPostDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DepartamentoService {

    @GET("departments")
    Call<List<Departamento>> getAll();

    @POST("/departments")
    Call<Departamento> cadDepartamento(@Body DepartamentoPostDto departamento);

    @DELETE("/departments/{department_id}")
    Call<Boolean> delete(@Path("department_id")int id);

    @GET("/departments/{department_id}")
    Call<Departamento> getByID(@Path("department_id")int id);

    @PUT("/departments/{department_id}")
    Call<Departamento> update(@Path("department_id")int id, @Body DepartamentoPostDto departamento);

}
