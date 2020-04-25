package com.parkash.countrypicker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.parkash.countrypicker.R
import com.parkash.countrypicker.databinding.IndexAdapterDesignBinding

class IndexAdapter(
    private
    var list: ArrayList<String>, private var callback: CallBack
) : RecyclerView.Adapter<IndexAdapter.MyHolderView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding = DataBindingUtil.inflate<IndexAdapterDesignBinding>(
            LayoutInflater.from(parent.context),
            R.layout.index_adapter_design,
            parent,
            false
        )
        return MyHolderView(
            binding
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) {
        holder.binding.titleTV.text = list[position]

        holder.binding.titleTV.setOnClickListener {
            callback.indexSelected(list[position])
        }

    }

    class MyHolderView(var binding: IndexAdapterDesignBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface CallBack {
        fun indexSelected(data: String)
    }
}