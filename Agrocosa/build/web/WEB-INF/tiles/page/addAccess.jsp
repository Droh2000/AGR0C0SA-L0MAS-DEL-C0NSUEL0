<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->

<script src="../plugins/bootstrap-duallistbox/jquery.bootstrap-duallistbox.min.js"></script>
<link rel="stylesheet" type="text/css" href="../plugins/bootstrap-duallistbox/bootstrap-duallistbox.css">

<script type="text/javascript">
    $(function () {
        $("#btnBack").click(function () {
            $("#formBack").submit();
        });
        $("#btnSubmit").click(function () {
            $("#selectedMenus").val($("#duallistbox_user").val());
            $("#saveForm").submit();
        });
        $("#btnSearch").click(function () {
            $("#SelectedRootMenu").val($("#RootMenu").val());
            $("#formSearch").submit();
        });
    });

</script>
<style>

</style>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            <i class='fa fa-lock'></i> Accesos
            <small>Asignación de accesos</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-gear"></i> Configuración</a></li>
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

        <form name="saveForm" id="saveForm"  method="POST" action="addAccess.do">
            <div class="box box-solid">
                <div class="box-header with-border">
                    <i class="fa fa-unlock"></i>
                    <h3 class="box-title">Asignacion de Accesos a Usuarios</h3>
                    <div class="box-tools pull-right">
                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div>
                </div>
                <div class="box-body box-profile">
                    <div class="row"> 
                        <div class="col-md-3">

                            <div class="box box-widget widget-user">
                                <!-- Add the bg color to the header using any of the bg-* classes -->
                                <div class="widget-user-header bg-green">
                                    <h3 class="widget-user-username">   ${requestScope.UserCompleteName}</h3>
                                    <h5 class="widget-user-desc">   ${requestScope.RoleName}</h5>
                                </div>
                                <div class="widget-user-image">
                                    <img class="img-circle" src="showUserPictureById.do?idUser=${requestScope.idUser}" alt="usuario">
                                </div>
                                <div class="box-footer">
                                    <div class="row">
                                        <div class="col-sm-4 border-right">
                                            <div class="description-block">
                                                <h5 class="description-header"></h5>
                                                <span class="description-text"></span>
                                            </div><!-- /.description-block -->
                                        </div><!-- /.col -->
                                        <div class="col-sm-4 border-right">
                                            <div class="description-block">
                                                <h5 class="description-header"></h5>
                                                <span class="description-text"></span>
                                            </div><!-- /.description-block -->
                                        </div><!-- /.col -->
                                        <div class="col-sm-4">
                                            <div class="description-block">
                                                <h5 class="description-header"></h5>
                                                <span class="description-text"></span>
                                            </div><!-- /.description-block -->
                                        </div><!-- /.col -->
                                    </div><!-- /.row -->
                                </div>                                
                            </div><!-- /.widget-user -->

                        </div>
                    </div>
                                <br><br>                                
                    <div class="row"> 
                        <div class="col-md-3">
                            <div class="form-group">
                                <label >Menu</label>
                                <div class="input-group input-group-sm">
                                    <select name="RootMenu" id="RootMenu" class="form-control select2" data-title="Seleccione el tipo de menu">
                                        <option value="empty"></option>
                                        <c:forEach items="${Menu_HashMap}" var="item">
                                            <option value="${item.value}"  ${requestScope.SelectedRootMenu == item.value  ? 'selected' : ''}>${item.value}</option>
                                        </c:forEach>
                                    </select>
                                    <span class="input-group-btn">
                                        <button type="button" id="btnSearch" class="btn btn-primary btn-flat"><i class="fa fa-search"></i> Buscar</button>
                                    </span>
                                </div>                                
                            </div>
                        </div>
                    </div>



                    <div class="row"> 
                        <div class="col-md-7"> 
                            <select multiple="multiple" size="20" name="duallistbox_user" id="duallistbox_user" class="guard"> 
                                <c:forEach items="${AvailableMenus_Table}" var="row" varStatus="status">
                                    <option value="${row.idMenu}">${row.Title}</option> 
                                </c:forEach>
                                <c:forEach items="${SelectedMenus_Table}" var="row2" varStatus="status">
                                    <option value="${row2.idMenu}" selected="selected">${row2.Title}</option> 
                                </c:forEach>
                            </select> 
                            <script>
                                var demo2 = $('.guard').bootstrapDualListbox({
                                    nonSelectedListLabel: 'Opciones Disponibles',
                                    selectedListLabel: 'Opciones Asignadas',
                                    preserveSelectionOnMove: 'moved',
                                    moveOnSelect: false,
                                    infoText: false
                                            //                                nonSelectedFilter: 'ion ([7-9]|[1][0-2])'
                                });
                            </script> 
                        </div> 
                    </div> 
                </div>
                <div class="box-footer text-center">
                    <div class="form-group">
                        <button type="button" id="btnBack" class="btn btn-primary pull-left"><i class="fa fa-backward"></i> Regresar</button>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <button type="button" id="btnSubmit" class="btn btn-primary"><i class="fa fa-save"></i> Guardar</button>
                        <input type="hidden" name="Action" id="Action"  value="SaveSelectedMenus"/>
                        <input type="hidden" name="idUser" id="idUser" value="${requestScope.idUser}"/>
                        <input type="hidden" name="selectedMenus" id="selectedMenus" value=""/>
                    </div><!-- /.form-group -->                        
                </div>
            </div>
        </form>


        <div class="hide">
            <form name="formBack" id="formBack" action="userManagement.do" method="post">
                <input type="hidden" name="idUser" value="${requestScope.idUser}"/>   
            </form>                        
        </div>

        <div class="hide">
            <form name="formSearch" id="formSearch" action="addAccess.do" method="post">
                <!--<input type="hidden" name="Action" id="Action"  value="SearchGuard"/>-->
                <input type="hidden" name="idUser" value="${requestScope.idUser}"/>
                <input type="hidden" name="SelectedRootMenu" id="SelectedRootMenu" value=""/>
            </form>                        
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