package com.meridiane.notepadroomlibrary.activity

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.meridiane.notepadroomlibrary.dateAndTime.Calendar
import com.meridiane.notepadroomlibrary.R
import com.meridiane.notepadroomlibrary.adapter.MainAdapter
import com.meridiane.notepadroomlibrary.databinding.ActivityMainBinding
import com.meridiane.notepadroomlibrary.db.MainApp
import com.meridiane.notepadroomlibrary.viewModel.MainViewModel
import com.meridiane.notepadroomlibrary.viewModel.MainViewModelFactory
import java.util.*


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding?= null
    private val binding get() = _binding!!

    private lateinit var  mainViewModel: MainViewModel

    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRcView()

        mainViewModel = ViewModelProvider(this,
            MainViewModelFactory((this.applicationContext as MainApp).database))[MainViewModel::class.java]

        mainViewModel.buttonDateTextSet().observe(this,{
            binding.btAddDate.text = it
            observerAdapter()
        })

        // устанавливаем значение даты, для отображения списка с указанной датой
        binding.btAddDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, { _, yearPicker, monthPicker, dayPicker ->
                mainViewModel.buttonDateTextGet.value =
                    getString(R.string.bt_text_addDate_change,dayPicker,monthPicker+1,yearPicker)

                observerAdapter()
            }, Calendar().year, Calendar().month, Calendar().day)
            datePickerDialog.show()
        }

        // запуск второй активити
        binding.btAddNote.setOnClickListener {
            val intent = Intent(this,ModifyActivity::class.java)
            startActivity(intent)
        }

        // сбрасываем фильтр и отображаем все данные списка
        binding.btAllNote.setOnClickListener {
            mainViewModel.buttonDateTextGet.value = "Задать дату"
            observerAdapter()
        }
    }

    // инициализируем RecyclerView
    private fun initRcView() = with(binding){

        rcView.layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = MainAdapter()
        rcView.adapter = adapter
    }

    // фильтрация списка по заданной дате
    private fun observerAdapter(){

        mainViewModel.getAllEntityByDate(
            if (binding.btAddDate.text.toString() == "Задать дату") "%%"
            else binding.btAddDate.text.toString())
            .observe(this,{
                adapter.submitList(it)
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}