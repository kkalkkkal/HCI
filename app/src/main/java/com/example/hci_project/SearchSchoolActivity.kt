package com.example.hci_project

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.hci_project.bean.FilterSetting
import com.example.hci_project.bean.SearchResult
import com.example.hci_project.databinding.ActivitySearchSchoolBinding
import com.example.hci_project.dummy.DummySchoolContent

class SearchSchoolActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchSchoolBinding
    var filterSetting: FilterSetting? = null
    val FILTER_SET_ACTIVITY = 1234

    lateinit var simpleFilterFragment: FilterSimpleStatusFragment
    lateinit var searchResultFragment: SearchResultFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchSchoolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "유치원 검색"

        initListener()
        simpleFilterFragment =
            supportFragmentManager.findFragmentById(R.id.simple_filter_fragment)!! as FilterSimpleStatusFragment
        searchResultFragment =
            supportFragmentManager.findFragmentById(R.id.search_result_fragment)!! as SearchResultFragment
    }

    private fun renderSearchResult() {
        Toast.makeText(this, "검색/필터를 반영하여 결과를 표시합니다", Toast.LENGTH_SHORT).show()
        //filtering with filterSetting and editText values
        val keyword= binding.searchKeyword.text.toString().trim()

        val result = ArrayList<SearchResult>()
        for (idx in 1..20) {
            result.add(SearchResult(SearchResult.TYPE_SCHOOL, "유치원 ${idx}", "설명"))
        }
        result.shuffle()
        searchResultFragment.setList(result)
    }

    private fun initListener() {
        binding.apply {
            filterBtn.setOnClickListener {
                val intent = Intent(this@SearchSchoolActivity, SearchFilterActivity::class.java)
                filterSetting?.setCallback(null)
                intent.putExtra("filter", filterSetting)
                startActivityForResult(intent, FILTER_SET_ACTIVITY)
            }
            searchBtn.setOnClickListener {
                val keyword= binding.searchKeyword.text.toString().trim()
                search(keyword)
            }
        }
    }
    fun search(text:String){
        if(text.length< 2){
            Toast.makeText(applicationContext, "2자 이상을 입력해야 합니다", Toast.LENGTH_SHORT).show()
            return
        }
        binding.searchKeyword.setText(text)
        renderSearchResult()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //handle filterInfo
        if (requestCode == FILTER_SET_ACTIVITY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val filterSetting = data.getSerializableExtra("filter") as FilterSetting
                if (this.filterSetting == null){
                    this.filterSetting = filterSetting
                    this.filterSetting?.setCallback{
                        renderSearchResult()
                        simpleFilterFragment.updateFilter(filterSetting)
                    }
                    this.filterSetting?.callback?.run()
                }else{
                    this.filterSetting?.setCallback{
                        renderSearchResult()
                        simpleFilterFragment.updateFilter(filterSetting)
                    }
                    this.filterSetting?.overwrite(filterSetting)
                }

                Toast.makeText(applicationContext, "필터가 적용되었습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        filterSetting?.setCallback(null)
    }
}