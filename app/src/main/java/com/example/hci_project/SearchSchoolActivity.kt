package com.example.hci_project

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hci_project.databinding.ActivitySearchSchoolBinding

class SearchSchoolActivity : AppCompatActivity() {
    lateinit var binding:ActivitySearchSchoolBinding
    val FILTER_SET_ACTIVITY= 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySearchSchoolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title= "유치원 검색"

        initListener()
    }

    fun initListener(){
        binding.apply {
            filterBtn.setOnClickListener{
                val intent= Intent(this@SearchSchoolActivity, SearchFilterActivity::class.java)
                startActivityForResult(intent, FILTER_SET_ACTIVITY)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //handle filterInfo
        if(requestCode == FILTER_SET_ACTIVITY && resultCode == Activity.RESULT_OK){
            Toast.makeText(this, "필터가 적용되었습니다", Toast.LENGTH_SHORT).show()
        }
    }
}