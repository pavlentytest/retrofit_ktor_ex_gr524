package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.gson.annotations.SerializedName
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class Answer (
    @SerializedName("endOfWord") var endOfWord: Boolean? = null,
    @SerializedName("pos") var pos: Int? = null,
    @SerializedName("text") var text : ArrayList<String> = arrayListOf()
)

class MainActivity : AppCompatActivity() {

    companion object {
        const val BASE_URL = "https://predictor.yandex.net/"
        const val DIR_URL = "api/v1/predict.json/complete"
        const val KEY = "pdct.1.1.20240425T061733Z.fb512d364fe7cb18.428dd8c4f034a6692dfe1b162ec902b211372e20"
        const val LANG = "en"
        const val LIMIT = 5
    }

    lateinit var binding: ActivityMainBinding
    lateinit var retrofit: Retrofit
    lateinit var yandexAPI: YandexAPI
    lateinit var ktorclient: HttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        yandexAPI = retrofit.create(YandexAPI::class.java)
        ktorclient = HttpClient{
            install(ContentNegotiation){
                gson()
            }
        }
        binding.editText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable?) {
                ktorRequest()
            }
        })
    }

    fun ktorRequest() {
        runBlocking {
            val request =
                ktorclient.get("$BASE_URL$DIR_URL?key=$KEY&q=${binding.editText.text}&limit=$LIMIT&lang=$LANG")
            binding.textView.text = request.body<Answer>().text.joinToString("\n")
        }
    }

    fun retrofitRequest() {
        yandexAPI.complete(KEY,binding.editText.text.toString(),LANG,LIMIT).enqueue(object: Callback<Answer>{
            override fun onResponse(p0: Call<Answer>, p1: Response<Answer>) {
                if(p1.isSuccessful) {
                    binding.textView.text = p1.body()!!.text.joinToString("\n")
                }
            }
            override fun onFailure(p0: Call<Answer>, p1: Throwable) {
                Log.d("RRR",p1.message.toString())
            }
        })
    }


}