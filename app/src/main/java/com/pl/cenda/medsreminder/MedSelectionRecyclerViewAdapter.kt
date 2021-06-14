package com.pl.cenda.medsreminder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ComplexColorCompat
import androidx.recyclerview.widget.RecyclerView

interface ListSelectionRecyclerViewClickListener {
    fun listItemClicked(med: MedInfoList)
}

class ListSelectionRecyclerViewAdapter(private val meds: ArrayList<MedInfoList>,
                                       val clickListener: ListSelectionRecyclerViewClickListener):
    RecyclerView.Adapter<MedSelectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedSelectionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.med_view_holder,
                parent,
                false)

        return MedSelectionViewHolder(view)
    }


    override fun onBindViewHolder(holder: MedSelectionViewHolder, position: Int) {
        holder.medPosition.text = (position + 1).toString()
        holder.medName.text = meds.get(position).name
        var color = R.color.item2
        if(position%2 == 0){
            //holder.medPosition.setBackgroundColor(color)
        }
        holder.itemView.setOnClickListener {
            clickListener.listItemClicked(meds[position])
        }
    }

    override fun getItemCount(): Int {
        return meds.count()
    }

    fun addMed(med: MedInfoList) {
        meds.add(med)
        notifyItemInserted(meds.size - 1)
    }

    fun delMed(med: MedInfoList) {
        meds.remove(med)
        notifyItemInserted(meds.size - 1)
    }
}