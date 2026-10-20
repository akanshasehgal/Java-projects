<c:set var="pageTitle" value="Syllabus Demos"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<h1 class="h4 fw-bold">Advanced Java syllabus demonstrations</h1>
<p class="text-muted">Small, focused pages that demonstrate topics which do not naturally belong in the shopping flow.
    The main store uses the MVC flow <code class="demo">JSP &rarr; Servlet &rarr; Service &rarr; DAO &rarr; JDBC</code>.</p>

<div class="row g-3">
    <c:set var="demos" value="${[
        ['jsp-elements.jsp', 'bi-code-slash', 'JSP elements', 'Directives, declarations, scriptlets, expressions, comments, implicit objects, jsp:useBean and jsp:include.'],
        ['lifecycle', 'bi-arrow-repeat', 'Servlet life cycle', 'init(), service(), doGet(), destroy(), ServletConfig and ServletContext.'],
        ['i18n.jsp', 'bi-translate', 'Internationalization', 'Locale, ResourceBundle and locale-aware currency/date formatting.'],
        ['jstl-xml.jsp', 'bi-filetype-xml', 'JSTL XML', 'Parse the live product XML feed with x:parse, x:forEach and XPath.'],
        ['jstl-sql.jsp', 'bi-database', 'JSTL SQL', 'sql:setDataSource and sql:query through the JNDI DataSource.'],
        ['jndi', 'bi-diagram-3', 'JNDI lookup', 'Look up a pooled DataSource and an env-entry with InitialContext.'],
        ['url', 'bi-globe', 'URL &amp; URLConnection', 'Split a URL into parts and read headers/content with a protocol handler.'],
        ['feed.xml', 'bi-rss', 'XML feed servlet', 'A servlet that generates XML instead of HTML.']
    ]}"/>
    <c:forEach items="${demos}" var="d">
        <div class="col-md-6 col-lg-3">
            <a class="card h-100 border-0 shadow-sm text-decoration-none text-dark product-card" href="${ctx}/demo/${d[0]}">
                <div class="card-body">
                    <i class="bi ${d[1]} fs-2 text-brand"></i>
                    <h2 class="h6 fw-bold mt-2">${d[2]}</h2>
                    <p class="small text-muted mb-0">${d[3]}</p>
                </div>
            </a>
        </div>
    </c:forEach>
</div>

<h2 class="h5 fw-bold mt-5">Where every topic appears in ShopSphere</h2>
<div class="bg-white rounded-3 shadow-sm table-responsive">
    <table class="table table-sm mb-0">
        <thead class="table-light"><tr><th>Topic</th><th>Implementation</th></tr></thead>
        <tbody>
        <tr><td>Swing, MVC, pluggable look and feel</td><td><code class="demo">swing-admin</code> desktop admin (LoginFrame, AdminDashboard, ProductFrame ...)</td></tr>
        <tr><td>JDBC, DAO, PreparedStatement, transactions</td><td><code class="demo">core</code> module: <code>dao</code> package, CheckoutService</td></tr>
        <tr><td>java.net Socket / ServerSocket, Serialization</td><td><code class="demo">network</code> module: InventoryServer and InventoryClient</td></tr>
        <tr><td>RMI, RMI Registry, Naming / JNDI</td><td><code class="demo">rmi</code> module: InventoryService</td></tr>
        <tr><td>Servlets, filters, listeners, sessions, cookies</td><td><code class="demo">web</code> module: controller, filter, listener packages</td></tr>
        <tr><td>JSP, EL, JSTL core/fmt/functions</td><td>All store pages under <code>WEB-INF/views</code></td></tr>
        <tr><td>JSP fragments</td><td><code>WEB-INF/views/fragments</code>: header, navbar, footer, sidebar, totals</td></tr>
        <tr><td>Tag files and tag handlers</td><td><code>WEB-INF/tags/*.tag</code> and <code>&lt;ss:price&gt;</code> (PriceTag + shopsphere.tld)</td></tr>
        <tr><td>Applets (historical)</td><td>See <code>docs/APPLETS.md</code> in the repository</td></tr>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
