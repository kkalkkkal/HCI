package com.example.hci_project

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.hci_project.bean.*
import com.example.hci_project.databinding.ActivitySearchSchoolBinding

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
        if (
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission_group.LOCATION
                ) == PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            LocationUtil.requestUserLocation(this)
        }
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
                val keyword = binding.searchKeyword.text.toString().trim()
                search(keyword)
            }

            searchKeyword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s.toString().contains("\n")) {
                        searchKeyword.setText(s.toString().replace("\n", ""))
                        search(searchKeyword.text.toString())
                    }
                }

            })
        }
    }

    fun search(text: String) {
        if (text.length < 2) {
            Toast.makeText(applicationContext, "2자 이상을 입력해야 합니다", Toast.LENGTH_SHORT).show()
            return
        }
        SearchResultManager.getInstance().use(this) { manager ->
            manager?.apply {
                searchResultList.add(0, SearchResult(SearchResult.TYPE_SEARCH, text.trim()))
                save(applicationContext)
            }
        }
        if(binding.searchKeyword.text.toString()!= text){
            binding.searchKeyword.setText(text)
        }
        renderSearchResult()
    }

    private fun renderSearchResult() {
        //filtering with filterSetting and editText values
        val keyword = binding.searchKeyword.text.toString().trim()

        SchoolManager.getInstance().use(this) {
            if (!isDestroyed)
                runOnUiThread {
                    if (it == null) {
                        Toast.makeText(applicationContext, "유치원 정보를 로드할 수 없습니다", Toast.LENGTH_SHORT)
                            .show()
                        return@runOnUiThread
                    }
                    val resultList = it.search(keyword)
                    if (resultList.isEmpty()) {
                        searchResultFragment.setList(ArrayList())
                        Toast.makeText(applicationContext, "해당하는 유치원이 없습니다", Toast.LENGTH_SHORT)
                            .show()
                        return@runOnUiThread
                    }
                    Toast.makeText(this, "검색/필터를 반영하여 ${resultList.size}개의 결과를 표시합니다", Toast.LENGTH_SHORT).show()
                    searchResultFragment.setList(SearchResult.convert(resultList))
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //handle filterInfo
        if (requestCode == FILTER_SET_ACTIVITY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val filterSetting = data.getSerializableExtra("filter") as FilterSetting
                if (this.filterSetting == null) {
                    this.filterSetting = filterSetting
                    this.filterSetting?.setCallback {
                        renderSearchResult()
                        simpleFilterFragment.updateFilter(filterSetting)
                    }
                    this.filterSetting?.callback?.run()
                } else {
                    this.filterSetting?.setCallback {
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