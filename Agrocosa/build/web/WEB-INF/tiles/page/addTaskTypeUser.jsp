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
<link rel="stylesheet" href="../plugins/iCheck/all.css">

<script type="text/javascript">
    $(function () {
        $("#btnBack1").click(function () {
            $("#formBack").submit();
        });
    });

</script>
<style>
    .big{ width: 20px; height: 20px; }    
</style>
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            <i class='fa fa-user'></i> Asignación de Supervisor de Tareas
            <small></small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-caret-right"></i> Configuración</a></li>
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


        <form name="saveForm" id="saveForm"  method="POST" action="addTaskTypeUser.do">
            <div class="box box-success">
                <div class="box-header with-border">

                    <h3 class="box-title">Seleccionar Supervisor</h3>
                    <div class="box-tools pull-right">
                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div>
                </div>
                <div class="box-body info">


                    <div class="table-responsive">

                        <table id="dtable" class="table table-bordered table-striped table-hover">
                            <thead>
                                <tr>                                            
                                    <th>Foto</th>
                                    <th>Supervisor</th>      
                                    <th>Rol</th>
                                    <th>Asignación</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${User_Table}" var="row" varStatus="status">
                                    <tr>
                                        <td>
                                            <div class="user-panel">
                                                <div class="pull-left image">
                                                    <img src="showUserPictureById.do?idUser=${row.idUser}" alt="supervisor image"/>
                                                </div>
                                            </div>                                                            
                                        </td>                                                
                                        <td style="vertical-align: middle;">${row.UserName}</td>
                                        <td style="vertical-align: middle;">${row.Role}</td>
                                        <td style="vertical-align: middle;">
                                            <input type="radio" name = "idSupervisor" value="${row.idUser}" ${row.UserName == requestScope.SelectedSupervisor  ? 'checked' : ''} class="big" style="vertical-align: middle;">
                                        </td>

                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table> 
                    </div>

                </div><!-- /.box-body -->
                <div class="box-footer text-center">
                    <div class="form-group">
                        <input type="hidden" name="idTaskType" id="idTaskType" value="${requestScope.idTaskType}"/>
                        <input type="hidden" name="idTaskTypeUser" id="idTaskTypeUser" value="${requestScope.idTaskTypeUser}"/>

                        <button type="button" id="btnBack1" class="btn btn-primary pull-left"><i class="fa fa-backward"></i> Regresar</button>
                        <button type="submit" id="btnSubmit" class="btn btn-primary pull-right"><i class="fa fa-save"></i> Guardar</button>
                        <input type="hidden" name="Action" id="Action"  value="SaveSelectedUser"/>
                    </div><!-- /.form-group -->
                </div>
            </div><!-- /.box -->       
        </form>




        <div class="hide">
            <form name="formBack" id="formBack" action="taskTypeUserManagement.do" method="post">

            </form>                        
        </div>


        <!-- iCheck 1.0.1 -->
        <script src="../plugins/iCheck/icheck.min.js"></script>

        <script>
    //iCheck for checkbox and radio inputs
    $('input[type="checkbox"].minimal, input[type="radio"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    });
    //Red color scheme for iCheck
    $('input[type="checkbox"].minimal-red, input[type="radio"].minimal-red').iCheck({
        checkboxClass: 'icheckbox_minimal-red',
        radioClass: 'iradio_minimal-red'
    });
    //Flat red color scheme for iCheck
    $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
        checkboxClass: 'icheckbox_flat-green',
        radioClass: 'iradio_flat-green'
    });

        </script> 

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
