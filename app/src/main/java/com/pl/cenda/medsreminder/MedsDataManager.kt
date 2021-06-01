package com.pl.cenda.medsreminder

import android.content.Context
import androidx.preference.PreferenceManager

class MedsDataManager(private val context: Context) {

    fun saveMed(med: MedInfoList) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
        sharedPreferences.putStringSet(med.name, med.medDetails.toHashSet())
        sharedPreferences.apply()
    }

    fun delMed(med: MedInfoList) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
        sharedPreferences.remove(med.name)
        sharedPreferences.apply()
    }

    fun readList(): ArrayList<MedInfoList> {
        val meds = ArrayList<MedInfoList>()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val sharedPreferencesContents = sharedPreferences.all

        for (medDetails in sharedPreferencesContents) {
            val itemHashSet = ArrayList(medDetails.value as HashSet<String>)
            val med = MedInfoList(medDetails.key, itemHashSet)
            meds.add(med)
        }

        return meds
    }
}