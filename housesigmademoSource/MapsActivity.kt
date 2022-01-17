package com.example.housesigmademo

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.core.view.isVisible
import com.blankj.utilcode.util.*
import com.example.housesigmademo.databinding.ActivityMapsBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.youth.banner.Banner
import com.youth.banner.listener.OnPageChangeListener
import com.youth.banner.transformer.MZScaleInTransformer
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.item_marker_bg_oval.view.*
import kotlinx.android.synthetic.main.item_marker_bg_rec.view.*
import java.util.zip.Inflater

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapListBean: List<MapBean>
    private val bottomBounds = 48.60817749983161
    private val topBounds = 49.35079725810497
    private val leftBounds = -123.38502243161201
    private val rightBounds = -121.58630222082138
    private val swBounds: LatLng = LatLng(bottomBounds, leftBounds)
    private val neBounds: LatLng = LatLng(topBounds, rightBounds)
    private val centerZoom: LatLng = LatLng(49.216987628956424, -122.91427128016949)
    private var initSetZoom = true
    private lateinit var bannerF: Banner<MapBean, ImageNetAdapter>

    private  var markerList: MutableList<Marker> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun initData() {
        val readAssets2String = ResourceUtils.readAssets2String("mapdata.json")
        mapListBean = GsonUtils.fromJson(
            Gson(), readAssets2String,
            object : TypeToken<List<MapBean>>() {}.type
        )

    }

    //
    @SuppressLint("Recycle")
    override fun onMapReady(googleMap: GoogleMap) {
        initData()

        mMap = googleMap

        with(mMap) {
            //Controls and Gestures
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isCompassEnabled = true
            uiSettings.isMapToolbarEnabled = false


            //Camera and View
            setMinZoomPreference(9.0f)
            setMaxZoomPreference(16.0f)


            val adelaideBounds = LatLngBounds(
                swBounds,  // SW bounds
                neBounds // NE bounds
            )
            setLatLngBoundsForCameraTarget(adelaideBounds)


            // Instantiates a new Polyline object and adds points to define a rectangle
            // Get back the mutable Polyline
            val polylineOptions = PolylineOptions().apply {
                add(LatLng(bottomBounds, leftBounds))
                add(LatLng(bottomBounds, rightBounds))
                add(LatLng(topBounds, rightBounds))
                add(LatLng(topBounds, leftBounds))
                add(LatLng(bottomBounds, leftBounds))
                color(Color.YELLOW)
            }

            val pattern = listOf(
                Dot(), Gap(20F), Dash(30F), Gap(20F)
            )
            // addPolyline(polylineOptions).pattern = pattern

            //addPolygon
            val polygonOptions = PolygonOptions().apply {
                add(LatLng(49.36677882060674, -123.15868623554707))
                add(LatLng(49.31949233543441, -122.89394583553076))
                add(LatLng(49.30830432315096, -122.6068314537406))
                add(LatLng(49.230541936211324, -122.40366283804178))
                add(LatLng(49.232831349271684, -121.97499454021454))
                add(LatLng(49.31801756130404, -121.64370786398649))
                add(LatLng(49.17034578827988, -121.73785608261825))
                add(LatLng(49.02096558057489, -122.07019452005625))
                add(LatLng(48.91406050555638, -122.25899152457714))
                add(LatLng(48.84478516407297, -122.25977942347525))
                add(LatLng(48.70855046743341, -122.42637310177088))
                add(LatLng(48.690073543699015, -122.49031428247689))
                add(LatLng(48.751704190731395, -122.58643191307783))
                add(LatLng(48.83771341466055, -122.76855055242777))
                add(LatLng(49.01483373490462, -122.88593675941229))
                add(LatLng(48.95442461674382, -123.04688572883606))
                add(LatLng(49.26268952555133, -123.30942437052727))
                add(LatLng(49.360818742900236, -123.14069766551255))
                strokeColor(Color.parseColor("#ff5b29"))
            }

            //feedbackEvents
            var lastCameraZoom = 13f
            val needChangeBounds = 12.5f
            setOnCameraIdleListener {
                if (initSetZoom) {
                    initSetZoom = false
                    addMarkerData(cameraPosition.zoom < needChangeBounds)
                } else {
                    if (lastCameraZoom <= needChangeBounds) {
                        if (cameraPosition.zoom >= lastCameraZoom) {
                            if (cameraPosition.zoom >= needChangeBounds) {
                                addMarkerData(false)
                            }
                        }
                    } else {
                        if (cameraPosition.zoom < lastCameraZoom) {
                            if (cameraPosition.zoom < needChangeBounds) {
                                addMarkerData(true)
                                addPolygon(polygonOptions.fillColor(Color.parseColor("#280000FF")))
                            }
                        }
                    }
                }


                camera_bounds.text = projection.fromScreenLocation(Point(0, 0)).toString()
                camera_bounds_one.text =
                    projection.fromScreenLocation(Point(ScreenUtils.getAppScreenWidth(), 0))
                        .toString()
                if (!adelaideBounds.contains(cameraPosition.target)) {
                    ToastUtils.showShort("Camera超出范围")
                }
                if (projection.fromScreenLocation(
                        Point(
                            0,
                            0
                        )
                    ).longitude <= adelaideBounds.southwest.longitude
                ) {
                    ToastUtils.showShort("超过左边界")
                }
                if (projection.fromScreenLocation(
                        Point(
                            ScreenUtils.getAppScreenWidth(),
                            0
                        )
                    ).longitude >= adelaideBounds.northeast.longitude
                ) {
                    ToastUtils.showShort("超过右边界")
                }

                camera_text.text = cameraPosition.toString()
            }

            setOnCameraMoveStartedListener {
                lastCameraZoom = cameraPosition.zoom
            }

            setOnMapClickListener {
                ToastUtils.showShort(it.toString())
                Log.e("weiwei", "onMapReady:$it ")
            }

            bannerF = findViewById(R.id.banner)
            val imageNetAdapter = ImageNetAdapter(mapListBean)
            bannerF.setBannerGalleryMZ(20,0.8f)
            bannerF.addPageTransformer(MZScaleInTransformer())
            bannerF.setAdapter(imageNetAdapter,false)
            bannerF.addOnPageChangeListener(object :OnPageChangeListener{
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    markerList.forEachIndexed { index, marker ->
                        if (position==index){
                            marker.alpha=0.7f
                        }else{
                            marker.alpha=1f
                        }
                    }
                    animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder()
                        .target(LatLng(mapListBean[position].location.lat,mapListBean[position].location.lng))
                        .zoom(13f)
                        .build()),500, null)
                }

                override fun onPageScrollStateChanged(state: Int) {
                }
            })
            imageNetAdapter.setOnImageClickListener { id, view ->

            }

            val detail: CameraPosition = CameraPosition.Builder()
                .target(centerZoom)
                .zoom(13f)
                .build()
            setOnMarkerClickListener {
                if (it.tag==-1){
                    val animator = ObjectAnimator
                        .ofFloat(bannerF, "translationY", SizeUtils.dp2px(200f).toFloat(), 0f)
                        .setDuration(1000)
                    animator.interpolator= AnticipateOvershootInterpolator()
                    animator.addListener(onEnd = {
                        bannerF.visibility = View.VISIBLE
                    }, onStart = {
                        bannerF.visibility = View.VISIBLE
                    })
                    animator.start()
                    setPadding(0, 0, 0, SizeUtils.dp2px(200f))
                    animateCamera(CameraUpdateFactory.newCameraPosition(detail),500, null)
                }else{
                    if (!bannerF.isVisible){
                        val animator = ObjectAnimator
                            .ofFloat(bannerF, "translationY", SizeUtils.dp2px(200f).toFloat(), 0f)
                            .setDuration(1000)
                        animator.interpolator= AnticipateOvershootInterpolator()
                        animator.addListener(onEnd = {
                            bannerF.visibility = View.VISIBLE
                        }, onStart = {
                            bannerF.visibility = View.VISIBLE
                        })
                        animator.start()
                        setPadding(0, 0, 0, SizeUtils.dp2px(200f))
                    }
                    markerList.forEachIndexed { index, marker ->
                        if (it.tag.toString().toInt()==index){
                            marker.alpha=0.7f
                        }else{
                            marker.alpha=1f
                        }
                    }
                    bannerF.setCurrentItem(it.tag.toString().toInt(),true)
                }
                return@setOnMarkerClickListener true

            }




            addMarkerData(true)
            addPolygon(polygonOptions.fillColor(Color.parseColor("#280000FF")))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(centerZoom))

        }



    }


    private fun addMarkerData(b: Boolean) {
        mMap.clear()
        if (b) {

            val animator = ObjectAnimator
                .ofFloat(bannerF, "translationY", 0f, SizeUtils.dp2px(200f).toFloat())
                .setDuration(1000)
            animator.interpolator= AnticipateOvershootInterpolator()
            animator.addListener(onEnd = {
                bannerF.visibility = View.GONE
            })
            animator.start()
            mMap.setPadding(0, 0, 0, SizeUtils.dp2px(0f))
            val inflate = LayoutInflater.from(this)
                .inflate(R.layout.item_marker_bg_oval, null, false)
            inflate.tv_num.text = "4"
            val markerOptions = MarkerOptions().position(centerZoom)
                .icon(BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(inflate)))
            mMap.addMarker(markerOptions).tag=-1
        } else {
            markerList.clear()
            mapListBean.forEachIndexed { index, mapBean ->
                val inflate = LayoutInflater.from(this)
                    .inflate(R.layout.item_marker_bg_rec, null, false)
                inflate.tv_text.text = "D ${mapBean.price.substring(0, 3)}万"
                val latLng = LatLng(mapBean.location.lat, mapBean.location.lng)
                val markerOptions = MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(inflate)))
                val marker =  mMap.addMarker(markerOptions)
                marker.tag=index
                markerList.add(marker)
            }
        }
    }

    private fun loadBitmapFromView(v: View): Bitmap? {
        if (v.measuredHeight <= 0) {
            v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val b = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            v.layout(0, 0, v.measuredWidth, v.measuredHeight)
            v.draw(c)
            return b
        } else {
            val b = Bitmap.createBitmap(
                v.layoutParams.width,
                v.layoutParams.height,
                Bitmap.Config.ARGB_8888
            )
            val c = Canvas(b)
            v.layout(v.left, v.top, v.right, v.bottom)
            v.draw(c)
            return b
        }
    }

}