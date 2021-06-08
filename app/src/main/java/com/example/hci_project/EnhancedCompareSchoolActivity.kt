package com.example.hci_project

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.hci_project.bean.LocationUtil
import com.example.hci_project.bean.School
import com.example.hci_project.bean.SchoolManager
import com.example.hci_project.databinding.ActivityEnhancedCompareSchoolBinding


class EnhancedCompareSchoolActivity : AppCompatActivity() {
    lateinit var binding: ActivityEnhancedCompareSchoolBinding
    private val materialColorList = ChipAdapter.colors
    lateinit var schoolList: ArrayList<School>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEnhancedCompareSchoolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "유치원 비교"
        if (!intent.hasExtra("list")) {
            throw IllegalStateException("extra list is not passed")
        }
        schoolList = intent.getSerializableExtra("list") as ArrayList<School>

        renderCompareEntries()
        if(LocationUtil.requirePermission(this, "거리 정보를 보려면 위치 권한이 필요합니다")){
            LocationUtil.requestUserLocation(this){
                renderCompareEntries()
            }
        }
    }

    private fun renderCompareEntries() {
        val compareTypes = ArrayList<String>()
        val compareEntries = ArrayList<ArrayList<MaterialItemAdapter.MaterialItem>>()
        val builder = CompareEntryAdapter.Builder(materialColorList)
        compareTypes.add("주소")
        schoolList.map {
            builder.append(
                MaterialItemAdapter.MaterialItem(
                    it.name,
                    R.drawable.ic_baseline_apartment_24,
                    it.addr
                ) {
                    openSchoolInfoActivity(it)
                }
            )
        }
        compareEntries.add(builder.build())
        compareTypes.add("거리")
        schoolList.map {
            builder.append(
                MaterialItemAdapter.MaterialItem(
                    it.name,
                    R.drawable.ic_baseline_commute_24,
                    if (it.getDistanceFromUserLocation() != 0f) "거리: ${"%.2f km".format(it.getDistanceFromUserLocation())}\n(걸어서 약 ${
                        "%d분".format(
                            LocationUtil.getGoingTime(
                                it.getDistanceFromUserLocation().toDouble(),
                                4.0
                            ).toInt()
                        )
                    })" else "거리 정보 없음"
                ) {
                    openSchoolInfoActivity(it)
                }
            )
        }
        compareEntries.add(builder.build())
        compareTypes.add("설립/운영형태")
        schoolList.map {
            builder.append(
                MaterialItemAdapter.MaterialItem(
                    it.name,
                    R.drawable.ic_baseline_apartment_24,
                    "%s (%s)".format(it.getOnlySchoolType(), it.getServiceType())
                ) {
                    openSchoolInfoActivity(it)
                }
            )
        }
        compareEntries.add(builder.build())
        compareTypes.add("운영시간")
        schoolList.map {
            builder.append(
                MaterialItemAdapter.MaterialItem(
                    it.name,
                    R.drawable.ic_baseline_access_time_24,
                    if (it.serviceTime != null) it.serviceTime!!.toString() else "운영시간 정보 없음"
                ) {
                    openSchoolInfoActivity(it)
                }
            )
        }
        compareEntries.add(builder.build())
        compareTypes.add("급식운영")
        schoolList.map {
            builder.append(
                MaterialItemAdapter.MaterialItem(
                    it.name,
                    R.drawable.ic_baseline_set_meal_24,
                    if (it.mealServiceType != null) "${it.mealServiceType}" else "급식운영 정보 없음"
                ) {
                    openSchoolInfoActivity(it)
                }
            )
        }
        compareEntries.add(builder.build())
        compareTypes.add("학생/교사 수 정보")
        schoolList.map {
            builder.append(
                MaterialItemAdapter.MaterialItem(
                    it.name,
                    R.drawable.ic_baseline_escalator_warning_24,
                    if (it.getKidsPerTeacher() != 0f) "교사 수: ${it.teacherCnt}명 / 학생 수 : ${it.currentStudentCnt}명\n(교사 당 학생 ${
                        "%.2f".format(
                            it.getKidsPerTeacher()
                        )
                    }명)" else "학생/교사 수 정보 없음"
                ) {
                    openSchoolInfoActivity(it)
                }
            )
        }
        compareEntries.add(builder.build())
        compareTypes.add("스쿨버스 운영 여부")
        schoolList.map {
            builder.append(
                MaterialItemAdapter.MaterialItem(
                    it.name,
                    R.drawable.ic_baseline_directions_bus_24,
                    if (it.isAvailableBus) "스쿨버스 운영" else "스쿨버스 미운영"
                ) {
                    openSchoolInfoActivity(it)
                }
            )
        }
        compareEntries.add(builder.build())

        val chipBuilder = ChipAdapter.Chip.builder()
        schoolList.map {
            chipBuilder.append(it.name) {
                SchoolManager.getInstance().use(this@EnhancedCompareSchoolActivity) { manager ->
                    if (manager == null)
                        return@use

                    val target = manager.list.find { _school ->
                        it.name == _school.name
                    }
                    if (target != null) {
                        val intent = Intent(
                            this@EnhancedCompareSchoolActivity,
                            SchoolInfoActivity::class.java
                        )
                        intent.putExtra("school", target)
                        startActivity(intent)
                    }
                }
            }
        }

        binding.apply {
            schoolList.adapter = ChipAdapter(chipBuilder.end())
            compareVisualizerList.adapter = CompareEntryAdapter(compareTypes, compareEntries)
        }
    }
    private fun openSchoolInfoActivity(school: School){
        SchoolManager.getInstance().use(this@EnhancedCompareSchoolActivity) { manager ->
            if (manager == null)
                return@use

            val target = manager.list.find { _school ->
                school.name == _school.name
            }
            if (target != null) {
                val intent = Intent(
                    this@EnhancedCompareSchoolActivity,
                    SchoolInfoActivity::class.java
                )
                intent.putExtra("school", target)
                startActivity(intent)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(LocationUtil.isPermissionGranted(this)){
            LocationUtil.requestUserLocation(this){
                renderCompareEntries()
            }
        }
    }
}