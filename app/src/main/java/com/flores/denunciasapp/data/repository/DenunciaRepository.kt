package com.flores.denunciasapp.data.repository

import com.flores.denunciasapp.data.model.Denuncia
import com.flores.denunciasapp.data.network.RetrofitClient
import retrofit2.Response

class DenunciaRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun registrarDenuncia(denuncia: Denuncia): Response<Denuncia> {
        return apiService.registrarDenuncia(denuncia)
    }

    suspend fun obtenerDenunciasPaginadas(offset: Int, limit: Int = 5): Response<List<Denuncia>> {
        return apiService.obtenerDenunciasPaginadas(offset, limit)
    }

    suspend fun obtenerDenunciaPorId(id: Int): Response<Denuncia> {
        return apiService.obtenerDenunciaPorId(id)
    }

    suspend fun obtenerDenunciasPorDni(dni: String): Response<List<Denuncia>> {
        return apiService.obtenerDenunciasPorDni(dni)
    }

    suspend fun atenderDenuncia(id: Int): Response<Denuncia> {
        return apiService.atenderDenuncia(id)
    }
}
