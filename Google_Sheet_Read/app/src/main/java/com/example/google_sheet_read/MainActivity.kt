package com.example.google_sheet_read

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var fetchButton: Button
    private lateinit var sheetRecyclerView: RecyclerView
    private lateinit var userRVAdapter: UserRVAdapter // Use the new adapter
    private val userModalArrayList = ArrayList<UserModal>() // List to hold user data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchButton = findViewById(R.id.fetchButton)
        sheetRecyclerView = findViewById(R.id.sheetRecyclerView)

        // Initialize the new adapter
        userRVAdapter = UserRVAdapter(userModalArrayList, this)
        sheetRecyclerView.layoutManager = LinearLayoutManager(this)
        sheetRecyclerView.adapter = userRVAdapter // Set the new adapter

        fetchButton.setOnClickListener {
            val url = "https://docs.google.com/spreadsheets/d/e/2PACX-1vQlLsJUKrUv22ulAXvBXLwT2rcMhSOKOS4BiIAPc-WZQTssJ5S0LeIWDwtgYW90fI-IZaE7sEOW1hVP/pub?output=csv"
            if (url.isNotEmpty()) {
                fetchSheetData(url)
            }
        }
    }

    private fun fetchSheetData(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Convert Google Sheets URL to CSV export URL
                val csvUrl = url.replace("/edit#gid=", "/export?format=csv&gid=")
                val response = URL(csvUrl).readText()
                val rows = parseCSV(response)

                // Clear the existing data
                userModalArrayList.clear()

                var flag=0

                // Convert CSV rows to UserModal objects
                for (row in rows) {
                    if(flag==0) {
                        flag=1;
                        continue;
                    }
                    if (row.cells.size >= 4) { // Ensure the row has enough columns
                        val userModal = UserModal(
                            first_name = row.cells[0],
                            last_name = row.cells[1],
                            email = row.cells[2],
                            avatar = row.cells[3]
                        )
                        userModalArrayList.add(userModal)
                    }
                }

                // Update the adapter on the main thread
                withContext(Dispatchers.Main) {
                    userRVAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {

                    // Handle errors (e.g., show a toast or log the error)
                    println("Error: ${e.message}")
                }
            }
        }
    }

    private fun parseCSV(csvData: String): List<SheetRow> {
        return csvData.split("\n")
            .filter { it.isNotBlank() }
            .map { row ->
                // Split by comma, but respect quoted values
                val cells = row.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)".toRegex())
                    .map { cell ->
                        cell.trim()
                            .removeSurrounding("\"")
                            .replace("\"\"", "\"")
                    }
                SheetRow(cells)
            }
    }
}