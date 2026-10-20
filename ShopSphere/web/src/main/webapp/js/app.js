// ShopSphere client-side helpers (all business rules are re-checked on the server)
document.addEventListener("DOMContentLoaded", function () {

    // Ask before destructive actions: <form data-confirm="Are you sure?">
    document.querySelectorAll("form[data-confirm]").forEach(function (form) {
        form.addEventListener("submit", function (e) {
            if (!window.confirm(form.getAttribute("data-confirm"))) e.preventDefault();
        });
    });

    // Quantity selectors in the cart submit automatically when changed
    document.querySelectorAll("select[data-autosubmit]").forEach(function (select) {
        select.addEventListener("change", function () { select.form.submit(); });
    });

    // Star rating picker on the product page
    var picker = document.querySelector(".star-picker");
    if (picker) {
        var input = document.getElementById("ratingInput");
        var stars = picker.querySelectorAll("i");
        var paint = function (n) {
            stars.forEach(function (s, i) {
                s.className = i < n ? "bi bi-star-fill" : "bi bi-star";
            });
        };
        stars.forEach(function (star, index) {
            star.addEventListener("mouseenter", function () { paint(index + 1); });
            star.addEventListener("click", function () { input.value = index + 1; paint(index + 1); });
        });
        picker.addEventListener("mouseleave", function () { paint(parseInt(input.value || "0", 10)); });
    }

    // Auto-hide success alerts after a few seconds
    document.querySelectorAll(".alert-success").forEach(function (alert) {
        setTimeout(function () {
            if (window.bootstrap) bootstrap.Alert.getOrCreateInstance(alert).close();
        }, 4000);
    });
});
