package com.example.hci_project

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hci_project.bean.FilterSetting
import com.example.hci_project.databinding.ActivitySearchFilterBinding

class SearchFilterActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchFilterBinding
    private lateinit var filterSetting: FilterSetting
    private lateinit var facilitateImageButtonLayoutParams: ViewGroup.LayoutParams

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        filterSetting = if (intent.getSerializableExtra("filter") != null) {
            FilterSetting(intent.getSerializableExtra("filter") as FilterSetting)
        } else {
            FilterSetting()
        }

        supportActionBar?.title = "검색 필터 설정"
        initComponent()
        initListener()

        filterSetting.setCallback {
            renderFacilitateList()
            renderFilterValues()
        }

    }

    private fun initComponent() {
        facilitateImageButtonLayoutParams = binding.facilitiesDummy.layoutParams
        renderFacilitateList()

        binding.apply {
            schoolSizeMaxBar.max = 4
            schoolSizeMaxBar.progress = filterSetting.minSchoolSize / 30
            schoolSizeMaxValue.text = "제한없음"
            schoolSizeMaxBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    filterSetting.use {
                        it.minSchoolSize = progress * 30
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })


            kidsPerTeacherMaxBar.max = 4
            kidsPerTeacherMaxBar.progress =
                if (filterSetting.maxKidsPerTeacher == 0) 0 else (filterSetting.maxKidsPerTeacher / 10) - 1
            kidsPerTeacherMaxBar.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    filterSetting.use {
                        it.maxKidsPerTeacher = if (progress == 0) progress else (progress + 1) * 10
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })


            schoolDistanceMaxBar.max = 5
            schoolDistanceMaxBar.progress = filterSetting.maxDistanceKmFromHere
            schoolDistanceMaxBar.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    filterSetting.use {
                        it.maxDistanceKmFromHere = progress
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })

            val hours = ArrayList<String>()
            hours.add("제한없음")
            for (hour in 1..24) {
                hours.add(hour.toString() + "시")
            }

            openTimeSpinner.adapter =
                ArrayAdapter(this@SearchFilterActivity, android.R.layout.simple_list_item_1, hours)
            openTimeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    filterSetting.use {
                        filterSetting.schoolStartHour =
                            if (position != 0) hours[position].replace("시", "")
                                .toInt() else 0
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            if (filterSetting.schoolStartHour != 0)
                openTimeSpinner.setSelection(filterSetting.schoolStartHour)
            closeTimeSpinner.adapter =
                ArrayAdapter(this@SearchFilterActivity, android.R.layout.simple_list_item_1, hours)
            closeTimeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    filterSetting.use {
                        filterSetting.schoolEndHour =
                            if (position != 0) hours[position].replace("시", "")
                                .toInt() else 0
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            if (filterSetting.schoolEndHour != 0)
                closeTimeSpinner.setSelection(filterSetting.schoolEndHour)

            renderFilterValues()
        }
    }

    private fun renderFilterValues() {
        filterSetting.apply {
            binding.schoolSizeMaxValue.text =
                if (minSchoolSize == 0) "제한없음" else "${minSchoolSize}평 이상"
            binding.kidsPerTeacherMaxValue.text =
                if (maxKidsPerTeacher == 0) "제한없음" else "${maxKidsPerTeacher}명 이하"
            binding.schoolDistanceMaxValue.text =
                if (maxDistanceKmFromHere == 5) "제한없음" else "반경 ${maxDistanceKmFromHere + 1}km 내"
        }
    }

    private fun renderFacilitateList() {
        binding.apply {
            facilitiesList.removeAllViews()

            FilterSetting.Facilitate.getAll().map { facilitate ->
                val btn: ImageButton =
                    layoutInflater.inflate(R.layout.facilitate_button, null) as ImageButton
                val iconDrawable: Drawable = resources.getDrawable(facilitate.icon, null)
                iconDrawable.setTint(if (filterSetting.facilitates.contains(facilitate)) Color.BLACK else Color.GRAY)
                btn.setImageDrawable(iconDrawable)
                btn.setOnClickListener {
                    if (filterSetting.facilitates.contains(facilitate)) {
                        filterSetting.use { filterSetting ->
                            filterSetting.facilitates.remove(facilitate)
                            Toast.makeText(
                                this@SearchFilterActivity,
                                "${facilitate.name} 필터가 해제되었습니다",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@SearchFilterActivity,
                            "${facilitate.name} 필터가 적용되었습니다",
                            Toast.LENGTH_SHORT
                        ).show()

                        filterSetting.use { filterSetting ->
                            filterSetting.facilitates.add(facilitate)
                        }
                    }
                }
                btn.layoutParams = facilitateImageButtonLayoutParams
                facilitiesList.addView(btn)
            }
            facilitiesList.requestLayout()
        }
    }

    private fun initListener() {
        binding.apply {
            filterSetFinishBtn.setOnClickListener {
                filterSetting.setCallback(null)
                val intent = Intent()
                intent.putExtra("filter", filterSetting)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("경고")
            .setMessage("필터를 적용하지 않고 나가시겠습니까?")
            .setPositiveButton("네") { dialog, _ ->
                dialog.dismiss()
                super.onBackPressed()
            }
            .setNegativeButton("아니오") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()

        filterSetting.setCallback(null)
    }
}