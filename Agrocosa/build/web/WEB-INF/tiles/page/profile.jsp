
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script src="../plugins/jasny/js/jasny-bootstrap.min.js" type="text/javascript"></script>
<link href="../plugins/jasny/css/jasny-bootstrap.min.css" rel="stylesheet" type="text/css" />

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            <i class='fa fa-user'></i> Perfil
            <small>Actualizar cuenta</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-caret-right"></i> Perfil</a></li>
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
        <div class="container">

            <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-6 col-md-offset-2">
                    <div class="box box-success">
                        <div class="box-header with-border">
                            <h3 class="box-title">
                                <i class='fa fa-lock'></i>
                                Cambiar Contraseña   
                                <a href="main.do"><i class='fa fa-close pull-right'></i></a>
                            </h3>
                        </div>
                        <form name="dataInputForm" id="dataInputForm" method ="POST" action="savePassword.do" onsubmit="">
                            <div class="box-body">
                                <div class="row">
                                    <div style="margin-top:20px" class="col-xs-6 col-sm-6 col-md-6 login-box">
                                        <div class="form-group">
                                            <div class="input-group">
                                                <div class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></div>
                                                <input class="form-control" type="text" placeholder="Contraseña actual" required autofocus value="" name="currentPassword">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="input-group">
                                                <div class="input-group-addon"><span class="glyphicon glyphicon-log-in"></span></div>
                                                <input class="form-control" type="password" placeholder="Nueva contraseña" required autofocus name="newPassword">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="box-footer">
                                <div class="row">
                                    <div class="col-xs-6 col-sm-6 col-md-6"></div>
                                    <div class="col-xs-6 col-sm-6 col-md-6">
                                        <button class="btn btn-primary" type="submit">
                                            <span class="btn-save-label"><i class="fa fa-save"></i></span> Guardar</button>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>                        
                </div>
            </div>

            <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-6 col-md-offset-2">
                    <div class="box box-success">
                        <div class="box-header with-border">
                            <h3 class="box-title">
                                <i class='fa fa-picture-o'></i>
                                Cargar foto   
                            </h3>
                        </div>
                        <div class="box-body">
                            <div class="row">
                                <div style="margin-top:20px" class="col-xs-6 col-sm-6 col-md-6 login-box">
                                    <div class="form-group">
                                        <div class="input-group">
                                            <div class="col-sm-12">

                                                <form name="loadImgForm" method ="POST" action="saveUserPicture.do" enctype="multipart/form-data">
                                                    <div class="fileinput fileinput-new" data-provides="fileinput">
                                                        <div class="fileinput-preview thumbnail" data-trigger="fileinput" style="width: 200px; height: 150px;"></div>
                                                        <div>
                                                            <span class="btn btn-primary btn-file">
                                                                <span class="fileinput-new">
                                                                    <i class="fa fa-photo"></i> Seleccionar imagen
                                                                </span>
                                                                <span class="fileinput-exists">
                                                                    <i class="fa fa-photo"></i> Cambiar
                                                                </span>
                                                                <input type="file" name="fileName">
                                                            </span>
                                                            <a href="#" class="btn btn-primary fileinput-exists" data-dismiss="fileinput">
                                                                <i class="fa fa-remove"></i> Remover
                                                            </a>
                                                            <span class="fileinput-exists" style="padding-top: 5px;">
                                                                <button class="btn btn-primary" type="submit">
                                                                    <span class="btn-save-label">
                                                                        <i class="fa fa-upload"></i>
                                                                    </span> Cargar
                                                                </button>
                                                            </span>
                                                        </div>
                                                    </div>
                                                </form>

                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>                        
                </div>
            </div>            
        </div>


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

    </section><!-- /.content -->
</div><!-- /.content-wrapper -->
