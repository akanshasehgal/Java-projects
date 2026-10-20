<%-- JSP fragment: price summary box shared by the cart and checkout pages (needs "summary") --%>
<div class="bg-white rounded-3 shadow-sm p-3">
    <h2 class="h6 fw-bold text-uppercase text-muted">Price details</h2>
    <div class="d-flex justify-content-between py-1">
        <span><fmt:message key="cart.subtotal"/> (${summary.itemCount} items)</span>
        <ss:price value="${summary.subtotal}"/>
    </div>
    <c:if test="${summary.discount > 0}">
        <div class="d-flex justify-content-between py-1 text-success">
            <span><fmt:message key="cart.discount"/> (<c:out value="${summary.coupon.code}"/>)</span>
            <span>- <ss:price value="${summary.discount}"/></span>
        </div>
    </c:if>
    <div class="d-flex justify-content-between py-1">
        <span><fmt:message key="cart.shipping"/></span>
        <c:choose>
            <c:when test="${summary.shipping == 0}"><span class="text-success fw-semibold"><fmt:message key="cart.free"/></span></c:when>
            <c:otherwise><ss:price value="${summary.shipping}"/></c:otherwise>
        </c:choose>
    </div>
    <hr>
    <div class="d-flex justify-content-between fw-bold fs-5">
        <span><fmt:message key="cart.total"/></span>
        <ss:price value="${summary.total}"/>
    </div>
    <c:if test="${summary.shipping > 0}">
        <p class="small text-muted mt-2 mb-0"><i class="bi bi-truck"></i> Add items worth
            <ss:price value="${499 - (summary.subtotal - summary.discount)}"/> more for free delivery.</p>
    </c:if>
</div>
