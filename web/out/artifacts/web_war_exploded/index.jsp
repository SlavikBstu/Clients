
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>

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
          <span class="nav pull-left"> <a title="Shop Master" class="logo "><img alt="logo" id="logot" src="/img/logo.png"></a> </span>
          <ul id="header_nav" class="nav pull-right">
            <li><i class="icon-circle-arrow-right"></i> <a href="#">Подключить SOAP </a></li>
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
            <li><a href="index.jsp?type=soap">Список продуктов</a></li>
        </ul>
      </div>
      <!--Menu end-->
    </div>
  </header>
  <!--Header end-->

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
