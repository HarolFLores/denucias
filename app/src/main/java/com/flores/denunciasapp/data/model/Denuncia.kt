package com.flores.denunciasapp.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Denuncia(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("dni")
    val dni: String,
    @SerializedName("fecha")
    val fecha: String? = null,
    @SerializedName("tipo")
    val tipo: String,
    @SerializedName("direccion")
    val direccion: String,
    @SerializedName("descripcion")
    val descripcion: String,
    @SerializedName("estado")
    val estado: Int = 1 // 1: PENDIENTE, 0: ATENDIDA
) : Serializable
