<c:set var="pageTitle" value="JNDI"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<h1 class="h4 fw-bold">JNDI resource lookup</h1>
<p class="text-muted">Java Naming and Directory Interface: the application asks Tomcat for resources <em>by name</em>
    instead of creating them itself.</p>

<pre class="preview small">Context ctx = new InitialContext();
Context env = (Context) ctx.lookup("java:comp/env");
String version = (String) env.lookup("appVersion");          // &lt;env-entry&gt; in web.xml
DataSource ds  = (DataSource) env.lookup("jdbc/ShopSphereDB"); // &lt;Resource&gt; in META-INF/context.xml</pre>

<c:choose>
    <c:when test="${not empty error}">
        <div class="alert alert-danger"><c:out value="${error}"/><br>
            <small>The DataSource needs MySQL running and the MySQL Connector/J JAR in Tomcat's <code>lib</code> folder.</small></div>
    </c:when>
    <c:otherwise>
        <div class="bg-white rounded-3 shadow-sm p-3">
            <table class="table table-sm mb-0">
                <tr><th>appVersion (env-entry)</th><td>${appVersion}</td></tr>
                <tr><th>DataSource implementation</th><td><code>${dataSourceClass}</code></td></tr>
                <tr><th>Database</th><td>${databaseProduct}</td></tr>
                <tr><th>Products counted through the pooled connection</th><td><strong>${productCount}</strong></td></tr>
            </table>
        </div>
    </c:otherwise>
</c:choose>
<c:if test="${not empty appVersion and not empty error}"><p class="mt-2">appVersion (env-entry) = ${appVersion}</p></c:if>

<p class="small text-muted mt-3">RMI naming is shown in the <code>rmi</code> module, where the client finds the remote
    inventory service with both <code>Naming.lookup()</code> and a JNDI <code>InitialContext</code>.</p>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
