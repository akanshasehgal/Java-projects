<%-- JSP fragment: page head + navbar + flash message. Set "pageTitle" before including. --%>
<!DOCTYPE html>
<html lang="${lang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${pageTitle}"/> | ${applicationScope.appName}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body class="d-flex flex-column min-vh-100">
<%@ include file="/WEB-INF/views/fragments/navbar.jsp" %>
<main class="flex-grow-1">
<div class="container py-4">
<c:if test="${not empty sessionScope.flashMessage}">
    <div class="alert alert-${sessionScope.flashType} alert-dismissible fade show" role="alert">
        <c:out value="${sessionScope.flashMessage}"/>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <c:remove var="flashMessage" scope="session"/>
    <c:remove var="flashType" scope="session"/>
</c:if>
