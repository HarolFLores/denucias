package com.flores.denunciasapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.flores.denunciasapp.databinding.ActivityBuscarDniBinding
import com.flores.denunciasapp.ui.adapter.DenunciaAdapter
import com.flores.denunciasapp.ui.viewmodel.DenunciaViewModel

class BuscarDniActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuscarDniBinding
    private val viewModel: DenunciaViewModel by viewModels()
    private lateinit var adapter: DenunciaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuscarDniBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        adapter = DenunciaAdapter(emptyList()) { denuncia ->
            val intent = Intent(this, DetalleDenunciaActivity::class.java).apply {
                putExtra("DENUNCIA", denuncia)
            }
            startActivity(intent)
        }
        binding.recyclerViewBusqueda.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewBusqueda.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.busquedaDenuncias.observe(this) { list ->
            adapter.updateList(list)
            val dni = binding.etBuscarDni.text.toString().trim()
            if (list.isEmpty()) {
                binding.tvResultadoInfo.text = "No hay denuncias asociadas a este DNI ($dni)"
                Toast.makeText(this, "No se encontraron denuncias para este DNI", Toast.LENGTH_SHORT).show()
            } else {
                binding.tvResultadoInfo.text = "Resultados para DNI: $dni • ${list.size} denuncias"
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

        binding.btnBuscar.setOnClickListener {
            val dni = binding.etBuscarDni.text.toString().trim()
            if (dni.length != 8 || !dni.all { it.isDigit() }) {
                binding.etBuscarDni.error = "Ingrese un DNI válido de 8 dígitos"
                binding.etBuscarDni.requestFocus()
                return@setOnClickListener
            }
            viewModel.buscarPorDni(dni)
        }
    }
}
