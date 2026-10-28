package com.flores.denunciasapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.flores.denunciasapp.data.model.Denuncia
import com.flores.denunciasapp.databinding.ActivityMainBinding
import com.flores.denunciasapp.ui.adapter.DenunciaAdapter
import com.flores.denunciasapp.ui.viewmodel.DenunciaViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: DenunciaViewModel by viewModels()
    private lateinit var adapter: DenunciaAdapter
    private var allDenuncias: List<Denuncia> = emptyList()
    private var currentFilter: Int = -1 // -1: Todos, 1: Pendientes, 0: Atendidas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
        setupListeners()

        viewModel.cargarPrimerasDenuncias()
    }

    override fun onResume() {
        super.onResume()
        viewModel.cargarPrimerasDenuncias()
    }

    private fun setupRecyclerView() {
        adapter = DenunciaAdapter(emptyList()) { denuncia ->
            val intent = Intent(this, DetalleDenunciaActivity::class.java).apply {
                putExtra("DENUNCIA", denuncia)
            }
            startActivity(intent)
        }
        binding.recyclerViewDenuncias.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewDenuncias.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.denuncias.observe(this) { list ->
            allDenuncias = list
            applyFilter()
        }
        viewModel.error.observe(this) { errorMsg ->
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
        }
    }

    private fun applyFilter() {
        val filtered = when (currentFilter) {
            1 -> allDenuncias.filter { it.estado == 1 }
            0 -> allDenuncias.filter { it.estado == 0 }
            else -> allDenuncias
        }
        adapter.updateList(filtered)
        binding.tvTotalVisible.text = "Total visible: ${filtered.size} casos"
    }

    private fun setupListeners() {
        binding.btnNuevaDenuncia.setOnClickListener {
            startActivity(Intent(this, RegistrarDenunciaActivity::class.java))
        }

        binding.btnSearchAction.setOnClickListener {
            startActivity(Intent(this, BuscarDniActivity::class.java))
        }

        binding.btnCargarSiguientes.setOnClickListener {
            viewModel.cargarSiguientes5()
        }

        binding.chipGroupFiltros.setOnCheckedChangeListener { _, checkedId ->
            currentFilter = when (checkedId) {
                binding.chipPendientes.id -> 1
                binding.chipAtendidas.id -> 0
                else -> -1 // Todos
            }
            applyFilter()
        }
    }
}
