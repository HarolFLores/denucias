package com.flores.denunciasapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.flores.denunciasapp.databinding.ActivityMainBinding
import com.flores.denunciasapp.ui.adapter.DenunciaAdapter
import com.flores.denunciasapp.ui.viewmodel.DenunciaViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: DenunciaViewModel by viewModels()
    private lateinit var adapter: DenunciaAdapter

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
            adapter.updateList(list)
        }
        viewModel.error.observe(this) { errorMsg ->
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
        }
    }

    private fun setupListeners() {
        binding.btnIrRegistrar.setOnClickListener {
            val intent = Intent(this, RegistrarDenunciaActivity::class.java)
            startActivity(intent)
        }

        binding.btnIrBuscar.setOnClickListener {
            val intent = Intent(this, BuscarDniActivity::class.java)
            startActivity(intent)
        }

        binding.btnCargarSiguientes.setOnClickListener {
            viewModel.cargarSiguientes5()
        }
    }
}
