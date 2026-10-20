<c:set var="pageTitle" value="Manage Categories"/>
<c:set var="adminPage" value="categories"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<div class="row g-4">
    <aside class="col-lg-2"><%@ include file="/WEB-INF/views/fragments/admin-sidebar.jsp" %></aside>
    <section class="col-lg-10">
        <h1 class="h4 fw-bold mb-3">Categories</h1>
        <div class="row g-4">
            <div class="col-xl-8">
                <div class="bg-white rounded-3 shadow-sm table-responsive">
                    <table class="table table-hover mb-0">
                        <thead class="table-light"><tr><th>Name</th><th>Description</th><th class="text-center">Products</th><th>Status</th><th class="text-end">Actions</th></tr></thead>
                        <tbody>
                        <c:forEach items="${categories}" var="cat">
                            <tr>
                                <td class="fw-semibold"><c:out value="${cat.name}"/></td>
                                <td class="small"><c:out value="${cat.description}"/></td>
                                <td class="text-center">${cat.productCount}</td>
                                <td>${cat.status ? '<span class="badge text-bg-success">Active</span>' : '<span class="badge text-bg-secondary">Inactive</span>'}</td>
                                <td class="text-end text-nowrap">
                                    <a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/categories?edit=${cat.categoryId}"><i class="bi bi-pencil"></i></a>
                                    <form action="${ctx}/admin/categories" method="post" class="d-inline">
                                        <input type="hidden" name="action" value="toggle">
                                        <input type="hidden" name="id" value="${cat.categoryId}">
                                        <input type="hidden" name="active" value="${not cat.status}">
                                        <button class="btn btn-sm btn-outline-secondary" type="submit" title="Activate / deactivate">
                                            <i class="bi ${cat.status ? 'bi-toggle-on' : 'bi-toggle-off'}"></i></button>
                                    </form>
                                    <form action="${ctx}/admin/categories" method="post" class="d-inline" data-confirm="Delete this category?">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${cat.categoryId}">
                                        <button class="btn btn-sm btn-outline-danger" type="submit"><i class="bi bi-trash"></i></button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="col-xl-4">
                <form action="${ctx}/admin/categories" method="post" class="bg-white rounded-3 shadow-sm p-3">
                    <h2 class="h6 fw-bold">${empty editing ? 'Add category' : 'Edit category'}</h2>
                    <input type="hidden" name="action" value="save">
                    <input type="hidden" name="id" value="${empty editing ? 0 : editing.categoryId}">
                    <input class="form-control mb-2" name="name" placeholder="Name" required maxlength="80" value="${fn:escapeXml(editing.name)}">
                    <textarea class="form-control mb-2" name="description" rows="2" placeholder="Description" maxlength="255"><c:out value="${editing.description}"/></textarea>
                    <div class="mb-3">
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="status" id="catActive" value="true" ${empty editing or editing.status ? 'checked' : ''}>
                            <label class="form-check-label" for="catActive">Active</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="status" id="catInactive" value="false" ${not empty editing and not editing.status ? 'checked' : ''}>
                            <label class="form-check-label" for="catInactive">Inactive</label>
                        </div>
                    </div>
                    <button class="btn btn-brand btn-sm" type="submit">Save</button>
                    <c:if test="${not empty editing}"><a class="btn btn-link btn-sm" href="${ctx}/admin/categories">Cancel</a></c:if>
                </form>
            </div>
        </div>
    </section>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
