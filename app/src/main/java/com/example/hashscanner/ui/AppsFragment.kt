/*
package com.example.hashscanner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hashscanner.R
import com.example.hashscanner.database.AppDatabase
import kotlinx.coroutines.launch

class AppsFragment : Fragment() {

    private lateinit var recycler: RecyclerView

    override fun onCreateView(

        inflater: LayoutInflater,

        container: ViewGroup?,

        savedInstanceState: Bundle?

    ): View {

        val view = inflater.inflate(

            R.layout.fragment_apps,

            container,

            false

        )

        recycler = view.findViewById(

            R.id.recyclerApps

        )

        recycler.layoutManager =

            LinearLayoutManager(

                requireContext()

            )

        loadApps()

        return view

    }

    private fun loadApps() {

        viewLifecycleOwner.lifecycleScope.launch {

            val db =

                AppDatabase.getDatabase(

                    requireContext()

                )

            val apps =

                db.appDao().getAppsByRisk()

            recycler.adapter =

                AppAdapter(apps)

        }

    }

}*/
