<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script type="text/javascript">
    $(function () {


        $('.submitFormBtn').on("click", function () {
            var id = $(this).attr('id');
            $("#selectedId").val(id);
            $('#frmFill').submit();
        });

    });

</script>
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Riego
            <small>Por favor seleccione un registro para captura de producto</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-caret-right"></i> Aplicación Foliar</a></li>
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



        <div class="box box-success">
            <div class="box-header with-border">
                <h3 class="box-title">Captura de Productos</h3>
                <div class="box-tools pull-right">
                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                </div>
            </div><!-- /.box-header -->
            <div class="box-body">
                <div class="table-responsive">
                    <table id="dtable" class="table table-bordered table-striped table-hover">
                        <thead>
                            <tr>    
                                <th>Numero</th>
                                <th>Tarea</th>
                                <th>Campo</th>
                                <th>Sección</th>
                                <th>Formula</th>
                                <th>Fecha Tarea</th>
                                <th>Supervisor</th>
                                <th>Comentarios</th>
                                <th>Estatus</th>
                                <th>Fecha Inserción</th>                                
                                <th>Registrada por</th>                              
                                <th>Captura</th>
                            </tr>
                        </thead>
                        <tbody>

                            <c:forEach items="${TaskReport_Table}" var="row" varStatus="status">
                                <tr>                                 
                                    <td>${row.idTask}</td>
                                    <td>${row.TaskType}</td>
                                    <td>${row.CampName}</td>
                                    <td>${row.SectionName}</td>
                                    <td>${row.LaborTypeName}</td>
                                    <td>${row.TaskDate}</td>
                                    <td>${row.SupervisorName}</td>
                                    <td>${row.Comments}</td>
                                    <td>${row.Status}</td>
                                    <td>${row.InsertDate}</td>
                                    <td>${row.User}</td>
                                    <td>
                                        <form name="f${row.idTask}" id="${row.idTask}" action="" method="post">
                                            <input type="hidden" name="idTask" id="idTask" value="${row.idTask}"/>
                                            <button type="button" name="${row.idTask}" id="${row.idTask}" class="btn btn-primary xxs submitFormBtn" title ="Captura" onclick="">
                                                <i class="fa fa-edit"> Captura</i>
                                            </button>                                                
                                        </form>                                         
                                    </td>                                        
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table> 
                </div>

            </div><!-- /.box-body -->
        </div>


        <div class="hide">
            <form name="frmFill" id="frmFill" method="post" action="irrigationInfo.do">
                <input type="hidden" id="selectedId" name="idTask" value=""/>
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

    </section><!-- /.content -->
</div><!-- /.content-wrapper -->
