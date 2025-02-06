package com.example.kotlinapp.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.kotlinapp.R
import com.example.kotlinapp.databinding.ActivityDetailBinding
import com.example.kotlinapp.model.DoctorsModel

class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var item: DoctorsModel? = null  // Handle potential null cases

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBundle()

        val makeAppointmentButton: Button = findViewById(R.id.makeBtn)
        makeAppointmentButton.setOnClickListener {
            item?.let {
                val intent = Intent(this, BookAppointmentActivity::class.java)
                intent.putExtra("doctor_name", it.Name) // Pass doctor's name
                intent.putExtra("doctor_id", it.Id) // Pass doctor's ID if available
                startActivity(intent)
            } ?: run {
                Toast.makeText(this, "Error: Doctor details not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getBundle() {
        item = intent.getParcelableExtra("object")

        if (item == null) {
            Toast.makeText(this, "Error loading doctor details!", Toast.LENGTH_SHORT).show()
            finish() // Exit activity if no data
            return
        }

        binding.apply {
            specialTxtHere.text = item!!.Special
            patientTxt.text = item!!.Patiens
            bioTxt.text = item!!.Biography
            addressTxt.text = item!!.Address
            timeTxt.text = item!!.Time
            dateTxt.text = item!!.Date
            experienceTxt.text = "${item!!.Expriense} Years"
            ratingTxt.text = "${item!!.Rating}"

            backBtn.setOnClickListener { finish() }

            websiteBtn.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW, Uri.parse(item!!.Site))
                startActivity(i)
            }

            messageBtn.setOnClickListener {
                val uri = Uri.parse("smsto:${item!!.Mobile}")
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                intent.putExtra("sms_body", "This SMS text")
                startActivity(intent)
            }

            callBtn.setOnClickListener {
                val uri = "tel:${item!!.Mobile.trim()}"
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(uri))
                startActivity(intent)
            }

            directionBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item!!.Location))
                startActivity(intent)
            }

            shareBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, item!!.Name)
                    putExtra(Intent.EXTRA_TEXT, "${item!!.Name} - ${item!!.Address} - ${item!!.Mobile}")
                }
                startActivity(Intent.createChooser(intent, "Choose one"))
            }

            // Load image using Glide
            Glide.with(this@DetailActivity)
                .load(item!!.Picture)
                .into(imgPart)
        }
    }
}
