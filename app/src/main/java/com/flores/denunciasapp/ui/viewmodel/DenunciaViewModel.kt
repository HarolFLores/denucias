package com.flores.denunciasapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flores.denunciasapp.data.model.Denuncia
import com.flores.denunciasapp.data.repository.DenunciaRepository
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.io.IOException

class DenunciaViewModel : ViewModel() {
    private val repository = DenunciaRepository()

    private val _denuncias = MutableLiveData<List<Denuncia>>()
    val denuncias: LiveData<List<Denuncia>> get() = _denuncias

    private val _busquedaDenuncias = MutableLiveData<List<Denuncia>>()
    val busquedaDenuncias: LiveData<List<Denuncia>> get() = _busquedaDenuncias

    private val _registroResult = MutableLiveData<Pair<Boolean, String>>()
    val registroResult: LiveData<Pair<Boolean, String>> get() = _registroResult

    private val _atenderResult = MutableLiveData<Boolean>()
    val atenderResult: LiveData<Boolean> get() = _atenderResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private var currentOffset = 0
    private val limit = 5

    fun cargarPrimerasDenuncias() {
        currentOffset = 0
        cargarDenunciasPaginadas(currentOffset, limit, true)
    }

    fun cargarSiguientes5() {
        currentOffset += limit
        cargarDenunciasPaginadas(currentOffset, limit, false)
    }

    private fun cargarDenunciasPaginadas(offset: Int, limit: Int, isRefresh: Boolean) {
        viewModelScope.launch {
            try {
                val response = repository.obtenerDenunciasPaginadas(offset, limit)
                if (response.isSuccessful) {
                    response.body()?.let { newList ->
                        val currentList = if (isRefresh) {
                            mutableListOf()
                        } else {
                            _denuncias.value.orEmpty().toMutableList()
                        }
                        currentList.addAll(newList)
                        _denuncias.value = currentList
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    _error.value = "Error al listar (${response.code()}): $errorBody"
                }
            } catch (e: SocketTimeoutException) {
                _error.value = "Error de red: Tiempo de espera agotado."
            } catch (e: IOException) {
                _error.value = "Error de red: No se pudo conectar al servidor."
            } catch (e: Exception) {
                _error.value = "Excepción: ${e.localizedMessage}"
            }
        }
    }

    fun registrar(denuncia: Denuncia) {
        viewModelScope.launch {
            try {
                val response = repository.registrarDenuncia(denuncia)
                if (response.isSuccessful) {
                    val created = response.body()
                    val idMsg = created?.id?.let { " con código #$it" } ?: ""
                    _registroResult.value = Pair(true, "¡Genial! Denuncia registrada exitosamente$idMsg")
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Sin detalles"
                    _registroResult.value = Pair(false, "Error al registrar [${response.code()}]: $errorBody")
                }
            } catch (e: SocketTimeoutException) {
                _registroResult.value = Pair(false, "Error de red: Tiempo de espera agotado.")
            } catch (e: IOException) {
                _registroResult.value = Pair(false, "Error de red: No se pudo conectar al servidor.")
            } catch (e: Exception) {
                _registroResult.value = Pair(false, "Excepción: ${e.localizedMessage}")
            }
        }
    }

    fun atender(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.atenderDenuncia(id)
                if (response.isSuccessful) {
                    _atenderResult.value = true
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    _error.value = "Error al atender [${response.code()}]: $errorBody"
                    _atenderResult.value = false
                }
            } catch (e: SocketTimeoutException) {
                _error.value = "Error de red: Tiempo de espera agotado."
                _atenderResult.value = false
            } catch (e: IOException) {
                _error.value = "Error de red: No se pudo conectar al servidor."
                _atenderResult.value = false
            } catch (e: Exception) {
                _error.value = "Excepción: ${e.localizedMessage}"
                _atenderResult.value = false
            }
        }
    }

    fun buscarPorDni(dni: String) {
        viewModelScope.launch {
            try {
                val response = repository.obtenerDenunciasPorDni(dni)
                if (response.isSuccessful) {
                    _busquedaDenuncias.value = response.body() ?: emptyList()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    _error.value = "Error en búsqueda [${response.code()}]: $errorBody"
                }
            } catch (e: SocketTimeoutException) {
                _error.value = "Error de red: Tiempo de espera agotado."
            } catch (e: IOException) {
                _error.value = "Error de red: No se pudo conectar al servidor."
            } catch (e: Exception) {
                _error.value = "Excepción: ${e.localizedMessage}"
            }
        }
    }
}
