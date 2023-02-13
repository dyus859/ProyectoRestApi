package www.iesmurgi.proyectorestapi.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import www.iesmurgi.proyectorestapi.APIService
import www.iesmurgi.proyectorestapi.databinding.ActivityMainBinding
import www.iesmurgi.proyectorestapi.models.Emoji
import www.iesmurgi.proyectorestapi.models.EmojiAdapter

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: EmojiAdapter
    private val emojiList = mutableListOf<Emoji>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enlazamos el XML
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        binding.svEmojis.setOnQueryTextListener(this)

        searchByName("person")
    }

    private fun initRecyclerView() {
        adapter = EmojiAdapter(emojiList)
        binding.rvEmojis.adapter = adapter
        binding.rvEmojis.layoutManager = LinearLayoutManager(applicationContext)
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://emojihub.yurace.pro/api/all/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun searchByName(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java).getEmojis("$query")
            val body = call.body()

            runOnUiThread {
                if (call.isSuccessful) {
                    /*
                    emojiList.clear()
                    val emojis = body?.filter { it.name.contains(name, true) }
                    emojiList.addAll(emojis ?: listOf())
                    adapter.notifyDataSetChanged()
                     */
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

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()){
            searchByName(query.lowercase())
        }

        return true
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}