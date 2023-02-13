package www.iesmurgi.proyectorestapi.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import www.iesmurgi.proyectorestapi.APIService
import www.iesmurgi.proyectorestapi.databinding.ActivityMainBinding
import www.iesmurgi.proyectorestapi.models.Article
import www.iesmurgi.proyectorestapi.models.ArticleAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ArticleAdapter
    private val articleList = mutableListOf<Article>()

    private val FIRST_NEWS_NUMBER = 10
    private val ADD_MORE_NEWS = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enlazamos el XML
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        getFirstN(FIRST_NEWS_NUMBER)
    }

    private fun initRecyclerView() {
        adapter = ArticleAdapter(this, articleList)
        binding.rvArticles.adapter = adapter
        binding.rvArticles.layoutManager = LinearLayoutManager(applicationContext)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.rvArticles.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (!binding.rvArticles.canScrollVertically(1)) {
                    addNextN(ADD_MORE_NEWS)
                }
            }
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.spaceflightnewsapi.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getFirstN(number: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java)
                .getArticles("/v3/articles?_limit=$number")
            val articles = call.body()

            runOnUiThread {
                if (call.isSuccessful) {
                    articleList.clear()

                    if (articles != null) {
                        articleList.addAll(articles)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    showError()
                }

                hideKeyboard()
            }
        }
    }

    private fun addNextN(number: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java)
                .getArticles("/v3/articles?_limit=$number&_start=${articleList.size}")
            val articles = call.body()

            runOnUiThread {
                if (call.isSuccessful) {
                    if (articles != null) {
                        articleList.addAll(articles)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    showError()
                }

                hideKeyboard()
            }
        }
    }

    private fun showError() {
        Toast.makeText(
            this,
            "Ha ocurrido un error",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}