package com.example.kotlindemo

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlindemo.RoomListAdapter.*
import com.squareup.picasso.Picasso

class RoomListAdapter(
    val context: Context,
    private val list: List<Model>,
    var boolean: Boolean,
    var isDetail: Boolean
) :
    RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = list.get(position).name.toString()

        if (isDetail) {
            holder.tvCapacity.visibility = View.GONE
            holder.tvCapacityTitle.visibility = View.GONE
            holder.tvNameTitle.visibility = View.GONE
            holder.iv_icon.visibility = View.GONE
            if(boolean){
                holder.iv_subIcon.visibility = View.VISIBLE
                Picasso.get().load(list.get(position).iconUrl).into(holder.iv_subIcon)
            }else{
                holder.iv_subIcon.visibility = View.GONE
            }
        } else {
            holder.iv_subIcon.visibility = View.GONE
            holder.tvNameTitle.visibility = View.VISIBLE
            holder.tvCapacity.visibility = View.VISIBLE
            holder.tvCapacityTitle.visibility = View.VISIBLE
            holder.tvCapacity.text = list.get(position).capacity.toString()
            Picasso.get().load(list.get(position).thumbnailUrl).into(holder.iv_icon)
        }

        holder.linearLayout.setOnClickListener { v: View ->
            Unit
            if (!isDetail) {
                val intent = Intent(context, RoomDetailsActivity::class.java)
                intent.putExtra("key", list.get(position).key)
                context.startActivity(intent)
            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_icon = itemView.findViewById<ImageView>(R.id.iv_icon)
        var iv_subIcon = itemView.findViewById<ImageView>(R.id.iv_subIcon)
        var tvCapacity = itemView.findViewById<TextView>(R.id.tvCapacity)
        var tvCapacityTitle = itemView.findViewById<TextView>(R.id.tvCapacityTitle)
        var tvName = itemView.findViewById<TextView>(R.id.tvName)
        var tvNameTitle = itemView.findViewById<TextView>(R.id.tvNameTitle)
        var linearLayout = itemView.findViewById<LinearLayout>(R.id.linearLayout)
    }
}