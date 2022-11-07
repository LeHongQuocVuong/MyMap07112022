package com.example.mymap

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mymap.databinding.ActivityCreateMapBinding
import com.google.android.gms.maps.model.Marker
import com.google.android.material.snackbar.Snackbar

private const val TAG = "CreateMapActivity"
class CreateMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityCreateMapBinding
    private var markers: MutableList<Marker> = mutableListOf<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //title được nhận từ intent
        val title = intent.getStringExtra(Utils.EXTRA_MAP_TITLE)
        //đặt lại tên menu
        supportActionBar?.title = title

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        //Khi map mở thì hiển thị snackbar
        mapFragment.view?.let {
            Snackbar.make(it,"Long press to add marker!",Snackbar.LENGTH_INDEFINITE)
                .setAction("OK",{})
                .setActionTextColor(ContextCompat.getColor(this,R.color.white))
                .show()
        }
    }

    //Thêm menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_create_map,menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Bấm lưu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menuSave){
            Log.i(TAG,"Clicked on save")
            if(markers.isEmpty()){ //nếu ko có
                Toast.makeText(this,"there must be at least one marker on the map",Toast.LENGTH_LONG).show()
                return true
            }
            //Nếu có thêm marker
            val places = markers.map {
                it -> Place(it.title!!, it.snippet!!, it.position.latitude,it.position.longitude)
            }

            val userMap = UserMap(intent.getStringExtra(Utils.EXTRA_MAP_TITLE)!!,places)
            val data = Intent()
            data.putExtra(Utils.EXTRA_USER_MAP,userMap)
            setResult(Activity.RESULT_OK,data) // trả về tín hiệu thành công và userMap
            finish()
            return true

        }
        return super.onOptionsItemSelected(item)
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Bấm vào info của marker thì xoá marker đó
        mMap.setOnInfoWindowClickListener {
            marker ->
            Log.i(TAG, "setOnInfoWindowClickListener - Delete")
            markers.remove(marker) //xoá ra khỏi mảng
            marker.remove() //xoá khỏi màn hình
        }

        //Bấm giữ chuột trái để tạo mới
        mMap.setOnMapLongClickListener {
            latLng ->
            Log.i(TAG, "setOnMapLongClickListener")
            val placeFromView = LayoutInflater.from(this).inflate(R.layout.dialog_create_place,null)
            AlertDialog.Builder(this).setTitle("Create Marker")
                .setView(placeFromView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK"){
                    _,_ ->
                    val _title = placeFromView.findViewById<EditText>(R.id.et_title).text.toString()
                    val _desc = placeFromView.findViewById<EditText>(R.id.et_desc).text.toString()

                    if(_title.trim().isEmpty() || _desc.trim().isEmpty()){
                        Toast.makeText(this,"Fill out title & description",Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }
                    val marker = mMap.addMarker( MarkerOptions().position(latLng).title(_title).snippet(_desc))
                    markers.add(marker!!)
                }
                .show()
        }

        // Add a marker in CTU and move the camera
        val ctu = LatLng(10.031452976258134, 105.77197889530333)
        mMap.addMarker(MarkerOptions().position(ctu).title("Đại Học Cần Thơ"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ctu, 10f))
    }
}