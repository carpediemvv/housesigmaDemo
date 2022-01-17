package com.example.housesigmademo

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.util.BannerUtils


class ImageNetAdapter(mData: List<MapBean?>?) : BannerAdapter<MapBean?, ImageNetAdapter.ImageHolder>(mData) {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = BannerUtils.getView(parent, R.layout.banner_image)
        BannerUtils.setBannerRound(view.findViewById(R.id.fl_container), 15F);
        return ImageHolder(view)
    }

    private lateinit var listener: (id: Int, view: View) -> Unit

    fun setOnImageClickListener(listener: (id: Int, view: View) -> Unit) {
        this.listener = listener
    }

    override fun onBindView(holder: ImageHolder?, data: MapBean?, position: Int, size: Int) {
        if (holder != null) {
            if (data != null) {
                holder.itemView.setOnClickListener {
                    listener.invoke(getRealPosition(position),it)
                }
                Glide.with(holder.itemView)
                    .load( data.photo)
                    .placeholder(R.drawable.bg_shape_banner_title)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.imageView)
                holder.tvTitle.text=data.address
            }
        }
    }

    class ImageHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view.findViewById(R.id.image) as ImageView
        var tvTitle: TextView = view.findViewById(R.id.tv_title) as TextView

    }
}