package br.edu.puccampinas.pi3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class AvaliacoesAdapter(private val dataSet: List<Avaliacoes>) :
    ListAdapter<Avaliacoes, AvaliacoesAdapter.AvaliacaoViewHolder>(TarefaDiffCallback) {
    /* ViewHolder para a TAREFA, define as referencias das views e acrescenta o "click". */
    class AvaliacaoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val nomeavTextView: AppCompatTextView = itemView.findViewById(R.id.tvNomeAv)
        private val comentTextView: AppCompatTextView = itemView.findViewById(R.id.tvComentario)
        private var avaliacaoAtual: Avaliacoes? = null
        fun bind(t: Avaliacoes) {
            avaliacaoAtual = t
            nomeavTextView.text = t.nome
            comentTextView.text = t.comentario
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvaliacaoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.avaliacao_item, parent, false)
        return AvaliacaoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvaliacaoViewHolder, position: Int) {
        val t = dataSet[position]
        holder.bind(t)
    }

    override fun getItemCount() = dataSet.size
}
object TarefaDiffCallback : DiffUtil.ItemCallback<Avaliacoes>() {
    override fun areItemsTheSame(oldItem: Avaliacoes, newItem: Avaliacoes): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: Avaliacoes, newItem: Avaliacoes): Boolean {
        return oldItem.nome == newItem.nome
    }
}