<%@ page import="requester.Requester" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String id = request.getParameter("pharmacy");

    boolean result = false;
    try
    {
        Requester requester = new Requester();
        result = requester.delete(Long.valueOf(id));
        if(result)
            result = requester.delete(Long.valueOf(id));
    } catch (NumberFormatException e) {
        e.printStackTrace();
    }
%>
<jsp:forward page="index.jsp"/>