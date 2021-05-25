package com.pl.cenda.medsreminder

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MedDetailsActivity : AppCompatActivity() {

    lateinit var medInfo: MedInfoList
    lateinit var medInfoRecyclerView: RecyclerView
    lateinit var addMedInfoButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_med_details)

        medInfo = intent.getParcelableExtra(MainActivity.INTENT_MED_KEY)
        title = medInfo.name

        medInfoRecyclerView = findViewById(R.id.med_info_list_recycler_view)
        medInfoRecyclerView.adapter = MedInfoRecyclerViewAdapter(medInfo)
        medInfoRecyclerView.layoutManager = LinearLayoutManager(this)

        addMedInfoButton = findViewById(R.id.add_med_info_button)
        addMedInfoButton.setOnClickListener {
            showCreateTaskDialog()
        }
    }

    override fun onBackPressed() {
        val bundle = Bundle()
        bundle.putParcelable(MainActivity.INTENT_MED_KEY, medInfo)

        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)

        super.onBackPressed()
    }

    private fun showCreateTaskDialog() {
        val dialogTitle = getString(R.string.task_to_add_title)
        val positiveButtonTitle = getString(R.string.add_task)

        val builder = AlertDialog.Builder(this)
        val medInfoEditText = EditText(this)

        medInfoEditText.inputType = InputType.TYPE_CLASS_TEXT

        builder.setTitle(dialogTitle)
        builder.setView(medInfoEditText)

        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            val info = medInfoEditText.text.toString()
            medInfo.medDetails.add(info)
            val recyclerAdapter = medInfoRecyclerView.adapter as MedInfoRecyclerViewAdapter
            recyclerAdapter.notifyItemInserted(medInfo.medDetails.size - 1)
            dialog.dismiss()
        }
        builder.create().show()
    }
}