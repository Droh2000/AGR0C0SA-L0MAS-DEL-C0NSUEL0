<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<link rel="stylesheet" href="../plugins/iCheck/all.css">
<script type="text/javascript">
    var id=0;
    $(function () {
        $('.evaluationFormBtn').on("click", function () {
            id = $(this).data('id');
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
        
        $('.closeFormBtn').on("click", function () {
            id = $(this).attr('id');
            id = id.replace("c", "");
            $("#id4OnlyWeight").val(id);
            
            var company = $(this).data('company') || '';
            var boxNumber  = $(this).data('boxNumber')  || '';
            
            $('#lblTransportista').text('Transportista: ' + company);
            $('#lblNumeroCaja').text('Numero de Caja: ' + boxNumber);
            
            $('#cancelModal').modal('show');
        });
        
        
        // Cambiar el contenido del Modal
        $('.siFormBtn').on("click", function () {
            $('#cancelModal').modal('hide');
            
            $("#id4NetWeight").val(id);
            
            $('#saveNetWeightModal').modal('show');
        });
        
    });
</script>

<!-- Codigo de los Inputs de Tara y Rampa -->
<script>
(function() {
  // Doble click: activar edición
  $(document).on('dblclick', '.cell-edit', function() {
    const $td   = $(this);
    const $span = $td.find('.cell-text');
    const $inp  = $td.find('.cell-input');

    // si ya está visible, no repetir
    if ($inp.is(':visible')) return;

    $span.hide();
    $inp.show().focus().select();
  });

  // Enter para guardar / ESC para cancelar
  $(document).on('keydown', '.cell-input', function(e) {
    const $inp  = $(this);
    const $td   = $inp.closest('.cell-edit');
    const $span = $td.find('.cell-text');

    if (e.key === 'Escape') {
      // cancelar
      $inp.val($span.text().trim() === '?' ? '' : $span.text().trim());
      $inp.hide();
      $span.show();
      return;
    }

    if (e.key !== 'Enter') return;
    e.preventDefault();

    const id    = $td.data('id');
    const field = $td.data('field');   // 'Tare' o 'Ramp'
    const value = $inp.val().trim();

    if (field === 'Tare' && value !== '' && isNaN(value)) {
      showErr('El valor de Tara debe ser numérico');
      return;
    }
    
    if (field === 'Ramp' && value !== '' && isNaN(value)) {
      showErr('El valor de la Rampa debe ser numérico');
      return;
    }

    // Mapea a la Action que tu servlet ya entiende
    const action = (field === 'Tare') ? 'WeightTare' : 'Ramp';
    // Clave del parámetro EXACTA que espera tu Transaction
    const payload = {
      Action: action,
      idStockTransferOrder: id
    };
    payload[field] = value; // Tare o Ramp

    $.ajax({
      url: 'assignWeightRamp.do',
      method: 'POST',
      dataType: 'json',
      data: payload,
      success: function(resp) {
        if (resp && resp.code === 'PASS') {
          showOk(resp.message || 'Guardado correctamente');

          // Actualizar renderizado: esconder input y poner texto plano
          $inp.hide();
          $span.text(value === '' ? '?' : value).show();
        } else {
          showErr((resp && resp.message) ? resp.message : 'No se pudo guardar: '+resp);
        }
      },
      error: function() {
        showErr('Error al guardar');
      }
    });
  });

  function showOk(msg) {
    $('#alertArea').html('<div class="alert alert-success" role="alert">'+msg+'</div>');
    setTimeout(() => $('#alertArea .alert').fadeOut(300, function(){ $(this).remove(); }), 2000);
  }
  function showErr(msg) {
    $('#alertArea').html('<div class="alert alert-danger" role="alert">'+msg+'</div>');
  }
})();
</script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Asignar Peso y Rampa
            <small>Asignacion del peso y la rampa del transportista</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-caret-right"></i> Empaque</a></li>
            <li class="active">Aqui</li>
        </ol>
    </section>

    <!-- Main content -->
    <section class="content">
        <div id="alertArea"></div>
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
        <form name="formsearch" role="form" action="assignWeightRamp.do" method="post">
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title">Asignar Peso y Rampa</h3>
                    <div class="box-tools pull-right">
                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div>
                </div><!-- /.box-header -->
                <div class="box-body offset-md-4">
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
                            <label >Numero de Caja</label>
                            <input type="text" placeholder="Numero de Caja" data-title="" name="truckBoxNumber" class="form-control" value="${requestScope.truckBoxNumber}" />                                             
                        </div>
                    </div>
                </div><!-- /.box-body -->
                <div class="box-footer">
                    <button type="submit" class="btn btn-primary"><i class="fa fa-search"></i> Consultar</button>
                </div>
            </div><!-- /.box -->
        </form>
        
        <c:if test="${not empty TransportWeightedInfo_Table}">
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title">Embarques creados</h3>
                    <div class="box-tools pull-right">
                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div>
                </div><!-- /.box-header -->
                <div class="box-body">
                    <div class="table-responsive">
                        <table id="dtable" class="table table-bordered table-striped table-hover">
                            <thead>
                                <tr>    
                                    <th>Id de Embarque</th>
                                    <th>No. Caja</th>
                                    <th>Transportista</th>
                                    <th>Evaluacion</th>
                                    <th>Peso (Tara)</th> 
                                    <th>Rampa</th>
                                    <th>Cerrar</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${TransportWeightedInfo_Table}" var="row" varStatus="status">
                                    <tr>                                 
                                        <td>${row.idStockTransferOrder}</td>
                                        <td>${row.TruckBoxNumber}</td>
                                        <td>${row.TransportCompany}</td>
                                        <td>
                                            <button 
                                                type="button" 
                                                name="${row.idStockTransferOrder}"
                                                class="btn btn-primary xxs evaluationFormBtn" 
                                                data-id="${row.idStockTransferOrder}"
                                                data-action="Evaluation"
                                                title ="Evaluacion del Camion" 
                                                onclick=""
                                            >
                                                <i class="fa fa-eye"></i>
                                            </button>
                                        </td>
                                         <!-- Peso (Tara) -->
                                        <td class="cell-edit"
                                            data-id="${row.idStockTransferOrder}"
                                            data-field="Tare">
                                          <span class="cell-text">
                                            <c:choose>
                                              <c:when test="${not empty row.Tare}">
                                                ${row.Tare}
                                              </c:when>
                                              <c:otherwise>-</c:otherwise>
                                            </c:choose>
                                          </span>
                                          <input escapeXml="false" type="number" step="0.01" class="cell-input form-control input-sm" style="display:none"
                                                 value="${not empty row.Tare ? row.Tare : ''}"/>
                                        </td>
                                        <!-- Rampa -->
                                        <td class="cell-edit"
                                            data-id="${row.idStockTransferOrder}"
                                            data-field="Ramp">
                                          <span class="cell-text">
                                            <c:choose>
                                              <c:when test="${not empty row.Ramp}">
                                                ${row.Ramp}
                                              </c:when>
                                              <c:otherwise>?</c:otherwise>
                                            </c:choose>
                                          </span>
                                          <input escapeXml="false" type="text" maxlength="45" class="cell-input form-control input-sm" style="display:none"
                                                 value="${not empty row.Ramp ? row.Ramp : ''}"/>
                                        </td>
                                        <td>
                                            <button 
                                                type="button" 
                                                name="${row.idStockTransferOrder}"
                                                id="c${row.idStockTransferOrder}"
                                                
                                                data-company="${fn:escapeXml(row.TransportCompany)}"
                                                data-box-number="${fn:escapeXml(row.TruckBoxNumber)}"
                                                
                                                class="btn btn-primary xxs closeFormBtn" 
                                                title ="Cerrar" 
                                                onclick=""
                                            >
                                                <i class="fa fa-times"></i>
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table> 
                    </div>

                </div><!-- /.box-body -->
            </div>
        </c:if>


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
            <form name="formCancel" action="assignWeightRamp.do" method="post">
                <div class="modal-header alert-warning">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel"><i class="icon fa fa-truck"></i> Completar El Peso Neto</h4>
                </div>
                <div class="modal-body">
                    <p><h3>¿La carga del camión requiere calcular el peso neto?</h3></p>
                    <br>
                    <label id="lblTransportista" class="form-label">
                        Transportista:
                    </label>
                    <br>
                    <label id="lblNumeroCaja" class="form-label">
                        Numero de Caja:
                    </label>
                    <input type="hidden" name="idStockTransferOrder" id="id4OnlyWeight" value=""/>
                    <input type="hidden" name="Action" id="Action" value="WeightNetNot"/>
                </div>
                <div class="modal-footer">
                    <button type="submit" id="btnConfirmYes" class="btn btn-primary pull-left"><i class="fa fa-floppy-o"></i> No</button>

                    <button type="button" class="btn btn-primary pull-right siFormBtn">Si</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="saveNetWeightModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form name="formRelease" action="assignWeightRamp.do" method="post">
                <div class="modal-header alert-success">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel"><i class="icon fa fa-truck"></i> Completar El Peso Neto</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label>Peso Bruto:</label>
                        <div class="form-group">
                            <input type="text" name="gross" id="inputWeightNet" class="form-control" placeholder="Ingrese el peso bruto" data-title="No puede estar vacio" required value="${requestScope.Gross}"/>
                        </div>
                    </div>
                    <input type="hidden" name="idStockTransferOrder" id="id4NetWeight" value=""/>
                    <input type="hidden" name="Action" id="Action" value="WeightNetYes"/>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary pull-left" data-dismiss="modal">Salir</button>
                    
                    <button type="submit" id="btnConfirmYes" class="btn btn-primary pull-right"><i class="fa fa-floppy-o"></i> Guardar</button>
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
