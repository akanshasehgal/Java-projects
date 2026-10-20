<c:set var="pageTitle" value="Internationalization"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>
<jsp:useBean id="today" class="java.util.Date"/>
<c:set var="amount" value="${123456.5}"/>

<h1 class="h4 fw-bold">Internationalization (i18n)</h1>
<p class="text-muted">ShopSphere text comes from <code class="demo">i18n/messages.properties</code> (English) and
    <code class="demo">i18n/messages_hi.properties</code> (Hindi). Switch language from the <i class="bi bi-translate"></i> menu in the navbar.
    Current language: <strong>${lang}</strong>.</p>

<div class="bg-white rounded-3 shadow-sm table-responsive mb-4">
    <table class="table mb-0">
        <thead class="table-light"><tr><th>Locale</th><th>Currency (fmt:formatNumber)</th><th>Date (fmt:formatDate)</th><th>Message "nav.cart"</th></tr></thead>
        <tbody>
        <c:forEach items="${['en_IN', 'hi_IN', 'en_US', 'fr_FR']}" var="loc">
            <fmt:setLocale value="${loc}"/>
            <tr>
                <td><code class="demo">${loc}</code></td>
                <td><fmt:formatNumber value="${amount}" type="currency"/></td>
                <td><fmt:formatDate value="${today}" dateStyle="full"/></td>
                <td><fmt:bundle basename="i18n.messages"><fmt:message key="nav.cart"/></fmt:bundle></td>
            </tr>
        </c:forEach>
        <%-- restore the page locale chosen by the user --%>
        <fmt:setLocale value="${lang}"/>
        </tbody>
    </table>
</div>

<div class="bg-white rounded-3 shadow-sm p-3">
    <h2 class="h6 fw-bold">Message with a parameter (MessageFormat)</h2>
    <p class="mb-0"><fmt:message key="product.onlyLeft"><fmt:param value="3"/></fmt:message></p>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
