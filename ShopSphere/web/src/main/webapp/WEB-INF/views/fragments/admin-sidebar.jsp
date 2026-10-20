<%-- JSP fragment: admin navigation. Set "adminPage" to highlight the current item. --%>
<div class="list-group admin-sidebar mb-3">
    <a href="${ctx}/admin/dashboard" class="list-group-item list-group-item-action ${adminPage == 'dashboard' ? 'active' : ''}"><i class="bi bi-speedometer2"></i> Dashboard</a>
    <a href="${ctx}/admin/products" class="list-group-item list-group-item-action ${adminPage == 'products' ? 'active' : ''}"><i class="bi bi-box"></i> Products</a>
    <a href="${ctx}/admin/categories" class="list-group-item list-group-item-action ${adminPage == 'categories' ? 'active' : ''}"><i class="bi bi-tags"></i> Categories</a>
    <a href="${ctx}/admin/orders" class="list-group-item list-group-item-action ${adminPage == 'orders' ? 'active' : ''}"><i class="bi bi-receipt"></i> Orders</a>
    <a href="${ctx}/admin/users" class="list-group-item list-group-item-action ${adminPage == 'users' ? 'active' : ''}"><i class="bi bi-people"></i> Customers</a>
    <a href="${ctx}/admin/coupons" class="list-group-item list-group-item-action ${adminPage == 'coupons' ? 'active' : ''}"><i class="bi bi-ticket-perforated"></i> Coupons</a>
    <a href="${ctx}/home" class="list-group-item list-group-item-action"><i class="bi bi-shop"></i> View store</a>
</div>
