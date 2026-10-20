<%-- JSP fragment: order tracking timeline + items + payment (customer and admin order pages) --%>
<c:choose>
    <c:when test="${order.orderStatus == 'CANCELLED'}">
        <div class="alert alert-danger"><i class="bi bi-x-circle"></i> This order was cancelled.</div>
    </c:when>
    <c:otherwise>
        <div class="timeline">
            <c:forEach items="${statusFlow}" var="st" varStatus="s">
                <c:choose>
                    <c:when test="${s.index < order.statusStep}"><c:set var="stepClass" value="done"/></c:when>
                    <c:when test="${s.index == order.statusStep}"><c:set var="stepClass" value="current"/></c:when>
                    <c:otherwise><c:set var="stepClass" value=""/></c:otherwise>
                </c:choose>
                <div class="step ${stepClass}">
                    <div class="dot">
                        <c:choose>
                            <c:when test="${st == 'PLACED'}"><i class="bi bi-receipt"></i></c:when>
                            <c:when test="${st == 'CONFIRMED'}"><i class="bi bi-check2-circle"></i></c:when>
                            <c:when test="${st == 'SHIPPED'}"><i class="bi bi-truck"></i></c:when>
                            <c:otherwise><i class="bi bi-house-check"></i></c:otherwise>
                        </c:choose>
                    </div>
                    <div class="label">${st}</div>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<div class="row g-3">
    <div class="col-lg-8">
        <div class="bg-white rounded-3 shadow-sm">
            <table class="table mb-0">
                <thead class="table-light"><tr><th>Product</th><th class="text-center">Qty</th><th class="text-end">Price</th><th class="text-end">Subtotal</th></tr></thead>
                <tbody>
                <c:set var="itemsTotal" value="0"/>
                <c:forEach items="${order.items}" var="i">
                    <c:set var="itemsTotal" value="${itemsTotal + i.subtotal}"/>
                    <tr>
                        <td><a href="${ctx}/product?id=${i.productId}" class="text-decoration-none"><c:out value="${i.productName}"/></a></td>
                        <td class="text-center">${i.quantity}</td>
                        <td class="text-end"><ss:price value="${i.price}"/></td>
                        <td class="text-end"><ss:price value="${i.subtotal}"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <div class="col-lg-4">
        <div class="bg-white rounded-3 shadow-sm p-3 mb-3">
            <div class="d-flex justify-content-between"><span>Items</span><ss:price value="${itemsTotal}"/></div>
            <c:if test="${order.discount > 0}">
                <div class="d-flex justify-content-between text-success"><span>Coupon discount</span><span>- <ss:price value="${order.discount}"/></span></div>
            </c:if>
            <c:set var="shippingPaid" value="${order.totalAmount - itemsTotal + order.discount}"/>
            <div class="d-flex justify-content-between"><span>Shipping</span>
                <c:choose>
                    <c:when test="${shippingPaid > 0.5}"><ss:price value="${shippingPaid}"/></c:when>
                    <c:otherwise><span class="text-success">FREE</span></c:otherwise>
                </c:choose>
            </div>
            <hr class="my-2">
            <div class="d-flex justify-content-between fw-bold"><span>Total</span><ss:price value="${order.totalAmount}"/></div>
        </div>
        <div class="bg-white rounded-3 shadow-sm p-3 small">
            <div class="mb-2"><strong>Payment:</strong> ${order.paymentMethod} <t:statusBadge status="${order.paymentStatus}"/>
                <c:if test="${not empty payment.transactionReference}"><br><span class="text-muted">Txn: ${payment.transactionReference}</span></c:if>
            </div>
            <div><strong>Deliver to:</strong><br><c:out value="${order.shippingAddress}"/></div>
        </div>
    </div>
</div>
