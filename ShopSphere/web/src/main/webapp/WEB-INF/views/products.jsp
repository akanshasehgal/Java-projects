<c:set var="pageTitle" value="Products"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<div class="row g-4">
    <aside class="col-lg-3">
        <%@ include file="/WEB-INF/views/fragments/sidebar.jsp" %>
    </aside>

    <section class="col-lg-9">
        <div class="d-flex flex-wrap justify-content-between align-items-baseline mb-3">
            <h1 class="h4 fw-bold mb-0">
                <c:choose>
                    <c:when test="${not empty param.q}">Results for "<c:out value="${param.q}"/>"</c:when>
                    <c:otherwise><fmt:message key="products.title"/></c:otherwise>
                </c:choose>
            </h1>
            <%-- JSTL functions: fn:length --%>
            <span class="text-muted small">${fn:length(products)} <fmt:message key="products.found"/></span>
        </div>

        <c:choose>
            <c:when test="${empty products}">
                <div class="text-center py-5 bg-white rounded-3 shadow-sm">
                    <i class="bi bi-search display-4 text-muted"></i>
                    <p class="mt-3 mb-1"><fmt:message key="products.none"/></p>
                    <a href="${ctx}/products" class="btn btn-brand btn-sm mt-2"><fmt:message key="products.clear"/></a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row g-3">
                    <c:forEach items="${products}" var="p">
                        <div class="col-6 col-md-4"><t:productCard product="${p}"/></div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
