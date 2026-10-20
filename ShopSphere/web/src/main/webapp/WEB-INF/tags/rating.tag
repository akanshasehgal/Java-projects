<%@ tag description="Star rating, e.g. 4.5 = four and a half stars" pageEncoding="UTF-8" %>
<%@ attribute name="value" required="true" type="java.lang.Double" %>
<%@ attribute name="count" required="false" type="java.lang.Integer" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<span class="rating text-warning" title="${value} out of 5">
    <c:forEach begin="1" end="5" var="i">
        <c:choose>
            <c:when test="${value >= i}"><i class="bi bi-star-fill"></i></c:when>
            <c:when test="${value >= i - 0.5}"><i class="bi bi-star-half"></i></c:when>
            <c:otherwise><i class="bi bi-star"></i></c:otherwise>
        </c:choose>
    </c:forEach>
</span>
<c:if test="${not empty count}"><small class="text-muted">${value} (${count})</small></c:if>
