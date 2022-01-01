package br.com.cotiinformatica.reports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.Document;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import br.com.cotiinformatica.entities.Compromisso;
import br.com.cotiinformatica.entities.Usuario;

public class CompromissoReportPDF {
	// método para gerar um relatorio PDF em formato de arquivo..
	public ByteArrayInputStream gerarRelatorio(List<Compromisso> compromissos, Date dataInicio, Date dataFim,
			Usuario usuario) throws Exception {
		// objeto que irá permitir fazermos o download do documento
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// criando o documento do relatorio..
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, out);
		document.open(); // abrindo o arquivo..
		// escrevendo o conteudo do relatorio..
		document.add(new Paragraph("Relatório de Compromissos"));
		document.add(new Paragraph("Gerado em: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())));
		document.add(new Paragraph("Usuário: " + usuario.getNome()));
		document.add(new Paragraph("Período de: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(dataInicio)));
		document.add(new Paragraph("Período até: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(dataFim)));
		// desenhando uma tabela para exibir os compromissos..
		PdfPTable table = new PdfPTable(5);
		// 5 -> quantidade de colunas da tabela
		table.setWidthPercentage(100); // largura de 100%
		// células de cabeçalho da tabela
		table.addCell("Nome do Compromisso");
		table.addCell("Data");
		table.addCell("Hora");
		table.addCell("Tipo");
		table.addCell("Prioridade");
		// percorrer a consulta e popular as celulas da tabela..
		for (Compromisso item : compromissos) {
			table.addCell(item.getNome());
			table.addCell(new SimpleDateFormat("dd/MM/yyyy").format(item.getDataCompromisso()));
			table.addCell(new SimpleDateFormat("HH:mm").format(item.getHoraCompromisso()));
			table.addCell(item.getTipo().toString());
			table.addCell(item.getPrioridade().toString());
		}
		document.add(table); // adicionando a tabela no documento PDF
		document.close();
		writer.close();
		return new ByteArrayInputStream(out.toByteArray());
	}

}
