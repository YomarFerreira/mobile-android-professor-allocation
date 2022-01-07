package br.com.professorallocation.service;

import java.util.List;

import br.com.professorallocation.model.Alocacao;
import br.com.professorallocation.model.AlocacaoPostDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AlocacaoService {

    @GET("allocations")
    Call<List<Alocacao>> getAll();

    @POST("/allocations")
    Call<Alocacao> cadAlocacao(@Body AlocacaoPostDto alocacao);

    @DELETE("/allocations/{alocation_id}")
    Call<Boolean> delete(@Path("alocation_id")int id);

    @GET("/allocations/{alocation_id}")
    Call<Alocacao> getByID(@Path("alocation_id")int id);

    @PUT("/allocations/{alocation_id}")
    Call<Alocacao> update(@Path("alocation_id")int id, @Body AlocacaoPostDto alocacao);

}
