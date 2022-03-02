package com.meridiane.notepadroomlibrary.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.meridiane.notepadroomlibrary.Constants
import com.meridiane.notepadroomlibrary.dateAndTime.TimeDialog
import com.meridiane.notepadroomlibrary.dateAndTime.Calendar
import com.meridiane.notepadroomlibrary.dateAndTime.ConverterData
import com.meridiane.notepadroomlibrary.R
import com.meridiane.notepadroomlibrary.databinding.ActivityModifyBinding
import com.meridiane.notepadroomlibrary.db.Entity
import com.meridiane.notepadroomlibrary.db.MainApp
import com.meridiane.notepadroomlibrary.viewModel.ModifyViewModel
import java.util.*

class ModifyActivity : AppCompatActivity() {

    private var _binding: ActivityModifyBinding ?= null
    private val binding get() = _binding!!

    private val modifyViewModel: ModifyViewModel by lazy {
        ModifyViewModel((this.applicationContext as MainApp).database)
    }

    private var i = intent

    private var sendDbTime = 100
    private var sendDbDate = 100L

    val converter = ConverterData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        i = intent
        if (i.getStringExtra(Constants.TITLE_KEY) != null) getIntents()

        checkOnSavedInstance(savedInstanceState)

        binding.btAddDateModify.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, { _, yearPicker, monthPicker, dayPicker ->
                binding.btAddDateModify.text = getString(
                    R.string.bt_text_addDate_change,
                    dayPicker,
                    monthPicker + 1,
                    yearPicker
                )
                sendDbDate = converter.convertString(binding.btAddDateModify.text.toString())

            }, Calendar().year,Calendar().month,Calendar().day)
            datePickerDialog.show()
        }

        binding.btAddTime.setOnClickListener {
            TimeDialog.showDialog(this as AppCompatActivity, object : TimeDialog.Listener {
                override fun onClick(int: Int?) {
                    if (int != null) {
                        binding.btAddTime.text =
                            getString(R.string.text_selection_time, int, int + 1)
                        sendDbTime = converter.converterTimeMillis(int)
                    }
                }
            })
        }

        binding.btAddNoteModify.setOnClickListener {
            checkDateTime()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(Constants.DATE, sendDbDate)
        outState.putInt(Constants.TIME, sendDbTime)
    }

    private fun checkOnSavedInstance(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            sendDbDate = savedInstanceState.getLong(Constants.DATE)
            sendDbTime = savedInstanceState.getInt(Constants.TIME)
            binding.btAddDateModify.text = converter.convertMillis(sendDbDate)
            binding.btAddTime.text =
                getString(R.string.text_selection_time, sendDbTime, sendDbTime + 1)
        }
    }

    private fun checkDateTime() = with(binding) {
        if (edTitle.text.isNotEmpty() &&
            edContent.text.isNotEmpty() &&
            sendDbTime != 100 &&
            sendDbDate != 100L
        ) {

            if (i.getStringExtra(Constants.TITLE_KEY) == null)
                modifyViewModel.insertEntity(addEntity(null))
            else
                modifyViewModel.updateEntity(addEntity(intent.getIntExtra(Constants.ID_KEY, 0)))

            finish()
        } else {

            if (edTitle.text.isEmpty()) edTitle.error = "Поле не заполнено"

            if (edContent.text.isEmpty()) edContent.error = "Поле не заполнено"

            if (sendDbTime == 100 || sendDbDate == 100L)
                Toast.makeText(
                    this@ModifyActivity, "Добавьте дату и время заметки",
                    Toast.LENGTH_LONG
                ).show()
        }
    }

    private fun addEntity(id: Int?): Entity {
        return Entity(
            id,
            binding.edTitle.text.toString(),
            binding.edContent.text.toString(),
            sendDbDate,
            sendDbTime,
            binding.btAddDateModify.text.toString()
        )
    }

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

            sendDbTime = time
            sendDbDate = converter.convertString("$date")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}