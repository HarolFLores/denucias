package com.flores.denunciasapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.flores.denunciasapp.R
import com.flores.denunciasapp.data.model.Denuncia
import com.flores.denunciasapp.databinding.ItemDenunciaBinding

class DenunciaAdapter(
    private var denuncias: List<Denuncia>,
    private val onItemClick: (Denuncia) -> Unit
) : RecyclerView.Adapter<DenunciaAdapter.DenunciaViewHolder>() {

    inner class DenunciaViewHolder(val binding: ItemDenunciaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(denuncia: Denuncia) {
            binding.tvTipo.text = denuncia.tipo
            binding.tvDniFecha.text = "DNI: ${denuncia.dni} • ${denuncia.fecha ?: "Sin fecha"}"
            binding.tvDireccion.text = "📍 ${denuncia.direccion}"

            if (denuncia.estado == 1) {
                binding.tvEstadoBadge.text = "PENDIENTE"
                binding.tvEstadoBadge.background = ContextCompat.getDrawable(binding.root.context, R.drawable.bg_chip_pendiente)
                binding.tvEstadoBadge.setTextColor(ContextCompat.getColor(binding.root.context, R.color.badge_pendiente_text))
            } else {
                binding.tvEstadoBadge.text = "ATENDIDA"
                binding.tvEstadoBadge.background = ContextCompat.getDrawable(binding.root.context, R.drawable.bg_chip_atendida)
                binding.tvEstadoBadge.setTextColor(ContextCompat.getColor(binding.root.context, R.color.badge_atendida_text))
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
