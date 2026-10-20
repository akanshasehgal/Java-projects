<%-- JSP fragment: responsive navbar with search, cart counter and language switch --%>
<nav class="navbar navbar-expand-lg navbar-dark bg-brand sticky-top shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${ctx}/home"><i class="bi bi-bag-heart-fill"></i> ShopSphere</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav"
                aria-controls="mainNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="mainNav">
            <form class="d-flex mx-lg-3 my-2 my-lg-0 flex-grow-1 nav-search" action="${ctx}/products" method="get" role="search">
                <input class="form-control" type="search" name="q" value="<c:out value='${param.q}'/>"
                       placeholder="<fmt:message key='nav.search'/>" aria-label="Search">
                <button class="btn btn-warning ms-1" type="submit" aria-label="Search"><i class="bi bi-search"></i></button>
            </form>

            <ul class="navbar-nav ms-auto align-items-lg-center">
                <li class="nav-item"><a class="nav-link" href="${ctx}/products"><fmt:message key="nav.products"/></a></li>
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <c:if test="${sessionScope.user.admin}">
                            <li class="nav-item"><a class="nav-link text-warning" href="${ctx}/admin/dashboard">
                                <i class="bi bi-speedometer2"></i> <fmt:message key="nav.admin"/></a></li>
                        </c:if>
                        <li class="nav-item"><a class="nav-link" href="${ctx}/wishlist" title="Wishlist">
                            <i class="bi bi-heart"></i> <span class="d-lg-none"><fmt:message key="nav.wishlist"/></span></a></li>
                        <li class="nav-item">
                            <a class="nav-link position-relative" href="${ctx}/cart" title="Cart">
                                <i class="bi bi-cart3 fs-5"></i>
                                <c:if test="${sessionScope.cartCount > 0}">
                                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger cart-badge">
                                        ${sessionScope.cartCount}</span>
                                </c:if>
                                <span class="d-lg-none"><fmt:message key="nav.cart"/></span>
                            </a>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <i class="bi bi-person-circle"></i> <c:out value="${fn:split(sessionScope.user.name, ' ')[0]}"/>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li><a class="dropdown-item" href="${ctx}/profile"><i class="bi bi-person"></i> <fmt:message key="nav.profile"/></a></li>
                                <li><a class="dropdown-item" href="${ctx}/orders"><i class="bi bi-box-seam"></i> <fmt:message key="nav.orders"/></a></li>
                                <li><a class="dropdown-item" href="${ctx}/wishlist"><i class="bi bi-heart"></i> <fmt:message key="nav.wishlist"/></a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <form action="${ctx}/logout" method="post" class="px-2">
                                        <button class="dropdown-item text-danger" type="submit"><i class="bi bi-box-arrow-right"></i> <fmt:message key="nav.logout"/></button>
                                    </form>
                                </li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item"><a class="nav-link" href="${ctx}/login"><fmt:message key="nav.login"/></a></li>
                        <li class="nav-item"><a class="btn btn-warning btn-sm ms-lg-2" href="${ctx}/register"><fmt:message key="nav.register"/></a></li>
                    </c:otherwise>
                </c:choose>
                <li class="nav-item dropdown ms-lg-2">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false"
                       title="<fmt:message key='nav.language'/>"><i class="bi bi-translate"></i> ${lang == 'hi' ? 'हिं' : 'EN'}</a>
                    <ul class="dropdown-menu dropdown-menu-end">
                        <li><a class="dropdown-item ${lang == 'en' ? 'active' : ''}" href="${ctx}/language?lang=en">English</a></li>
                        <li><a class="dropdown-item ${lang == 'hi' ? 'active' : ''}" href="${ctx}/language?lang=hi">हिन्दी</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>
