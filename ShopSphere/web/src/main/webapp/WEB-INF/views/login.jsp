<c:set var="pageTitle" value="Login"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-6 col-lg-5">
        <div class="card border-0 shadow-sm">
            <div class="card-body p-4">
                <h1 class="h4 fw-bold text-center mb-1"><i class="bi bi-person-circle text-brand"></i> <fmt:message key="nav.login"/></h1>
                <p class="text-center text-muted small mb-4">Customers and admins log in here.</p>

                <c:if test="${not empty error}"><div class="alert alert-danger py-2"><c:out value="${error}"/></div></c:if>

                <%-- cookie implicit object: pre-fill the remembered email --%>
                <c:set var="emailValue" value="${not empty email ? email : cookie.rememberEmail.value}"/>
                <form action="${ctx}/login" method="post" novalidate>
                    <input type="hidden" name="redirect" value="${fn:escapeXml(param.redirect)}">
                    <div class="mb-3">
                        <label class="form-label" for="email">Email</label>
                        <input class="form-control" type="email" id="email" name="email" required autofocus
                               value="${fn:escapeXml(emailValue)}">
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="password">Password</label>
                        <input class="form-control" type="password" id="password" name="password" required>
                    </div>
                    <div class="form-check mb-3">
                        <input class="form-check-input" type="checkbox" id="remember" name="remember"
                               ${not empty cookie.rememberEmail.value ? 'checked' : ''}>
                        <label class="form-check-label small" for="remember">Remember my email</label>
                    </div>
                    <button class="btn btn-brand w-100" type="submit"><fmt:message key="nav.login"/></button>
                </form>
                <p class="text-center small mt-3 mb-0">New to ShopSphere? <a href="${ctx}/register"><fmt:message key="nav.register"/></a></p>
            </div>
        </div>
        <div class="alert alert-light border small mt-3">
            <strong>Demo accounts</strong><br>
            Admin: <code>admin@shopsphere.com</code> / <code>admin123</code><br>
            Customer: <code>rahul@example.com</code> / <code>customer123</code>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
