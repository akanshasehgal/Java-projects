<%@ page isErrorPage="true" %>
<c:set var="code" value="${requestScope['jakarta.servlet.error.status_code']}"/>
<c:set var="pageTitle" value="${empty code ? 'Error' : code}"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<div class="text-center py-5">
    <c:choose>
        <c:when test="${code == 404}">
            <i class="bi bi-compass display-1 text-muted"></i>
            <h1 class="h3 fw-bold mt-3">Page not found</h1>
            <p class="text-muted">The page or product you are looking for does not exist.</p>
        </c:when>
        <c:when test="${code == 403}">
            <i class="bi bi-shield-lock display-1 text-danger"></i>
            <h1 class="h3 fw-bold mt-3">Access denied</h1>
            <p class="text-muted">This area is only for administrators.</p>
        </c:when>
        <c:otherwise>
            <i class="bi bi-exclamation-triangle display-1 text-warning"></i>
            <h1 class="h3 fw-bold mt-3">Something went wrong</h1>
            <p class="text-muted">Please try again in a moment. If the problem continues, check that MySQL is running.</p>
            <%-- Details are written to the Tomcat log, never shown to the user --%>
            <% if (exception != null) application.log("ShopSphere error", exception); %>
        </c:otherwise>
    </c:choose>
    <a class="btn btn-brand mt-2" href="${ctx}/home"><i class="bi bi-house"></i> <fmt:message key="nav.home"/></a>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
