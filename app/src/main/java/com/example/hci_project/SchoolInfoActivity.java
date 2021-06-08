package com.example.hci_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hci_project.bean.FavoriteSchoolManager;
import com.example.hci_project.bean.LocationUtil;
import com.example.hci_project.bean.Safety;
import com.example.hci_project.bean.School;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import kotlin.Unit;

public class SchoolInfoActivity extends AppCompatActivity implements OnMapReadyCallback {


    private TableLayout tableLayout, tableLayout2, tableLayout3;
    private NaverMap naverMap2;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    public FusedLocationSource locationSource;
    CameraPosition cameraPosition;
    private Marker marker; // 마커
    private School school;
    private ArrayList<MaterialItemAdapter.MaterialItem> schoolDescList = new ArrayList<>();


    @Override
    public void onMapReady(@NonNull @NotNull NaverMap naverMap) {
        this.naverMap2 = naverMap;


        naverMap2.setMapType(NaverMap.MapType.Basic); // 기본형 지도
        naverMap2.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true); // 빌딩 그룹 생성

        // 네이버 지도 설정
        cameraPosition = new CameraPosition(new LatLng(school.getLat(), school.getLng()), 13);// 넘겨받은 위치 기준으로 연다

        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(school.getLat(), school.getLng()));
        naverMap2.moveCamera(cameraUpdate);



        //네이버 지도 추가 UI 설정
        UiSettings uiSettings = naverMap2.getUiSettings();
        uiSettings.setLocationButtonEnabled(false); // 현 위치 버튼 활성화
        uiSettings.setTiltGesturesEnabled(false); // 특수 제스처 기능 봉인
        uiSettings.setZoomControlEnabled(false);

        // 마커 표시하기 (자기 위치 기준 검색)
        marker = new Marker();
        marker.setMap(null); // 기존 마커 삭제
        marker.setPosition(new LatLng(school.getLat(), school.getLng())); // 그냥 건국대 임시 마커
        if (school.getType().contains("어린이집")) {
            marker.setIcon(MarkerIcons.BLACK);
            marker.setIconTintColor(Color.rgb(170, 0, 170)); // 빨간 색 + 파란색 = 보라색
        } else {
            marker.setIcon(MarkerIcons.YELLOW); // 마커 색깔, 유치원은 노랑
        }
        marker.setMap(naverMap2); // 마커 표시

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_info);

        Intent intent = getIntent(); /*데이터 수신*/

        if (intent.hasExtra("school")) {
            school = (School) intent.getSerializableExtra("school");
        }
        if (school == null) {
            throw new IllegalArgumentException("school object is not passed");
        }

        TextView shcool_name = findViewById(R.id.school_name);
        TextView school_addr = findViewById(R.id.school_addr);

        ImageButton compareBtn = findViewById(R.id.compare_add);
        ImageButton favoriteBtn = findViewById(R.id.addFavoriteBtn);
        compareBtn.setOnClickListener(v -> {
            Intent __intent = new Intent(SchoolInfoActivity.this, CompareSchoolActivity.class);
            __intent.putExtra("school", school);
            __intent.putExtra("last", 'b');
            startActivity(__intent);
            finish();
        });
        FavoriteSchoolManager.Companion.getInstance().use(this, manager -> {
            if (manager == null)
                return Unit.INSTANCE;

            if (manager.isFavorite(school)) {
                favoriteBtn.setColorFilter(getResources().getColor(R.color.yellow));
            } else {
                favoriteBtn.setColorFilter(getResources().getColor(R.color.gray_light));
            }
            favoriteBtn.setOnClickListener(v -> {
                //action
                if (manager.isFavorite(school)) {
                    Iterator<School> iterator = manager.getList().iterator();
                    while (iterator.hasNext()) {
                        School s = iterator.next();
                        if (s.getName().equals(school.getName())) {
                            iterator.remove();
                        }
                    }
                } else {
                    manager.getList().add(school);
                }
                //ui update
                if (manager.isFavorite(school)) {
                    Toast.makeText(this, "즐겨찾기에 추가되었습니다", Toast.LENGTH_SHORT).show();
                    favoriteBtn.setColorFilter(getResources().getColor(R.color.yellow));
                } else {
                    Toast.makeText(this, "즐겨찾기에서 삭제되었습니다", Toast.LENGTH_SHORT).show();
                    favoriteBtn.setColorFilter(getResources().getColor(R.color.gray_light));
                }
                //save action result
                manager.save(SchoolInfoActivity.this);
            });

            return Unit.INSTANCE;
        });

        renderSchoolDescribers();
        if (LocationUtil.Companion.isPermissionGranted(this)) {
            LocationUtil.Companion.requestUserLocation(this, (location) -> {
                if (school.getDistanceFromUserLocation() != 0f) {
                    schoolDescList.add(
                            new MaterialItemAdapter.MaterialItem(
                                    R.drawable.ic_baseline_commute_24,
                                    String.format("거리: %.2fkm\n(걸어서 약 %d분)",
                                            school.getDistanceFromUserLocation(),
                                            (int) LocationUtil.Companion.getGoingTime(school.getDistanceFromUserLocation(), 4)),
                                    () -> {
                                        return Unit.INSTANCE;
                                    })
                    );
                    RecyclerView recyclerView = findViewById(R.id.school_describe_list);
                    if (recyclerView.getAdapter() != null) {
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }

                return Unit.INSTANCE;
            });
        } else {
            LocationUtil.Companion.requirePermission(this, "현재 위치에서부터 선택한 유치원의 거리를 알기 위해서 위치 권한이 필요합니다");
        }


        // 네이버 지도 호출하기
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map_fragment);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_fragment, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        // 이름
        shcool_name.setText(school.getName());
        school_addr.setText(school.getAddr());

    }

    private void renderSchoolDescribers() {
        RecyclerView recyclerView = findViewById(R.id.school_describe_list);
        {
            MaterialItemAdapter.MaterialItem item = new MaterialItemAdapter.MaterialItem(R.drawable.ic_baseline_apartment_24, String.format("%s (%s)", school.getOnlySchoolType(), school.getServiceType()), () -> {
                return Unit.INSTANCE;
            });
            item.setTint(R.color.yellow);
            schoolDescList.add(item);
        }
        if (school.getServiceTime() != null) {
            schoolDescList.add(new MaterialItemAdapter.MaterialItem(R.drawable.ic_baseline_access_time_24, "운영시간: " + school.getServiceTime().toString(), () -> {
                return Unit.INSTANCE;
            }));
        }
        schoolDescList.add(new MaterialItemAdapter.MaterialItem(R.drawable.ic_baseline_call_24, school.getTel(), () -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", school.getTel(), null));
            startActivity(intent);
            return Unit.INSTANCE;
        }));
        if (!school.getHomePage().isEmpty()) {
            schoolDescList.add(new MaterialItemAdapter.MaterialItem(R.drawable.ic_baseline_home_24, school.getHomePage(), () -> {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(school.getHomePage()));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return Unit.INSTANCE;
            }));
        } else {
            MaterialItemAdapter.MaterialItem item = new MaterialItemAdapter.MaterialItem(R.drawable.ic_baseline_home_24, "홈페이지 정보 없음", () -> {
                return Unit.INSTANCE;
            });
            item.setTint(R.color.gray_light);
            schoolDescList.add(item);
        }

        if (school.getMealServiceType() == null) {
            MaterialItemAdapter.MaterialItem item = new MaterialItemAdapter.MaterialItem(R.drawable.ic_baseline_set_meal_24, "급식운영 정보 없음", () -> {
                return Unit.INSTANCE;
            });
            item.setTint(R.color.gray_light);
            schoolDescList.add(item);
        } else {
            String mealDesc = "급식운영: " + school.getMealServiceType();
            schoolDescList.add(new MaterialItemAdapter.MaterialItem(R.drawable.ic_baseline_set_meal_24, mealDesc, () -> {
                return Unit.INSTANCE;
            }));
        }
        if (school.getCurrentStudentCnt() != 0 && school.getTeacherCnt() != 0) {
            String desc = String.format("교사 수: %d명 / 학생 수: %d명\n(교사 당 학생 %.2f명)", school.getTeacherCnt(), school.getCurrentStudentCnt(), school.getKidsPerTeacher());
            schoolDescList.add(new MaterialItemAdapter.MaterialItem(R.drawable.ic_baseline_escalator_warning_24, desc, () -> {
                return Unit.INSTANCE;
            }));
        } else {
            MaterialItemAdapter.MaterialItem item = new MaterialItemAdapter.MaterialItem(R.drawable.ic_baseline_escalator_warning_24, "학생/교사 수 정보 없음", () -> {
                return Unit.INSTANCE;
            });
            item.setTint(R.color.gray_light);
            schoolDescList.add(item);
        }
        if (school.isAvailableBus()) {
            schoolDescList.add(new MaterialItemAdapter.MaterialItem(R.drawable.ic_baseline_directions_bus_24, "스쿨버스 운영", () -> {
                return Unit.INSTANCE;
            }));
        } else {
            MaterialItemAdapter.MaterialItem item = new MaterialItemAdapter.MaterialItem(R.drawable.ic_baseline_directions_bus_24, "스쿨버스 운영 안함/정보없음", () -> {
                return Unit.INSTANCE;
            });
            item.setTint(R.color.gray_light);
            schoolDescList.add(item);
        }
        if (school.getSafety() != null) {
            Safety safety = school.getSafety();
            schoolDescList.add(new MaterialItemAdapter.MaterialItem(R.drawable.ic_baseline_camera_alt_24, String.format("CCTV 설치 대수: 총 %d대", safety.getCctvCnt()), () -> {
                return Unit.INSTANCE;
            }));
            if (safety.getFireCheck()) {
                schoolDescList.add(new MaterialItemAdapter.MaterialItem(R.drawable.ic_baseline_fire_extinguisher_24, "소방 안전 검사 완료", () -> {
                    return Unit.INSTANCE;
                }));
            }
            if (safety.getEscapeCheck()) {
                schoolDescList.add(new MaterialItemAdapter.MaterialItem(R.drawable.ic_baseline_run_circle_24, "소방 대피 훈련 완료", () -> {
                    return Unit.INSTANCE;
                }));
            }
            if (safety.getGasCheck()) {
                schoolDescList.add(new MaterialItemAdapter.MaterialItem(R.drawable.ic_baseline_cloud_done_24, "가스 안전 검사 완료", () -> {
                    return Unit.INSTANCE;
                }));
            }
        }
        recyclerView.setAdapter(new MaterialItemAdapter(schoolDescList));
        recyclerView.setNestedScrollingEnabled(false);

        NestedScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.post(() -> {
            scrollView.scrollTo(0, 0);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (LocationUtil.Companion.isPermissionGranted(this)) {
            LocationUtil.Companion.requestUserLocation(this, (location) -> {
                if (school.getDistanceFromUserLocation() != 0f) {
                    schoolDescList.add(
                            new MaterialItemAdapter.MaterialItem(
                                    R.drawable.ic_baseline_commute_24,
                                    String.format("거리: %.2fkm\n(걸어서 약 %d분)",
                                            school.getDistanceFromUserLocation(),
                                            (int) LocationUtil.Companion.getGoingTime(school.getDistanceFromUserLocation(), 4)),
                                    () -> {
                                        return Unit.INSTANCE;
                                    })
                    );
                    RecyclerView recyclerView = findViewById(R.id.school_describe_list);
                    if (recyclerView.getAdapter() != null) {
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }

                return Unit.INSTANCE;
            });
        }
    }
}