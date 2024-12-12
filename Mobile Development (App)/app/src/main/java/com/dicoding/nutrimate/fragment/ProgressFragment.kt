package com.dicoding.nutrimate.fragment


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dicoding.nutrimate.R
import com.dicoding.nutrimate.api.ApiConfig2
import com.dicoding.nutrimate.api.ApiService2
import com.dicoding.nutrimate.data.ProgressRequest
import com.dicoding.nutrimate.data.UserPreferences
import com.dicoding.nutrimate.response.ProgressResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProgressFragment : Fragment(R.layout.fragment_progress) {

    private lateinit var saveProgressButton: Button
    private lateinit var bbProgressTextView: TextView
    private lateinit var tbProgressTextView: TextView
    private lateinit var progressTextView: TextView
    private lateinit var weightEditText: EditText
    private lateinit var heightEditText: EditText
    private lateinit var userPreferences: UserPreferences
    private lateinit var progressApiService: ApiService2
    private lateinit var loadingProgressBar: ProgressBar


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saveProgressButton = view.findViewById(R.id.saveprogress)
        bbProgressTextView = view.findViewById(R.id.bb_progress)
        tbProgressTextView = view.findViewById(R.id.tb_progess)
        progressTextView = view.findViewById(R.id.progress)
        weightEditText = view.findViewById(R.id.editberatbadam)
        heightEditText = view.findViewById(R.id.edittinggibadan)
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar)

        userPreferences = UserPreferences(requireContext())

        progressApiService = ApiConfig2.apiService

        saveProgressButton.setOnClickListener {
            val weight = weightEditText.text.toString()
            val height = heightEditText.text.toString()

            if (weight.isNotEmpty() && height.isNotEmpty()) {
                loadingProgressBar.visibility = View.VISIBLE
                userPreferences.saveHeightWeightStatus(height, weight, "Menunggu Status")
                sendDataToApi(height, weight)
            } else {
                Toast.makeText(requireContext(), "Tolong isi semua data", Toast.LENGTH_SHORT).show()
            }
        }
        val (savedHeight, savedWeight, savedStatus) = userPreferences.getHeightWeightStatus()
        if (savedHeight != null && savedWeight != null && savedStatus != null) {
            tbProgressTextView.text = "$savedHeight"
            bbProgressTextView.text = "$savedWeight"
            progressTextView.text = savedStatus
        }
    }

    private fun sendDataToApi(height: String, weight: String) {
        val progressRequest = ProgressRequest(height, weight, "")

        progressApiService.updateProgress(progressRequest).enqueue(object : Callback<ProgressResponse> {
            override fun onResponse(call: Call<ProgressResponse>, response: Response<ProgressResponse>) {
                loadingProgressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val progressData = response.body()?.progress
                    if (progressData != null) {
                        val status = progressData.status ?: "Tidak Diketahui"
                        userPreferences.saveHeightWeightStatus(progressData.height ?: "", progressData.weight ?: "", status)

                        tbProgressTextView.text = "${progressData.height}"
                        bbProgressTextView.text = "${progressData.weight}"
                        progressTextView.text = status

                        Toast.makeText(requireContext(), "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Terjadi kesalahan saat mengirim data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProgressResponse>, t: Throwable) {
                loadingProgressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "API Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}



