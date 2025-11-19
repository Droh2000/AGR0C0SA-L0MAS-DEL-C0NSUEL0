<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script src="https://cdn.jsdelivr.net/npm/zebra-browser-print-min@3.0.216/BrowserPrint-3.0.216.min.js"></script>
<script>
    $(function () {
        $("#storageName").change(function () {
            if ($("#storageName").val().length > 0) {
                var v = $("#storageName").val();
                var param = 'storageName=' + v;
                $.ajax({
                    url: 'ajaxSubstorage.do',
                    type: 'POST',
                    data: param,
                    success: function (result) {
                        $('#substorageName').children().remove();
                        $("#substorageName").append(result);
                    }
                });
            } else {
                $('#substorageName').children().remove();
            }
        });
        var fileId = "${FileGeneratedId}";
        $('#print').on("click", function () {
            $('#showModal').find('.modal-body').html(
                '<embed src="readPalletIDToPrint.do?idDoc=' + fileId + '" frameborder="0" width="100%" height="600px">'
            );
            $('#showModal').modal('show');
        });
        
        $('#zebraPrint').on("click", function () {
            $('#selectPrinterModal').modal('show');
        });
        
        $("#configPrinterButton").on("click", function(){
            var printerName = $("#printerName").val();
            var quantityBatch = $("#quantityBatch").val();
            
            // mostrar modal de "imprimiendo"
            $("#iconContent").html("<i class='fa fa-print fa-3x text-primary'></i>");
            $("#printTitle").html("Generando Etiquetas");
            $("#printStatus").text("Por Favor Espere...");
            $("#printingModal").modal("show");
            
            // Enviar AJAX al servlet
            $.ajax({
                url: "newPallet.do", // o el mapping que uses
                type: "POST",
                data: { 
                    Action: "PrintZebra",
                    idUser: "${sessionScope.idUser}",
                    printerName: printerName,
                    quantityBatch: quantityBatch
                }, // Llamar el If del Servlet Correspondiente
                dataType: "json",
                success: function(response){
                    // Mostrar mensaje al usuario
                    if (response.ZPL_DATA) {
                        $("#iconContent").html("<i class='fa fa-check-square fa-3x text-success'></i>");
                        $("#printTitle").text("ZPL Generador Correctamente");
                        $("#printStatus").text("Etiquetas Generadas correctamente");
                        setTimeout(hidePrintModal, 2500);
                        sendToZebra(response.ZPL_DATA);
                    } else {
                        $("#iconContent").html("<i class='fa fa-times-circle fa-3x text-danger'></i>");
                        $("#printTitle").text("Error al generar ZPL");
                        $("#printStatus").text("No se generó ZPL desde el servidor.");
                    }
                },
                error: function(xhr, status, error){
                    console.error("Error en AJAX:", error);
                    $("#iconContent").html("<i class='fa fa-times-circle fa-3x text-danger'></i>");
                    $("#printTitle").text("Error al generar ZPL");
                    $("#printStatus").text("Error al generar ZPL en el servidor: " + error);
                }
            });

            function hidePrintModal() {
                $("#printingModal").modal("hide");
            }
        });
        
        $('#cancelButton').on("click", function () {
            $('#cancelModal').modal('show');
        });
    });
</script>
<script>
    
    function hidePrintModal() {
        $("#printingModal").modal("hide");
    }
    
    let selectedPrinter = null;

    window.onload = function() {
        BrowserPrint.getDefaultDevice("printer", 
            function(device) {
                if (device == null) {
                    $("#printingModal").modal("show");
                    $("#iconContent").html("<i class='fa fa-times-circle fa-3x text-danger'></i>");
                    $("#printTitle").text("Sin conexion");
                    $("#printStatus").text("No se encontró ninguna impresora Zebra conectada");
                    setTimeout(hidePrintModal, 1500);
                }else {
                    selectedPrinter = device;
                }
        }, function(error) {
            $("#printingModal").modal("show");
            $("#iconContent").html("<i class='fa fa-print fa-3x text-danger'></i>");
            $("#printTitle").text("Error");
            $("#printStatus").text("No se detectó impresora Zebra: " + error);
        });
    };

    function sendToZebra(zplData) {
        if (selectedPrinter == null) {
            $("#printingModal").modal("show");
            $("#iconContent").html("<i class='fa fa-print fa-3x text-danger'></i>");
            $("#printTitle").text("Error de conexion");
            $("#printStatus").text("No hay impresora seleccionada");
            return;
        }

        selectedPrinter.send(
            zplData,
            function() {
                $("#printingModal").modal("show");
                $("#iconContent").html("<i class='fa fa-print fa-3x text-success'></i>");
                $("#printTitle").text("Envio de etiquetas finalizado correctamente");
                $("#printStatus").text("Impresión enviada correctamente a la Zebra");
                setTimeout(hidePrintModal, 2500);
                location.reload();
            },
            function(error) {
                $("#printingModal").modal("show");
                $("#iconContent").html("<i class='fa fa-print fa-3x text-danger'></i>");
                $("#printTitle").text("Error al imprimir");
                $("#printStatus").text("Detalles: " + error);
            }
        );
    }
</script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Crear Pallet
            <small>Crear nuevo pallet para embarque</small>
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
        <form name="formsearch" role="form" action="newPallet.do" method="post">
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title">Nuevo Pallet</h3>
                </div><!-- /.box-header -->
                <div class="box-body">

                    <div class="col-md-2">
                        <div class="form-group">
                            <label >Tipo de Pallet</label>
                            <select name="palletName" id="palletName" class="form-control" required>
                                <option value=""></option>
                                <c:forEach items="${PalletDefinition_HashMap}" var="item">
                                    <option value="${item.value}"  ${requestScope.SelectedPalletName == item.value  ? 'selected' : ''}>${item.value}</option>
                                </c:forEach>
                            </select>                                                
                        </div>
                        <div class="form-group">
                            <label >Producto</label>
                            <select name="productName" id="productName" class="form-control" required>
                                <option value=""></option>
                                <c:forEach items="${PalletProduct_HashMap}" var="item">
                                    <option value="${item.value}"  ${requestScope.SelectedProductName == item.value  ? 'selected' : ''}>${item.value}</option>
                                </c:forEach>
                            </select>                                                
                        </div>
                    </div>
                    <div class="col-md-2">
                        <div class="form-group">
                            <label >Localidad</label>
                            <select name="storageName" id="storageName" class="form-control" required>
                                <option value=""></option>
                                <c:forEach items="${Storage_HashMap}" var="item">
                                    <option value="${item.value}"  ${requestScope.SelectedStorageName == item.value  ? 'selected' : ''}>${item.value}</option>
                                </c:forEach>
                            </select> 
                        </div>                        
                        <div class="form-group">
                            <label >Sublocalidad</label>
                            <select name="substorageName" id="substorageName" class="form-control" title="Seleccione primero la localidad" required>                                
                                <c:if test="${requestScope.SelectedSubstorageName ne ''}"> 
                                    <option value=""></option>
                                    <c:forEach items="${Substorage_HashMap}" var="item">
                                        <option value="${item.value}"  ${requestScope.SelectedSubstorageName == item.value  ? 'selected' : ''}>${item.value}</option>
                                    </c:forEach>
                                </c:if>
                            </select>                                                                                    
                        </div>                        
                    </div>
                    <div class="col-md-2">
                        <div class="form-group">
                            <label >Cantidad</label>
                            <input name="quantity" type="text" class="form-control" id="quantity" required value=""/>
                        </div>
                        <div class="form-group">
                            <label>Campo</label>
                            <select name="CampName" id="CampName" class="form-control" title="Seleccione el campo" required>                                
                                <option value=""></option>
                                <c:forEach items="${Camp_HashMap}" var="item">
                                    <option value="${item.value}"  ${requestScope.SelectedCampName == item.value  ? 'selected' : ''}>${item.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                </div><!-- /.box-body -->
                <div class="box-footer">
                    <button type="submit" class="btn btn-primary"><i class="fa fa-save"></i> Guardar</button>
                    <button type="button" name="print" id="print" class="btn btn-primary xxs showFormBtn" title ="Imprimir"<c:if test="${empty FileGeneratedId}">disabled</c:if>><i class="fa fa-file-pdf-o"></i> Visualizar</button>
                    <input type="hidden" name="Action" value="Save"/>    
                </div>
            </div><!-- /.box -->
        </form>
                    
        <c:if test="${not empty PalletJustSaved_Table}">       
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title">Pallets Recientemente Guardados</h3>
                    <div class="box-tools pull-right">
                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div>
                </div><!-- /.box-header -->
                <div class="box-body">
                    <div class="table-responsive">
                        <table id="tablaTemporal" class="table table-bordered table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>Pallet ID</th>
                                    <th>Producto</th>
                                    <th>Color</th>
                                    <th>Peso por Arpilla</th>
                                    <th>Tamaño</th>
                                    <th>Campo</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="row" items="${PalletJustSaved_Table}">
                                    <tr>
                                      <td>${row.PalletId}</td>
                                      <td>${row.ProductName}</td>
                                      <td>${row.Color}</td>
                                      <td>${row.Quantity}</td>
                                      <td>${row.Size}</td>
                                      <td>${row.CampName}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table> 
                    </div>
                </div><!-- /.box-body -->
            </div>

            <script>
                $('#tablaTemporal').DataTable({
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
        </c:if>


        <div class="modal fade" id="showModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <form name="formsearch" role="form" action="newPallet.do" method="post">
                        <div class="modal-header alert-success">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <button type="button" id="cancelButton" class="btn btn-primary pull-right btn-danger"><i class="fa fa-ban"></i> Cancelar</button>
                            <button type="button" name="printZebra" id="zebraPrint" class="btn btn-primary pull-left showFormZebra" title ="Imprimir"><i class="fa fa-print"></i> Imprimir</button>
                            <h4 class="modal-title text-center" id="myModalLabel"><i class="icon fa fa-info-circle"></i> Pallet ID</h4>
                        </div>
                        <div class="modal-body">
                            <input type="hidden" name="Action" id="ActionZebra" value="PrintZebra"/>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">Salir</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

    </section><!-- /.content -->
</div><!-- /.content-wrapper -->

<div class="modal fade" id="cancelModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form name="formcancel" role="form" action="newPallet.do" method="post">
                <div class="modal-header alert-warning">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel"><i class="icon fa fa-info-circle"></i> Cancelar Los Pallets ID Generados</h4>
                </div>
                <div class="modal-body">
                    <h3>¿Esta seguro que desea cancelar los pallets id generados?</h3>
                    <input type="hidden" name="Action" id="Action" value="CancelPalletID"/>
                </div>
                <div class="modal-footer">
                    <button type="submit" id="btnConfirmYes" class="btn btn-primary pull-left">Si</button>

                    <button type="button" data-dismiss="modal" class="btn btn-primary pull-right">No</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal para seleccionar la impresora que se quiere usar y cantidad del lote generado -->
<div class="modal fade" id="selectPrinterModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form name="formselect" role="form" action="newPallet.do" method="post">
                <div class="modal-header alert-warning">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel"><i class="icon fa fa-info-circle"></i> Seleccione la impresora y cantidad a imprimir</h4>
                </div>
                <div class="modal-body">
                    <label>Seleccione la Impresora</label>
                    <select name="printerName" id="printerName" class="form-control" required>
                        <c:forEach items="${PrinterDefinition_HashMap}" var="item">
                            <option value="${item.value}" ${requestScope.SelectedPrinterName == item.value  ? 'selected' : ''}>${item.value}</option>
                        </c:forEach>
                    </select>                                                
                    
                    <!--<div class="form-group">
                        <label>Cantidad por cada lote generado</label>
                        <input name="quantityBatch" type="text" class="form-control" id="quantityBatch" required value="1"/>                                                
                    </div>
                    <input type="hidden" name="Action" id="Action" value=""/>-->
                </div>
                <div class="modal-footer">
                    <button type="button" data-dismiss="modal" class="btn btn-primary pull-left">Cancelar</button>
                    <button type="button" id="configPrinterButton" data-dismiss="modal" class="btn btn-primary pull-right"><i class="fa fa-print"></i> Imprimir</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal para indicar que se esta imprimiendo -->
<div class="modal fade" id="printingModal" tabindex="-1" role="dialog">
  <div class="modal-dialog modal-sm" role="document">
    <div class="modal-content text-center">
      <div class="modal-body">
        <div id="iconContent"></div>  
        <h4 id="printTitle"></h4>
        <p id="printStatus"></p>
      </div>
      <div class="modal-footer">
         <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">Cerrar</span></button>
      </div>
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
