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

@Service
public class ReportServiceImpl {
    public byte[] generateReport(int totalDocs, int suicideDocs, int nonSuicideDocs) {
        double suicidePercent = ((double) suicideDocs / totalDocs) * 100;
        double nonSuicidePercent = 100 - suicidePercent;

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Espacio inicial para el texto
            float startY = 750;
            float marginLeft = 50;
            float lineHeight = 20;

            // Escribir el título
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.setLeading(lineHeight);
            contentStream.newLineAtOffset(marginLeft, startY);
            contentStream.showText("Reporte de Análisis de Documentos");
            contentStream.endText();

            // Escribir los datos en forma de tabla
            startY -= 40;
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.setLeading(lineHeight);
            contentStream.newLineAtOffset(marginLeft, startY);
            contentStream.showText("Total de Documentos: " + totalDocs);
            contentStream.newLine();
            contentStream.showText("Documentos de Suicidio: " + suicideDocs);
            contentStream.newLine();
            contentStream.showText("Documentos No Suicidio: " + nonSuicideDocs);
            contentStream.newLine();
            contentStream.showText("Porcentaje Suicidio: " + String.format("%.2f", suicidePercent) + "%");
            contentStream.endText();

            // Generar gráfico y agregarlo
            byte[] chartBytes = generatePieChart(suicidePercent, nonSuicidePercent);
            PDImageXObject chartImage = PDImageXObject.createFromByteArray(document, chartBytes, "chart");

            // Dejar espacio después del texto antes de agregar la imagen
            startY -= 120;
            contentStream.drawImage(chartImage, marginLeft, startY - 300, 400, 300);

            contentStream.close();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            document.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generando el PDF", e);
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
