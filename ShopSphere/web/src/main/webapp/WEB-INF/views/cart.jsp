<c:set var="pageTitle" value="Cart"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<h1 class="h4 fw-bold mb-3"><i class="bi bi-cart3"></i> <fmt:message key="cart.title"/>
    <c:if test="${not empty summary.items}"><span class="text-muted fs-6">(${summary.itemCount} items)</span></c:if></h1>

<c:choose>
    <c:when test="${empty summary.items}">
        <div class="text-center py-5 bg-white rounded-3 shadow-sm">
            <i class="bi bi-cart-x display-3 text-muted"></i>
            <p class="mt-3"><fmt:message key="cart.empty"/></p>
            <a class="btn btn-brand" href="${ctx}/products"><fmt:message key="cart.continue"/></a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="row g-4">
            <div class="col-lg-8">
                <div class="bg-white rounded-3 shadow-sm">
                    <c:forEach items="${summary.items}" var="item">
                        <c:set var="p" value="${item.product}"/>
                        <div class="d-flex gap-3 p-3 border-bottom align-items-center">
                            <a href="${ctx}/product?id=${p.productId}" style="width: 90px; flex-shrink: 0">
                                <t:productImage product="${p}"/>
                            </a>
                            <div class="flex-grow-1">
                                <a class="fw-semibold text-decoration-none text-dark" href="${ctx}/product?id=${p.productId}"><c:out value="${p.name}"/></a>
                                <div class="small text-muted"><c:out value="${p.brand}"/></div>
                                <ss:price value="${p.finalPrice}" mrp="${p.price}"/>
                                <c:if test="${item.quantity > p.stock}">
                                    <div class="small text-danger">Only ${p.stock} left in stock - please reduce the quantity.</div>
                                </c:if>
                                <div class="d-flex align-items-center gap-2 mt-2">
                                    <form action="${ctx}/cart" method="post" class="d-flex align-items-center gap-1">
                                        <input type="hidden" name="action" value="update">
                                        <input type="hidden" name="productId" value="${p.productId}">
                                        <label class="small" for="qty${p.productId}">Qty</label>
                                        <select class="form-select form-select-sm w-auto" id="qty${p.productId}" name="quantity" data-autosubmit>
                                            <c:forEach begin="1" end="${p.stock < 10 ? (p.stock < 1 ? 1 : p.stock) : 10}" var="q">
                                                <option value="${q}" ${q == item.quantity ? 'selected' : ''}>${q}</option>
                                            </c:forEach>
                                        </select>
                                        <noscript><button class="btn btn-sm btn-outline-secondary">Update</button></noscript>
                                    </form>
                                    <form action="${ctx}/cart" method="post">
                                        <input type="hidden" name="action" value="remove">
                                        <input type="hidden" name="productId" value="${p.productId}">
                                        <button class="btn btn-sm btn-link text-danger" type="submit"><i class="bi bi-trash"></i> Remove</button>
                                    </form>
                                </div>
                            </div>
                            <div class="fw-bold text-end" style="min-width: 90px"><ss:price value="${item.subtotal}"/></div>
                        </div>
                    </c:forEach>
                </div>
                <a class="btn btn-link mt-2" href="${ctx}/products"><i class="bi bi-arrow-left"></i> <fmt:message key="cart.continue"/></a>
            </div>

            <div class="col-lg-4">
                <%@ include file="/WEB-INF/views/fragments/totals.jsp" %>
                <a class="btn btn-warning btn-lg w-100 mt-3 fw-semibold" href="${ctx}/checkout"><fmt:message key="cart.checkout"/></a>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
