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

<script type="text/javascript">
    $(function () {

        $('.receiptBtn').on("click", function () {
            var id = $(this).attr('id');
            id = id.replace("t", "");
            $('#idTask4Update').val(id);

            $('#weight').val("");
            $('#binQty').val("");
            $('#comments').val("");
            $('#msgReceipt').text("");

            $.ajax({
                url: 'ajaxGetTaskCropName.do',
                type: 'POST',
                data: jQuery.param({idTask: id}),
                success: function (result) {
                    $('#cropName').children().remove();
                    $("#cropName").append(result);
                }
            });

            $('#receiptTruck').modal('show');
        });

        $('#buttonSaveTruck').on("click", function () {
            var form = $('#receiptForm');
            $.ajax({
                url: 'ajaxSaveReceiptPickingInfo.do',
                type: 'POST',
                data: form.serialize(),
                success: function (result) {
                    $('#msgReceipt').text(result);
                }
            });
        });


    });

</script> 


<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Recibo de Cosechas
            <small>Recibir cosechas, capturar peso y asignar ubicación</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-caret-right"></i> Secado</a></li>
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
        <form name="formsearch" role="form" action="receiptPicking.do" method="post">
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title">Filtros de Busqueda</h3>
                </div><!-- /.box-header -->
                <div class="box-body">



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
                    <div class="col-md-2">
                        <div class="form-group">
                            <label>Fecha Inicio</label>
                            <div class="input-group">
                                <div class="input-group-addon">
                                    <i class="fa fa-calendar"></i>
                                </div>
                                <input type="text" name="startDate" class="form-control" id="startDate" value="${requestScope.SelectedStartDate}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>Fecha Fin</label>
                            <div class="input-group">
                                <div class="input-group-addon">
                                    <i class="fa fa-calendar"></i>
                                </div>
                                <input type="text" name="endDate" class="form-control" id="endDate" value="${requestScope.SelectedEndDate}"/>
                            </div>                                
                        </div>                                                            
                    </div>
                    <div class="col-md-2">
                        <div class="form-group">
                            <label>Num de Cosecha</label>
                            <div class="input-group">
                                <input type="text" name="idTask" class="form-control" id="idTask" value="${requestScope.SelectedIdTask}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>&nbsp;</label>
                            <div class="input-group">                                
                            </div>                                
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
                <h3 class="box-title">Cosechas</h3>
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
                                <th>Secciones</th>
                                <th>Cultivo</th>
                                <th>Fecha de Tarea</th>
                                <th>Comentarios</th>
                                <th>Estatus</th>
                                <th>Supervisor</th>
                                <th>Recibir</th>
                        </thead>
                        <tbody>

                            <c:forEach items="${TaskReport_Table}" var="row" varStatus="status">
                                <tr>                                 
                                    <td><a href="viewTask.do?idTask=${row.idTask}&controller=taskReport.do&menu=Report">${row.idTask}</a></td>
                                    <td>${row.TaskType}</td>
                                    <td>${row.CampName}</td>
                                    <td>${row.SectionName}</td>
                                    <td>${row.SectionCrop}</td>
                                    <td>${row.TaskDate}</td>
                                    <td>${row.Comments}</td>
                                    <td>${row.Status}</td>
                                    <td>${row.SupervisorName}</td>
                                    <td>
                                        <button type="button" name="t${row.idTask}" id="t${row.idTask}" class="btn btn-primary xxs receiptBtn" title ="Recibo" onclick="">
                                            <i class="fa fa-truck"></i>
                                        </button>                                                                                          
                                    </td>
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


<div class="modal fade" id="receiptTruck" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog" role="document">
        <form name="receiptForm" id="receiptForm" action="" method="post">
            <div class="modal-content">
                <div class="modal-header alert-success">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel"><i class="icon fa fa-truck"></i> Captura de peso y numero de cajones o bines</h4>
                </div>

                <div class="modal-body">
                    <div class="form-group">
                        <label>Peso</label>
                        <input name="weight" type="text" class="form-control pull-right" id="weight" required value=""/>
                    </div>  
                    <div class="form-group">
                        <label>Unidad de Medida</label>
                        <select name="uom" id="uom" class="form-control" data-title="Unida de Medida" required>
                            <c:forEach items="${UOM_HashMap}" var="item">
                                <option value="${item.value}"  ${requestScope.SelectedUOM == item.value  ? 'selected' : ''}>${item.value}</option>
                            </c:forEach>
                        </select>                                 
                    </div>  
                    <div class="form-group">
                        <label>Numero de Cajones o Bines</label>
                        <input name="binQty" type="text" class="form-control pull-right" id="binQty" required value=""/>
                    </div>
                    <div class="form-group">
                        <label >Nombre de Cultivo</label>
                        <select name="cropName" id="cropName" class="form-control">
                        </select> 
                    </div>                        
                    <div class="form-group">
                        <label >Localidad</label>
                        <select name="storageName" id="storageName" class="form-control" required>
                            <c:forEach items="${Storage_HashMap}" var="item">
                                <option value="${item.value}"  ${requestScope.SelectedStorageName == item.value  ? 'selected' : ''}>${item.value}</option>
                            </c:forEach>
                        </select> 
                    </div>                        
                    <div class="form-group">
                        <label >Sublocalidad</label>
                        <select name="substorageName" id="substorageName" class="form-control" required>
                            <c:forEach items="${Substorage_HashMap}" var="item">
                                <option value="${item.value}"  ${requestScope.SelectedSubstorageName == item.value  ? 'selected' : ''}>${item.value}</option>
                            </c:forEach>
                        </select> 
                    </div>                        
                    <div class="form-group">
                        <label >Comentarios</label>
                        <textarea name="comments" class="form-control" rows="5" id="comments"></textarea>
                    </div>                        
                    <div class="form-group">
                        <label>&nbsp;&nbsp;</label>
                        <span id="msgReceipt" class="pull-left" style="color: red;">&nbsp;&nbsp;</span>
                    </div>
                </div>

                <div class="modal-footer">                            
                    <input type="hidden" name="idTask" id="idTask4Update" value=""/>
                    <button type="button" class="btn btn-primary pull-left" data-dismiss="modal">Cerrar</button>
                    <button type="button" class="btn btn-primary pull-right" id="buttonSaveTruck">Guardar</button>
                </div>
            </div>
        </form>
    </div>
</div>


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
