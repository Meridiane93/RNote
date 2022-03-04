/**
 * object TimeDialog - используется для выбора времени заметки
 */

package com.meridiane.notepadroomlibrary.presentation

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import com.meridiane.notepadroomlibrary.R
import com.meridiane.notepadroomlibrary.databinding.TimeDialogBinding

object TimeDialog {
    fun showDialog(context: Context, listener: Listener){
        var dialog: AlertDialog?= null
        val builder = AlertDialog.Builder(context)
        val binding = TimeDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)

        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter
            .createFromResource(context, R.array.text_selection_times,android.R.layout.simple_list_item_1)
        binding.lv.adapter = adapter

        binding.lv.setOnItemClickListener { _, _, i, _ ->
            listener.onClick(i)
            dialog?.dismiss()
        }

        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }
    interface Listener {
        fun onClick(int: Int?)
    }
}