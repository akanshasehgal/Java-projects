<c:set var="pageTitle" value="Customers"/>
<c:set var="adminPage" value="users"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<div class="row g-4">
    <aside class="col-lg-2"><%@ include file="/WEB-INF/views/fragments/admin-sidebar.jsp" %></aside>
    <section class="col-lg-10">
        <h1 class="h4 fw-bold mb-3">Users <span class="text-muted fs-6">(${fn:length(users)})</span></h1>
        <div class="bg-white rounded-3 shadow-sm table-responsive">
            <table class="table table-hover mb-0">
                <thead class="table-light"><tr><th>ID</th><th>Name</th><th>Email</th><th>Mobile</th><th>Role</th><th>Joined</th><th>Status</th><th></th></tr></thead>
                <tbody>
                <c:forEach items="${users}" var="u">
                    <tr>
                        <td>${u.userId}</td>
                        <td><c:out value="${u.name}"/></td>
                        <td class="small"><c:out value="${u.email}"/></td>
                        <td class="small"><c:out value="${empty u.mobile ? '-' : u.mobile}"/></td>
                        <td><span class="badge ${u.admin ? 'text-bg-dark' : 'text-bg-light border'}">${u.role}</span></td>
                        <td class="small"><fmt:formatDate value="${u.createdAt}" type="date" dateStyle="medium"/></td>
                        <td><t:statusBadge status="${u.status}"/></td>
                        <td>
                            <c:if test="${u.userId != sessionScope.user.userId}">
                                <form action="${ctx}/admin/users" method="post"
                                      data-confirm="${u.active ? 'Block' : 'Unblock'} this user?">
                                    <input type="hidden" name="id" value="${u.userId}">
                                    <input type="hidden" name="block" value="${u.active}">
                                    <button class="btn btn-sm ${u.active ? 'btn-outline-danger' : 'btn-outline-success'}" type="submit">
                                        ${u.active ? 'Block' : 'Unblock'}</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </section>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
