package com.gfg.google_sheet_java;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public final class SheetRow {
    private final List<String> cells;

    public SheetRow(List<String> cells) {
        this.cells = cells;
    }

    public List<String> getCells() {
        return cells;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SheetRow sheetRow = (SheetRow) o;
        return Objects.equals(cells, sheetRow.cells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cells);
    }

    @Override
    public String toString() {
        return "SheetRow{" +
                "cells=" + cells +
                '}';
    }
}