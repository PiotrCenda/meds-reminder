package com.pl.cenda.medsreminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MedInfoRecyclerViewAdapter(var med: MedInfoList): RecyclerView.Adapter<MedViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.med_view_holder, parent, false)
        return MedViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedViewHolder, position: Int) {
        holder.medTextView.text = med.medDetails[position]
    }

    override fun getItemCount(): Int {
        return med.medDetails.size
    }
}