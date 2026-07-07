/*
package com.example.hashscanner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.apphashscanner.R
import com.example.apphashscanner.database.AppDatabaseProvider
import com.example.apphashscanner.export.CsvExporter
import com.example.apphashscanner.export.ExportManager
import com.example.apphashscanner.export.JsonExporter
import com.example.apphashscanner.report.PdfGenerator
import com.example.apphashscanner.report.TextReportWriter
import kotlinx.coroutines.launch

class ReportsFragment : Fragment() {

    override fun onCreateView(

        inflater: LayoutInflater,

        container: ViewGroup?,

        savedInstanceState: Bundle?

    ): View {

        val view = inflater.inflate(

            R.layout.fragment_reports,

            container,

            false

        )

        view.findViewById<Button>(R.id.btnPdf)

            .setOnClickListener {

                exportPdf()

            }

        view.findViewById<Button>(R.id.btnJson)

            .setOnClickListener {

                exportJson()

            }

        view.findViewById<Button>(R.id.btnCsv)

            .setOnClickListener {

                exportCsv()

            }

        view.findViewById<Button>(R.id.btnTxt)

            .setOnClickListener {

                exportTxt()

            }

        return view

    }

    private fun exportPdf() {

        lifecycleScope.launch {

            val db = AppDatabaseProvider.getDatabase(requireContext())

            PdfGenerator(

                requireContext(),

                db

            ).generatePdf()

        }

    }

    private fun exportJson() {

        lifecycleScope.launch {

            val db = AppDatabaseProvider.getDatabase(requireContext())

            val apps = db.appDao().getAll()

            val file = ExportManager(requireContext()).jsonFile()

            JsonExporter().export(

                apps,

                file

            )

        }

    }

    private fun exportCsv() {

        lifecycleScope.launch {

            val db = AppDatabaseProvider.getDatabase(requireContext())

            val apps = db.appDao().getAll()

            val file = ExportManager(requireContext()).csvFile()

            CsvExporter().export(

                apps,

                file

            )

        }

    }

    private fun exportTxt() {

        lifecycleScope.launch {

            val db = AppDatabaseProvider.getDatabase(requireContext())

            TextReportWriter(db)

                .write()

        }

    }

}*/
