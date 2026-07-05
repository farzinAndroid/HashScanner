package com.example.hashscanner.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hashscanner.R
import com.example.hashscanner.database.entity.AppInfo

class AppAdapter(

    private val list: List<AppInfo>

) : RecyclerView.Adapter<AppAdapter.Holder>() {

    class Holder(

        view: View

    ) : RecyclerView.ViewHolder(view) {

        val name: TextView =

            view.findViewById(R.id.txtName)

        val packageName: TextView =

            view.findViewById(R.id.txtPackage)

        val risk: TextView =

            view.findViewById(R.id.txtRisk)

    }

    override fun onCreateViewHolder(

        parent: ViewGroup,

        viewType: Int

    ): Holder {

        val view = LayoutInflater.from(

            parent.context

        ).inflate(

            R.layout.item_app,

            parent,

            false

        )

        return Holder(view)

    }

    override fun getItemCount(): Int {

        return list.size

    }

    override fun onBindViewHolder(

        holder: Holder,

        position: Int

    ) {

        val app = list[position]

        holder.name.text = app.appName

        holder.packageName.text = app.packageName

        holder.risk.text = app.riskLevel

    }

}