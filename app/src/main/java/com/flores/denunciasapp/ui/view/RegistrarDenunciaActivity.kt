package com.flores.denunciasapp.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.flores.denunciasapp.data.model.Denuncia
import com.flores.denunciasapp.databinding.ActivityRegistrarDenunciaBinding
import com.flores.denunciasapp.ui.viewmodel.DenunciaViewModel

class RegistrarDenunciaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarDenunciaBinding
    private val viewModel: DenunciaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarDenunciaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.registroResult.observe(this) { (success, message) ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            if (success) {
                finish()
            }
        }
    }

    private fun setupListeners() {
        binding.btnGuardar.setOnClickListener {
            val dni = binding.etDni.text.toString().trim()
            val tipo = binding.etTipo.text.toString().trim()
            val direccion = binding.etDireccion.text.toString().trim()
            val descripcion = binding.etDescripcion.text.toString().trim()

            if (dni.isEmpty() || dni.length != 8 || !dni.all { it.isDigit() }) {
                binding.etDni.error = "El DNI debe tener exactamente 8 dígitos numéricos"
                binding.etDni.requestFocus()
                return@setOnClickListener
            }
            if (tipo.isEmpty()) {
                binding.etTipo.error = "Ingrese el tipo de denuncia"
                binding.etTipo.requestFocus()
                return@setOnClickListener
            }
            if (direccion.isEmpty() || direccion.length > 200) {
                binding.etDireccion.error = "Dirección requerida (máx 200 caracteres)"
                binding.etDireccion.requestFocus()
                return@setOnClickListener
            }
            if (descripcion.isEmpty() || descripcion.length > 255) {
                binding.etDescripcion.error = "Descripción requerida (máx 255 caracteres)"
                binding.etDescripcion.requestFocus()
                return@setOnClickListener
            }

            val denuncia = Denuncia(
                id = null,
                dni = dni,
                fecha = null,
                tipo = tipo,
                direccion = direccion,
                descripcion = descripcion,
                estado = 1
            )

            viewModel.registrar(denuncia)
        }
    }
}
