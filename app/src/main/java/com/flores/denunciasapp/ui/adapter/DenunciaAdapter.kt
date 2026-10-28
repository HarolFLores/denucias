package com.flores.denunciasapp.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flores.denunciasapp.data.model.Denuncia
import com.flores.denunciasapp.databinding.ItemDenunciaBinding

class DenunciaAdapter(
    private var denuncias: List<Denuncia>,
    private val onItemClick: (Denuncia) -> Unit
) : RecyclerView.Adapter<DenunciaAdapter.DenunciaViewHolder>() {

    inner class DenunciaViewHolder(val binding: ItemDenunciaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(denuncia: Denuncia) {
            binding.tvTipo.text = "Tipo: ${denuncia.tipo}"
            binding.tvDni.text = "DNI: ${denuncia.dni}"
            binding.tvDireccion.text = "Dirección: ${denuncia.direccion}"
            binding.tvFecha.text = "Fecha: ${denuncia.fecha ?: "N/A"}"
            
            if (denuncia.estado == 1) {
                binding.tvEstado.text = "PENDIENTE"
                binding.tvEstado.setTextColor(Color.parseColor("#FFA500")) // Naranja
            } else {
                binding.tvEstado.text = "ATENDIDA"
                binding.tvEstado.setTextColor(Color.parseColor("#008000")) // Verde
            }

            binding.root.setOnClickListener {
                onItemClick(denuncia)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DenunciaViewHolder {
        val binding = ItemDenunciaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DenunciaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DenunciaViewHolder, position: Int) {
        holder.bind(denuncias[position])
    }

    override fun getItemCount(): Int = denuncias.size

    fun updateList(newList: List<Denuncia>) {
        denuncias = newList
        notifyDataSetChanged()
    }
}
