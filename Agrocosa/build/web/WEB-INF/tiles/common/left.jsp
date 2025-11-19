<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>



<!-- Left side column. contains the logo and sidebar -->
<aside class="main-sidebar">

    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">

        <c:if test="${sessionScope.firstName ne null}">



            <!-- Sidebar user panel (optional) -->
            <div class="user-panel">
                <div class="pull-left image">
                    <img src="showUserPicture.do" class="img-circle" alt="User Image" />
                </div>
                <div class="pull-left info">
                    <p>${sessionScope.name}</p>
                    <!-- Status -->
                    <a href="${requestScope['javax.servlet.forward.context_path']}/public/logout"><i class="fa fa-circle text-success"></i> <span class="hidden-xs">${sessionScope.firstName}&nbsp;${sessionScope.lastName}</span></a>
                </div>
            </div>

            <!-- Sidebar Menu -->
            <ul class="sidebar-menu">
                <li class="header"></li>   

                <!-- HOME -->
                <li class="treeview ${sessionScope.acordion == 'Inicio'  ? 'active' : ''}">
                    <a href="main.do"><i class='fa fa-home'></i> <span>Inicio</span></a>
                </li>


                <c:forEach items="${sessionScope.Menu_Table}" var="row" varStatus="status">
                    <li id="${row.Acordion}" class="treeview ${sessionScope.acordion == row.Acordion  ? 'active' : ''}" >
                        <a href="#"><i class='${row.Icon}'></i> <span>${row.Title}</span> <i class="fa fa-angle-left pull-right"></i></a>
                            <c:set var = "submenu" scope = "session" value = "${row.Title}_Table"/>
                        <ul class="treeview-menu">
                            <c:forEach items="${sessionScope[submenu]}" var="row2" varStatus="status">
                                <li><a href="${row2.Class}">
                                        <i class='${sessionScope.Controller == row2.Class ? 'fa fa-dot-circle-o text-red' : 'fa fa-circle-o'}'></i>
                                        ${row2.Title}</a>
                                    </c:forEach>
                        </ul>
                    </li>
                </c:forEach>

                <li class="treeview ${sessionScope.acordion == 'Salir'  ? 'active' : ''}">
                    <a href="${requestScope['javax.servlet.forward.context_path']}/public/logout"><i class='fa fa-sign-out'></i> <span>Salir</span></a>
                </li> 

            </ul><!-- /.sidebar-menu -->

        </c:if>
    </section>
    <!-- /.sidebar -->
</aside>
