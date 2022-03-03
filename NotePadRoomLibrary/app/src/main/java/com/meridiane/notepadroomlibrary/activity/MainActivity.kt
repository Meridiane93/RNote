package com.meridiane.notepadroomlibrary.activity

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.meridiane.notepadroomlibrary.Constants
import com.meridiane.notepadroomlibrary.dateAndTime.Calendar
import com.meridiane.notepadroomlibrary.R
import com.meridiane.notepadroomlibrary.adapter.MainAdapter
import com.meridiane.notepadroomlibrary.databinding.ActivityMainBinding
import com.meridiane.notepadroomlibrary.db.MainApp
import com.meridiane.notepadroomlibrary.viewModel.MainViewModel
import java.util.*


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding?= null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by lazy {
        MainViewModel((this.applicationContext as MainApp).database)
    }

    private lateinit var adapter: MainAdapter

    // сохранение значения даты для фильтраии списка
    private var dateString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dateString(savedInstanceState)

        // устанавливаем значение даты, для отображения списка с указанной датой
        binding.btAddDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, { _, yearPicker, monthPicker, dayPicker ->
                binding.btAddDate.text = getString(R.string.bt_text_addDate_change,dayPicker,monthPicker+1,yearPicker)
                dateString = binding.btAddDate.text.toString()
                observer()
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
            binding.btAddDate.text = "Задать дату"
            dateString = "Задать дату"
            observer()
        }

        initRcView()
        observer()
    }

    // сохраняем значение которое служит для фильтрации списка Entity
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.DATE_STRING, dateString)
    }

    // Текст кнопки "Задать дату" служит для фильтрации списка Entity
    private fun dateString(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            dateString = savedInstanceState.getString(Constants.DATE_STRING) ?: "Задать дату"

            binding.btAddDate.text = dateString
        }
    }

    // инициализируем RecyclerView
    private fun initRcView() = with(binding){
        rcView.layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = MainAdapter()
        rcView.adapter = adapter
    }

    // фильтрация списка по заданной дате
    private fun observer(){

        mainViewModel.getAllEntityByDate(
            if (binding.btAddDate.text == "Задать дату") "%%"
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