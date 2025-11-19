<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- daterange picker -->
<link href="../plugins/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
    $(function () {
        $('.detailFormBtn').on("click", function () {
            var id = $(this).attr('id');
            $.ajax({
                url: 'ajaxStockTransferOrderDetail.do',
                type: 'POST',
                data: jQuery.param({idStockTransferOrder: id}),
                success: function (result) {
                    $('#myModal').find('.modal-body').html(result);
                    $('#myModal').modal('show');
                }
            });
        });
    });

    $(function () {
        $('.evaluationFormBtn').on("click", function () {
            var id = $(this).data('id');
            $.ajax({
              url: 'assignWeightRamp.do',  // el servlet
              method: 'POST',
              data: {
                Action: 'Evaluation',      // lo que el Servlet espera
                idStockTransferOrder: id   // usa el nombre EXACTO que lee el Transaction
              },
              success: function (html) {
                $('#evaluateTruckModal .modal-body').html(html); // insertar contenido
                $('#evaluateTruckModal').modal('show');
              },
              error: function (xhr) {
                $('#evaluateTruckModal .modal-body').html('<div class="text-danger">Error al cargar evaluación</div>');
                $('#evaluateTruckModal').modal('show');
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
            Reporte de Embarques al Paso
            <small>Consulta de ordenes de embarque</small>
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
        <form name="formsearch" role="form" action="stockTransferOrderReport.do" method="post">
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title">Filtros de Busqueda</h3>
                </div><!-- /.box-header -->
                <div class="box-body">

                    <div class="col-md-2">
                        <div class="form-group">
                            <label >ID de Embarque</label>
                            <input type="text" placeholder="ID Embarque" data-title="" name="idEmbarque" class="form-control" value="${requestScope.idEmbarque}" />                                             
                        </div>
                    </div>
                    <div class="col-md-2">
                        <div class="form-group">
                            <label >Proforma</label>
                            <input name="proforma" type="text" class="form-control pull-right" id="proforma" value="${requestScope.SelectedProforma}"/>
                        </div>                      
                    </div>
                    <div class="col-md-2">
                        <div class="form-group">
                            <label >Transportista</label>
                            <select name="transportCompanyName" id="transportCompanyName" class="form-control">
                                <option value=""></option>
                                <c:forEach items="${TransportCompany_HashMap}" var="item">
                                    <option value="${item.value}"  ${requestScope.SelectedTransportCompanyName == item.value  ? 'selected' : ''}>${item.value}</option>
                                </c:forEach>
                            </select>                                                
                        </div>
                    </div>
                    <div class="col-md-2">                        
                        <div class="form-group">
                            <label >Estatus</label>
                            <select name="status" id="status" class="form-control">                                
                                <option value=""></option>
                                <option value="Nueva"  ${requestScope.SelectedStatus == 'Nueva'  ? 'selected' : ''}>Nueva</option>
                                <option value="Embarcada"  ${requestScope.SelectedStatus == 'Embarcada'  ? 'selected' : ''}>Embarcada</option>
                                <option value="EnTransito"  ${requestScope.SelectedStatus == 'EnTransito'  ? 'selected' : ''}>EnTransito</option>
                                <option value="Recibida"  ${requestScope.SelectedStatus == 'Recibida'  ? 'selected' : ''}>Recibida</option>
                                <option value="Cancelada"  ${requestScope.SelectedStatus == 'Cancelada'  ? 'selected' : ''}>Cancelada</option>
                            </select>                                                                                    
                        </div>                        
                    </div>
                    <div class="col-md-2">
                        <div class="form-group">
                            <label >Fecha</label>
                            <div class="input-group">
                                <div class="input-group-addon">
                                    <i class="fa fa-calendar"></i>
                                </div>
                                <input type="text" name="mainDate" class="form-control" id="mainDate" value="${requestScope.SelectedDate}"/>
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
                <h3 class="box-title">Embarques</h3>
                <div class="box-tools pull-right">
                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                </div>
            </div><!-- /.box-header -->
            <div class="box-body">
                <div class="table-responsive">
                    <table id="dtable" class="table table-bordered table-striped table-hover">
                        <thead>
                            <tr>    
                                <th>Consultar</th>
                                <th>Embarque ID</th>
                                <th>Proforma</th> 
                                <th>Status</th>
                                <th>Transportista</th>
                                <th>Nombre de Chofer</th>
                                <th>No de Caja</th>
                                <th>Placa Caja</th>
                                <th>Placa Camion</th>
                                <th>Cruzador</th>
                                <th>Tipo de Caja</th>
                                <th>Pies de Caja</th>
                                <th>Evaluacion</th>
                                <th>Tara</th>
                                <th>Bruto</th>
                                <th>Neto</th>
                                <th>Fecha</th>
                            </tr>
                        </thead>
                        <tbody>

                            <c:forEach items="${StockTransferOrderInfo_Table}" var="row" varStatus="status">
                                <tr>  
                                    <td>
                                        <button type="button" name="${row.idStockTransferOrder}" id="${row.idStockTransferOrder}" class="btn btn-primary xxs detailFormBtn" title ="Consultar" onclick="">
                                            <i class="fa fa-search-plus"></i>
                                        </button>                                                                                          
                                    </td>
                                    <td>${row.idStockTransferOrder}</td>
                                    <td>${row.Proforma}</td>
                                    <td>${row.Status}</td>
                                    <td>${row.TransportCompany}</td>
                                    <td>${row.TruckDriverName}</td>
                                    <td>${row.TruckBoxNumber}</td>
                                    <td>${row.TruckBoxPlate}</td>
                                    <td>${row.TruckPlate}</td>
                                    <td>${row.Crosser}</td>
                                    <td>${row.TypeBox}</td>
                                    <td>${row.BoxFeet}</td>
                                    <td>
                                        <button 
                                            type="button" 
                                            name="${row.idStockTransferOrder}"
                                            class="btn btn-info xxs evaluationFormBtn" 
                                            data-id="${row.idStockTransferOrder}"
                                            data-action="Evaluation"
                                            title ="Evaluacion del Camion" 
                                            onclick=""
                                        >
                                            <i class="fa fa-eye"></i>
                                        </button>
                                    </td>
                                    <td>${row.Tare}</td>
                                    <td>${row.Gross}</td>
                                    <td>${row.Net}</td>
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
            });

        </script>


        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header alert-success">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel"> Orden de Embarque a el Paso</h4>
                    </div>

                    <div class="modal-body"></div>

                    <div class="modal-footer">
                        <span id="msg" class="pull-left" style="color: red;"></span>
                        <button type="button" class="btn btn-primary" data-dismiss="modal">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="modal fade" id="evaluateTruckModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <form name="formRelease" action="assignWeightRamp.do" method="post">
                        <div class="modal-header alert-success">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel"><i class="fa fa-pencil-square"></i> Resultados de la evaluacion del Camion</h4>
                        </div>
                        <div class="modal-body" style="font-size: 25px;">
                            <!--  Aqui se inserta lo que mande el Servlet que se mando a llamar desde Ajax -->
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary pull-right" data-dismiss="modal">Salir</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        
    </section><!-- /.content -->
</div><!-- /.content-wrapper -->

<!-- date-range-picker -->
<script src="../plugins/daterangepicker/moment.min.js" type="text/javascript"></script>
<script src="../plugins/daterangepicker/daterangepicker.js" type="text/javascript"></script>
<script>
    $(document).ready(function () {
        $('#mainDate').daterangepicker({
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
