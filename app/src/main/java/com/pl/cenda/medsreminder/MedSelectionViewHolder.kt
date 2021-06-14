package com.pl.cenda.medsreminder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MedSelectionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val medPosition = itemView.findViewById(R.id.med_number) as TextView
    val medName = itemView.findViewById(R.id.med_name) as TextView

}