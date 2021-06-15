package com.pl.cenda.medsreminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MedInfoRecyclerViewAdapter(var med: MedInfoList): RecyclerView.Adapter<MedInfoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedInfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.med_info_view_holder, parent, false)
        return MedInfoViewHolder(view)
    }

    override fun onBindViewHolder(holderInfo: MedInfoViewHolder, position: Int) {
        holderInfo.medTextView.text = med.medDetails[position]

        if(position%2 == 0){
            holderInfo.medTextView.setBackgroundResource(R.drawable.text_bg)
        } else {
            holderInfo.medTextView.setBackgroundResource(R.drawable.text_bg2)
        }
    }

    override fun getItemCount(): Int {
        return med.medDetails.size
    }
}