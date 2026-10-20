<%-- JSP fragment: address input fields (checkout and profile pages) --%>
<div class="mb-2">
    <label class="form-label small" for="addressLine">House no., street, area</label>
    <input class="form-control form-control-sm" id="addressLine" name="addressLine" required minlength="5" maxlength="255">
</div>
<div class="row g-2 mb-2">
    <div class="col-sm-4">
        <label class="form-label small" for="city">City</label>
        <input class="form-control form-control-sm" id="city" name="city" required maxlength="80">
    </div>
    <div class="col-sm-4">
        <label class="form-label small" for="state">State</label>
        <input class="form-control form-control-sm" id="state" name="state" required maxlength="80">
    </div>
    <div class="col-sm-4">
        <label class="form-label small" for="pincode">PIN code</label>
        <input class="form-control form-control-sm" id="pincode" name="pincode" required pattern="[1-9][0-9]{5}" maxlength="6">
    </div>
</div>
<div class="mb-3">
    <span class="form-label small d-block">Address type</span>
    <div class="form-check form-check-inline">
        <input class="form-check-input" type="radio" name="addressType" id="typeHome" value="HOME" checked>
        <label class="form-check-label small" for="typeHome">Home</label>
    </div>
    <div class="form-check form-check-inline">
        <input class="form-check-input" type="radio" name="addressType" id="typeWork" value="WORK">
        <label class="form-check-label small" for="typeWork">Work</label>
    </div>
    <div class="form-check form-check-inline">
        <input class="form-check-input" type="radio" name="addressType" id="typeOther" value="OTHER">
        <label class="form-check-label small" for="typeOther">Other</label>
    </div>
</div>
