<c:set var="pageTitle" value="Order Placed"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-8 col-lg-6 text-center">
        <div class="bg-white rounded-3 shadow-sm p-5">
            <i class="bi bi-check-circle-fill text-success" style="font-size: 4.5rem"></i>
            <h1 class="h3 fw-bold mt-3">Thank you! Your order is placed.</h1>
            <p class="text-muted">Order <strong>#${order.orderId}</strong> &middot;
                <fmt:formatDate value="${order.createdAt}" type="both" dateStyle="medium" timeStyle="short"/></p>

            <table class="table table-sm text-start mt-4 mb-4">
                <tr><th>Amount</th><td><ss:price value="${order.totalAmount}"/></td></tr>
                <c:if test="${order.discount > 0}"><tr><th>You saved</th><td class="text-success"><ss:price value="${order.discount}"/></td></tr></c:if>
                <tr><th>Payment</th><td>${order.paymentMethod} <t:statusBadge status="${order.paymentStatus}"/></td></tr>
                <c:if test="${not empty payment.transactionReference}">
                    <tr><th>Transaction ID</th><td><code>${payment.transactionReference}</code></td></tr>
                </c:if>
                <tr><th>Deliver to</th><td class="small"><c:out value="${order.shippingAddress}"/></td></tr>
            </table>

            <a class="btn btn-brand" href="${ctx}/order?id=${order.orderId}"><i class="bi bi-truck"></i> Track order</a>
            <a class="btn btn-outline-secondary ms-2" href="${ctx}/products"><fmt:message key="cart.continue"/></a>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
