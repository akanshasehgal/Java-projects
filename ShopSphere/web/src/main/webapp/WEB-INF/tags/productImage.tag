<%@ tag description="Product picture, or a coloured placeholder with the product's initial" pageEncoding="UTF-8" %>
<%@ attribute name="product" required="true" type="com.shopsphere.model.Product" %>
<%@ attribute name="large" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:choose>
    <c:when test="${not empty product.imageUrl}">
        <img src="<c:out value='${product.imageUrl}'/>" alt="<c:out value='${product.name}'/>"
             class="product-img ${large ? 'product-img-lg' : ''}" loading="lazy">
    </c:when>
    <c:otherwise>
        <div class="product-placeholder ${large ? 'product-img-lg' : ''}" style="--hue: ${(product.productId * 47) % 360}"
             role="img" aria-label="<c:out value='${product.name}'/>">
            <span><c:out value="${fn:toUpperCase(fn:substring(product.name, 0, 1))}"/></span>
            <small><c:out value="${product.categoryName}"/></small>
        </div>
    </c:otherwise>
</c:choose>
