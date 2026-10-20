<%-- Prelude automatically included at the top of every JSP (see jsp-config in web.xml) --%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="ss" uri="http://shopsphere.com/tags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<fmt:setLocale value="${empty lang ? 'en' : lang}" scope="request"/>
<fmt:setBundle basename="i18n.messages" scope="request"/>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
