<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<link rel="stylesheet" href="../plugins/iCheck/all.css">
<!-- Estilos para mostrar el contenido en las cajas -->
<style>
    /* En móvil (xs) se apila todo; no usamos flex. */
    /* Desde sm en adelante usamos flex para igualar alturas y repartir mitades */
    @media (min-width: 768px) {
      .row-flex {
        display: flex;
        align-items: stretch; /* estira las columnas a la misma altura */
      }
      .row-flex > [class*='col-'] {
        display: flex;
        flex-direction: column;
      }
      /* La caja de la derecha ocupa toda la altura disponible */
      .full-height {
        flex: 1;
      }
    }
    /* Altura mínima para que se note el efecto en la demo (ajusta a tu gusto) */
    .box-body {
      min-height: 120px;
    }
    /* Mensaje de error */
    #filtroError {
      color: red;
      margin-top: 5px;
      display: none; /* oculto por defecto */
    }
</style>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Llenado de Carga
            <small>Asignacion de los pallets al transportista</small>
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
                
        <div class="box-body">
          <div class="row row-flex">
            <!-- Columna izquierda (xs: 12; sm+: 6) con dos cajas mitad/mitad -->
            <div class="col-xs-12 col-sm-6">
              <div class="box box-primary" style="flex:1;">
                <div class="box-header with-border">
                    <h3 class="box-title">Seleccionar Embarque</h3>
                    <div class="box-tools pull-right">
                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div>
                </div>
                <!-- Caja de busquedad -->
                <div class="box-body">
                    <form name="formsearch" role="form" action="boxFilling.do" method="post">
                        <div class="col-md-8">
                            <div class="form-group">
                                <label >ID de Embarque</label>
                                <input type="text" id="filtroIdEmpaque" placeholder="ID Embarque" data-title="" name="idEmbarque" class="form-control" value="${requestScope.idEmbarque}" required/>                                                
                                <span id="filtroError">Debes escribir el Id del Embarque antes de guardar.!!!</span>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="box-footer">
                                <button type="submit" class="btn btn-primary" style="margin-top:14px;"><i class="fa fa-search"></i> Seleccionar</button>
                            </div>   
                        </div>
                    </form>
                </div>
              </div>
              <!-- Caja de pallet  -->
              <div class="box box-warning" style="flex:2;">
                <div class="box-header with-border">
                    <h3 class="box-title">Embarques creados</h3>
                    <div class="box-tools pull-right">
                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div>
                </div>
                <div class="box-body">
                    <div class="table-responsive">
                        <table id="dtable" class="table table-bordered table-striped table-hover">
                            <thead>
                                <tr>    
                                    <th>ID</th>
                                    <th>Transportista</th>
                                    <th>No. Caja</th>
                                    <th>Placa Caja</th>
                                    <th>Placa Camion</th>
                                    <th>Nombre del Chofer</th>
                                    <th>Fecha</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${StockTransferOrderInfo_Table}" var="row" varStatus="status">
                                    <tr>                          
                                        <td>${row.idStockTransferOrder}</td>
                                        <td>${row.TransportCompany}</td>
                                        <td>${row.TruckBoxNumber}</td>
                                        <td>${row.TruckBoxPlate}</td>
                                        <td>${row.TruckPlate}</td>
                                        <td>${row.TruckDriverName}</td>
                                        <td>${row.InsertDate}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table> 
                    </div>
                </div>
              </div>
            </div>

            <!-- Columna derecha (xs: 12; sm+: 6) caja que ocupa toda la altura -->
            <div class="col-xs-12 col-sm-6">
              <div class="box box-info full-height">
                <div class="box-header with-border">
                    <h4 class="box-title">Codigos Escaneados</h4>
                    <div class="box-tools pull-right">
                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div>
                </div>
                <div class="box-body">
                    <form name="formSave" id="formSave" action="boxFilling.do" method="post">
                        <div class="col-md-8">
                            <div class="form-group">
                                <label>Pallets</label>
                                <textarea name="palletInfo" class="form-control" rows="30" required id="palletInfo">
                                    ${fn:escapeXml(PalletInfo_Temp)}
                                </textarea>
                                <input type="hidden" id="idEmpaqueHidden" name="idEmbarque" value="${requestScope.idEmbarque}"/>
                            </div>                   
                        </div>
                        <div class="box-footer">
                            <button type="submit" class="btn btn-primary" style="margin-top:14px;"><i class="fa fa-save"></i> Guardar</button>
                            <input type="hidden" name="Action" value="Save"/> 
                        </div>
                    </form>
                </div>
              </div>
            </div>
          </div>
        </div>

        <script>
            $('#dtable').DataTable({
                "paging": false,
                "lengthChange": false,
                "searching": false,
                "ordering": true,
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
            
            $('#formSave').on('submit', function (e) {
                var filtroValue = $("#filtroIdEmpaque").val().trim();
                console.log("Entro al Submit");
                console.log(filtroValue);
                
                if (!filtroValue) {
                  e.preventDefault(); // cancelar submit
                  $("#filtroError").show(); // mostrar mensaje rojo
                  $("#filtroIdEmpaque").focus();
                } else {
                  $("#filtroError").hide(); // ocultar si ya tiene valor
                  $("#idEmpaqueHidden").val(filtroValue); // copiar al hidden
                  $('#idEmpaqueHidden').val(filtroValue);
                }
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
