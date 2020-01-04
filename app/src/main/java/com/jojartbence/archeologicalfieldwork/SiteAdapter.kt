package com.jojartbence.archeologicalfieldwork

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jojartbence.model.SiteModel
import kotlinx.android.synthetic.main.card_site.view.*

interface SiteListener {
    fun onSiteClick(site: SiteModel)
}

class SiteAdapter constructor(private var sites: List<SiteModel>,
                                   private val listener: SiteListener
) : RecyclerView.Adapter<SiteAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_site,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val site = sites[holder.adapterPosition]
        holder.bind(site, listener)
    }

    override fun getItemCount(): Int = sites.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(site: SiteModel, listener: SiteListener) {
            itemView.siteTitle.text = site.title
            itemView.description.text = site.description
            Glide.with(itemView.context).load(site.images[0]).into(itemView.imageIcon)
            itemView.location.text = "Lat: %.6f - Lng: %.6f".format(site.location.lat, site.location.lng)
            itemView.visited.isChecked = site.visited
            itemView.setOnClickListener { listener.onSiteClick(site) }
        }
    }
}