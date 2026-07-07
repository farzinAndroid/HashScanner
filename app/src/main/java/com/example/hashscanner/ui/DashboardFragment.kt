/*
package com.example.hashscanner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.apphashscanner.R
import com.example.apphashscanner.database.AppDatabase
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private lateinit var db: AppDatabase

    private lateinit var totalApps: TextView
    private lateinit var suspiciousApps: TextView
    private lateinit var safeApps: TextView
    private lateinit var highRiskApps: TextView

    override fun onCreateView(

        inflater: LayoutInflater,

        container: ViewGroup?,

        savedInstanceState: Bundle?

    ): View {

        val view = inflater.inflate(

            R.layout.fragment_dashboard,

            container,

            false

        )

        totalApps = view.findViewById(R.id.txtTotalApps)
        suspiciousApps = view.findViewById(R.id.txtSuspicious)
        safeApps = view.findViewById(R.id.txtSafe)
        highRiskApps = view.findViewById(R.id.txtHigh)

        db = AppDatabaseProvider.getDatabase(requireContext())

        loadStatistics()

        return view

    }

    private fun loadStatistics() {

        viewLifecycleOwner.lifecycleScope.launch {

            val dao = db.appDao()

            totalApps.text =
                dao.count().toString()

            suspiciousApps.text =
                dao.countSuspiciousApps().toString()

            safeApps.text =
                dao.countSafeApps().toString()

            highRiskApps.text =
                dao.countHighRiskApps().toString()

        }

    }

}*/
