<c:set var="pageTitle" value="${product.name}"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<nav aria-label="breadcrumb">
    <ol class="breadcrumb small">
        <li class="breadcrumb-item"><a href="${ctx}/home"><fmt:message key="nav.home"/></a></li>
        <li class="breadcrumb-item"><a href="${ctx}/products?category=${product.categoryId}"><c:out value="${product.categoryName}"/></a></li>
        <li class="breadcrumb-item active" aria-current="page"><c:out value="${fn:substring(product.name, 0, 40)}"/></li>
    </ol>
</nav>

<div class="row g-4 bg-white rounded-3 shadow-sm p-3 mb-4">
    <div class="col-md-5">
        <t:productImage product="${product}" large="true"/>
    </div>
    <div class="col-md-7">
        <div class="text-muted text-uppercase small fw-semibold"><c:out value="${product.brand}"/></div>
        <h1 class="h3 fw-bold"><c:out value="${product.name}"/></h1>
        <c:if test="${product.reviewCount > 0}">
            <a href="#reviews" class="text-decoration-none"><t:rating value="${product.avgRating}" count="${product.reviewCount}"/></a>
        </c:if>

        <div class="my-3"><ss:price value="${product.finalPrice}" mrp="${product.price}" size="large"/></div>
        <p class="small text-muted mb-2">Inclusive of all taxes. Free delivery on orders above &#8377;499.</p>

        <c:choose>
            <c:when test="${not product.inStock}"><span class="badge text-bg-danger mb-3"><fmt:message key="product.outOfStock"/></span></c:when>
            <c:when test="${product.lowStock}">
                <span class="badge text-bg-warning mb-3"><fmt:message key="product.onlyLeft"><fmt:param value="${product.stock}"/></fmt:message></span>
            </c:when>
            <c:otherwise><span class="badge text-bg-success mb-3"><fmt:message key="product.inStock"/></span></c:otherwise>
        </c:choose>

        <c:if test="${product.inStock}">
            <c:choose>
                <c:when test="${empty sessionScope.user}">
                    <div class="d-flex gap-2 mt-2">
                        <a class="btn btn-brand btn-lg" href="${ctx}/login?redirect=%2Fproduct%3Fid%3D${product.productId}">
                            <i class="bi bi-cart-plus"></i> <fmt:message key="product.addToCart"/></a>
                    </div>
                </c:when>
                <c:otherwise>
                    <form action="${ctx}/cart" method="post" class="d-flex flex-wrap gap-2 align-items-center mt-2">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="${product.productId}">
                        <input type="hidden" name="returnTo" value="/product?id=${product.productId}">
                        <label class="small" for="qty">Qty</label>
                        <select class="form-select w-auto" id="qty" name="quantity">
                            <c:forEach begin="1" end="${product.stock < 10 ? product.stock : 10}" var="q">
                                <option value="${q}">${q}</option>
                            </c:forEach>
                        </select>
                        <button class="btn btn-brand btn-lg" type="submit"><i class="bi bi-cart-plus"></i> <fmt:message key="product.addToCart"/></button>
                        <button class="btn btn-warning btn-lg" type="submit" name="buyNow" value="1"><i class="bi bi-lightning-fill"></i> <fmt:message key="product.buyNow"/></button>
                    </form>
                </c:otherwise>
            </c:choose>
        </c:if>

        <c:if test="${not empty sessionScope.user}">
            <form action="${ctx}/wishlist" method="post" class="mt-3">
                <input type="hidden" name="productId" value="${product.productId}">
                <input type="hidden" name="returnTo" value="/product?id=${product.productId}">
                <c:choose>
                    <c:when test="${inWishlist}">
                        <input type="hidden" name="action" value="remove">
                        <button class="btn btn-outline-danger btn-sm" type="submit"><i class="bi bi-heart-fill"></i> <fmt:message key="product.wishlistRemove"/></button>
                    </c:when>
                    <c:otherwise>
                        <input type="hidden" name="action" value="add">
                        <button class="btn btn-outline-secondary btn-sm" type="submit"><i class="bi bi-heart"></i> <fmt:message key="product.wishlistAdd"/></button>
                    </c:otherwise>
                </c:choose>
            </form>
        </c:if>

        <hr>
        <h2 class="h6 fw-bold"><fmt:message key="product.description"/></h2>
        <p class="mb-0"><c:out value="${product.description}"/></p>
    </div>
</div>

<%-- Reviews --%>
<div class="row g-4 mb-4" id="reviews">
    <div class="col-lg-7">
        <div class="bg-white rounded-3 shadow-sm p-3 h-100">
            <h2 class="h5 fw-bold">${product.reviewCount} <fmt:message key="product.reviews"/></h2>
            <c:choose>
                <c:when test="${empty reviews}"><p class="text-muted mb-0"><fmt:message key="product.noReviews"/></p></c:when>
                <c:otherwise>
                    <c:forEach items="${reviews}" var="r">
                        <div class="border-bottom py-2">
                            <t:rating value="${r.rating}"/>
                            <strong class="ms-1"><c:out value="${r.userName}"/></strong>
                            <small class="text-muted ms-2"><fmt:formatDate value="${r.createdAt}" type="date" dateStyle="medium"/></small>
                            <c:if test="${not empty r.reviewText}"><p class="mb-0 mt-1"><c:out value="${r.reviewText}"/></p></c:if>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <div class="col-lg-5">
        <div class="bg-white rounded-3 shadow-sm p-3 h-100">
            <h2 class="h6 fw-bold"><fmt:message key="product.writeReview"/></h2>
            <c:choose>
                <c:when test="${empty sessionScope.user}">
                    <p class="small mb-0"><a href="${ctx}/login?redirect=%2Fproduct%3Fid%3D${product.productId}">Log in</a> to write a review.</p>
                </c:when>
                <c:otherwise>
                    <form action="${ctx}/review" method="post">
                        <input type="hidden" name="productId" value="${product.productId}">
                        <input type="hidden" name="rating" id="ratingInput" value="5">
                        <div class="star-picker text-warning fs-4 mb-2" role="radiogroup" aria-label="Rating">
                            <i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i>
                        </div>
                        <textarea class="form-control mb-2" name="reviewText" rows="3" maxlength="1000"
                                  placeholder="What did you like or dislike?"></textarea>
                        <button class="btn btn-brand btn-sm" type="submit">Submit review</button>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<c:if test="${not empty related}">
    <h2 class="h5 fw-bold mb-3"><fmt:message key="product.related"/></h2>
    <div class="row g-3">
        <c:forEach items="${related}" var="p">
            <div class="col-6 col-md-3"><t:productCard product="${p}"/></div>
        </c:forEach>
    </div>
</c:if>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
