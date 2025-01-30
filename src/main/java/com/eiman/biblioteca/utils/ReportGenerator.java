package com.eiman.biblioteca.utils;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ReportGenerator {

    private static final String REPORTS_PATH = "/jasper/";

    /**
     * Generates a PDF report based on the provided data and Jasper template file.
     *
     * @param reportName The name of the Jasper report template without the extension.
     * @param data The data source for the report (List of JavaBeans).
     * @param parameters Additional parameters for the report.
     */
    public static void generateReport(String reportName, List<?> data, Map<String, Object> parameters) {
        try {
            // Load the JRXML file from resources
            InputStream reportStream = ReportGenerator.class.getResourceAsStream(REPORTS_PATH + reportName + ".jasper");
            if (reportStream == null) {
                throw new RuntimeException("Report file not found: " + REPORTS_PATH + reportName + ".jasper");
            }

            // Create the data source
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, dataSource);

            // Display the report in JasperViewer
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compiles a JRXML file to a Jasper file if needed (development only).
     *
     * @param jrxmlPath The path of the JRXML file.
     */
    public static void compileReport(String jrxmlPath) {
        try {
            String jasperPath = jrxmlPath.replace(".jrxml", ".jasper");
            JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
            System.out.println("Report compiled: " + jasperPath);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
