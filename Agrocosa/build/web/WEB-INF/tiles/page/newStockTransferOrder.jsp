<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<link rel="stylesheet" href="../plugins/iCheck/all.css">
<script type="text/javascript">

    $(function () {
        $('.releaseFormBtn').on("click", function () {
            var id = $(this).attr('id');
            
            var driver  = $(this).data('driver')  || '';
            var company = $(this).data('company') || '';
            
            id = id.replace("r", "");
            $("#id4Release").val(id);
            
            $('#lblTransportista').text('Transportista: ' + company);
            $('#lblNombreChofer').text('Nombre del Chofer: ' + driver);
            
            $('#evaluateTruckModal').modal('show');
        });
    });
</script>
<style>
    .form-control.pull-right label {
        margin-right: 20px; /* ajusta el valor a tu gusto */
    }
</style>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Registro del Embarque
            <small>Registrar los datos del camión y el transportista</small>
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
        <form name="formsearch" role="form" action="newStockTransferOrder.do" method="post">
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title">Nuevo Embarque</h3>
                    <br>
                    <p>Le damos la bienvenida por parte de Agrocosa por favor ingrese sus datos para poder continuar con el proceso del embarque, una vez ingresados los datos la persona encargada de la báscula le dará las indicaciones</p>
                    <div class="box-tools pull-right">
                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div>
                </div><!-- /.box-header -->
                <div class="box-body offset-md-4 text-center">
                    <div class="col-md-4"></div>
                    <div class="col-md-4">
                        <div class="form-group">
                            <label >Transportista</label>
                            <select name="transportCompanyName" id="transportCompanyName" required class="form-control">
                                <option value="" disabled selected>-- Elija una opción --</option>
                                <c:forEach items="${TransportCompany_HashMap}" var="item">
                                    <option value="${item.value}"  ${requestScope.SelectedTransportCompany == item.value  ? 'selected' : ''}>${item.value}</option>
                                </c:forEach>
                            </select>                                                
                        </div>
                        <div class="form-group">
                            <label >Numero de la Caja</label>
                            <input name="truckBoxNumber" type="text" class="form-control pull-right" id="truckBoxNumber" required value=""/>
                        </div>  
                        <div class="form-group">
                            <label >Placa de la Caja</label>
                            <input name="truckBoxPlate" type="text" class="form-control pull-right" id="truckBoxPlate" required value=""/>
                        </div> 
                        <div class="form-group">
                            <label >Placa del Camion</label>
                            <input name="truckPlate" type="text" class="form-control pull-right" id="truckPlate" required value=""/>
                        </div> 
                        <div class="form-group">
                            <label >Nombre del Chofer</label>
                            <input name="truckDriverName" type="text" class="form-control pull-right" id="truckDriverName" required value=""/>
                        </div>
                        <div class="form-group">
                            <label >Cruzador: </label>
                            <div class="form-control pull-right">
                                <label>
                                    <input type="radio" name="crosser" value="Si" required> Sí
                                </label>
                                <label>
                                    <input type="radio" name="crosser" value="No"> No
                                </label>
                            </div>
                        </div>        
                        <div class="form-group">
                            <label >Tipo de Caja: </label>
                            <select name="typeBox" id="typeBox" required class="form-control">
                                <option value="" disabled selected>-- Elija una opción --</option>
                                <option value="Refrigerado">Refrigerado</option>
                                <option value="Caja Seca">Caja Seca</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label >Tamaño de la Caja (Pies): </label>
                            <input name="boxFeet" type="text" class="form-control pull-right" id="boxFeet" required value=""/>
                        </div>
                    </div>      
                </div><!-- /.box-body -->
                <div class="box-footer text-center">
                    <button type="submit" class="btn btn-primary"><i class="fa fa-save"></i> Guardar</button>
                    <input type="hidden" name="Action" value="Save"/> 
                </div>
            </div><!-- /.box -->
        </form>  
        
        <c:if test="${not empty StockTransferOrderInfo_Table}">
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
                                    <th>ID</th>
                                    <th>Transportista</th>
                                    <th>Nombre de Chofer</th>
                                    <th>Placa Camion</th>
                                    <th>Placa Caja</th> 
                                    <th>Fecha de Registro</th>
                                    <th>Evaluar</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${StockTransferOrderInfo_Table}" var="row" varStatus="status">
                                    <tr>                                 
                                        <td>${row.idStockTransferOrder}</td>
                                        <td>${row.TransportCompany}</td>
                                        <td>${row.TruckDriverName}</td>
                                        <td>${row.TruckPlate}</td>
                                        <td>${row.TruckBoxPlate}</td>
                                        <td>${row.InsertDate}</td>
                                        <td>
                                            <button 
                                                type="button" 
                                                name="${row.idStockTransferOrder}" 
                                                id="${row.idStockTransferOrder}" 
                                                data-driver="${fn:escapeXml(row.TruckDriverName)}"
                                                data-company="${fn:escapeXml(row.TransportCompany)}"
                                                class="btn btn-primary xxs releaseFormBtn" 
                                                title ="Evaluar" 
                                                onclick=""
                                            >
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
            <form name="formRelease" action="newStockTransferOrder.do" method="post">
                <div class="modal-header alert-success">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel"><i class="fa fa-pencil-square"></i> Evaluar la higiene del Camion</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group"> 
                        <label id="lblTransportista" class="form-label">
                            Transportista:
                        </label>
                        <br>
                        <label id="lblNombreChofer" class="form-label">
                            Nombre del Chofer:
                        </label>
                        <table class="table table-striped">
                            <tr>
                                <td>Limpieza de Caja</td>
                                <td><input type="radio" name="truckBoxCleaning" class="minimal-red" value="Aceptable">Aceptable</td>
                                <td><input type="radio" name="truckBoxCleaning" class="minimal-red" value="No Aceptable">No&nbsp;Aceptable</td>
                            </tr>
                            <tr>
                                <td>Condiciones de Mantenimiento</td>
                                <td><input type="radio" name="maintenanceConditions" class="minimal-red" value="Aceptable">Aceptable</td>
                                <td><input type="radio" name="maintenanceConditions" class="minimal-red" value="No Aceptable">No&nbsp;Aceptable</td>
                            </tr>
                            <tr>
                                <td>Ventilas Abiertas</td>
                                <td><input type="radio" name="openWindows" class="minimal-red" value="Si">Si</td>
                                <td><input type="radio" name="openWindows" class="minimal-red" value="No">No</td>
                            </tr>
                            <tr>
                                <td>Tarimas Selladas</td>
                                <td><input type="radio" name="sealedPallets" class="minimal-red" value="Si">Si</td>
                                <td><input type="radio" name="sealedPallets" class="minimal-red" value="No">No</td>
                            </tr>
                            <tr>
                                <td>Libre de Plagas</td>
                                <td><input type="radio" name="pestFree" class="minimal-red" value="Si">Si</td>
                                <td><input type="radio" name="pestFree" class="minimal-red" value="No">No</td>
                            </tr>
                            <tr>
                                <td>Libre Aromas Extraños</td>
                                <td><input type="radio" name="smellStrangeFree" class="minimal-red" value="Si">Si</td>
                                <td><input type="radio" name="smellStrangeFree" class="minimal-red" value="No">No</td>
                            </tr>
                            <tr>
                                <td>Etiqueta</td>
                                <td><input type="radio" name="label" class="minimal-red" value="Aceptable">Aceptable</td>
                                <td><input type="radio" name="label" class="minimal-red" value="No Aceptable">No&nbsp;Aceptable</td>
                            </tr>

                        </table>
                    </div>
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
