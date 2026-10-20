<c:set var="pageTitle" value="Wishlist"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<h1 class="h4 fw-bold mb-3"><i class="bi bi-heart"></i> <fmt:message key="nav.wishlist"/> <span class="text-muted fs-6">(${fn:length(items)})</span></h1>

<c:choose>
    <c:when test="${empty items}">
        <div class="text-center py-5 bg-white rounded-3 shadow-sm">
            <i class="bi bi-heart display-3 text-muted"></i>
            <p class="mt-3">Your wishlist is empty. Tap the heart on any product to save it here.</p>
            <a class="btn btn-brand" href="${ctx}/products"><fmt:message key="home.shopNow"/></a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="row g-3">
            <c:forEach items="${items}" var="w">
                <c:set var="p" value="${w.product}"/>
                <div class="col-6 col-md-4 col-lg-3">
                    <div class="card product-card h-100 border-0 shadow-sm">
                        <a href="${ctx}/product?id=${p.productId}"><t:productImage product="${p}"/></a>
                        <div class="card-body d-flex flex-column">
                            <a href="${ctx}/product?id=${p.productId}" class="product-title"><c:out value="${p.name}"/></a>
                            <div class="mt-auto pt-2"><ss:price value="${p.finalPrice}" mrp="${p.price}"/></div>
                        </div>
                        <div class="card-footer bg-white border-0 pt-0 pb-3 d-flex gap-2">
                            <form action="${ctx}/wishlist" method="post" class="flex-grow-1">
                                <input type="hidden" name="action" value="move">
                                <input type="hidden" name="productId" value="${p.productId}">
                                <button class="btn btn-brand btn-sm w-100" type="submit" ${p.inStock and p.status ? '' : 'disabled'}>
                                    <i class="bi bi-cart-plus"></i> Move to cart</button>
                            </form>
                            <form action="${ctx}/wishlist" method="post">
                                <input type="hidden" name="action" value="remove">
                                <input type="hidden" name="productId" value="${p.productId}">
                                <button class="btn btn-outline-danger btn-sm" type="submit" title="Remove"><i class="bi bi-trash"></i></button>
                            </form>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
