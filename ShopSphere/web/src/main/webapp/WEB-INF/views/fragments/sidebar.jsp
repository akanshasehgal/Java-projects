<%-- JSP fragment: filter sidebar on the product listing page --%>
<form action="${ctx}/products" method="get" class="card border-0 shadow-sm">
    <div class="card-body">
        <h6 class="fw-bold mb-3"><i class="bi bi-funnel"></i> <fmt:message key="products.filters"/></h6>
        <c:if test="${not empty param.q}"><input type="hidden" name="q" value="<c:out value='${param.q}'/>"></c:if>

        <label class="form-label small fw-semibold" for="category"><fmt:message key="products.category"/></label>
        <select class="form-select form-select-sm mb-3" id="category" name="category">
            <option value=""><fmt:message key="products.allCategories"/></option>
            <c:forEach items="${categories}" var="cat">
                <option value="${cat.categoryId}" ${cat.categoryId == selectedCategory ? 'selected' : ''}><c:out value="${cat.name}"/></option>
            </c:forEach>
        </select>

        <label class="form-label small fw-semibold"><fmt:message key="products.price"/> (&#8377;)</label>
        <div class="d-flex gap-2 mb-3">
            <input class="form-control form-control-sm" type="number" name="min" min="0" placeholder="Min" value="<c:out value='${param.min}'/>">
            <input class="form-control form-control-sm" type="number" name="max" min="0" placeholder="Max" value="<c:out value='${param.max}'/>">
        </div>

        <label class="form-label small fw-semibold" for="sort"><fmt:message key="products.sort"/></label>
        <select class="form-select form-select-sm mb-3" id="sort" name="sort">
            <option value="newest" ${param.sort == 'newest' ? 'selected' : ''}>Newest first</option>
            <option value="price_asc" ${param.sort == 'price_asc' ? 'selected' : ''}>Price: low to high</option>
            <option value="price_desc" ${param.sort == 'price_desc' ? 'selected' : ''}>Price: high to low</option>
            <option value="rating" ${param.sort == 'rating' ? 'selected' : ''}>Customer rating</option>
            <option value="discount" ${param.sort == 'discount' ? 'selected' : ''}>Biggest discount</option>
            <option value="name" ${param.sort == 'name' ? 'selected' : ''}>Name A-Z</option>
        </select>

        <button class="btn btn-brand btn-sm w-100" type="submit"><fmt:message key="products.apply"/></button>
        <a class="btn btn-link btn-sm w-100" href="${ctx}/products"><fmt:message key="products.clear"/></a>
    </div>
</form>
