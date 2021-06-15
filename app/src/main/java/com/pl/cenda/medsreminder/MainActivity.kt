package com.pl.cenda.medsreminder

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity(),
    ListSelectionRecyclerViewClickListener {

    lateinit var medsRecyclerView: RecyclerView

    val medsDataManager: MedsDataManager = MedsDataManager(this)

    companion object {
        const val  INTENT_MED_KEY = "list"
        const val MED_DETAILS_REQUEST_CODE = 420
        const val MED_DELETE_REQUEST_CODE = 421
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val meds = medsDataManager.readList()

        findViewById<FloatingActionButton>(R.id.add_med_button).setOnClickListener { result ->
            showCreateMedDialog()
        }

        medsRecyclerView = findViewById<RecyclerView>(R.id.meds_recycler_view)
        medsRecyclerView.layoutManager = LinearLayoutManager(this)
        medsRecyclerView.adapter = ListSelectionRecyclerViewAdapter(meds, this)
    }

    override fun listItemClicked(meds: MedInfoList) {
        showMedDetails(meds)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MED_DETAILS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                medsDataManager.saveMed(data.getParcelableExtra(INTENT_MED_KEY))
                updateMedsData()
            }
        } else if (resultCode == MED_DELETE_REQUEST_CODE) {
            data?.let {
                medsDataManager.delMed(data.getParcelableExtra(INTENT_MED_KEY))
                updateMedsData()
            }
        }
    }

    private fun showMedDetails(medInfo: MedInfoList) {
        val listDetailIntent = Intent(this, MedDetailsActivity::class.java)
        listDetailIntent.putExtra(INTENT_MED_KEY, medInfo)
        startActivityForResult(listDetailIntent, MED_DETAILS_REQUEST_CODE)
    }

    private fun showCreateMedDialog() {
        val dialogTitle = getString(R.string.list_name_question)
        val positiveButtonTitle = getString(R.string.create_list)

        val builder = AlertDialog.Builder(this)
        val medTitleEditText = EditText(this)

        medTitleEditText.inputType = InputType.TYPE_CLASS_TEXT

        builder.setTitle(dialogTitle)
        builder.setView(medTitleEditText)

        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            val med = MedInfoList(medTitleEditText.text.toString())
            medsDataManager.saveMed(med)
            val recyclerViewAdapter = medsRecyclerView.adapter as ListSelectionRecyclerViewAdapter
            recyclerViewAdapter.addMed(med)
            showMedDetails(med)
        }

        builder.create().show()
    }

    private fun updateMedsData() {
        val meds = medsDataManager.readList()
        medsRecyclerView.adapter = ListSelectionRecyclerViewAdapter(meds, this)
    }
}