<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
             Historial de Tareas
            <small>Consulta de tareas</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-caret-right"></i> Reportes</a></li>
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
        <form name="formsearch" role="form" action="taskHistoryReport.do" method="post">
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title">Filtros de Busqueda</h3>
                </div><!-- /.box-header -->
                <div class="box-body">



                    <div class="col-md-2">
                        <div class="form-group">
                            <label >Numero de Tarea</label>
                            <input class="form-control" placeholder="Num Tarea" type="text" data-title="" required name="idTask" value="${requestScope.idTask}" />
                        </div>
                    </div>


                </div><!-- /.box-body -->
                <div class="box-footer">
                    <button type="submit" class="btn btn-primary"><i class="fa fa-search"></i> Buscar</button>
                    <input type="hidden" name="Action" value="Search"/>    
                </div>
            </div><!-- /.box -->
        </form>        

        <div class="box box-success">
            <div class="box-header with-border">
                <h3 class="box-title">Historial de Tarea</h3>
                <div class="box-tools pull-right">
                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                </div>
            </div><!-- /.box-header -->
            <div class="box-body">
                <div class="table-responsive">
                    <table id="dtable" class="table table-bordered table-striped table-hover">
                        <thead>
                            <tr>    
                                <th>Num Tarea</th>
                                <th>Tipo de Tarea</th>
                                <th>Campo</th>
                                <th>Seccion</th>
                                <th>Labor</th>
                                <th>Cultivo</th>
                                <th>Fecha de Tarea</th>
                                <th>Supervisor</th>                                
                                <th>Estatus</th>
                                <th>Comentarios</th>
                                <th>Registrado por</th>
                                <th>Fecha Registro</th>
                            </tr>
                        </thead>
                        <tbody>

                            <c:forEach items="${TaskHistory_Table}" var="row" varStatus="status">
                                <tr>                                 
                                    <td><a href="viewTask.do?idTask=${row.idTask}&controller=taskHistoryReport.do&menu=Report">${row.idTask}</a></td>
                                    <td>${row.TaskType}</td>
                                    <td>${row.CampName}</td>
                                    <td>${row.SectionName}</td>
                                    <td>${row.LaborTypeName}</td>
                                    <td>${row.SectionCrop}</td>
                                    <td>${row.TaskDate}</td>
                                    <td>${row.SupervisorName}</td>
                                    <td>${row.Status}</td>
                                    <td>${row.Comments}</td>
                                    <td>${row.User}</td>
                                    <td>${row.InsertDate}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table> 
                </div>

            </div><!-- /.box-body -->
        </div>


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
