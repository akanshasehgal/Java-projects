<c:set var="pageTitle" value="My Profile"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>
<c:set var="u" value="${sessionScope.user}"/>

<h1 class="h4 fw-bold mb-3"><i class="bi bi-person-circle"></i> <fmt:message key="nav.profile"/></h1>

<div class="row g-4">
    <div class="col-lg-6">
        <div class="bg-white rounded-3 shadow-sm p-3 mb-4">
            <h2 class="h6 fw-bold">Personal details</h2>
            <form action="${ctx}/profile" method="post">
                <input type="hidden" name="action" value="update">
                <div class="mb-2">
                    <label class="form-label small" for="name">Name</label>
                    <input class="form-control" id="name" name="name" required value="${fn:escapeXml(u.name)}">
                </div>
                <div class="mb-2">
                    <label class="form-label small" for="email">Email</label>
                    <input class="form-control" id="email" value="${fn:escapeXml(u.email)}" disabled>
                </div>
                <div class="mb-3">
                    <label class="form-label small" for="mobile">Mobile</label>
                    <input class="form-control" id="mobile" name="mobile" pattern="[6-9][0-9]{9}" maxlength="10" value="${fn:escapeXml(u.mobile)}">
                </div>
                <button class="btn btn-brand btn-sm" type="submit">Save changes</button>
                <span class="small text-muted ms-2">Member since <fmt:formatDate value="${u.createdAt}" pattern="MMMM yyyy"/></span>
            </form>
        </div>

        <div class="bg-white rounded-3 shadow-sm p-3">
            <h2 class="h6 fw-bold">Change password</h2>
            <form action="${ctx}/profile" method="post">
                <input type="hidden" name="action" value="changePassword">
                <input class="form-control mb-2" type="password" name="currentPassword" placeholder="Current password" required>
                <input class="form-control mb-2" type="password" name="newPassword" placeholder="New password (min 6 characters)" required minlength="6">
                <input class="form-control mb-3" type="password" name="confirmPassword" placeholder="Confirm new password" required minlength="6">
                <button class="btn btn-outline-primary btn-sm" type="submit">Update password</button>
            </form>
        </div>
    </div>

    <div class="col-lg-6">
        <div class="bg-white rounded-3 shadow-sm p-3 mb-4">
            <h2 class="h6 fw-bold">Saved addresses</h2>
            <c:if test="${empty addresses}"><p class="small text-muted">No saved addresses yet.</p></c:if>
            <c:forEach items="${addresses}" var="a">
                <div class="d-flex justify-content-between align-items-start border rounded-3 p-2 mb-2">
                    <div class="small">
                        <span class="badge text-bg-light border">${a.addressType}</span><br>
                        <c:out value="${a.fullAddress}"/>
                    </div>
                    <form action="${ctx}/profile" method="post" data-confirm="Delete this address?">
                        <input type="hidden" name="action" value="deleteAddress">
                        <input type="hidden" name="addressId" value="${a.addressId}">
                        <button class="btn btn-sm btn-link text-danger" type="submit" title="Delete"><i class="bi bi-trash"></i></button>
                    </form>
                </div>
            </c:forEach>
        </div>

        <div class="bg-white rounded-3 shadow-sm p-3">
            <h2 class="h6 fw-bold">Add a new address</h2>
            <form action="${ctx}/profile" method="post">
                <input type="hidden" name="action" value="addAddress">
                <%@ include file="/WEB-INF/views/fragments/address-fields.jsp" %>
                <button class="btn btn-outline-primary btn-sm" type="submit">Save address</button>
            </form>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
