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

<!-- daterange picker -->
<link href="../plugins/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />



<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Reporte de Agroquimicos
            <small>Consulta de agroquimicos por campo / sección</small>
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
        <form name="formsearch" role="form" action="taskAgrochemicalReport.do" method="post">
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title">Filtros de Busqueda</h3>
                </div><!-- /.box-header -->
                <div class="box-body">



                    <div class="col-md-2">
                        <div class="form-group">
                            <label >Agroquimico</label>
                            <select name="agrochemicalName" id="agrochemicalName" class="form-control">
                                <option value=""></option>
                                <c:forEach items="${Agrochemical_HashMap}" var="item">
                                    <option value="${item.value}"  ${requestScope.SelectedAgrochemicalName == item.value  ? 'selected' : ''}>${item.value}</option>
                                </c:forEach>
                            </select>                                                
                        </div>
                    </div>
                    <div class="col-md-2">
                        <div class="form-group">
                            <label >Campo</label>
                            <select name="campName" id="campName" class="form-control">
                                <option value=""></option>
                                <c:forEach items="${Camp_HashMap}" var="item">
                                    <option value="${item.value}"  ${requestScope.SelectedCampName == item.value  ? 'selected' : ''}>${item.value}</option>
                                </c:forEach>
                            </select> 
                        </div>                        
                    </div>
                    <div class="col-md-2">
                        <div class="form-group">
                            <label >Sección</label>
                            <select name="sectionName" id="sectionName" class="form-control">                                
                                <c:if test="${requestScope.SelectedCampName ne ''}"> 
                                    <option value=""></option>
                                    <c:forEach items="${CampSection_HashMap}" var="item">
                                        <option value="${item.value}"  ${requestScope.SelectedSectionName == item.value  ? 'selected' : ''}>${item.value}</option>
                                    </c:forEach>
                                </c:if>
                            </select>                                                                                    
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
                <h3 class="box-title">Tareas</h3>
                <div class="box-tools pull-right">
                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                </div>
            </div><!-- /.box-header -->
            <div class="box-body">
                <div class="table-responsive">
                    <table id="dtable" class="table table-bordered table-striped table-hover">
                        <thead>
                            <tr>    
                                <th>Num de Tarea</th>
                                <th>Tipo de Tarea</th>
                                <th>Campo</th>
                                <th>Seccion</th>
                                <th>Fecha de Tarea</th>
                                <th>Estatus</th>
                                <th>Agroquimico</th>
                                <th>Marca</th>
                                <th>Ingrediente activo</th>                                
                                <th>Dosis Minima</th>                                
                                <th>Dosis Maxima</th>                                
                                <th>Unida Medida</th>                                
                                <th>Tipo Agroquimico</th>                                
                                <th>Intervalo de Seguridad</th>                                
                                <th>Periodo de Reentrada</th>                                
                                <th>Lote</th>                                
                                <th>Caducidad</th>                                
                                <th>Total Aplicado</th>                                
                                <th>Uso Real</th>                                
                                <th>Fecha de Registro</th>                                
                        </thead>
                        <tbody>

                            <c:forEach items="${AgrochemicalReport_Table}" var="row" varStatus="status">
                                <tr>                                 
                                    <td>${row.idTask}</td>
                                    <td>${row.TaskType}</td>
                                    <td>${row.CampName}</td>
                                    <td>${row.SectionName}</td>
                                    <td>${row.TaskDate}</td>
                                    <td>${row.Status}</td>
                                    <td>${row.AgrochemicalName}</td>
                                    <td>${row.Brand}</td>
                                    <td>${row.ActiveIngredient}</td>
                                    <td>${row.MinDose}</td>
                                    <td>${row.MaxDose}</td>
                                    <td>${row.UOM}</td>
                                    <td>${row.AgrochemicalType}</td>
                                    <td>${row.SecurityInterval}</td>
                                    <td>${row.DelayPeriod}</td>
                                    <td>${row.Lote}</td>
                                    <td>${row.Caducity}</td>
                                    <td>${row.Total}</td>
                                    <td>${row.RealUse}</td>
                                    <td>${row.InsertDate}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table> 
                </div>

            </div><!-- /.box-body -->
        </div>


        <script>
            $('#dtable').DataTable({
                "paging": false,
                "lengthChange": false,
                "searching": true,
                "ordering": false,
                "info": false,
                "autoWidth": false,
                "dom": 'T<"clear">lfrtip',
                "tableTools": {
                    "sSwfPath": "../plugins/datatables/extensions/TableTools/swf/copy_csv_xls_pdf.swf"
                }
            });</script>


    </section><!-- /.content -->
</div><!-- /.content-wrapper -->

<!-- date-range-picker -->
<script src="../plugins/daterangepicker/moment.min.js" type="text/javascript"></script>
<script src="../plugins/daterangepicker/daterangepicker.js" type="text/javascript"></script>


<script>
            $(function () {



                //startDate
                $('#startDate').daterangepicker({
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
                //endDate
                $('#endDate').daterangepicker({
                    singleDatePicker: true,
                    format: 'DD/MM/YYYY',
                    drops: 'up',
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


                $("#campName").change(function () {
                    if ($("#campName").val().length > 0) {
                        var v = $("#campName").val();
                        var param = 'campName=' + v;
                        $.ajax({
                            url: 'ajaxCampSection.do',
                            type: 'POST',
                            data: param,
                            success: function (result) {
                                $('#sectionName').children().remove();
                                $("#sectionName").append(result);
                            }
                        });
                    } else {
                        $('#sectionName').children().remove();
                    }
                });


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
