package com.example.mydiary

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnChangePassword =
            findViewById<Button>(R.id.btnChangePassword)

        val preferences =
            getSharedPreferences("MyDiaryPassword", MODE_PRIVATE)

        btnLogin.setOnClickListener {

            val enteredPassword =
                etPassword.text.toString().trim()

            val savedPassword =
                preferences.getString("password", "1234")

            if (enteredPassword == savedPassword) {

                Toast.makeText(
                    this,
                    "Diary Unlocked!",
                    Toast.LENGTH_SHORT
                ).show()

                val intent =
                    Intent(this, MainActivity::class.java)

                startActivity(intent)
                finish()

            } else {

                Toast.makeText(
                    this,
                    "Wrong Password!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // CHANGE PASSWORD
        btnChangePassword.setOnClickListener {

            val currentPasswordInput = EditText(this)
            currentPasswordInput.hint = "Enter current password"
            currentPasswordInput.inputType = 129

            AlertDialog.Builder(this)
                .setTitle("Verify Password")
                .setMessage("Enter your current password")
                .setView(currentPasswordInput)

                .setPositiveButton("NEXT") { _, _ ->

                    val currentPassword =
                        currentPasswordInput.text.toString().trim()

                    val savedPassword =
                        preferences.getString("password", "1234")

                    if (currentPassword == savedPassword) {

                        // Ask for new password
                        val newPasswordInput = EditText(this)
                        newPasswordInput.hint = "Enter new password"
                        newPasswordInput.inputType = 129

                        AlertDialog.Builder(this)
                            .setTitle("Change Password")
                            .setMessage("Enter your new diary password")
                            .setView(newPasswordInput)

                            .setPositiveButton("SAVE") { _, _ ->

                                val newPassword =
                                    newPasswordInput.text.toString().trim()

                                if (newPassword.isNotEmpty()) {

                                    preferences.edit()
                                        .putString("password", newPassword)
                                        .apply()

                                    Toast.makeText(
                                        this,
                                        "Password changed successfully!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } else {

                                    Toast.makeText(
                                        this,
                                        "Please enter a new password",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            .setNegativeButton("CANCEL", null)
                            .show()

                    } else {

                        Toast.makeText(
                            this,
                            "Current password is wrong!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                .setNegativeButton("CANCEL", null)
                .show()
        }
    }
}