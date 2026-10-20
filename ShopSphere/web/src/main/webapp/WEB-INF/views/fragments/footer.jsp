<%-- JSP fragment: page footer and scripts --%>
</div>
</main>
<footer class="footer-brand text-light pt-4 pb-3 mt-auto">
    <div class="container">
        <div class="row g-3">
            <div class="col-md-5">
                <h5 class="fw-bold"><i class="bi bi-bag-heart-fill"></i> ShopSphere</h5>
                <p class="small text-white-50 mb-0"><fmt:message key="footer.tagline"/></p>
            </div>
            <div class="col-6 col-md-3">
                <h6>Shop</h6>
                <ul class="list-unstyled small">
                    <li><a href="${ctx}/products">All Products</a></li>
                    <li><a href="${ctx}/products?sort=discount">Top Deals</a></li>
                    <li><a href="${ctx}/products?sort=newest">New Arrivals</a></li>
                </ul>
            </div>
            <div class="col-6 col-md-4">
                <h6>Project</h6>
                <ul class="list-unstyled small">
                    <li><a href="${ctx}/demo/index.jsp"><fmt:message key="nav.demos"/></a></li>
                    <li><a href="${ctx}/admin/dashboard"><fmt:message key="nav.admin"/></a></li>
                    <li><span class="text-white-50">${initParam.supportEmail}</span></li>
                </ul>
            </div>
        </div>
        <hr class="border-secondary">
        <jsp:useBean id="footerDate" class="java.util.Date"/>
        <p class="small text-white-50 mb-0">
            &copy; <fmt:formatDate value="${footerDate}" pattern="yyyy"/> ShopSphere. <fmt:message key="footer.rights"/>
        </p>
    </div>
</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${ctx}/js/app.js"></script>
</body>
</html>
