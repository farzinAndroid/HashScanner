/*
package com.example.apphashscanner.ui

import android.os.Bundle
import android.text.Editable
import java.io.File
import android.text.TextWatcher
import android.widget.Toast
import com.example.hashscanner.network.ApkUploader
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.example.apphashscanner.R
import com.example.apphashscanner.database.AppDatabase
import com.example.apphashscanner.database.entity.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppListActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView

    private lateinit var adapter: AppAdapter

    private lateinit var db: AppDatabase

    private lateinit var totalApps: TextView

    private lateinit var suspiciousApps: TextView

    private lateinit var searchBox: EditText

    private lateinit var filterAll: Chip

    private lateinit var filterSafe: Chip

    private lateinit var filterLow: Chip

    private lateinit var filterMedium: Chip

    private lateinit var filterHigh: Chip

    private lateinit var filterCritical: Chip

    private var allApps = mutableListOf<AppInfo>()

    private var currentApps = mutableListOf<AppInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_app_list)

        db = AppDatabase.getDatabase(this)

        recycler = findViewById(R.id.recycler)

        totalApps = findViewById(R.id.totalApps)

        suspiciousApps = findViewById(R.id.suspiciousApps)

        searchBox = findViewById(R.id.searchBox)

        filterAll = findViewById(R.id.filterAll)

        filterSafe = findViewById(R.id.filterSafe)

        filterLow = findViewById(R.id.filterLow)

        filterMedium = findViewById(R.id.filterMedium)

        filterHigh = findViewById(R.id.filterHigh)

        filterCritical = findViewById(R.id.filterCritical)

        recycler.layoutManager = LinearLayoutManager(this)

adapter = AppAdapter(
    mutableListOf()
) { app ->

    lifecycleScope.launch(Dispatchers.IO) {

        val success = ApkUploader(
            "https://YOUR_SERVER/api/upload_apk"
        ).upload(
            File(app.apkPath),
            app.packageName
        )

        withContext(Dispatchers.Main) {

            if (success) {

                Toast.makeText(
                    this@AppListActivity,
                    "APK با موفقیت ارسال شد",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                Toast.makeText(
                    this@AppListActivity,
                    "ارسال APK ناموفق بود",
                    Toast.LENGTH_SHORT
                ).show()

            }

        }

    }

}

recycler.adapter = adapter

loadApps()

initSearch()

initFilters()
}
    private fun loadApps() {

        lifecycleScope.launch {

            val list = withContext(Dispatchers.IO) {

                db.appDao().getAppsByRisk()

            }

            allApps.clear()

            allApps.addAll(list)

            currentApps.clear()

            currentApps.addAll(list)

            adapter.update(currentApps)

            totalApps.text = list.size.toString()

            suspiciousApps.text =

                list.count {

                    it.suspicious

                }.toString()

        }

    }
    private fun initSearch() {

        searchBox.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

                val keyword = s.toString().lowercase()

                val result = allApps.filter {

                    it.appName.lowercase().contains(keyword) ||
                    it.packageName.lowercase().contains(keyword)

                }

                adapter.update(result)

            }

            override fun afterTextChanged(
                s: Editable?
            ) {
            }

        })

    }

    private fun initFilters() {

        filterAll.setOnClickListener {

            currentApps.clear()

            currentApps.addAll(allApps)

            adapter.update(currentApps)

        }

        filterSafe.setOnClickListener {

            showRisk("SAFE")

        }

        filterLow.setOnClickListener {

            showRisk("LOW")

        }

        filterMedium.setOnClickListener {

            showRisk("MEDIUM")

        }

        filterHigh.setOnClickListener {

            showRisk("HIGH")

        }

        filterCritical.setOnClickListener {

            showRisk("CRITICAL")

        }

    }

    private fun showRisk(level: String) {

        currentApps.clear()

        currentApps.addAll(

            allApps.filter {

                it.riskLevel == level

            }

        )

        adapter.update(currentApps)

    }


}
*/
