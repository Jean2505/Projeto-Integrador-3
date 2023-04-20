package br.edu.puccampinas.pi3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class EmergenciasAdapter(private val dataSet: List<Emergencia>):
    ListAdapter<Emergencia, EmergenciasAdapter.EmergenciaViewHolder>(EmergenciaDiffCallback) {
        class EmergenciaViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            private val EmergenciaTextView: AppCompatTextView = itemView.findViewById(R.id.tvEmergencia)
            private var emergenciaAtual: Emergencia? = null

            fun bind(t: Emergencia) {
                emergenciaAtual = t
                EmergenciaTextView.text = t.nome
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergenciaViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.emergencia_item, parent, false)
            return EmergenciaViewHolder(view)
        }
        /* Obtem a tarefa e a utiliza como "viewholder". */
        override fun onBindViewHolder(holder: EmergenciaViewHolder, position: Int) {
            val t = dataSet[position]
            holder.bind(t)
        }
        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size
    }
    object EmergenciaDiffCallback : DiffUtil.ItemCallback<Emergencia>() {
        override fun areItemsTheSame(oldItem: Emergencia, newItem: Emergencia): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Emergencia, newItem: Emergencia): Boolean {
            return oldItem.nome == newItem.nome
        }
    }
