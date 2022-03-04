/**
 * class ModifyActivity используется для создания и редактирования заметок
 */

package com.meridiane.notepadroomlibrary.presentation.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.meridiane.notepadroomlibrary.Constants
import com.meridiane.notepadroomlibrary.presentation.TimeDialog
import com.meridiane.notepadroomlibrary.domain.viewModel.dateAndTime.Calendar
import com.meridiane.notepadroomlibrary.domain.viewModel.dateAndTime.ConverterData
import com.meridiane.notepadroomlibrary.R
import com.meridiane.notepadroomlibrary.databinding.ActivityModifyBinding
import com.meridiane.notepadroomlibrary.data.Entity
import com.meridiane.notepadroomlibrary.data.MainApp
import com.meridiane.notepadroomlibrary.domain.viewModel.ModifyViewModel
import com.meridiane.notepadroomlibrary.domain.viewModel.ModifyViewModelFactory
import java.util.*

class ModifyActivity : AppCompatActivity() {

    private var _binding: ActivityModifyBinding? = null
    private val binding get() = _binding!!

    private lateinit var modifyViewModel: ModifyViewModel

    private val converter = ConverterData()

    private var i = intent // используется для редактирования заметок

    private var timeEntity: Int = 100 // используется для заполнения Entity в fun addEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // инициализация VM
        modifyViewModel = ViewModelProvider(this,
            ModifyViewModelFactory((this.applicationContext as MainApp).database)
        )[ModifyViewModel::class.java]

        // если интент не пустой, достаём из него данные и заполняем view
        i = intent
        if (i.getStringExtra(Constants.TITLE_KEY) != null) getIntents()

        // устанавливает текст у кнопки "Выбрать время заметки"
        modifyViewModel.sendDbTime().observe(this, {

            binding.btAddTime.text = if (it == 100) "Выбрать время заметки"
            else getString(R.string.text_selection_time, it, it + 1)
            timeEntity = it
        })

        // устанавливает текст у кнопки "Выбрать дату заметки"
        modifyViewModel.sendDbDate().observe(this, {

            binding.btAddDateModify.text = it
        })

        // установление даты в тексте кнопки и запись значения даты в VM
        binding.btAddDateModify.setOnClickListener {

            val datePickerDialog = DatePickerDialog(this, { _, yearPicker, monthPicker, dayPicker ->
                modifyViewModel.sendDbDate.value = getString(
                    R.string.bt_text_addDate_change,
                    dayPicker,
                    monthPicker + 1,
                    yearPicker
                )

            }, Calendar().year, Calendar().month, Calendar().day)
            datePickerDialog.show()
        }

        // установление времени в тексте кнопки и запись значения времени в VM
        binding.btAddTime.setOnClickListener {
            TimeDialog.showDialog(this as AppCompatActivity, object : TimeDialog.Listener {
                override fun onClick(int: Int?) {
                    if (int != null) {
                        modifyViewModel.sendDbTime.value = int
                    }
                }
            })
        }
        // проверка заполнения полей и подготовка данных к отправке в БД
        binding.btAddNoteModify.setOnClickListener {
            checkDateTime()
        }
    }

    // проверка заполнения полей и подготовка данных к отправке в БД
    private fun checkDateTime() = with(binding) {
        if (edTitle.text.isNotEmpty() &&
            edContent.text.isNotEmpty() &&
            binding.btAddTime.text != "Выбрать время заметки" &&
            binding.btAddDateModify.text != "Выбрать дату заметки"
        ) {

            if (i.getStringExtra(Constants.TITLE_KEY) == null)
                modifyViewModel.insertEntity(addEntity(null))
            else
                modifyViewModel.updateEntity(addEntity(intent.getIntExtra(Constants.ID_KEY, 0)))

            finish()
        } else {

            if (edTitle.text.isEmpty()) edTitle.error = "Поле не заполнено"

            if (edContent.text.isEmpty()) edContent.error = "Поле не заполнено"

            if (binding.btAddTime.text == "Выбрать время заметки" ||
                timeEntity == 100
            )
                Toast.makeText(
                    this@ModifyActivity, "Добавьте дату и время заметки", Toast.LENGTH_LONG
                ).show()
        }
    }

    // заполнение данными Entity для отправки в БД
    private fun addEntity(id: Int?): Entity {
        return Entity(
            id,
            binding.edTitle.text.toString(),
            binding.edContent.text.toString(),
            converter.convertString(binding.btAddDateModify.text.toString()),
            converter.converterTimeMillis(timeEntity),
            binding.btAddDateModify.text.toString()
        )
    }

    // получение данных из Intent для заполнения полей
    private fun getIntents() {
        binding.apply {

            val title = intent.getStringExtra(Constants.TITLE_KEY)
            val content = intent.getStringExtra(Constants.CONTENT_KEY)
            val time = intent.getIntExtra(Constants.TIME_KEY, 0)
            val date = intent.getStringExtra(Constants.DATE_KEY)

            edTitle.setText(title)
            edContent.setText(content)
            btAddTime.text = getString(R.string.text_selection_time, time, time + 1)
            btAddDateModify.text = "$date"

            modifyViewModel.sendDbTime.value = time
            modifyViewModel.sendDbDate.value = date
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}