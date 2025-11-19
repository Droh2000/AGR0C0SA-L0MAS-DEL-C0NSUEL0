<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- daterange picker -->
<link href="../plugins/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">

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

        $('.cancelFormBtn').on("click", function () {
            var id = $(this).attr('id');
            id = id.replace("c", "");
            $("#id4Cancel").val(id);
            $('#cancelModal').modal('show');
        });

        $('.releaseFormBtn').on("click", function () {
            var id = $(this).attr('id');
            id = id.replace("r", "");
            $("#id4Release").val(id);
            $('#releaseModal').modal('show');
        });
    });



</script>


<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Liberar Embarque
            <small>Asignar Proforma #</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-caret-right"></i> Empaque</a></li>
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
        <form name="formsearch" role="form" action="releaseStockTransferOrder.do" method="post">
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title">Liberar Embarque</h3>
                    <div class="box-tools pull-right">
                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div>
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
                    <button type="submit" class="btn btn-primary"><i class="fa fa-search"></i> Consultar</button>
                </div>
            </div><!-- /.box -->
        </form>  

        <div class="box box-success">
            <div class="box-header with-border">
                <h3 class="box-title">Embarques listos para liberar</h3>
                <div class="box-tools pull-right">
                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                </div>
            </div><!-- /.box-header -->
            <div class="box-body">
                <div class="table-responsive">
                    <table id="dtable" class="table table-bordered table-striped table-hover">
                        <thead>
                            <tr>    
                                <th>ID</th>
                                <th>Transportista</th>
                                <th>Nombre de Chofer</th>
                                <th>Num de Caja</th>
                                <th>Placa Caja</th>
                                <th>Placa Camion</th>
                                <th>Cruzador</th>
                                <th>Tipo de Caja</th>
                                <th>Pies de Caja</th>
                                <th>Evaluacion</th>
                                <th>Tara</th>
                                <th>Bruto</th>
                                <th>Neto</th>
                                <th>Status</th>
                                <th>Fecha</th>
                                <th>Liberar</th>
                                <th>Editar</th>
                            </tr>
                        </thead>
                        <tbody>

                            <c:forEach items="${StockTransferOrderInfo_Table}" var="row" varStatus="status">
                                <tr>                                 
                                    <td>${row.idStockTransferOrder}</td>
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
                                    <td>${row.Status}</td>
                                    <td>${row.InsertDate}</td>
                                    <td>
                                        <button type="button" name="r${row.idStockTransferOrder}" id="r${row.idStockTransferOrder}" class="btn btn-success xxs releaseFormBtn" title ="Liberar" onclick="">
                                            <i class="fa fa-truck"></i>
                                        </button>                                                                                   
                                    </td>
                                    <td>
                                        <button type="button"
                                                class="btn btn-warning cancelFormBtn"
                                                data-toggle="modal"
                                                data-target="#cancelModal"
                                                data-id="${row.idStockTransferOrder}"
                                                data-company="${fn:escapeXml(row.TransportCompany)}"
                                                data-driver="${fn:escapeXml(row.TruckDriverName)}"
                                                data-plate="${fn:escapeXml(row.TruckPlate)}"
                                                data-box-number="${fn:escapeXml(row.TruckBoxNumber)}"
                                                data-box-plate="${fn:escapeXml(row.TruckBoxPlate)}"
                                                data-crosser="${fn:escapeXml(row.Crosser)}"
                                                data-type-box="${fn:escapeXml(row.TypeBox)}"
                                                data-box-feet="${fn:escapeXml(row.BoxFeet)}"
                                                data-tara="${fn:escapeXml(row.Tare)}"
                                                data-bruto="${fn:escapeXml(row.Gross)}"
                                                data-neto="${fn:escapeXml(row.Net)}"
                                                title="Editar">
                                            <i class="fa fa-pencil-square"></i>
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
            });

        </script>




    </section><!-- /.content -->
</div><!-- /.content-wrapper -->

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

<div class="modal fade" id="cancelModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form name="formCancel" action="releaseStockTransferOrder.do" method="post">
                <div class="modal-header alert-warning">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel"><i class="icon fa fa-pencil-square"></i> Editar Informacion Del Embarque</h4>
                </div>
                <div class="modal-body">
                    
                    <!--<p><h3>Esta usted seguro de cancelar este embarque?</h3></p>
                    <div class="form-group">
                        <label>Comentarios</label>
                        <textarea name="comments" class="form-control" rows="5" id="comments" required></textarea>
                    </div>
                    <input type="hidden" name="idStockTransferOrder" id="id4Cancel" value=""/>
                    <input type="hidden" name="Action" id="Action" value="Cancel"/>-->
                    <div class="row">
                        <div class="col-md-6">
                          <div class="form-group">
                            <label >Transportista</label>
                            <select name="TransportCompany" id="edit_company" class="form-control">
                                <c:forEach items="${TransportCompany_HashMap}" var="item">
                                    <option value="${item.value}"  ${requestScope.SelectedTransportCompanyName == item.value  ? 'selected' : ''}>${item.value}</option>
                                </c:forEach>
                            </select>                                                
                          </div>
                        </div>

                        <div class="col-md-6">
                          <div class="form-group">
                            <label>Nombre del Chofer</label>
                            <input type="text" class="form-control" name="TruckDriverName" id="edit_driver">
                          </div>
                        </div>

                        <div class="col-md-6">
                          <div class="form-group">
                            <label>Número de Caja</label>
                            <input type="text" class="form-control" name="TruckBoxNumber" id="edit_box_number">
                          </div>
                        </div>

                        <div class="col-md-6">
                          <div class="form-group">
                            <label>Placas de Caja</label>
                            <input type="text" class="form-control" name="TruckBoxPlate" id="edit_box_plate">
                          </div>
                        </div>

                        <div class="col-md-6">
                          <div class="form-group">
                            <label>Placas del Camión</label>
                            <input type="text" class="form-control" name="TruckPlate" id="edit_plate">
                          </div>
                        </div>
                        
                        <div class="col-md-6">
                          <div class="form-group">
                                <label >Cruzador: </label>
                                <select name="Crosser" id="edit_crosser" required class="form-control">
                                    <option value="Si">Si</option>
                                    <option value="No">No</option>
                                </select>
                           </div>
                        </div>
                        
                        <div class="col-md-6">
                            <div class="form-group">
                                <label >Tipo de Caja: </label>
                                <select name="TypeBox" id="edit_type_box" required class="form-control">
                                    <option value="Refrigerado">Refrigerado</option>
                                    <option value="Caja Seca">Caja Seca</option>
                                </select>
                            </div>
                        </div>
                        
                        <div class="col-md-6">
                          <div class="form-group">
                            <label>Pies de Caja</label>
                            <input type="text" class="form-control" name="BoxFeet" id="edit_box_feet">
                          </div>
                        </div>

                        <div class="col-md-6">
                          <div class="form-group">
                            <label>Tara</label>
                            <input type="text" class="form-control" name="Tara" id="edit_tara">
                          </div>
                        </div>

                        <div class="col-md-6">
                          <div class="form-group">
                            <label>Bruto</label>
                            <input type="text" class="form-control" name="Bruto" id="edit_bruto">
                          </div>
                        </div>

                        <div class="col-md-6">
                          <div class="form-group">
                            <label>Neto</label>
                            <input type="text" class="form-control" name="Neto" id="edit_neto" disabled>
                          </div>
                        </div>

                        <!-- action para actualizar -->
                        <input type="hidden" name="Action" value="UpdateOrder">
                        <!-- id de la fila -->
                        <input type="hidden" name="idStockTransferOrder" id="edit_id">
                    
                    <!--<input type="hidden" name="idStockTransferOrder" id="id4Cancel" value=""/>
                    <input type="hidden" name="Action" id="Action" value="Edit"/>-->
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary pull-left" data-dismiss="modal">Cancelar</button>
                    <button type="submit" id="btnConfirmYes" class="btn btn-primary pull-right">Guardar</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="releaseModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form name="formRelease" action="releaseStockTransferOrder.do" method="post">
                <div class="modal-header alert-success">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel"><i class="icon fa fa-truck"></i> Liberar Embarque</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label>Proforma #</label>                                                
                        <div class="form-group">
                            <input type="text" name="proforma" class="form-control" placeholder="Proforma #" data-title="No puede estar vacio" required value="${requestScope.SelectedProforma}" />
                        </div>                                    
                    </div>
                    <h5>Formato valido: 2 digitos de año actual + 4 digitos de numero consecutivo.</h5>
                    <h5>Cualquier letras o caracteres especiales son invalidos.</h5>
                    <input type="hidden" name="idStockTransferOrder" id="id4Release" value=""/>
                    <input type="hidden" name="Action" id="Action" value="Release"/>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary pull-left" data-dismiss="modal">Cancelar</button>
                    <button type="submit" id="btnConfirmYes" class="btn btn-primary pull-right">Guardar</button>
                </div>
            </form>
        </div>
    </div>
</div>
      
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
    $('#cancelModal').on('show.bs.modal', function (e) {
        const btn = e.relatedTarget;                 // botón que abrió el modal
        const id  = btn.getAttribute('data-id');

        // Lee data-* (usa getAttribute o jQuery .data)
        const company   = btn.getAttribute('data-company')     || '';
        const driver    = btn.getAttribute('data-driver')      || '';
        const plate     = btn.getAttribute('data-plate')       || '';
        const boxNumber = btn.getAttribute('data-box-number')  || '';
        const boxPlate  = btn.getAttribute('data-box-plate')   || '';
        const crosser  = btn.getAttribute('data-crosser')   || '';
        const typeBox  = btn.getAttribute('data-type-box')   || '';
        const boxFeet  = btn.getAttribute('data-box-feet')   || '';
        const tara  = btn.getAttribute('data-tara')    || '';
        const bruto  = btn.getAttribute('data-bruto')    || '';
        const neto  = btn.getAttribute('data-neto')    || '';

        // setea campos del form de edición
        $('#edit_id').val(id);
        $('#edit_company').val(company);
        $('#edit_driver').val(driver);
        $('#edit_plate').val(plate);
        $('#edit_box_number').val(boxNumber);
        $('#edit_box_plate').val(boxPlate);  
        $('#edit_crosser').val(crosser);
        $('#edit_type_box').val(typeBox);
        $('#edit_box_feet').val(boxFeet);
        $('#edit_tara').val(tara);
        $('#edit_bruto').val(bruto);
        $('#edit_neto').val(neto);

        // setea el id del form de cancelar embarque
        //$('#cancel_id').val(id);
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
