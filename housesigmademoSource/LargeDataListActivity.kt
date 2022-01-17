package com.example.housesigmademo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ResourceUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_large_data_list.*

class LargeDataListActivity : AppCompatActivity() {

    private lateinit var mapListBean: MutableList<MapBean>
    private  val mapLargeListBean = mutableListOf<MapBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_large_data_list)
        initData()
        initView()
    }


    private fun initData() {
        val readAssets2String = ResourceUtils.readAssets2String("mapdata.json")
        mapListBean = GsonUtils.fromJson(
            Gson(), readAssets2String,
            object : TypeToken<List<MapBean>>() {}.type
        )
        for (i in 0 until 1000) {
            mapLargeListBean.addAll(mapListBean)
        }
    }


    private fun initView() {
        recyclerview.layoutManager=LinearLayoutManager(this)
        recyclerview.adapter=object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(
                            R.layout.item_data_large,
                            parent,
                            false
                        )
                )
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                if ((holder is  ItemHolder)) {
                    holder.bind(mapLargeListBean[position],position)
                }
            }

            override fun getItemCount()=mapLargeListBean.size

            inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                private var ivCover: ImageView = itemView.findViewById(R.id.iv_cover)
                private var tvText: TextView = itemView.findViewById(R.id.tv_text)

                fun bind(data: MapBean,pos :Int) {
                    tvText.text="第${pos}个数据"+data.address
                    Glide.with(itemView.context)
                        .load(data.photo)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
                        .placeholder(R.drawable.bg_shape_banner_title)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivCover)
                }
            }
        }
    }

}