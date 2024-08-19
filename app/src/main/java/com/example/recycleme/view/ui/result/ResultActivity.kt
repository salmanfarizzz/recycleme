package com.example.recycleme.view.ui.result

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.recycleme.R
import com.example.recycleme.data.remote.response.PredictionResponse

class ResultActivity : AppCompatActivity() {

    private lateinit var resultImageView: ImageView
    private lateinit var resultText: TextView
    private lateinit var adviseText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result1)

        resultImageView = findViewById(R.id.result_image)
        resultText = findViewById(R.id.result_text1)
        adviseText = findViewById(R.id.advise_text1)

        val prediction = intent.getParcelableExtra<PredictionResponse>("result")
        if (prediction != null) {
            Glide.with(this).load(prediction.storage_url).into(resultImageView)
            resultText.text = prediction.message
            adviseText.text = prediction.category
        }
    }
}
