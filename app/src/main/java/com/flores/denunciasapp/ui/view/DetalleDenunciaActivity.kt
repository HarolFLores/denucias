package com.flores.denunciasapp.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.flores.denunciasapp.R
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
        binding.tvIdTipo.text = "#${d.id ?: "N/A"} - ${d.tipo}"
        binding.tvFecha.text = "Fecha: ${d.fecha ?: "Sin fecha"}"
        binding.tvDni.text = "${d.dni} (Validado)"
        binding.tvDireccion.text = "📍 ${d.direccion}"
        binding.tvDescripcion.text = d.descripcion

        updateEstadoUI(d.estado)
    }

    private fun updateEstadoUI(estado: Int) {
        if (estado == 1) {
            binding.tvEstado.text = "PENDIENTE"
            binding.tvEstado.background = ContextCompat.getDrawable(this, R.drawable.bg_chip_pendiente)
            binding.tvEstado.setTextColor(ContextCompat.getColor(this, R.color.badge_pendiente_text))

            binding.btnAtender.isEnabled = true
            binding.btnAtender.text = "Marcar como Atendida"
            binding.btnAtender.backgroundTintList = ContextCompat.getColorStateList(this, R.color.badge_atendida_text)
        } else {
            binding.tvEstado.text = "ATENDIDA"
            binding.tvEstado.background = ContextCompat.getDrawable(this, R.drawable.bg_chip_atendida)
            binding.tvEstado.setTextColor(ContextCompat.getColor(this, R.color.badge_atendida_text))

            binding.btnAtender.isEnabled = false
            binding.btnAtender.text = "Denuncia Ya Atendida"
            binding.btnAtender.backgroundTintList = ContextCompat.getColorStateList(this, R.color.text_muted)
        }
    }

    private fun setupObservers() {
        viewModel.atenderResult.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Denuncia marcada como atendida exitosamente", Toast.LENGTH_SHORT).show()
                denuncia = denuncia?.copy(estado = 0)
                denuncia?.let { updateEstadoUI(it.estado) }
            } else {
                Toast.makeText(this, "Error al atender la denuncia", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.error.observe(this) { errorMsg ->
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnAtender.setOnClickListener {
            denuncia?.id?.let { id ->
                viewModel.atender(id)
            } ?: Toast.makeText(this, "ID de denuncia inválido", Toast.LENGTH_SHORT).show()
        }
    }
}
