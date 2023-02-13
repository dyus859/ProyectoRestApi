package www.iesmurgi.proyectorestapi.models

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import www.iesmurgi.proyectorestapi.databinding.EmojiItemBinding

class EmojiAdapter(
    private val emojiList: MutableList<Emoji>
) : RecyclerView.Adapter<EmojiAdapter.AlumnoViewHolder>() {

    inner class AlumnoViewHolder(private val itemBinding : EmojiItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(emoji: Emoji) = with(itemBinding) {
            tvEmoji.text = Html.fromHtml(emoji.htmlCode[0])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnoViewHolder {
        val itemBinding = EmojiItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlumnoViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: AlumnoViewHolder, position: Int) {
        val emoji = emojiList[position]
        holder.bind(emoji)
    }

    override fun getItemCount(): Int = emojiList.size

}