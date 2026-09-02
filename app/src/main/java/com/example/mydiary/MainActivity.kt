package com.example.mydiary

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val tvDate = findViewById<TextView>(R.id.tvDate)
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etDiaryContent = findViewById<EditText>(R.id.etDiaryContent)
        val moodGroup = findViewById<RadioGroup>(R.id.moodGroup)

        val btnSaveDiary = findViewById<Button>(R.id.btnSaveDiary)
        val btnViewDiary = findViewById<Button>(R.id.btnViewDiary)
        val btnDeleteDiary = findViewById<Button>(R.id.btnDeleteDiary)

        val dateFormat = SimpleDateFormat(
            "EEEE, dd MMMM yyyy",
            Locale.getDefault()
        )

        tvDate.text = dateFormat.format(Date())

        // SAVE DIARY
        btnSaveDiary.setOnClickListener {

            val title = etTitle.text.toString().trim()
            val diaryContent = etDiaryContent.text.toString().trim()
            val selectedMoodId = moodGroup.checkedRadioButtonId

            if (title.isEmpty()) {
                etTitle.error = "Please enter a title"
                return@setOnClickListener
            }

            if (diaryContent.isEmpty()) {
                etDiaryContent.error = "Please write your diary"
                return@setOnClickListener
            }

            if (selectedMoodId == -1) {
                Toast.makeText(
                    this,
                    "Please select your mood",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val mood = findViewById<RadioButton>(selectedMoodId)
                .text
                .toString()

            val preferences =
                getSharedPreferences("MyDiaryData", MODE_PRIVATE)

            val oldData =
                preferences.getString("allDiaries", "[]")

            val diaryArray = JSONArray(oldData)

            val newDiary = JSONObject()

            val dateTimeFormat = SimpleDateFormat(
                "EEEE, dd MMMM yyyy - hh:mm a",
                Locale.getDefault()
            )

            newDiary.put("date", dateTimeFormat.format(Date()))
            newDiary.put("title", title)
            newDiary.put("mood", mood)
            newDiary.put("content", diaryContent)

            diaryArray.put(newDiary)

            preferences.edit()
                .putString("allDiaries", diaryArray.toString())
                .apply()

            Toast.makeText(
                this,
                "Diary saved successfully!",
                Toast.LENGTH_SHORT
            ).show()

            etTitle.text.clear()
            etDiaryContent.text.clear()
            moodGroup.clearCheck()
        }

        // VIEW ALL DIARIES
        btnViewDiary.setOnClickListener {

            val preferences =
                getSharedPreferences("MyDiaryData", MODE_PRIVATE)

            val savedData =
                preferences.getString("allDiaries", "[]")

            val diaryArray = JSONArray(savedData)

            if (diaryArray.length() == 0) {

                Toast.makeText(
                    this,
                    "No diary entries saved yet!",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            var message = ""

            for (i in 0 until diaryArray.length()) {

                val diary = diaryArray.getJSONObject(i)

                message +=
                    "📅 " + diary.getString("date") + "\n\n" +
                            "📝 Title: " + diary.getString("title") + "\n\n" +
                            "😊 Mood: " + diary.getString("mood") + "\n\n" +
                            "📖 Diary:\n" +
                            diary.getString("content") +
                            "\n\n━━━━━━━━━━━━━━\n\n"
            }

            AlertDialog.Builder(this)
                .setTitle("My All Diaries")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show()
        }

        // DELETE ALL DIARIES
        btnDeleteDiary.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("Delete All Diaries?")
                .setMessage("Are you sure you want to delete all diary entries?")
                .setPositiveButton("DELETE") { _, _ ->

                    val preferences =
                        getSharedPreferences("MyDiaryData", MODE_PRIVATE)

                    preferences.edit()
                        .remove("allDiaries")
                        .apply()

                    Toast.makeText(
                        this,
                        "All diary entries deleted!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setNegativeButton("CANCEL", null)
                .show()
        }
    }
}