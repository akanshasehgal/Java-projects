<%-- ===== JSP directive: page (imports) ===== --%>
<%@ page import="java.time.LocalTime, java.time.format.DateTimeFormatter" %>
<c:set var="pageTitle" value="JSP Elements"/>
<%-- ===== JSP directive: include (static, at translation time) ===== --%>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<%-- ===== Declaration: becomes a field/method of the generated servlet class (shared by all requests) ===== --%>
<%!
    private int pageHits = 0;

    private String greeting(LocalTime time) {
        if (time.getHour() < 12) return "Good morning";
        if (time.getHour() < 17) return "Good afternoon";
        return "Good evening";
    }
%>

<%-- ===== Scriptlet: Java code that runs on every request ===== --%>
<%
    String name = "Student";
    LocalTime now = LocalTime.now();
    synchronized (this) {
        pageHits++;
    }
    pageContext.setAttribute("hits", pageHits);
%>

<h1 class="h4 fw-bold">Traditional JSP elements</h1>
<p class="text-muted">Shown here for the syllabus. The store itself uses EL and JSTL instead of scriptlets.</p>

<div class="row g-3">
    <div class="col-lg-6">
        <div class="bg-white rounded-3 shadow-sm p-3 h-100">
            <h2 class="h6 fw-bold">Expression <code class="demo">&lt;%= ... %&gt;</code></h2>
            <p class="mb-1"><%= greeting(now) %>, <%= name %>!</p>
            <p class="small text-muted mb-3">Server time: <%= now.format(DateTimeFormatter.ofPattern("HH:mm:ss")) %></p>

            <h2 class="h6 fw-bold">Declaration counter <code class="demo">&lt;%! ... %&gt;</code></h2>
            <p class="mb-3">This page has been viewed <strong>${hits}</strong> time(s) since the server started.</p>

            <h2 class="h6 fw-bold">Comments</h2>
            <%-- A JSP comment never reaches the browser --%>
            <!-- An HTML comment is sent to the browser (view the page source to see it) -->
            <p class="small mb-0">This page contains a JSP comment (invisible) and an HTML comment (visible in the page source).</p>
        </div>
    </div>

    <div class="col-lg-6">
        <div class="bg-white rounded-3 shadow-sm p-3 h-100">
            <h2 class="h6 fw-bold">Implicit objects</h2>
            <table class="table table-sm small mb-0">
                <tr><th>request.getMethod()</th><td><%= request.getMethod() %></td></tr>
                <tr><th>request.getRemoteAddr()</th><td><%= request.getRemoteAddr() %></td></tr>
                <tr><th>session.getId() (first 8 chars)</th><td><%= session.getId().substring(0, 8) %>...</td></tr>
                <tr><th>session.isNew()</th><td><%= session.isNew() %></td></tr>
                <tr><th>application.getServerInfo()</th><td><%= application.getServerInfo() %></td></tr>
                <tr><th>config.getServletName()</th><td><%= config.getServletName() %></td></tr>
                <tr><th>Same values with EL</th><td>${pageContext.request.method}, ${pageContext.session.new}</td></tr>
            </table>
        </div>
    </div>

    <div class="col-lg-6">
        <div class="bg-white rounded-3 shadow-sm p-3 h-100">
            <h2 class="h6 fw-bold">Standard actions: <code class="demo">jsp:useBean</code>, <code class="demo">jsp:setProperty</code>, <code class="demo">jsp:getProperty</code></h2>
            <jsp:useBean id="demoProduct" class="com.shopsphere.model.Product" scope="page"/>
            <jsp:setProperty name="demoProduct" property="name" value="Demo Headphones"/>
            <jsp:setProperty name="demoProduct" property="price" value="2000"/>
            <jsp:setProperty name="demoProduct" property="discount" value="25"/>
            <p class="mb-1">Name: <jsp:getProperty name="demoProduct" property="name"/></p>
            <p class="mb-1">MRP: <jsp:getProperty name="demoProduct" property="price"/></p>
            <p class="mb-0">Final price (EL calls getFinalPrice()): <ss:price value="${demoProduct.finalPrice}" mrp="${demoProduct.price}"/></p>
        </div>
    </div>

    <div class="col-lg-6">
        <div class="bg-white rounded-3 shadow-sm p-3 h-100">
            <h2 class="h6 fw-bold">Dynamic include <code class="demo">jsp:include</code> with <code class="demo">jsp:param</code></h2>
            <jsp:include page="/WEB-INF/views/demo/included.jsp">
                <jsp:param name="caller" value="jsp-elements.jsp"/>
            </jsp:include>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
