<c:set var="pageTitle" value="URL and URLConnection"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<h1 class="h4 fw-bold">java.net URL and URLConnection</h1>
<p class="text-muted">The URL's scheme selects a <em>protocol handler</em>; the response's content type tells the
    <em>content handler</em> how to read the data.</p>

<form class="d-flex gap-2 mb-3" method="get" action="${ctx}/demo/url">
    <input class="form-control" name="url" value="${fn:escapeXml(address)}" aria-label="URL">
    <button class="btn btn-brand text-nowrap" type="submit">Open URL</button>
</form>

<div class="row g-3">
    <div class="col-lg-5">
        <div class="bg-white rounded-3 shadow-sm p-3 mb-3">
            <h2 class="h6 fw-bold">URL parts</h2>
            <table class="table table-sm small mb-0">
                <c:forEach items="${parts}" var="e">
                    <tr><th>${e.key}</th><td><c:out value="${empty e.value ? '-' : e.value}"/></td></tr>
                </c:forEach>
            </table>
        </div>
        <div class="bg-white rounded-3 shadow-sm p-3 small">
            <strong>URLEncoder:</strong> "java books &amp; laptops" &rarr; <code>${encodedExample}</code>
        </div>
    </div>
    <div class="col-lg-7">
        <c:choose>
            <c:when test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}"/></div></c:when>
            <c:otherwise>
                <div class="bg-white rounded-3 shadow-sm p-3">
                    <h2 class="h6 fw-bold">Connection details</h2>
                    <table class="table table-sm small">
                        <c:forEach items="${info}" var="e">
                            <tr><th>${e.key}</th><td><c:out value="${empty e.value ? '-' : e.value}"/></td></tr>
                        </c:forEach>
                    </table>
                    <h2 class="h6 fw-bold">First part of the content</h2>
                    <pre class="preview small mb-0"><c:out value="${preview}"/></pre>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
