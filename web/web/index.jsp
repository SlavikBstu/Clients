<%@ page import="requester.Requester" %>
<%@ page import="products.Products" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>

<%
  String useSoap = request.getParameter("type");
  String resultSoap = "Соап не использован";
  if(useSoap != null && useSoap.equals("soap")) {
    resultSoap = "SOAP " + new Requester().useSoap("hello");
  }
%>

<html>
<head>
  <meta name="viewport" content="width=device-width" />

  <link rel="stylesheet" href="css/bootstrap.min.css"/>

  <link rel="stylesheet" href="css/bootstrap.css"/>
  <link rel="stylesheet" href="css/global.css"/>
  <link rel="stylesheet" href="css/reset.css"/>
  <link rel="stylesheet" href="css/grid.css"/>
  <link rel="stylesheet" href="css/menu.css"/>

  <title>Онлайн магазин компьютерных игр</title>
</head>
<body>
<div>
  <!--Header-->
  <header>
    <div class="navbar navbar-static  cbp-af-header">
      <div class="cbp-af-inner ">
        <div class="navbar-inner container_9 ">
          <span class="nav pull-left"> <a title="Shop Master" class="logo "><img alt="logo" id="logot" src="img/logo.png"></a> </span>
          <ul id="header_nav" class="nav pull-right">
              <li><i class="icon-user"></i><%= resultSoap%></li>
              <li><i class="icon-circle-arrow-right"></i> <a href="index.jsp?type=soap">Подключить SOAP </a></li>
            <li>
              <ul class="dropdown-menu2">
                <li>
                  <form id="searchbox" action="#" method="get">
                    <input type="text" name="search_query" value="" placeholder="Search here..">
                  </form>
                </li>
              </ul>
            </li>

          </ul>
        </div>
      </div>
      <!--Menu-->
      <div id="menu">
        <ul id="nav">
            <li><a href="index.jsp">Домашняя</a></li>
            <li><a href="index.jsp">Список продуктов</a></li>
        </ul>
      </div>
      <!--Menu end-->
    </div>
  </header>
  <!--Header end-->
    <% Requester requester = new Requester();
        Products[] products = requester.getAll();%>


    <div class="product-index"
        <%
            if(products != null && products.length != 0)
            {
                for(int i = 0; i < products.length; i++)
                {

        %>
        <div class="col-md-4 abouttop-left">
            <form action="delete-product.jsp" method="post">
                <input type="submit" class="btn btn-primary" value="Удалить"/>
                <input type="hidden" name="pharmacy" value="<%=products[i].getId()%>"/>
            </form>
        </div>

        <div class="col-md-8 abouttop-right">
            <p>Название игры: <%= products[i].getTitle()%></p>
            <p>Категория игры: <%= products[i].getCategory()%></p>
            <p>Количество продукции: <%= products[i].getAmount()%></p>
            <p>Цена: <%= products[i].getPrice()%></p>
            <p><span style="color:red"><a href="index.jsp">Показать полную информацию</a></span></p>
        </div>

        <div class="clearfix"></div>
        <hr>
        <%
            }
        %>
        <%
        }
        else {
        %>
        <p>Список игр пуст</p>
        <%
            for(int i = 0; i < 12; i++) {
        %>
        <br>
        <%
            }
        %>
        <%
            }
        %>
        </div>
    <hr />

  <!--Footer -->
  <footer id="footer">
    <div class="footer-wrap container_9 ">
      <a href="#" class="backtotop"><img src="img/backtotop.png" alt="top" title="top"></a>
      <div class="footer-set">
          <div class="block">
        <h4>О нас</h4>
        <div class="block_content">
          Магазин компьютерных игр <br>
            Беларусь, г. Минск, gameshop@mail.ru
        </div>
      </div>
      </div>
    </div>
  </footer>
  <!--Footer end-->
</div>
</body>
</html>
