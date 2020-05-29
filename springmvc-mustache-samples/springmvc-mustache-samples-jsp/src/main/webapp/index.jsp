<%@ page contentType="text/html; charset=utf-8" session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="mustache" uri="https://github.com/mjeanroy/springmvc-mustache/jsp/jstl/springmvc-mustache" %>

<%--@elvariable id="name" type="java.lang.String"--%>

<!DOCTYPE html>
<html>
    <head>
        <title>My Mustache Sample</title>
    </head>
    <body>
        <div class="header">
            <h1>My Mustache Sample</h1>
        </div>

        <div class="content">
            <mustache:render template="john">
                <mustache:parameter name="name" value="${name}"/>
            </mustache:render>
        </div>

        <div class="footer">
            Copyright - 2014
        </div>
    </body>
</html>