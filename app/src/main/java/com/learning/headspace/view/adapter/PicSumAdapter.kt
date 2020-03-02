package com.learning.headspace.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learning.headspace.R
import com.learning.headspace.model.PicSum
import kotlinx.android.synthetic.main.pic_sum_item_view.view.*

class PicSumAdapter(val context: Context): RecyclerView.Adapter<PicSumAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =   ItemViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.pic_sum_item_view, parent,false))

    private var picSumList:List<PicSum>? = null

    override fun getItemCount(): Int = picSumList?.size ?: 0

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        picSumList?.let {
            holder.bindData(it[position])
        }
    }

    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {
        fun bindData(picSum: PicSum) {
            itemView.dimensions.text = context.getString(R.string.dimensions, picSum.width, picSum.height)
            itemView.authorName.text = context.getString(R.string.author_name, picSum.author)
            Glide.with(context)
                .load(picSum.download_url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .fallback(R.drawable.ic_launcher_background)
                .into(itemView.imageView)
        }
    }

    fun setData(picSumList:List<PicSum>) {
        this.picSumList = picSumList
        notifyDataSetChanged()
    }
}