<c:set var="pageTitle" value="Online Shopping"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<section class="hero mb-4">
    <div class="row align-items-center">
        <div class="col-lg-7">
            <h1 class="display-6 fw-bold"><fmt:message key="home.hero.title"/></h1>
            <p class="lead opacity-75 mb-4"><fmt:message key="home.hero.subtitle"/></p>
            <a href="${ctx}/products" class="btn btn-warning btn-lg fw-semibold"><fmt:message key="home.shopNow"/> <i class="bi bi-arrow-right"></i></a>
            <c:if test="${empty sessionScope.user}">
                <a href="${ctx}/register" class="btn btn-outline-light btn-lg ms-2"><fmt:message key="nav.register"/></a>
            </c:if>
        </div>
        <div class="col-lg-5 d-none d-lg-block text-center">
            <i class="bi bi-bag-check" style="font-size: 9rem; opacity: .85"></i>
        </div>
    </div>
</section>

<%-- Categories --%>
<h2 class="h5 fw-bold mb-3"><fmt:message key="home.categories"/></h2>
<div class="row g-3 mb-5">
    <c:forEach items="${categories}" var="cat">
        <c:choose>
            <c:when test="${fn:contains(cat.name, 'Electronic')}"><c:set var="icon" value="bi-phone"/></c:when>
            <c:when test="${fn:contains(cat.name, 'Fashion')}"><c:set var="icon" value="bi-handbag"/></c:when>
            <c:when test="${fn:contains(cat.name, 'Book')}"><c:set var="icon" value="bi-book"/></c:when>
            <c:when test="${fn:contains(cat.name, 'Home')}"><c:set var="icon" value="bi-house-heart"/></c:when>
            <c:when test="${fn:contains(cat.name, 'Sport')}"><c:set var="icon" value="bi-trophy"/></c:when>
            <c:when test="${fn:contains(cat.name, 'Beauty')}"><c:set var="icon" value="bi-stars"/></c:when>
            <c:otherwise><c:set var="icon" value="bi-grid"/></c:otherwise>
        </c:choose>
        <div class="col-4 col-md-2">
            <a class="category-tile" href="${ctx}/products?category=${cat.categoryId}">
                <i class="bi ${icon}"></i>
                <div class="fw-semibold small mt-1"><c:out value="${cat.name}"/></div>
                <div class="text-muted" style="font-size: .7rem">${cat.productCount} <fmt:message key="home.products"/></div>
            </a>
        </div>
    </c:forEach>
</div>

<%-- Top deals --%>
<c:if test="${not empty deals}">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="h5 fw-bold mb-0"><i class="bi bi-lightning-charge-fill text-warning"></i> <fmt:message key="home.deals"/></h2>
        <a href="${ctx}/products?sort=discount" class="small"><fmt:message key="home.viewAll"/> <i class="bi bi-chevron-right"></i></a>
    </div>
    <div class="row g-3 mb-5">
        <c:forEach items="${deals}" var="p">
            <div class="col-6 col-md-3"><t:productCard product="${p}"/></div>
        </c:forEach>
    </div>
</c:if>

<%-- New arrivals --%>
<div class="d-flex justify-content-between align-items-center mb-3">
    <h2 class="h5 fw-bold mb-0"><fmt:message key="home.latest"/></h2>
    <a href="${ctx}/products?sort=newest" class="small"><fmt:message key="home.viewAll"/> <i class="bi bi-chevron-right"></i></a>
</div>
<div class="row g-3 mb-5">
    <c:forEach items="${latest}" var="p">
        <div class="col-6 col-md-4 col-lg-3"><t:productCard product="${p}"/></div>
    </c:forEach>
</div>

<%-- Recently viewed (read from a cookie) --%>
<c:if test="${not empty recent}">
    <h2 class="h5 fw-bold mb-3"><i class="bi bi-clock-history"></i> <fmt:message key="home.recent"/></h2>
    <div class="row g-3 mb-4">
        <c:forEach items="${recent}" var="p" end="3">
            <div class="col-6 col-md-3"><t:productCard product="${p}"/></div>
        </c:forEach>
    </div>
</c:if>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
