package com.example.hashscanner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import com.example.hashscanner.R

class SettingsFragment : Fragment() {

    override fun onCreateView(

        inflater: LayoutInflater,

        container: ViewGroup?,

        savedInstanceState: Bundle?

    ): View {

        val view = inflater.inflate(

            R.layout.fragment_settings,

            container,

            false

        )

        val autoUpload =

            view.findViewById<Switch>(

                R.id.switchAutoUpload

            )

        val autoScan =

            view.findViewById<Switch>(

                R.id.switchAutoScan

            )

        autoUpload.setOnCheckedChangeListener { _, checked ->

            // ذخیره تنظیمات

        }

        autoScan.setOnCheckedChangeListener { _, checked ->

            // ذخیره تنظیمات

        }

        return view

    }

}