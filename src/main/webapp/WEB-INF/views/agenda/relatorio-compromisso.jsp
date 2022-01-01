<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:useBean id="data" class="java.util.Date" />

<fmt:formatDate var="dataAtual" value="${data}"
	pattern="dd 'de' MMMM 'de' yyyy" />

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Relatorio</title>

<!-- Folhas de estilo CSS -->
<link rel="stylesheet" href="resources/css/bootstrap.min.css" />

<link rel="stylesheet"
	href="//cdn.datatables.net/1.10.23/css/jquery.dataTables.min.css" />

<style>
label.error {
	color: #d9534f;
}

input.error {
	border: 1px solid #d9534f;
}
</style>

</head>
<body>
	<c:import url="menu.jsp"></c:import>

	<c:if test="${not empty mensagem_sucesso}">

		<div class="alert alert-success alert-dismissible fade show"
			role="alert">
			<strong>Sucesso!</strong> ${mensagem_sucesso}
			<button type="button" class="btn-close" data-bs-dismiss="alert"
				aria-label="Close"></button>
		</div>

	</c:if>

	<c:if test="${not empty mensagem_erro}">

		<div class="alert alert-danger alert-dismissible fade show"
			role="alert">
			<strong>Erro!</strong> ${mensagem_erro}
			<button type="button" class="btn-close" data-bs-dismiss="alert"
				aria-label="Close"></button>
		</div>

	</c:if>

	<div class="container mt-4">

		<div class="row">
			<div class="col-md-6">
				<h5>Seja bem vindo à Agenda de Compromissos</h5>
				<p>${dataAtual}</p>
			</div>
			<div class="col-md-6 text-end">
				<strong>${usuario_autenticado.nome}</strong> <small>${usuario_autenticado.email}</small>
				<br /> <a href="/springMvc02/logout"
					onclick="return confirm('Deseja realmente sair do sistema?')">
					Sair do Sistema </a>

			</div>
		</div>

		<hr />

		<h5>Relatório de compromissos</h5>

		<form id="formRelatorio" autocomplete="off" action="gerarRelatorio"
			method="post">

			<div class="row">
				<div class="col-md-12">
					<div class="row">
						<div class="col-md-3">
							<label>Data da Início:</label>
							<form:input path="compromisso.dataInicio" type="date"
								name="dataInicio" id="dataInicio" class="form-control" />
						</div>

						<div class="col-md-3">
							<label>Data de Término:</label>
							<form:input path="compromisso.dataFim" type="date" name="dataFim"
								id="dataFim" class="form-control" />
						</div>

						<div class="col-md-3">
							<label>Formato do Relatório</label>
							<form:select path="compromisso.formato" name="formato"
								id="formato" class="form-control">
								<option value="">Escolha uma opção:</option>
								<form:options items="${formatos}" />
							</form:select>
						</div>

					</div>
				</div>
			</div>

			<br /> <input type="submit" value="Gerar Relatório"
				class="btn btn-success" />

		</form>
	</div>

	<br />





	<!-- Arquivos javascript -->
	<script src="resources/js/bootstrap.min.js"></script>

	<script src="resources/js/jquery-3.5.1.min.js"></script>
	<script src="resources/js/jquery.validate.min.js"></script>
	<script src="resources/js/messages_pt_BR.min.js"></script>
	<script src="//cdn.datatables.net/1.10.23/js/jquery.dataTables.min.js"></script>

	<script>
		$(document)
				.ready(
						function() {

							//aplicando a formatação do datatable
							$('#tabelaCompromissos')
									.DataTable(
											{
												language : {
													url : '//cdn.datatables.net/plug-ins/1.10.22/i18n/Portuguese-Brasil.json'
												}
											});

							//aplicando a validação do jquery-validate
							//validação do formulário
							$("#formRelatorio").validate({
								rules : {
									'dataInicio' : {
										required : true
									},
									'dataFim' : {
										required : true
									},
									'formato' : {
										required : true
									}
								}
							});

						})
	</script>

</body>
</html>





