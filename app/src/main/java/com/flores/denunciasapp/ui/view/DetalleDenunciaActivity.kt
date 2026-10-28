package com.flores.denunciasapp.ui.view

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.flores.denunciasapp.data.model.Denuncia
import com.flores.denunciasapp.databinding.ActivityDetalleDenunciaBinding
import com.flores.denunciasapp.ui.viewmodel.DenunciaViewModel

class DetalleDenunciaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleDenunciaBinding
    private val viewModel: DenunciaViewModel by viewModels()
    private var denuncia: Denuncia? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleDenunciaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        denuncia = intent.extras?.getSerializable("DENUNCIA") as? Denuncia
        denuncia?.let { displayDetails(it) }

        setupObservers()
        setupListeners()
    }

    private fun displayDetails(d: Denuncia) {
        binding.tvId.text = "ID: ${d.id ?: "N/A"}"
        binding.tvDni.text = "DNI: ${d.dni}"
        binding.tvTipo.text = "Tipo: ${d.tipo}"
        binding.tvDireccion.text = "Dirección: ${d.direccion}"
        binding.tvDescripcion.text = "Descripción: ${d.descripcion}"
        binding.tvFecha.text = "Fecha: ${d.fecha ?: "N/A"}"

        updateEstadoUI(d.estado)
    }

    private fun updateEstadoUI(estado: Int) {
        if (estado == 1) {
            binding.tvEstado.text = "Estado: PENDIENTE"
            binding.tvEstado.setTextColor(Color.parseColor("#FFA500"))
            binding.btnAtender.isEnabled = true
        } else {
            binding.tvEstado.text = "Estado: ATENDIDA"
            binding.tvEstado.setTextColor(Color.parseColor("#008000"))
            binding.btnAtender.isEnabled = false
            binding.btnAtender.text = "Atendida"
        }
    }

    private fun setupObservers() {
        viewModel.atenderResult.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Denuncia atendida exitosamente", Toast.LENGTH_SHORT).show()
                updateEstadoUI(0)
            } else {
                Toast.makeText(this, "Error al atender denuncia", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.error.observe(this) { errorMsg ->
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
        }
    }

    private fun setupListeners() {
        binding.btnAtender.setOnClickListener {
            denuncia?.id?.let { id ->
                viewModel.atender(id)
            } ?: Toast.makeText(this, "ID de denuncia inválido", Toast.LENGTH_SHORT).show()
        }
    }
}
