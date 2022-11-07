package com.example.mymap

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.data.DataHolder
import java.text.FieldPosition

private const val TAG = "MapsAdapter"
class MapsAdapter(val context: Context, val userMaps: List<UserMap>, val onClickListener: OnClickListener) : RecyclerView.Adapter<MapsAdapter.MyViewHolder>() {
 interface OnClickListener{
  fun onItemClick(position: Int)
 }


 override fun onCreateViewHolder(parent: ViewGroup, view: Int):MyViewHolder {
  val view = LayoutInflater.from(context).inflate(R.layout.row_place,parent,false)
  return MyViewHolder(view)
 }

 override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
  val userMap = userMaps[position]
  val tvTitle = holder.itemView.findViewById<TextView>(R.id.tv_place)
  tvTitle.text = userMap.title

  holder.itemView.setOnClickListener{
   Log.i(TAG, "Click on position $position")
   onClickListener.onItemClick(position)
  }
 }

 override fun getItemCount(): Int = userMaps.size

 class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
}
