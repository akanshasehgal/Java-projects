<c:set var="pageTitle" value="Create Account"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-7 col-lg-6">
        <div class="card border-0 shadow-sm">
            <div class="card-body p-4">
                <h1 class="h4 fw-bold text-center mb-4"><i class="bi bi-person-plus text-brand"></i> Create your account</h1>

                <c:if test="${not empty error}"><div class="alert alert-danger py-2"><c:out value="${error}"/></div></c:if>

                <form action="${ctx}/register" method="post">
                    <div class="mb-3">
                        <label class="form-label" for="name">Full name</label>
                        <input class="form-control" id="name" name="name" required minlength="2" maxlength="100"
                               value="${fn:escapeXml(param.name)}">
                    </div>
                    <div class="row g-3 mb-3">
                        <div class="col-sm-7">
                            <label class="form-label" for="email">Email</label>
                            <input class="form-control" type="email" id="email" name="email" required
                                   value="${fn:escapeXml(param.email)}">
                        </div>
                        <div class="col-sm-5">
                            <label class="form-label" for="mobile">Mobile <span class="text-muted small">(optional)</span></label>
                            <input class="form-control" type="tel" id="mobile" name="mobile" pattern="[6-9][0-9]{9}" maxlength="10"
                                   value="${fn:escapeXml(param.mobile)}">
                        </div>
                    </div>
                    <div class="row g-3 mb-4">
                        <div class="col-sm-6">
                            <label class="form-label" for="password">Password</label>
                            <input class="form-control" type="password" id="password" name="password" required minlength="6">
                        </div>
                        <div class="col-sm-6">
                            <label class="form-label" for="confirmPassword">Confirm password</label>
                            <input class="form-control" type="password" id="confirmPassword" name="confirmPassword" required minlength="6">
                        </div>
                    </div>
                    <button class="btn btn-brand w-100" type="submit"><fmt:message key="nav.register"/></button>
                </form>
                <p class="text-center small mt-3 mb-0">Already have an account? <a href="${ctx}/login"><fmt:message key="nav.login"/></a></p>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
