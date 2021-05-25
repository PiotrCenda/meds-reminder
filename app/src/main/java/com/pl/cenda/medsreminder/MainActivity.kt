package com.pl.cenda.medsreminder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(),
    ListSelectionRecyclerViewClickListener {

    lateinit var medsRecyclerView: RecyclerView

    val medsDataManager: MedsDataManager = MedsDataManager(this)

    companion object {
        const val  INTENT_MED_KEY = "list"
        const val MED_DETAILS_REQUEST_CODE = 420
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val meds = medsDataManager.readList()

        findViewById<FloatingActionButton>(R.id.add_med_button).setOnClickListener { result ->
            showCreateListDialog()
        }

        medsRecyclerView = findViewById<RecyclerView>(R.id.meds_recycler_view)
        medsRecyclerView.layoutManager = LinearLayoutManager(this)
        medsRecyclerView.adapter = ListSelectionRecyclerViewAdapter(meds, this)
    }

    override fun listItemClicked(meds: MedInfoList) {
        showListDetail(meds)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MED_DETAILS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                medsDataManager.saveList(data.getParcelableExtra(INTENT_MED_KEY))
                updateMedsData()
            }
        }
    }


    private fun showListDetail(medInfo: MedInfoList) {
        val listDetailIntent = Intent(this, MedDetailsActivity::class.java)
        listDetailIntent.putExtra(INTENT_MED_KEY, medInfo)
        startActivityForResult(listDetailIntent, MED_DETAILS_REQUEST_CODE)
    }

    private fun showCreateListDialog() {
        val dialogTitle = getString(R.string.list_name_question)
        val positiveButtonTitle = getString(R.string.create_list)

        val builder = AlertDialog.Builder(this)
        val medTitleEditText = EditText(this)

        medTitleEditText.inputType = InputType.TYPE_CLASS_TEXT

        builder.setTitle(dialogTitle)
        builder.setView(medTitleEditText)

        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            val list = MedInfoList(medTitleEditText.text.toString())
            medsDataManager.saveList(list)
            val recyclerViewAdapter = medsRecyclerView.adapter as ListSelectionRecyclerViewAdapter
            recyclerViewAdapter.addList(list)
            showListDetail(list)
        }

        builder.create().show()
    }

    private fun updateMedsData() {
        val meds = medsDataManager.readList()
        medsRecyclerView.adapter = ListSelectionRecyclerViewAdapter(meds, this)
    }
}