package br.edu.puccampinas.pi3

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable

class EmergenciasAdapter(private val dataSet: List<Emergencia>):
    ListAdapter<Emergencia, EmergenciasAdapter.EmergenciaViewHolder>(EmergenciaDiffCallback) {
        class EmergenciaViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            private val EmergenciaTextView: AppCompatTextView = itemView.findViewById(R.id.tvNome)
            private var emergenciaAtual: Emergencia? = null
            private val TesteTextView: AppCompatTextView = itemView.findViewById(R.id.tvData)
            private val BlocoTextView: AppCompatTextView = itemView.findViewById(R.id.tvBloco)

            fun bind(t: Emergencia) {
                emergenciaAtual = t
                EmergenciaTextView.text = t.nome
                TesteTextView.text = t.dataHora
                BlocoTextView.setOnClickListener {
                    Toast.makeText(itemView.context, "Funcionou!!!!!!!!!!!!!!!!!!!!", Toast.LENGTH_SHORT).show()

                    val iEmergencia = Intent(itemView.context,EmergenciaActivity::class.java)
                    iEmergencia.putExtra("nome",t.nome)
                    iEmergencia.putExtra("telefone",t.telefone)
                    iEmergencia.putExtra("Foto1",t.foto1)
                    iEmergencia.putExtra("Foto2",t.foto2)
                    iEmergencia.putExtra("Foto3",t.foto3)
                    iEmergencia.putExtra("status",t.status)
                    iEmergencia.putExtra("dataHora",t.dataHora)
                    iEmergencia.putExtra("emergencia",t.emergencia)

                    itemView.context.startActivity(iEmergencia)
                }

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
