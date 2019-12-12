package com.r00t.demetapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.r00t.demetapp.R
import com.r00t.demetapp.models.DataModel
import kotlinx.android.synthetic.main.grid_recycler_view_item.view.*
import javax.annotation.Nonnull

class GridRecyclerViewAdapter : RecyclerView.Adapter<GridRecyclerViewAdapter.ViewHolder> {
    var tempDataList:List<DataModel> = emptyList()
    var itemClickListener:GridRecyclerViewItemClickListener?=null
    var height:Int=0
    var layoutManager:GridLayoutManager?=null
    protected var imageList:ArrayList<Int> = arrayListOf()
    constructor(tempDataList:List<DataModel>) {
        this.tempDataList=tempDataList
        imageList.add(R.drawable.jup)
        imageList.add(R.drawable.mars)
        imageList.add(R.drawable.mekik)
        imageList.add(R.drawable.merkul)
        imageList.add(R.drawable.meteor)
        imageList.add(R.drawable.star)
        imageList.add(R.drawable.sun)
        imageList.add(R.drawable.uydu)
        imageList.shuffle()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView:View=LayoutInflater.from(parent.context).inflate(R.layout.grid_recycler_view_item,parent,false)
        height = parent.measuredHeight/(tempDataList.size/layoutManager!!.spanCount)
        return ViewHolder(itemView,itemClickListener!!)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        layoutManager=recyclerView.layoutManager as GridLayoutManager
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv1?.setText(tempDataList.get(position).tempData.toString())
        holder.tv1?.visibility=TextView.INVISIBLE
        holder.imageView?.setImageResource(imageList.get(tempDataList.get(position).tempData!!))
        holder.imageView?.visibility = if(tempDataList.get(position).isShowing || tempDataList.get(position).isMatch) TextView.VISIBLE else TextView.INVISIBLE
        holder.itemView.isClickable=tempDataList.get(position).isEnabled;
        var params:LinearLayout.LayoutParams= LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        params.height=height
        holder.itemView.layoutParams = params
    }

    override fun getItemCount(): Int {
        return tempDataList.size
    }

    public inner class ViewHolder : RecyclerView.ViewHolder,View.OnClickListener {
        private var itemClickListener:GridRecyclerViewItemClickListener?=null
        var tv1:TextView?=null
        var imageView:ImageView?=null

        constructor(itemView:View,itemClickListener: GridRecyclerViewItemClickListener) : super(itemView) {
            tv1 = itemView.findViewById(R.id.tv1)
            imageView=itemView.findViewById(R.id.imageView)
            tv1?.visibility = TextView.INVISIBLE
            this.itemClickListener = itemClickListener
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            itemClickListener?.onItemClicked(p0!!,adapterPosition,tempDataList.get(adapterPosition))
        }
    }

    interface GridRecyclerViewItemClickListener {
        fun onItemClicked(view:View,position:Int,data:DataModel)
    }
}