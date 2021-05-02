package com.example.hci_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hci_project.databinding.ActivitySearchSchoolBinding

class SearchSchoolActivity : AppCompatActivity() {
    lateinit var binding:ActivitySearchSchoolBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySearchSchoolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title= "유치원 검색"
    }
}