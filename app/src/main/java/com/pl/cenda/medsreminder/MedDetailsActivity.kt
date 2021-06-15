package com.pl.cenda.medsreminder

import android.app.*
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MedDetailsActivity : AppCompatActivity() {

    lateinit var medInfo: MedInfoList
    lateinit var medInfoRecyclerView: RecyclerView
    lateinit var addMedInfoButton: FloatingActionButton
    lateinit var delMedButton: FloatingActionButton

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
//            showCreateMedDetailDialog()
            pickDateTime()
        }

        delMedButton = findViewById(R.id.del_med_button)
        delMedButton.setOnClickListener {
            showDeleteMedDialog()
        }
    }

    fun remind(hourOfDay: Int, minute: Int) {
        val alarmManager =
            getSystemService(Context.ALARM_SERVICE) as AlarmManager //we are using alarm manager for the notification

        val notificationIntent = Intent(
            this,
            MainActivity::class.java
        ) //this intent will be called when taping the notification

        val broadcast = PendingIntent.getBroadcast(
            this,
            100,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        ) //this pendingIntent will be called by the broadcast receiver

        val cal = Calendar.getInstance() //getting calender instance

        cal.timeInMillis = System.currentTimeMillis() //setting the time from device

        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            cal.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            broadcast
        ) //alarm manager will repeat the notification each day at the set time
    }

    private fun pickDateTime() {
        val mcurrentTime = Calendar.getInstance()
        val hourCal = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minuteCal = mcurrentTime.get(Calendar.MINUTE)

        TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                if (minute < 10) {
                    medInfo.medDetails.add(String.format("%d : 0%d", hourOfDay, minute))
                } else {
                    medInfo.medDetails.add(String.format("%d : %d", hourOfDay, minute))
                }

                val recyclerAdapter = medInfoRecyclerView.adapter as MedInfoRecyclerViewAdapter
                recyclerAdapter.notifyItemInserted(medInfo.medDetails.size - 1)

                remind(hourOfDay, minute)
            }
        }, hourCal, minuteCal, true).show()
    }

    override fun onBackPressed() {
        val bundle = Bundle()
        bundle.putParcelable(MainActivity.INTENT_MED_KEY, medInfo)

        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)

        super.onBackPressed()
    }

    fun onDeletePressed() {
        val bundle = Bundle()
        bundle.putParcelable(MainActivity.INTENT_MED_KEY, medInfo)

        val intent = Intent()
        intent.putExtras(bundle)
        setResult(MainActivity.MED_DELETE_REQUEST_CODE, intent)

        super.onBackPressed()
    }

    private fun showCreateMedDetailDialog() {
        val dialogTitle = getString(R.string.med_info_to_add_title)
        val positiveButtonTitle = getString(R.string.add_med_info)

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

    private fun showDeleteMedDialog() {
        val dialogTitle = getString(R.string.delete_med_title)
        val positiveButtonTitle = getString(R.string.delete_med)
        val negativeButtonTitle = getString(R.string.not_delete_med)

        val builder = AlertDialog.Builder(this)

        builder.setTitle(dialogTitle)

        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            dialog.dismiss()
            onDeletePressed()
        }

        builder.setNegativeButton(negativeButtonTitle) { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }
}