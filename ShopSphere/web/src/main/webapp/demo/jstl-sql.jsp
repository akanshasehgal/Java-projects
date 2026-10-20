<%@ taglib prefix="sql" uri="jakarta.tags.sql" %>
<c:set var="pageTitle" value="JSTL SQL"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<h1 class="h4 fw-bold">JSTL SQL tags</h1>
<div class="alert alert-info small">
    Syllabus demonstration only. Real applications (including the ShopSphere store) keep SQL out of JSP pages and use
    the DAO layer instead. This page uses the JNDI DataSource <code>jdbc/ShopSphereDB</code> from <code>META-INF/context.xml</code>.
</div>

<form class="d-flex gap-2 mb-3" method="get">
    <label class="col-form-label small" for="minDiscount">Minimum discount %</label>
    <input class="form-control form-control-sm w-auto" type="number" id="minDiscount" name="minDiscount" min="0" max="90"
           value="${empty param.minDiscount ? 20 : fn:escapeXml(param.minDiscount)}">
    <button class="btn btn-sm btn-brand" type="submit">Run query</button>
</form>

<c:catch var="sqlError">
    <sql:setDataSource var="db" dataSource="jdbc/ShopSphereDB"/>
    <sql:query dataSource="${db}" var="result">
        SELECT p.product_id, p.name, c.name AS category, p.price, p.discount, p.stock
        FROM products p LEFT JOIN categories c ON p.category_id = c.category_id
        WHERE p.status = TRUE AND p.discount >= ?
        ORDER BY p.discount DESC
        <sql:param value="${empty param.minDiscount ? 20 : param.minDiscount}"/>
    </sql:query>

    <p class="small text-muted">${result.rowCount} row(s) returned. Columns: ${fn:join(result.columnNames, ', ')}</p>
    <div class="bg-white rounded-3 shadow-sm table-responsive">
        <table class="table table-sm mb-0">
            <thead class="table-light"><tr><th>ID</th><th>Name</th><th>Category</th><th class="text-end">MRP</th><th class="text-center">Discount</th><th class="text-center">Stock</th></tr></thead>
            <tbody>
            <c:forEach items="${result.rows}" var="row">
                <tr>
                    <td>${row.product_id}</td>
                    <td><c:out value="${row.name}"/></td>
                    <td><c:out value="${row.category}"/></td>
                    <td class="text-end"><ss:price value="${row.price}"/></td>
                    <td class="text-center"><fmt:formatNumber value="${row.discount}" maxFractionDigits="0"/>%</td>
                    <td class="text-center">${row.stock}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:catch>
<c:if test="${not empty sqlError}">
    <div class="alert alert-danger">
        Query failed: <c:out value="${sqlError.message}"/><br>
        <small>Check that MySQL is running, the credentials in META-INF/context.xml are correct and the MySQL
            Connector/J JAR is in Tomcat's <code>lib</code> folder (needed for container-managed DataSources).</small>
    </div>
</c:if>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
