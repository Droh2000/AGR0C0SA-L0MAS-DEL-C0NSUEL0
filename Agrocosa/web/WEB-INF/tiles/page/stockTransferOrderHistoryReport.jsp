<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Reporte de Historial de Embarques al Paso
            <small>Consulta historial de ordenes de embarque</small>
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
        <form name="formsearch" role="form" action="stockTransferOrderHistoryReport.do" method="post">
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title">Filtros de Busqueda</h3>
                </div><!-- /.box-header -->
                <div class="box-body">

                    <div class="col-md-2">
                        <div class="form-group">
                            <label >Embarque ID</label>
                            <input name="idStockTransferOrder" type="text" class="form-control pull-right" id="idStockTransferOrder" value="${requestScope.SelectedIdStockTransferOrder}"/>
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
                            <label >Proceso</label>
                            <select name="process" id="process" class="form-control">                                
                                <option value=""></option>
                                <option value="Embarque"  ${requestScope.SelectedProcess == 'Embarque'  ? 'selected' : ''}>Embarque</option>
                                <option value="Recibo"  ${requestScope.SelectedProcess == 'Recibo'  ? 'selected' : ''}>Recibo</option>
                            </select>                                                                                    
                        </div>                        
                    </div>
                    <div class="col-md-2">                        
                        <div class="form-group">
                            <label >Movimiento</label>
                             <select name="movementType" id="movementType" class="form-control">                                
                                <option value=""></option>
                                <option value="Nueva"  ${requestScope.SelectedMovementType == 'Nueva'  ? 'selected' : ''}>Nueva</option>
                                <option value="Entrada"  ${requestScope.SelectedMovementType == 'Entrada'  ? 'selected' : ''}>Entrada</option>
                                <option value="Salida"  ${requestScope.SelectedMovementType == 'Salida'  ? 'selected' : ''}>Salida</option>
                                <option value="Transferencia"  ${requestScope.SelectedMovementType == 'Transferencia'  ? 'selected' : ''}>Transferencia</option>
                                <option value="Cancelada"  ${requestScope.SelectedMovementType == 'Cancelada'  ? 'selected' : ''}>Cancelada</option>
                            </select>                                                                                   
                        </div>                        
                    </div>
                    <div class="col-md-2">                        
                        <div class="form-group">
                            <label >Transportista</label>
                            <select name="transportCompany" id="transportCompany" class="form-control">                                
                                <option value=""></option>
                                <c:forEach items="${TransportCompany_HashMap}" var="item">
                                    <option value="${item.value}"  ${requestScope.SelectedTransportCompany == item.value  ? 'selected' : ''}>${item.value}</option>
                                </c:forEach>
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
                                <th>Embarque ID</th>
                                <th>Transportista</th>
                                <th>Nombre de Chofer</th>
                                <th>Placas</th>
                                <th>Proforma</th>
                                <th>Proceso</th>
                                <th>Movimiento</th>
                                <th>Cantidad</th>
                                <th>Registrado Por</th>
                                <th>Fecha de Registro</th>
                                <th>Comentarios</th>
                            </tr>
                        </thead>
                        <tbody>

                            <c:forEach items="${StockTransferOrderInfo_Table}" var="row" varStatus="status">
                                <tr>                                 
                                    <td>${row.idStockTransferOrder}</td>
                                    <td>${row.TransportCompany}</td>
                                    <td>${row.TruckDriverName}</td>
                                    <td>${row.Plate}</td>
                                    <td>${row.fProforma}</td>
                                    <td>${row.Process}</td>
                                    <td>${row.MovementType}</td>
                                    <td>${row.Quantity}</td>
                                    <td>${row.User}</td>
                                    <td>${row.fInsertDate}</td>
                                    <td>${row.Comments}</td>
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
