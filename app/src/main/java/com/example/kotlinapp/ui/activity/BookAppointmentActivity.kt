package com.example.kotlinapp.ui.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinapp.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class BookAppointmentActivity : AppCompatActivity() {

    private lateinit var doctorName: TextView
    private lateinit var patientName: EditText
    private lateinit var patientEmail: EditText
    private lateinit var dateTxt: TextView
    private lateinit var datePickerButton: Button
    private lateinit var timeTxt: TextView
    private lateinit var timePickerButton: Button
    private lateinit var confirmButton: Button

    private var selectedDate: String? = null
    private var selectedTime: String? = null

    private lateinit var database: DatabaseReference
    private var doctorId: Int = -1 // Default invalid ID
    private var doctorNameStr: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appointment)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("appointments")

        // Initialize views
        doctorName = findViewById(R.id.doctorName)
        patientName = findViewById(R.id.patientName)
        patientEmail = findViewById(R.id.patient_email)
        dateTxt = findViewById(R.id.dateTxt)
        datePickerButton = findViewById(R.id.date_picker_actions)
        timeTxt = findViewById(R.id.timeTxt)
        timePickerButton = findViewById(R.id.material_timepicker_view)
        confirmButton = findViewById(R.id.confirm_button)

        // Retrieve doctor details from Intent
        doctorId = intent.getIntExtra("doctor_id", -1)
        doctorNameStr = intent.getStringExtra("doctor_name") ?: "Unknown Doctor"
        doctorName.text = doctorNameStr

        // Set up Date Picker
        datePickerButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val selectedCalendar = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }
                    selectedDate = sdf.format(selectedCalendar.time)
                    dateTxt.text = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // Disable past dates
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        }

        // Set up Time Picker
        timePickerButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    val time = String.format("%02d:%02d", hourOfDay, minute)
                    selectedTime = time
                    timeTxt.text = selectedTime
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePickerDialog.show()
        }

        // Confirm Appointment Button
        confirmButton.setOnClickListener {
            val name = patientName.text.toString().trim()
            val email = patientEmail.text.toString().trim()

            if (validateInput(name, email)) {
                showConfirmationDialog(name, email)
            }
        }
    }

    private fun validateInput(name: String, email: String): Boolean {
        return when {
            name.isEmpty() -> {
                showToast("Please enter your name.")
                false
            }
            email.isEmpty() -> {
                showToast("Please enter your email.")
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showToast("Invalid email format.")
                false
            }
            selectedDate.isNullOrEmpty() -> {
                showToast("Please select a date.")
                false
            }
            selectedTime.isNullOrEmpty() -> {
                showToast("Please select a time.")
                false
            }
            else -> true
        }
    }

    private fun showConfirmationDialog(name: String, email: String) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Appointment")
            .setMessage(
                "Are you sure you want to book an appointment for:\n\n" +
                        "👨‍⚕ Doctor: $doctorNameStr\n" +
                        "🧑 Patient: $name\n" +
                        "📧 Email: $email\n" +
                        "📅 Date: $selectedDate\n" +
                        "⏰ Time: $selectedTime"
            )
            .setPositiveButton("Confirm") { _, _ ->
                saveAppointmentToFirebase(name, email)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveAppointmentToFirebase(name: String, email: String) {
        val appointmentId = database.push().key ?: return

        val appointment = mapOf(
            "doctorId" to doctorId,
            "doctorName" to doctorNameStr,
            "patientName" to name,
            "patientEmail" to email,
            "date" to selectedDate,
            "time" to selectedTime
        )

        database.child(appointmentId).setValue(appointment)
            .addOnSuccessListener {
                showToast("Appointment booked successfully!")
            }
            .addOnFailureListener { e ->
                showToast("Failed to book appointment: ${e.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
