package com.satyamthakur.learning.retrofitwithcoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.satyamthakur.learning.retrofitwithcoroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

const val TAG = "TODOMAINACTIVITY"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var todoApdapter: TodoApdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        lifecycleScope.launch launchWhenCreated@{
            binding.progressBar.isVisible = true
            val response = try {
                RetrofitInstance.api.getTodos()
            } catch (e: IOException) {
                Log.e(TAG, "you might not be connected with the internet")
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, ${e.message()}")
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            }

            if (response.isSuccessful && response.body() != null) {
                todoApdapter.todos = response.body()!!
            } else {
                Log.e(TAG, "Request not successfull")
            }
            binding.progressBar.isVisible = false
        }

    }

    private fun setupRecyclerView() = binding.rvTodos.apply {
        todoApdapter = TodoApdapter()
        adapter = todoApdapter
        layoutManager = LinearLayoutManager(this@MainActivity)
    }
}