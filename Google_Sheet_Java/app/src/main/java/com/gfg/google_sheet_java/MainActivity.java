package com.gfg.google_sheet_java;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private Button fetchButton;
    private RecyclerView sheetRecyclerView;

    // Use the new adapter
    private UserRVAdapter userRVAdapter;

    // List to hold user data
    private ArrayList<UserModal> userModalArrayList = new ArrayList<>();

    private Executor executor = Executors.newSingleThreadExecutor();
    private android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchButton = findViewById(R.id.fetchButton);
        sheetRecyclerView = findViewById(R.id.sheetRecyclerView);

        // Initialize the new adapter
        userRVAdapter = new UserRVAdapter(userModalArrayList, this);
        sheetRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set the new adapter
        sheetRecyclerView.setAdapter(userRVAdapter);

        fetchButton.setOnClickListener(v -> {
            String url = "https://docs.google.com/spreadsheets/d/e/2PACX-1vQlLsJUKrUv22ulAXvBXLwT2rcMhSOKOS4BiIAPc-WZQTssJ5S0LeIWDwtgYW90fI-IZaE7sEOW1hVP/pub?output=csv";
            if (!url.isEmpty()) {
                fetchSheetData(url);
            }
        });
    }

    private void fetchSheetData(String url) {
        executor.execute(() -> {
            try {

                // Convert Google Sheets URL to CSV export URL
                String csvUrl = url.replace("/edit#gid=", "/export?format=csv&gid=");
                URL urlObj = new URL(csvUrl);
                java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(urlObj.openStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line).append("\n");
                }
                reader.close();

                List<SheetRow> rows = parseCSV(response.toString());

                // Clear the existing data
                userModalArrayList.clear();

                boolean flag = false;

                // Convert CSV rows to UserModal objects
                for (SheetRow row : rows) {
                    if (!flag) {
                        flag = true;
                        continue;
                    }

                    // Ensure the row has enough columns
                    if (row.getCells().size() >= 4) {
                        UserModal userModal = new UserModal(
                                row.getCells().get(0),
                                row.getCells().get(1),
                                row.getCells().get(2),
                                row.getCells().get(3)
                        );
                        userModalArrayList.add(userModal);
                    }
                }

                // Update the adapter on the main thread
                mainHandler.post(() -> {
                    userRVAdapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                mainHandler.post(() -> {

                    // Handle errors (e.g., show a toast or log the error)
                    System.out.println("Error: " + e.getMessage());
                });
            }
        });
    }

    private List<SheetRow> parseCSV(String csvData) {
        List<SheetRow> rows = new ArrayList<>();
        String[] lines = csvData.split("\n");

        Pattern pattern = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] cellsArray = pattern.split(line);
            List<String> cells = Arrays.stream(cellsArray)
                    .map(cell -> cell.trim()
                            .replaceAll("^\"|\"$", "") // Remove surrounding quotes
                            .replace("\"\"", "\""))     // Replace double quotes with single
                    .collect(Collectors.toList());

            rows.add(new SheetRow(cells));
        }

        return rows;
    }


    private static class SheetRow {
        private List<String> cells;

        public SheetRow(List<String> cells) {
            this.cells = cells;
        }

        public List<String> getCells() {
            return cells;
        }
    }
}