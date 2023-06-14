package br.edu.puccampinas.pi3

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class AvaliacoesAdapter(private val dataSet: List<Avaliacoes>) :
    ListAdapter<Avaliacoes, AvaliacoesAdapter.AvaliacaoViewHolder>(TarefaDiffCallback) {
    /* ViewHolder para a TAREFA, define as referencias das views e acrescenta o "click". */
    class AvaliacaoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val denunciaButton: Button = itemView.findViewById(R.id.btnDenuncia)
        private val notaImageView:  ImageView = itemView.findViewById(R.id.ivNota)
        private val nomeavTextView: AppCompatTextView = itemView.findViewById(R.id.tvNomeAv)
        private val comentTextView: AppCompatTextView = itemView.findViewById(R.id.tvComentario)
        private val notaTextView: AppCompatTextView = itemView.findViewById(R.id.tvNota)
        private var avaliacaoAtual: Avaliacoes? = null
        fun bind(t: Avaliacoes) {
            avaliacaoAtual = t
            if( t.estrela.toString() == "0"){
                notaImageView.setImageResource(R.drawable.dente0)
            } else if (t.estrela.toString() == "1"){
                notaImageView.setImageResource(R.drawable.dente1)
            } else if (t.estrela.toString() == "2"){
                notaImageView.setImageResource(R.drawable.dente2)
            } else if (t.estrela.toString() == "3"){
                notaImageView.setImageResource(R.drawable.dente3)
            } else if (t.estrela.toString() == "4"){
                notaImageView.setImageResource(R.drawable.dente4)
            } else if (t.estrela.toString() == "5"){
                notaImageView.setImageResource(R.drawable.dente5)
            }

            nomeavTextView.text = t.nome
            comentTextView.text = t.comentario
            notaTextView.text = "${t.estrela}/5"

            denunciaButton.setOnClickListener {
                val iDenuncia = Intent(itemView.context,DenunciaActivity::class.java)
                iDenuncia.putExtra("comentario", t.comentario)
                iDenuncia.putExtra("nome", t.nome)
                itemView.context.startActivity(iDenuncia)
            }
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