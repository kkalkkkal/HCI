package com.example.hci_project

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hci_project.bean.FilterSetting
import com.example.hci_project.databinding.ActivitySearchFilterBinding

class SearchFilterActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchFilterBinding
    private val filterSetting: FilterSetting = FilterSetting()
    private lateinit var facilitateImageButtonLayoutParams: ViewGroup.LayoutParams

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "검색 필터 설정"

        initComponent()
        initListener()
    }

    private fun initComponent() {
        facilitateImageButtonLayoutParams = binding.facilitiesDummy.layoutParams
        renderFacilitateList()

        binding.apply {
            //TODO: seekBar 값이 바뀔 때 마다 filterSetting에 저장
            schoolSizeMaxBar.max = 4
            schoolSizeMaxBar.progress = 0
            schoolSizeMaxValue.text = "제한없음"
            schoolSizeMaxBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (progress == 0) {
                        schoolSizeMaxValue.text = "제한없음"
                    } else {
                        schoolSizeMaxValue.text = "${(progress) * 30}평 이상"
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })


            kidsPerTeacherMaxBar.max = 4
            kidsPerTeacherMaxBar.progress = 0
            kidsPerTeacherMaxValue.text = "제한없음"
            kidsPerTeacherMaxBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (progress == 0) {
                        kidsPerTeacherMaxValue.text = "제한없음"
                    } else {
                        kidsPerTeacherMaxValue.text = "${(progress + 1) * 10}명 이하"
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })



            schoolDistanceMaxBar.max = 4
            schoolDistanceMaxBar.progress = 0
            schoolDistanceMaxValue.text = "반경 ${schoolDistanceMaxBar.progress + 1}km 내"
            schoolDistanceMaxBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    schoolDistanceMaxValue.text = "반경 ${(progress + 1)}km 내"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
        }
    }

    private fun renderFacilitateList() {
        binding.apply {
            facilitiesList.removeAllViews()

            FilterSetting.Facilitate.getAll().map { facilitate ->
                val btn: ImageButton = layoutInflater.inflate(R.layout.facilitate_button, null) as ImageButton
                val iconDrawable: Drawable = resources.getDrawable(facilitate.icon, null)
                iconDrawable.setTint(if (filterSetting.facilitates.contains(facilitate)) Color.BLACK else Color.GRAY)
                btn.setImageDrawable(iconDrawable)
                btn.setOnClickListener {
                    if (filterSetting.facilitates.contains(facilitate)) {
                        Toast.makeText(this@SearchFilterActivity, "${facilitate.name} 필터가 해제되었습니다", Toast.LENGTH_SHORT).show()
                        filterSetting.facilitates.remove(facilitate)
                    } else {
                        Toast.makeText(this@SearchFilterActivity, "${facilitate.name} 필터가 적용되었습니다", Toast.LENGTH_SHORT).show()
                        filterSetting.facilitates.add(facilitate)
                    }
                    renderFacilitateList()
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
                setResult(Activity.RESULT_OK)
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
}