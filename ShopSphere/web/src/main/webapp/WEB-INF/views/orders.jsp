<c:set var="pageTitle" value="My Orders"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<h1 class="h4 fw-bold mb-3"><i class="bi bi-box-seam"></i> <fmt:message key="nav.orders"/></h1>

<c:choose>
    <c:when test="${empty orders}">
        <div class="text-center py-5 bg-white rounded-3 shadow-sm">
            <i class="bi bi-bag display-3 text-muted"></i>
            <p class="mt-3">You have not placed any orders yet.</p>
            <a class="btn btn-brand" href="${ctx}/products"><fmt:message key="home.shopNow"/></a>
        </div>
    </c:when>
    <c:otherwise>
        <c:forEach items="${orders}" var="o">
            <div class="bg-white rounded-3 shadow-sm p-3 mb-3">
                <div class="d-flex flex-wrap justify-content-between align-items-center gap-2">
                    <div>
                        <strong>Order #${o.orderId}</strong>
                        <span class="text-muted small ms-2"><fmt:formatDate value="${o.createdAt}" type="date" dateStyle="medium"/></span>
                        <div class="small text-muted">
                            ${o.itemCount} item(s):
                            <c:forEach items="${o.items}" var="i" varStatus="s" end="2"><c:out value="${i.productName}"/>${s.last ? '' : ', '}</c:forEach>
                            <c:if test="${fn:length(o.items) > 3}"> and more</c:if>
                        </div>
                    </div>
                    <div class="text-end">
                        <div class="fw-bold"><ss:price value="${o.totalAmount}"/></div>
                        <t:statusBadge status="${o.orderStatus}"/>
                    </div>
                    <a class="btn btn-sm btn-outline-primary" href="${ctx}/order?id=${o.orderId}">View details</a>
                </div>
            </div>
        </c:forEach>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
