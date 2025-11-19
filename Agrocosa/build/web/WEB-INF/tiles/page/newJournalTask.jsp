<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script src="../plugins/bootstrap-duallistbox/jquery.bootstrap-duallistbox.min.js"></script>
<link rel="stylesheet" type="text/css" href="../plugins/bootstrap-duallistbox/bootstrap-duallistbox.css">
<!-- daterange picker -->
<link href="../plugins/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />

<script src="../plugins/select2/select2.js" type="text/javascript"></script>
<link href="../plugins/select2/select2.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
    $(function () {

        $('#saveForm').submit(function (event) {
            var x = $("#duallistboxCampSection").val();
            if (!x) {
                x = "empty";
            }
            $("#selectedSection").val(x);

            var y = $("#duallistboxLaborType").val();
            if (!y) {
                y = "empty";
            }
            $("#selectedLabor").val(y);
        });

        $("#btnSearch").click(function () {
            $("#SelectedCamp").val($("#CampName").val());
            $("#formSearch").submit();
        });


        $('.confirmFormBtn').on("click", function () {
            var id = $(this).attr('id');
            $("#id4Delete").val(id);
            $('#confirmModal').modal('show');
        });



    });

</script>

<style>
    .select2{
        width:100%!important;
    }
</style>
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            <i class='fa fa-calendar-check-o'></i> Nuevo Jornal
            <small>Crear nueva tarea</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-caret-rights"></i> Tarea</a></li>
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
        <form name="saveForm" id="saveForm"  method="POST" action="newJournalTask.do">
            <div class="box box-success">
                <div class="box-header with-border"></div><!-- /.box-header -->
                <div class="box-body">
                    <div class="col-md-4">
                        <div class="form-group">
                            <label >Campo</label>
                            <div class="input-group">
                                <select name="Camp" id="CampName" class="form-control select2" data-title="Seleccione el campo" required >
                                    <option value="empty"></option>
                                    <c:forEach items="${Camp_HashMap}" var="item">
                                        <option value="${item.value}"  ${requestScope.SelectedCamp == item.value  ? 'selected' : ''}>${item.value}</option>
                                    </c:forEach>
                                </select>
                                <span class="input-group-btn">
                                    <button type="button" id="btnSearch" class="btn btn-primary btn-flat"><i class="fa fa-search"></i> Buscar</button>
                                </span>
                            </div>                                
                        </div>

                        <div class="form-group"> 
                            <select multiple="multiple" size="10" name="duallistboxCampSection" id="duallistboxCampSection" class="campSection" required> 
                                <c:forEach items="${CampSectionAvailable_Table}" var="row" varStatus="status">
                                    <option value="${row.SectionName}">${row.SectionName}</option> 
                                </c:forEach>
                                <c:forEach items="${CampSection_Table}" var="row2" varStatus="status">
                                    <option value="${row2.SectionName}" selected="selected">${row2.SectionName}</option> 
                                </c:forEach>
                            </select> 
                            <script>
                                var demo2 = $('.campSection').bootstrapDualListbox({
                                    nonSelectedListLabel: 'Secciones Disponibles',
                                    selectedListLabel: 'Secciones Seleccionadas',
                                    preserveSelectionOnMove: 'moved',
                                    moveOnSelect: false,
                                    infoText: false,
                                    showFilterInputs: false
                                });
                            </script> 
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-group">
                            <label>Fecha</label>
                            <div class="input-group">
                                <div class="input-group-addon">
                                    <i class="fa fa-calendar"></i>
                                </div>
                                <input type="text" name="taskDate" class="form-control" id="taskDate" required value=""/>
                            </div>
                        </div>
                        <div class="form-group"> 
                            <select multiple="multiple" size="10" name="duallistboxLaborType" id="duallistboxLaborType" class="laborType" required> 
                                <c:forEach items="${LaborTypeAvailable_Table}" var="row" varStatus="status">
                                    <option value="${row.LaborTypeName}">${row.LaborTypeName}</option> 
                                </c:forEach>
                                <c:forEach items="${LaborType_Table}" var="row2" varStatus="status">
                                    <option value="${row2.LaborTypeName}" selected="selected">${row2.LaborTypeName}</option> 
                                </c:forEach>
                            </select> 
                            <script>
                                var demo2 = $('.laborType').bootstrapDualListbox({
                                    nonSelectedListLabel: 'Labores Disponibles',
                                    selectedListLabel: 'Labores Seleccionadas',
                                    preserveSelectionOnMove: 'moved',
                                    moveOnSelect: false,
                                    infoText: false,
                                    showFilterInputs: false
                                });
                            </script> 
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-group">
                            <label>Jefe de Cuadrilla</label>
                            <div class="input-group">
                                <div class="input-group-addon">
                                    <i class="fa fa-male"></i>
                                </div>
                                <input type="text" name="manager" class="form-control" id="manager" value=""/>
                            </div>
                        </div>                                    
                        <div class="form-group">
                            <label>Trabajadores</label>
                            <div class="input-group">
                                <div class="input-group-addon">
                                    <i class="fa fa-users"></i>
                                </div>
                                <input type="text" name="workersQty" class="form-control" id="workersQty" value=""/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>Comentarios</label>
                            <textarea name="comments" class="form-control" rows="5" id="comments"></textarea>
                        </div>                                    
                    </div>                                
                </div>
                <div class="box-footer">
                    <button type="submit" id="btnSubmit" class="btn btn-primary pull-right"> 
                        <i class="fa fa-save"> Guardar</i>
                    </button>
                    <input type="hidden" name="taskType" id="taskType" value="Journal"/>
                    <input type="hidden" name="Action" id="Action"  value="saveTask"/>
                    <input type="hidden" name="selectedSection" id="selectedSection" value=""/>
                    <input type="hidden" name="selectedLabor" id="selectedLabor" value=""/>                    
                </div>
            </div><!-- /.box -->
        </form>


        <div class="box box-success">
            <div class="box-header with-border">
                <h3 class="box-title">Tares Nuevas</h3>
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
                                <th>Seccion</th>
                                <th>Labor</th>
                                <th>Fecha Tarea</th>
                                <th>Jefe de Cuadrilla</th>
                                <th>Num Trabajadores</th>
                                <th>Supervisor</th>
                                <th>Comentarios</th>
                                <th>Estatus</th>
                                <th>Fecha Inserción</th>                                
                                <th>Registrada por</th>                              
                                <th>Borrar</th>
                            </tr>
                        </thead>
                        <tbody>

                            <c:forEach items="${NewTaskTable_Table}" var="row" varStatus="status">
                                <tr>                                 
                                    <td>${row.idTask}</td>
                                    <td>${row.TaskType}</td>
                                    <td>${row.CampName}</td>
                                    <td>${row.SectionName}</td>
                                    <td>${row.LaborTypeName}</td>
                                    <td>${row.TaskDate}</td>
                                    <td>${row.Manager}</td>
                                    <td>${row.WorkersQty}</td>
                                    <td>${row.SupervisorName}</td>
                                    <td>${row.Comments}</td>
                                    <td>${row.Status}</td>
                                    <td>${row.InsertDate}</td>
                                    <td>${row.User}</td>
                                    <td>
                                        <form name="f${row.idTask}" id="${row.idTask}" action="" method="post">
                                            <input type="hidden" name="idTask" id="idTask" value="${row.idTask}"/>
                                            <input type="hidden" name="Action" id="Action" value="DeleteTask"/>

                                            <button type="button" name="${row.idTask}" id="${row.idTask}" class="btn btn-primary xxs confirmFormBtn" title ="Borrar" onclick="">
                                                <i class="fa fa-trash"></i>
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


        <!-- date-range-picker -->
        <script src="../plugins/daterangepicker/moment.min.js" type="text/javascript"></script>
        <script src="../plugins/daterangepicker/daterangepicker.js" type="text/javascript"></script>

        <script type="text/javascript">


                                $(document).ready(function () {
                                    $("#CampName").select2();

                                    $('#taskDate').daterangepicker({
                                        singleDatePicker: true,
                                        format: 'DD/MM/YYYY',
                                        drops: 'auto',
                                        locale: {
                                            daysOfWeek: [
                                                "Do",
                                                "Lu",
                                                "Ma",
                                                "Mi",
                                                "Ju",
                                                "Vi",
                                                "Sa"
                                            ],
                                            monthNames: [
                                                "Enero",
                                                "Febrero",
                                                "Marzo",
                                                "Abril",
                                                "Mayo",
                                                "Junio",
                                                "Julio",
                                                "Agosto",
                                                "Septiembre",
                                                "Octubre",
                                                "Noviembre",
                                                "Diciembre"
                                            ]
                                        }
                                    });
                                });
        </script>        

        <div class="hide">
            <form name="formSearch" id="formSearch" action="newJournalTask.do" method="post">
                <input type="hidden" name="SelectedCamp" id="SelectedCamp" value=""/>
                <input type="hidden" name="Action" id="Action"  value="searchCampSection"/>
            </form>                        
        </div>

        <div class="modal fade" id="confirmModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <form name="formConfirm" action="newJournalTask.do" method="post">
                        <div class="modal-header alert-warning">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel"><i class="icon fa fa-trash"></i> Borrar Tarea</h4>
                        </div>
                        <div class="modal-body">
                            <p><h3>Esta usted seguro de borrar esta tarea?</h3></p>
                            <input type="hidden" name="idTask" id="id4Delete" value=""/>
                            <input type="hidden" name="taskType" id="taskType" value="Jornal"/>
                            <input type="hidden" name="Action" id="Action" value="DeleteTask"/>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary pull-left" data-dismiss="modal">No</button>
                            <button type="submit" id="btnConfirmYes" class="btn btn-primary pull-right">Si</button>
                        </div>
                    </form>
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
