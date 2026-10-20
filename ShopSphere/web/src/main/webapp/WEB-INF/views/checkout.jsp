<c:set var="pageTitle" value="Checkout"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<h1 class="h4 fw-bold mb-3"><i class="bi bi-shield-check"></i> Checkout</h1>

<div class="row g-4">
    <div class="col-lg-7">
        <%-- Main order form: address + payment. The coupon and new-address forms are separate (no nested forms). --%>
        <form id="orderForm" action="${ctx}/checkout" method="post">
            <input type="hidden" name="action" value="placeOrder">

            <div class="bg-white rounded-3 shadow-sm p-3 mb-3">
                <h2 class="h6 fw-bold"><span class="badge text-bg-dark me-1">1</span> Delivery address</h2>
                <c:choose>
                    <c:when test="${empty addresses}">
                        <p class="text-muted small mb-0">You have no saved addresses. Please add one below.</p>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${addresses}" var="a" varStatus="s">
                            <div class="form-check border rounded-3 p-3 ps-5 mb-2">
                                <input class="form-check-input" type="radio" name="addressId" id="addr${a.addressId}"
                                       value="${a.addressId}" ${s.first ? 'checked' : ''} required>
                                <label class="form-check-label" for="addr${a.addressId}">
                                    <span class="badge text-bg-light border me-1">${a.addressType}</span>
                                    <strong><c:out value="${sessionScope.user.name}"/></strong><br>
                                    <span class="small"><c:out value="${a.fullAddress}"/></span>
                                </label>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                <button class="btn btn-link btn-sm px-0" type="button" data-bs-toggle="collapse" data-bs-target="#newAddress">
                    <i class="bi bi-plus-circle"></i> Add a new address</button>
            </div>

            <div class="bg-white rounded-3 shadow-sm p-3 mb-3">
                <h2 class="h6 fw-bold"><span class="badge text-bg-dark me-1">2</span> Payment method <span class="badge text-bg-warning ms-1">Mock payment</span></h2>
                <c:forEach items="${paymentMethods}" var="m" varStatus="s">
                    <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" name="paymentMethod" id="pay${m}" value="${m}" ${s.first ? 'checked' : ''}>
                        <label class="form-check-label" for="pay${m}">
                            <c:choose>
                                <c:when test="${m == 'COD'}"><i class="bi bi-cash-coin"></i> Cash on Delivery</c:when>
                                <c:when test="${m == 'UPI'}"><i class="bi bi-phone"></i> UPI <small class="text-muted">(simulated - always succeeds)</small></c:when>
                                <c:otherwise><i class="bi bi-credit-card"></i> Credit / Debit Card <small class="text-muted">(simulated - no card details needed)</small></c:otherwise>
                            </c:choose>
                        </label>
                    </div>
                </c:forEach>
            </div>
        </form>

        <div class="collapse ${empty addresses ? 'show' : ''}" id="newAddress">
            <form action="${ctx}/checkout" method="post" class="bg-white rounded-3 shadow-sm p-3 mb-3">
                <input type="hidden" name="action" value="addAddress">
                <%@ include file="/WEB-INF/views/fragments/address-fields.jsp" %>
                <button class="btn btn-outline-primary btn-sm" type="submit">Save address</button>
            </form>
        </div>
    </div>

    <div class="col-lg-5">
        <div class="bg-white rounded-3 shadow-sm p-3 mb-3">
            <h2 class="h6 fw-bold">Order summary</h2>
            <c:forEach items="${summary.items}" var="item">
                <div class="d-flex justify-content-between small py-1">
                    <span><c:out value="${item.product.name}"/> &times; ${item.quantity}</span>
                    <ss:price value="${item.subtotal}"/>
                </div>
            </c:forEach>
        </div>

        <div class="bg-white rounded-3 shadow-sm p-3 mb-3">
            <h2 class="h6 fw-bold"><i class="bi bi-ticket-perforated"></i> Coupon</h2>
            <c:choose>
                <c:when test="${not empty summary.coupon}">
                    <div class="d-flex justify-content-between align-items-center">
                        <span><span class="badge text-bg-success"><c:out value="${summary.coupon.code}"/></span>
                            <small class="text-muted"><c:out value="${summary.coupon.description}"/></small></span>
                        <form action="${ctx}/checkout" method="post">
                            <input type="hidden" name="action" value="removeCoupon">
                            <button class="btn btn-sm btn-link text-danger" type="submit">Remove</button>
                        </form>
                    </div>
                </c:when>
                <c:otherwise>
                    <form action="${ctx}/checkout" method="post" class="d-flex gap-2">
                        <input type="hidden" name="action" value="applyCoupon">
                        <input class="form-control form-control-sm text-uppercase" name="couponCode" placeholder="Enter coupon code" required>
                        <button class="btn btn-sm btn-outline-success" type="submit">Apply</button>
                    </form>
                    <c:if test="${not empty coupons}">
                        <div class="small mt-2 text-muted">Available offers:</div>
                        <ul class="small mb-0 ps-3">
                            <c:forEach items="${coupons}" var="cp">
                                <li><code class="demo"><c:out value="${cp.code}"/></code> - <c:out value="${cp.description}"/></li>
                            </c:forEach>
                        </ul>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </div>

        <%@ include file="/WEB-INF/views/fragments/totals.jsp" %>

        <c:choose>
            <c:when test="${empty addresses}">
                <button class="btn btn-warning btn-lg w-100 mt-3" disabled>Add an address to continue</button>
            </c:when>
            <c:otherwise>
                <button class="btn btn-warning btn-lg w-100 mt-3 fw-semibold" type="submit" form="orderForm">
                    <i class="bi bi-bag-check"></i> Place order</button>
            </c:otherwise>
        </c:choose>
        <p class="small text-muted text-center mt-2 mb-0"><i class="bi bi-lock"></i> Totals are recalculated on the server when you place the order.</p>
    </div>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
