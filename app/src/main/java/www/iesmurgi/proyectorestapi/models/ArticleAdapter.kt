package www.iesmurgi.proyectorestapi.models

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import www.iesmurgi.proyectorestapi.databinding.ArticleItemBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ArticleAdapter(
    private val context: Context,
    private val articleList: MutableList<Article>
) : RecyclerView.Adapter<ArticleAdapter.AlumnoViewHolder>() {

    inner class AlumnoViewHolder(private val itemBinding : ArticleItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(article: Article) = with(itemBinding) {
            tvTitle.text = article.title

            Glide.with(context)
                .load(article.imageUrl)
                .into(ivArticle)

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")

            try {
                val date = inputFormat.parse(article.publishedAt) as Date
                val formattedDate: String = outputFormat.format(date)

                tvSitePublishedAt.text = String.format("%s | %s", article.newsSite, formattedDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            // Clic corto -> abrimos la noticia en Google
            this.root.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }

            // Clic largo -> resumen de la noticia
            this.root.setOnLongClickListener {
                Toast.makeText(
                    context,
                    article.summary,
                    Toast.LENGTH_SHORT
                ).show()

                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnoViewHolder {
        val itemBinding = ArticleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlumnoViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: AlumnoViewHolder, position: Int) {
        val article = articleList[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int = articleList.size

}