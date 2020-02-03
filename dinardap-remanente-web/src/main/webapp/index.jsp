<%@page import="java.util.Map"%>
<%@page import="javax.faces.context.FacesContext"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%
    /*session = request.getSession();
    session.setAttribute("perfil", "Registrador");
    session.setAttribute("institucionId", "186");
    session.setAttribute("usuarioId", "1");*/
//    response.sendRedirect("paginas/brand.jsf");
    session.invalidate();
    response.sendRedirect("login.jsf");
%>