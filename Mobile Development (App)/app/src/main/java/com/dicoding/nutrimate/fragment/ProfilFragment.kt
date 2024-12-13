package com.dicoding.nutrimate.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dicoding.nutrimate.R
import com.dicoding.nutrimate.data.UserPreferences
import com.dicoding.nutrimate.databinding.FragmentProfilBinding
import com.dicoding.nutrimate.login.LoginActivity

class ProfilFragment : Fragment() {

    private lateinit var userPreferences: UserPreferences
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfilBinding.inflate(inflater, container, false)

        userPreferences = UserPreferences(requireContext())

        userNameTextView = binding.userName
        logoutButton = binding.logout

        val userData = userPreferences.getUserData()
        if (userData != null) {
            userNameTextView.text = userData.name
        }

        logoutButton.setOnClickListener {
            userPreferences.clearUserData()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return binding.root
    }
}
