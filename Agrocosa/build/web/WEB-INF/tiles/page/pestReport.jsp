<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Reporte de Plagas
            <small>Consulta de plagas, prevención y tratamiento</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-caret-right"></i> Plagas</a></li>
            <li class="active">Aqui</li>
        </ol>
    </section>

    <!-- Main content -->
    <section class="content">

        <c:choose>
            <c:when test="${requestScope.RESPONSE_CODE == 'PASS'}">                    
                <c:if test="${requestScope.RESPONSE_MESSAGE ne 'OK'}">


                    <div class="alert alert-success alert-dismissable">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                        <h4>	<i class="icon fa fa-check"></i> OK</h4>
                        <c:out value="${requestScope.RESPONSE_MESSAGE}" /><br />
                        <c:out value="${requestScope.RESPONSE_DETAIL}" />
                    </div>
                </c:if>
            </c:when>
            <c:when test="${requestScope.RESPONSE_CODE == 'FAIL'}">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                    <h4><i class="icon fa fa-ban"></i> Error !</h4>
                    <c:out value="${requestScope.RESPONSE_MESSAGE}" /><br />
                    <c:out value="${requestScope.RESPONSE_DETAIL}" />
                </div>

            </c:when>
        </c:choose>

        <!-- Search Meeting -->
        <form name="formSearch" role="form" action="pestsReport.do" method="post">
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title">Buscar</h3>
                </div><!-- /.box-header -->
                <div class="box-body">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label >Buscar:</label>
                            <input class="form-control" placeholder="word" type="text" data-title="" name="word" id="searchfor" value="${requestScope.SelectedWord}"/>
                        </div><!-- /.form-group -->
                    </div>

                </div><!-- /.box-body -->
                <div class="box-footer">
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary"><i class="fa fa-search"></i> Buscar</button>
                        <input type="hidden" name="Action" value="Search"/>
                    </div><!-- /.form-group -->                        
                </div>
            </div><!-- /.box -->
        </form>



        <c:if test="${requestScope.Pest_Table ne null}">

            <c:forEach items="${Pest_Table}" var="row" varStatus="status">

                <div class="box box-widget">
                    <div class='box-header with-border'>
                        <div class='user-block'>
                            <span class='username'><a href="#">${row.PestName}</a></span>
                        </div><!-- /.user-block -->
                        <div class='box-tools'></div><!-- /.box-tools -->
                    </div><!-- /.box-header -->
                    <div class='box-body'>
                        <img class="img-responsive pad" src="showPestPictureById.do?idPest=${row.idPest}" alt="imagen">
                    </div><!-- /.box-body -->
                    <div class='box-footer box-comments'>
                        <div class='box-comment'>
                            <div class='comment-text'>
                                <span class="username" style="font-size: 18px; font-weight: bold;">
                                    Descripción
                                </span><!-- /.username -->
                                <p>${row.Description}</p>
                            </div><!-- /.comment-text -->
                        </div><!-- /.box-comment -->
                        <div class='box-comment'>
                            <div class='comment-text'>
                                <span class="username" style="font-size: 18px; font-weight: bold;">
                                    Prevención
                                </span><!-- /.username -->
                                <p>${row.Prevention}</p>
                            </div><!-- /.comment-text -->
                        </div><!-- /.box-comment -->
                        <div class='box-comment'>
                            <div class='comment-text' >
                                <span class="username" style="font-size: 18px; font-weight: bold;">
                                    Tratamiento
                                </span><!-- /.username -->
                                <p>${row.Medication}</p>
                            </div><!-- /.comment-text -->
                        </div><!-- /.box-comment -->
                    </div><!-- /.box-footer -->
                    <div class="box-footer"></div><!-- /.box-footer -->
                </div><!-- /.box -->
                <br>
            </c:forEach>

        </c:if>        





    </section><!-- /.content -->
</div><!-- /.content-wrapper -->

<c:if test="false">
    <c:forEach items='${requestScope}' var='p'>
        <ul>
            <%-- Display the key of the current item, which
                 represents the parameter name --%>
            <li>Parameter Name: <c:out value='${p.key}'/></li>

            <%-- Display the value of the current item, which
                 represents the parameter value --%>
            <li>Parameter Value: <c:out value='${p.value}'/></li>
        </ul>
    </c:forEach>
</c:if>
