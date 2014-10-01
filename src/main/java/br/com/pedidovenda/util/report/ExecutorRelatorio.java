package br.com.pedidovenda.util.report;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;

import org.hibernate.jdbc.Work;

public class ExecutorRelatorio implements Work {

	private String caminhoRelatorio;

	private HttpServletResponse response;
	private Map<String, Object> parametros;
	private String nomeArquivoSaida;
	private boolean relatorioGerado;

	public ExecutorRelatorio(String caminhoRelatorio,
			HttpServletResponse response, Map<String, Object> parametros,
			String nomeArquivoSaida) {
		this.caminhoRelatorio = caminhoRelatorio;
		this.response = response;
		this.parametros = parametros;
		this.nomeArquivoSaida = nomeArquivoSaida;
	}

	@Override
	public void execute(Connection connection) throws SQLException {
		try {
			InputStream relatorioStream = this.getClass().getResourceAsStream(
					caminhoRelatorio);

			JasperPrint print = JasperFillManager.fillReport(relatorioStream,
					parametros, connection);
			this.relatorioGerado = print.getPages().size() > 0;

			if (this.relatorioGerado) {
				/*
				 * PrintRequestAttributeSet printRequestAttributeSet = new
				 * HashPrintRequestAttributeSet();
				 * 
				 * MediaSizeName mediaSizeName = MediaSize.ISO.A4
				 * .getMediaSizeName();
				 * printRequestAttributeSet.add(mediaSizeName);
				 * printRequestAttributeSet.add(new Copies(4));
				 * JRPrintServiceExporter exporter = new
				 * JRPrintServiceExporter();
				 * exporter.setParameter(JRExporterParameter.JASPER_PRINT,
				 * print); exporter.setParameter(
				 * JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET,
				 * printRequestAttributeSet); exporter.setParameter(
				 * JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG,
				 * Boolean.FALSE); exporter.setParameter(
				 * JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG,
				 * Boolean.FALSE); exporter.exportReport();
				 */

				JRExporter exportaPdf = new JRPdfExporter();
				exportaPdf.setParameter(JRExporterParameter.OUTPUT_STREAM,
						response.getOutputStream());
				exportaPdf
						.setParameter(JRExporterParameter.JASPER_PRINT, print);

				PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
				printRequestAttributeSet.add(new Copies(1));
				JRPrintServiceExporter exporter = new JRPrintServiceExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
				exporter.setParameter(
						JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET,
						printRequestAttributeSet);
				exporter.setParameter(
						JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG,
						Boolean.FALSE);
				exporter.setParameter(
						JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG,
						Boolean.FALSE);

				// JasperPrintManager.printPage(print, 0, false);

				// JasperViewer viewer = new JasperViewer(print); //
				// viewer.setVisible(true);

				/*
				 * response.setContentType("application/pdf");
				 * response.setHeader("Content-Disposition",
				 * "attachment; filename=\"" + this.nomeArquivoSaida + "\"");
				 * 
				 * exportaPdf.exportReport();
				 */
				exporter.exportReport();
			}

		} catch (JRException e) {
			// TODO Auto-generated catch block
			throw new SQLException("Erro ao executar relatório"
					+ this.caminhoRelatorio, e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean isRelatorioGerado() {
		return relatorioGerado;
	}

}
