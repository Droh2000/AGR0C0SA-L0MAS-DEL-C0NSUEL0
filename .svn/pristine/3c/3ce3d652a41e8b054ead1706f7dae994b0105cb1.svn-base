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

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Supervisores de Tareas
            <small>Configuración de supervisores en los diferentes tipos de tareas</small>
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




        <c:if test="${requestScope.TaskTypeUser_Table ne null}">
            <div class="box box-success">
                <div class="box-header with-border">

                    <h3 class="box-title">Tipos de Tareas</h3>
                    <div class="box-tools pull-right">
                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div>
                </div>
                <div class="box-body" id="info" > 
                    <div class="table-responsive">

                        <table id="dtable" class="table table-bordered table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>Tipo de Tarea</th>
                                    <th>Nombre de Supervisor</th>
                                    <th>Rol</th>
                                    <th>Foto</th>
                                    <th>Asignar Supervisor</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${TaskTypeUser_Table}" var="row" varStatus="status">
                                    <tr>
                                        <td>${row.TaskTypeName}</td>
                                        <td>${row.UserName}</td>
                                        <td>${row.RoleName}</td>
                                        <td>
                                            <table>
                                                <tr>
                                                    <td>
                                                        <div>
                                                            <div class="pull-left image">
                                                                <img class="profile-user-img img-responsive center-block img-circle " src="showUserPictureById.do?idUser=${row.idUser}" alt="Supervisor">
                                                            </div>
                                                        </div>                                                            
                                                    </td>                                                    
                                                </tr>
                                            </table>
                                        </td>
                                        <td>
                                            <form name="supervisorform" action="addTaskTypeUser.do" method="post">
                                                <input type="hidden" name="idTaskType" id="idTaskType" value="${row.idTaskType}"/>
                                                <input type="hidden" name="idTaskTypeUser" id="idTaskTypeUser" value="${row.idTaskTypeUser}"/>
                                                <button type="submit" class="btn btn-primary"><i class="fa fa-user-plus"></i> Asignar Supervisor</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table> 
                    </div>
                </div>
            </div>
        </c:if>






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
