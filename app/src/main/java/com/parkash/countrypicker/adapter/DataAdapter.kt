package com.parkash.countrypicker.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.parkash.countrypicker.model.DataClass
import com.parkash.countrypicker.R
import com.parkash.countrypicker.databinding.DataAdapterDesignBinding

class DataAdapter(private var list: ArrayList<DataClass>) :
    RecyclerView.Adapter<DataAdapter.MyHolderView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding = DataBindingUtil.inflate<DataAdapterDesignBinding>(
            LayoutInflater.from(parent.context),
            R.layout.data_adapter_design,
            parent,
            false
        )
        return MyHolderView(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MyHolderView, position: Int) {
        val context = holder.itemView.context
        if (list[position].isHeader) {
            holder.binding.titleTV.text = list[position].name
            holder.binding.titleTV.visibility = View.VISIBLE
            holder.binding.dataTV.visibility = View.GONE
        } else {
            holder.binding.dataTV.text = list[position].name
            holder.binding.titleTV.visibility = View.GONE
            holder.binding.dataTV.visibility = View.VISIBLE
        }

        holder.binding.dataTV.setOnClickListener {
            Toast.makeText(context, "Clicked on : ${list[position].name}", Toast.LENGTH_LONG).show()
        }


    }

    class MyHolderView(var binding: DataAdapterDesignBinding) :
        RecyclerView.ViewHolder(binding.root)
}