package com.musiq

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_start.setOnClickListener{
            val intent = Intent(this, ArtistActivity::class.java)
            //intent.putExtra(Constants.USER_NAME, et_name.text.toString())
            startActivity(intent)
            finish()
        }

    }
}