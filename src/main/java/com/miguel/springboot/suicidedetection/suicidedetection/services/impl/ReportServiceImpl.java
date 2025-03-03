package com.miguel.springboot.suicidedetection.suicidedetection.services.impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl {
    public byte[] generateReport(List<Map<String, Object>> documents) {
        int totalDocs = documents.size();
        int suicideDocs = (int) documents.stream().filter(doc -> (boolean) doc.get("isSuicidal")).count();
        int nonSuicideDocs = totalDocs - suicideDocs;

        double suicidePercent = ((double) suicideDocs / totalDocs) * 100;
        double nonSuicidePercent = 100 - suicidePercent;

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Espacio inicial para el texto
            float startY = 750;
            float marginLeft = 50;
            float lineHeight = 20; // Ajuste de altura de línea

            // Escribir el título
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.newLineAtOffset(marginLeft, startY);
            contentStream.showText("Reporte de Análisis de Documentos");
            contentStream.endText();

            // Separación entre secciones
            startY -= 40;

            // Escribir datos generales con separación correcta
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.setLeading(lineHeight); // Establecer separación entre líneas
            contentStream.newLineAtOffset(marginLeft, startY);

            contentStream.showText("Documentos probables con ideas suicidas: " + suicideDocs);
            contentStream.newLine();
            contentStream.showText("Documentos sin detección de ideas suicidas: " + nonSuicideDocs);
            contentStream.newLine();
            contentStream.showText("Porcentaje de documentos con ideas suicidas: " + String.format("%.2f", suicidePercent) + "%");
            contentStream.newLine();
            contentStream.showText("Porcentaje sin detección de ideas suicidas: " + String.format("%.2f", nonSuicidePercent) + "%");
            contentStream.endText();

            // Espacio antes de la tabla
            startY -= 80;
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(marginLeft, startY);
            contentStream.showText("Lista de Documentos Procesados:");
            contentStream.endText();

            // Dibujar la tabla de documentos
            startY -= 30;
            drawTable(contentStream, documents, startY, marginLeft);

            // Generar gráfico y agregarlo
            byte[] chartBytes = generatePieChart(suicidePercent, nonSuicidePercent);
            PDImageXObject chartImage = PDImageXObject.createFromByteArray(document, chartBytes, "chart");

            // Ajustar espacio para la imagen
            contentStream.drawImage(chartImage, marginLeft, 100, 400, 300);
            contentStream.close();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            document.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generando el PDF", e);
        }
    }

    private void drawTable(PDPageContentStream contentStream, List<Map<String, Object>> documents, float startY, float marginLeft) throws IOException {
        float rowHeight = 20;
        float tableWidth = 500;
        float x = marginLeft;
        float y = startY;
        float col1Width = 350; // Nombre del documento
        float col2Width = 150; // Clasificación

        // Encabezado de la tabla
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText("Nombre del Documento");
        contentStream.newLineAtOffset(col1Width, 0);
        contentStream.showText("Clasificación");
        contentStream.endText();
        y -= rowHeight;

        // Dibujar filas
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        for (Map<String, Object> doc : documents) {
            String name = (String) doc.get("name");
            boolean isSuicidal = (boolean) doc.get("isSuicidal");
            String classification = isSuicidal ? "Suicida" : "No Suicida";

            contentStream.beginText();
            contentStream.newLineAtOffset(x, y);
            contentStream.showText(name);
            contentStream.newLineAtOffset(col1Width, 0);
            contentStream.showText(classification);
            contentStream.endText();

            y -= rowHeight;
            if (y < 100) break; // Evitar escribir fuera de los límites de la página
        }
    }

    private byte[] generatePieChart(double suicidePercent, double nonSuicidePercent) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Suicidio", suicidePercent);
        dataset.setValue("No Suicidio", nonSuicidePercent);

        JFreeChart chart = ChartFactory.createPieChart("Clasificación de Documentos", dataset, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Suicidio", new Color(255, 0, 0));
        plot.setSectionPaint("No Suicidio", new Color(0, 128, 0));

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(outputStream, chart, 600, 400);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generando el gráfico", e);
        }
    }
}
