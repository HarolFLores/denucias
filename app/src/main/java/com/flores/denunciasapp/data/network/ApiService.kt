package com.flores.denunciasapp.data.network

import com.flores.denunciasapp.data.model.Denuncia
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("denuncias/")
    suspend fun registrarDenuncia(@Body denuncia: Denuncia): Response<Denuncia>

    @GET("denuncias/")
    suspend fun obtenerDenunciasPaginadas(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 5
    ): Response<List<Denuncia>>

    @GET("denuncias/{id}")
    suspend fun obtenerDenunciaPorId(@Path("id") id: Int): Response<Denuncia>

    @GET("denuncias/ciudadano/{dni}")
    suspend fun obtenerDenunciasPorDni(@Path("dni") dni: String): Response<List<Denuncia>>

    @PUT("denuncias/{id}")
    suspend fun atenderDenuncia(@Path("id") id: Int): Response<Denuncia>
}
