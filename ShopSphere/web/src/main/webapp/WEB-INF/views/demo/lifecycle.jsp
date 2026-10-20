<c:set var="pageTitle" value="Servlet Life Cycle"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<h1 class="h4 fw-bold">Servlet life cycle, ServletConfig and ServletContext</h1>
<p class="text-muted">Refresh the page: <code class="demo">service()</code> runs on every request, <code class="demo">init()</code> ran only once.</p>

<div class="row g-3">
    <div class="col-lg-6">
        <div class="bg-white rounded-3 shadow-sm p-3 h-100">
            <h2 class="h6 fw-bold">Life cycle</h2>
            <pre class="preview small mb-3">Loading -> Instantiation -> init()
        -> service()  -> doGet()/doPost()   (every request)
        -> destroy()                        (on shutdown)</pre>
            <table class="table table-sm small mb-0">
                <tr><th>Servlet name</th><td>${servletName}</td></tr>
                <tr><th>init() called at</th><td><fmt:formatDate value="${initTime}" type="both" dateStyle="medium" timeStyle="medium"/></td></tr>
                <tr><th>Requests handled by service()</th><td><strong>${requestCount}</strong></td></tr>
                <tr><th>HTTP method / protocol</th><td>${method} / ${protocol}</td></tr>
                <tr><th>User-Agent</th><td class="text-break"><c:out value="${userAgent}"/></td></tr>
            </table>
        </div>
    </div>
    <div class="col-lg-6">
        <div class="bg-white rounded-3 shadow-sm p-3 mb-3">
            <h2 class="h6 fw-bold">ServletConfig <small class="text-muted">(this servlet only, from web.xml init-param)</small></h2>
            <p class="mb-0">welcomeMessage = <strong><c:out value="${welcomeMessage}"/></strong></p>
        </div>
        <div class="bg-white rounded-3 shadow-sm p-3">
            <h2 class="h6 fw-bold">ServletContext <small class="text-muted">(whole application)</small></h2>
            <table class="table table-sm small mb-0">
                <tr><th>appName (attribute set by AppListener)</th><td>${appName}</td></tr>
                <tr><th>Application started</th><td><fmt:formatDate value="${startTime}" type="both" dateStyle="medium" timeStyle="medium"/></td></tr>
                <tr><th>Active sessions (HttpSessionListener)</th><td>${activeSessions}</td></tr>
                <tr><th>supportEmail (context-param)</th><td>${supportEmail}</td></tr>
                <tr><th>Server</th><td>${serverInfo}</td></tr>
                <tr><th>Servlet API version</th><td>${servletVersion}</td></tr>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
