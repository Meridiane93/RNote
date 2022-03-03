package com.meridiane.notepadroomlibrary.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import com.meridiane.notepadroomlibrary.Constants
import com.meridiane.notepadroomlibrary.dateAndTime.ConverterData
import com.meridiane.notepadroomlibrary.R
import com.meridiane.notepadroomlibrary.activity.ModifyActivity
import com.meridiane.notepadroomlibrary.databinding.ListItemBinding
import com.meridiane.notepadroomlibrary.db.Entity
import com.meridiane.notepadroomlibrary.db.MainApp
import com.meridiane.notepadroomlibrary.viewModel.MainViewModel

class MainAdapter: ListAdapter<Entity, MainAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder = ItemHolder.create(parent)

    override fun onBindViewHolder(holder: ItemHolder, position: Int) = holder.setData(getItem(position))

    class ItemHolder(private val view: View): RecyclerView.ViewHolder(view){
        
        private val mainViewModel: MainViewModel by lazy {
            MainViewModel((view.context.applicationContext as MainApp).database)
        }

        private val converterData = ConverterData()

        private val binding = ListItemBinding.bind(view)

        fun setData(note:Entity) = with(binding){

            tvTitle.text = if (note.title.length > 15) note.title.substring(0,15) else note.title
            tvContent.text = if (note.content.length > 20) note.content.substring(0,20) else note.content

            val convertedDate = converterData.convertMillis(note.date)
            val convertedTime = converterData.converterTimeInt(note.time)

            tvTextTime.text = view.context.getString(R.string.text_time_adapter,convertedTime,convertedTime+1)
            tvTextDate.text = convertedDate

            itemView.setOnClickListener {
                val intent = Intent(view.context, ModifyActivity::class.java).apply {
                    putExtra(Constants.TITLE_KEY, note.title)
                    putExtra(Constants.CONTENT_KEY, note.content)
                    putExtra(Constants.DATE_KEY, convertedDate)
                    putExtra(Constants.TIME_KEY, convertedTime)
                    putExtra(Constants.ID_KEY, note.id)
                }
                view.context.startActivity(intent)
            }

            imDelete.setOnClickListener {
                mainViewModel.deleteEntity(note.id!!)
            }
        }

        companion object{
            fun create(parent:ViewGroup): ItemHolder = ItemHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item,parent,false))
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<Entity>(){
        override fun areItemsTheSame(oldItem: Entity, newItem: Entity): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Entity, newItem: Entity): Boolean = oldItem == newItem
    }
}