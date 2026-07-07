/*
package com.example.hashscanner.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apphashscanner.R
import com.example.apphashscanner.database.entity.AppInfo

class AppAdapter(

    private var apps: MutableList<AppInfo>,

    

    private val onSendApk: (AppInfo) -> Unit

) : RecyclerView.Adapter<AppAdapter.AppViewHolder>() {

    inner class AppViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val appName: TextView =
            itemView.findViewById(R.id.appName)

        val packageName: TextView =
            itemView.findViewById(R.id.packageName)

        val riskScore: TextView =
            itemView.findViewById(R.id.riskScore)

        val riskLevel: TextView =
            itemView.findViewById(R.id.riskLevel)

        val reasons: TextView =
            itemView.findViewById(R.id.reasons)

        val recommendation: TextView =
            itemView.findViewById(R.id.recommendation)

        val progress: ProgressBar =
            itemView.findViewById(R.id.riskProgress)

        

        val sendApk: Button =
            itemView.findViewById(R.id.sendApkButton)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppViewHolder {

        val view = LayoutInflater.from(
            parent.context
        ).inflate(

            R.layout.item_app,

            parent,

            false

        )

        return AppViewHolder(view)

    }

    override fun getItemCount(): Int {

        return apps.size

    }

    override fun onBindViewHolder(

        holder: AppViewHolder,

        position: Int

    ) {

        val app = apps[position]

        holder.appName.text =
            app.appName

        holder.packageName.text =
            app.packageName

        holder.riskScore.text =
            app.riskScore.toString()

        holder.riskLevel.text =
            app.riskLevel

        holder.reasons.text =
            app.riskReasons

        holder.recommendation.text =
            app.recommendation

        holder.progress.progress =
            app.riskScore

        when (app.riskLevel) {

            "SAFE" -> {

                holder.riskLevel.text = "ایمن"

                holder.riskLevel.setTextColor(
                    Color.parseColor("#00C853")
                )

            }

            "LOW" -> {

                holder.riskLevel.text = "کم خطر"

                holder.riskLevel.setTextColor(
                    Color.parseColor("#FFD600")
                )

            }

            "MEDIUM" -> {

                holder.riskLevel.text = "متوسط"

                holder.riskLevel.setTextColor(
                    Color.parseColor("#FF9100")
                )

            }

            "HIGH" -> {

                holder.riskLevel.text = "پرخطر"

                holder.riskLevel.setTextColor(
                    Color.RED
                )

            }

            "CRITICAL" -> {

                holder.riskLevel.text = "بحرانی"

                holder.riskLevel.setTextColor(
                    Color.RED
                )

            }

        }


        if (app.recommendUpload) {

            holder.sendApk.visibility =
                View.VISIBLE

        } else {

            holder.sendApk.visibility =
                View.GONE

        }

        holder.sendApk.setOnClickListener {

            onSendApk(app)

        }

    }

    fun update(

        newApps: List<AppInfo>

    ) {

        apps.clear()

        apps.addAll(newApps)

        notifyDataSetChanged()

    }

}*/
