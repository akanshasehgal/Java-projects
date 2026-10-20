<%@ tag description="Reusable product card used on the home, listing and wishlist pages" pageEncoding="UTF-8" %>
<%@ attribute name="product" required="true" type="com.shopsphere.model.Product" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="ss" uri="http://shopsphere.com/tags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="fwdPath" value="${requestScope['jakarta.servlet.forward.servlet_path']}"/>
<c:set var="fwdQuery" value="${requestScope['jakarta.servlet.forward.query_string']}"/>
<c:set var="returnTo" value="${fwdPath}${empty fwdQuery ? '' : '?'}${fwdQuery}"/>

<div class="card product-card h-100 border-0 shadow-sm">
    <a href="${ctx}/product?id=${product.productId}" class="product-thumb-link">
        <t:productImage product="${product}"/>
        <c:if test="${product.discount > 0}">
            <span class="badge bg-danger discount-badge"><fmt:formatNumber value="${product.discount}" maxFractionDigits="0"/>% OFF</span>
        </c:if>
    </a>
    <div class="card-body d-flex flex-column">
        <small class="text-muted text-uppercase small-caps"><c:out value="${product.brand}"/></small>
        <a href="${ctx}/product?id=${product.productId}" class="product-title stretched-link-off">
            <c:out value="${product.name}"/>
        </a>
        <div class="small my-1">
            <c:choose>
                <c:when test="${product.reviewCount > 0}"><t:rating value="${product.avgRating}" count="${product.reviewCount}"/></c:when>
                <c:otherwise><span class="text-muted">&nbsp;</span></c:otherwise>
            </c:choose>
        </div>
        <div class="mt-auto">
            <ss:price value="${product.finalPrice}" mrp="${product.price}"/>
            <c:choose>
                <c:when test="${not product.inStock}">
                    <div class="small text-danger fw-semibold mt-1"><fmt:message key="product.outOfStock"/></div>
                </c:when>
                <c:when test="${product.lowStock}">
                    <div class="small text-warning-emphasis fw-semibold mt-1"><fmt:message key="product.onlyLeft"><fmt:param value="${product.stock}"/></fmt:message></div>
                </c:when>
            </c:choose>
        </div>
    </div>
    <div class="card-footer bg-white border-0 pt-0 pb-3">
        <c:choose>
            <c:when test="${not product.inStock}">
                <button class="btn btn-outline-secondary btn-sm w-100" disabled><fmt:message key="product.outOfStock"/></button>
            </c:when>
            <c:when test="${empty sessionScope.user}">
                <a class="btn btn-brand btn-sm w-100" href="${ctx}/login?redirect=%2Fproduct%3Fid%3D${product.productId}">
                    <i class="bi bi-cart-plus"></i> <fmt:message key="product.addToCart"/></a>
            </c:when>
            <c:otherwise>
                <form action="${ctx}/cart" method="post">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="productId" value="${product.productId}">
                    <input type="hidden" name="returnTo" value="${fn:escapeXml(returnTo)}">
                    <button class="btn btn-brand btn-sm w-100" type="submit"><i class="bi bi-cart-plus"></i> <fmt:message key="product.addToCart"/></button>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</div>
