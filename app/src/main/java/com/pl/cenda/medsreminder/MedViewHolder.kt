package com.pl.cenda.medsreminder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val medTextView = itemView.findViewById(R.id.med_text_view) as TextView
}