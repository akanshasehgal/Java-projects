<c:set var="isNew" value="${product.productId == 0}"/>
<c:set var="pageTitle" value="${isNew ? 'Add Product' : 'Edit Product'}"/>
<c:set var="adminPage" value="products"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<div class="row g-4">
    <aside class="col-lg-2"><%@ include file="/WEB-INF/views/fragments/admin-sidebar.jsp" %></aside>
    <section class="col-lg-10">
        <a href="${ctx}/admin/products" class="small text-decoration-none"><i class="bi bi-arrow-left"></i> Products</a>
        <h1 class="h4 fw-bold mb-3">${isNew ? 'Add product' : 'Edit product #'}${isNew ? '' : product.productId}</h1>

        <c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}"/></div></c:if>

        <form action="${ctx}/admin/products" method="post" class="bg-white rounded-3 shadow-sm p-4">
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="id" value="${product.productId}">
            <div class="row g-3">
                <div class="col-md-8">
                    <label class="form-label" for="name">Product name</label>
                    <input class="form-control" id="name" name="name" required maxlength="150" value="${fn:escapeXml(product.name)}">
                </div>
                <div class="col-md-4">
                    <label class="form-label" for="brand">Brand</label>
                    <input class="form-control" id="brand" name="brand" maxlength="80" value="${fn:escapeXml(product.brand)}">
                </div>
                <div class="col-md-4">
                    <label class="form-label" for="categoryId">Category</label>
                    <select class="form-select" id="categoryId" name="categoryId">
                        <option value="0">- None -</option>
                        <c:forEach items="${categories}" var="cat">
                            <option value="${cat.categoryId}" ${cat.categoryId == product.categoryId ? 'selected' : ''}><c:out value="${cat.name}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label" for="price">MRP (&#8377;)</label>
                    <input class="form-control" type="number" step="0.01" min="0.01" id="price" name="price" required
                           value="${product.price > 0 ? product.price : ''}">
                </div>
                <div class="col-md-2">
                    <label class="form-label" for="discount">Discount %</label>
                    <input class="form-control" type="number" step="0.01" min="0" max="90" id="discount" name="discount" value="${product.discount}">
                </div>
                <div class="col-md-3">
                    <label class="form-label" for="stock">Stock</label>
                    <input class="form-control" type="number" min="0" id="stock" name="stock" required value="${product.stock}">
                </div>
                <div class="col-12">
                    <label class="form-label" for="description">Description</label>
                    <textarea class="form-control" id="description" name="description" rows="4"><c:out value="${product.description}"/></textarea>
                </div>
                <div class="col-md-8">
                    <label class="form-label" for="imageUrl">Image URL <span class="text-muted small">(optional - a placeholder is shown if empty)</span></label>
                    <input class="form-control" id="imageUrl" name="imageUrl" maxlength="255" value="${fn:escapeXml(product.imageUrl)}">
                </div>
                <div class="col-md-4 d-flex align-items-end">
                    <div class="form-check form-switch">
                        <input class="form-check-input" type="checkbox" role="switch" id="status" name="status" ${product.status ? 'checked' : ''}>
                        <label class="form-check-label" for="status">Visible in store</label>
                    </div>
                </div>
            </div>
            <hr>
            <button class="btn btn-brand" type="submit"><i class="bi bi-check-lg"></i> Save product</button>
            <a class="btn btn-link" href="${ctx}/admin/products">Cancel</a>
        </form>
    </section>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
