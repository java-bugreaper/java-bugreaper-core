package net.bugreaper.core.allurereporter;

import net.bugreaper.core.mappers.StringMappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class AllureBuilder {

    private AllureBuilder() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AllureBuilder.class);

    private static final String ROW_END = "</tr>";
    private static final String HEADER_END = "</th>";
    private static final String CELL_END = "</td>";


    public static String reportHtmlCsvCases(String name, List<List<String>> dataFromCsv) {

        ArrayList<String> columnsList = new ArrayList<>(dataFromCsv.get(0));

        LOGGER.debug("Csv data: {}", dataFromCsv);
        LOGGER.debug("Columns list for header: {}", columnsList);

        final int columnsCount = columnsList.size();

        LOGGER.debug("Columns count for header: {}", columnsCount);

        int testsSize = 0;
        for (int i = 1; i < columnsCount; i++) {

            if (!Objects.equals(columnsList.get(i), "")) {
                testsSize++;
            }

        }

        LOGGER.debug("Filled columns count(first cleaned!) for header: {}", testsSize);

        // Table header constructor
        StringBuilder header = new StringBuilder();
        for (int i = 0; i < columnsCount; i++) {

            if (i==0){
                header.append("<th>").append(testsSize).append(HEADER_END);
            }else{
                header.append("<th>").append(columnsList.get(i)).append(HEADER_END);
            }

        }

        // Table body constructor
        final int rowsCount = dataFromCsv.size();

        LOGGER.debug("Rows count for body: {}", rowsCount);

        StringBuilder dataRows = new StringBuilder();
        for (int rowNumber = 1; rowNumber < rowsCount; rowNumber++) {

            // separate table row on hardcode name in csv row
            if (dataFromCsv.get(rowNumber).contains("==EXPECTED RESULT==")) {
                dataRows.append("<tr style=\"background-color:green;\">");
            } else {
                dataRows.append("<tr>");
            }

            // create table row line on empty row from csv
            if (dataFromCsv.get(rowNumber).isEmpty()) {
                dataRows.append("<td></td>".repeat(columnsCount));
                LOGGER.debug("Empty row found");
            }
            // create table row line on data row csv
            else {
                generateCells(dataFromCsv, dataRows, columnsCount, rowNumber);
            }

            dataRows.append(ROW_END).append("\n");
        }

        // Allure attachment
        var attachment = reportHtmlBuilder(header, dataRows);
        AllureReporter.createHtmlAllureAttachment(name, attachment);

        return attachment;
    }

    private static void generateCells(List<List<String>> dataFromCsv,
                                      StringBuilder dataRows,
                                      int columnsCount,
                                      int rowNumber) {

        for (int c = 0; c < columnsCount; c++) {

            // first column make sticky
            if (c == 0 && dataFromCsv.get(rowNumber).get(c).startsWith("--")) {
                dataRows.append("<td style=\"text-align:left\" id=\"stick-column\">").append(dataFromCsv.get(rowNumber).get(c)).append(CELL_END);
            } else if (c == 0) {
                dataRows.append("<td id=\"stick-column\">").append(dataFromCsv.get(rowNumber).get(c)).append(CELL_END);
            } else {

                try {

                    // accent important data if ends with '*'
                    if (dataFromCsv.get(rowNumber).get(c).endsWith("*") || dataFromCsv.get(rowNumber).get(c).endsWith("*\"")) {
                        dataRows.append("<td bgcolor=\"yellow\" style=\"font-weight:bold\">");
                    } else {
                        dataRows.append("<td>");
                    }
                    dataRows.append(dataFromCsv.get(rowNumber).get(c)).append(CELL_END);

                    // draw empty cell if no data in csv row
                } catch (ArrayIndexOutOfBoundsException e) {
                    dataRows.append("<td></td>");
                }
            }
        }

    }


    private static String reportHtmlBuilder(StringBuilder columns, StringBuilder dataRows) {

        return
                StringMappers.stringMapperV2("""
                                <html title="test title">
                                <head>
                                <style>
                                                    table {
                                                        width: 100%;
                                                        margin-bottom: 1rem;
                                                        color: #111314;
                                                        border-collapse: collapse;
                                                        font-size: 12;
                                                    }
                                                    table td, table th {
                                                        vertical-align: center;
                                                        text-align:center;
                                                        border: 1px solid #dee2e6;
                                                    }
                                                    tr:nth-of-type(even) {
                                                        background-color:#faf7eb;
                                                    }
                                                    tr:nth-of-type(odd) {
                                                        background-color:#f0faf4;
                                                    }
                                                    table th{
                                                        position: sticky;
                                                        top: 0;
                                                        background-color:#FBFCFC!important;
                                                    }
                                                    #stick-column {
                                                        position: sticky;
                                                        left: 0;
                                                        font-weight: bold;
                                                    }
                                </style>
                                </head>
                                <body>
                                <table>
                                <tr>
                                $${columns}</tr>
                                $${dataRows}</table>
                                </body>
                                </html>""",
                        Map.of(
                                "columns", columns,
                                "dataRows", dataRows
                        ));

    }

}
