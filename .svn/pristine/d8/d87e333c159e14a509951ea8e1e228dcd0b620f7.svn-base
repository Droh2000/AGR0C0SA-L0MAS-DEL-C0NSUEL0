<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- daterange picker -->
<link href="../plugins/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />



<div class="" >
    <div style="content-wrapper">
        <section class="content-header">
            <h1>
                Reporte de Historial de Inventario
                <small>Consulta de movimientos historicos de inventario</small>
            </h1>
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
            <form name="formSearch" role="form" action="warehouseHistoryReport.do" method="post">
                <div class="box box-success">
                    <div class="box-header with-border">
                        <h3 class="box-title">Filtros de Busqueda</h3>
                        <div class="box-tools pull-right">
                            <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                        </div>                    
                    </div><!-- /.box-header -->
                    <div class="box-body">
                        <div class='row'>
                            <div class="col-md-2">
                                <div class="form-group">
                                    <label >Producto:</label>
                                    <input class="form-control" placeholder="product" type="text"  name="product" value="${requestScope.SelectedProduct}"/>
                                </div> 
                            </div>
                            <div class="col-md-2">
                                <div class="form-group">
                                    <label >Linea:</label>
                                    <input class="form-control" placeholder="line" type="text"  name="line" size ="5" value="${requestScope.SelectedLine}"/>
                                </div><!-- /.form-group -->
                            </div>                            
                            <div class="col-md-2">
                                <div class="form-group">
                                    <label >Movimiento:</label>
                                    <select name="movement" id="status" class="form-control" data-title="Movimiento">
                                        <option value=""></option>
                                        <option value="Entrada"  ${requestScope.SelectedStatus == 'Entrada'  ? 'selected' : ''}>Entrada</option>
                                        <option value="Salida"  ${requestScope.SelectedStatus == 'Salida'  ? 'selected' : ''}>Salida</option>
                                        <!--<option value="Transferencia"  ${requestScope.SelectedStatus == 'Transferencia'  ? 'selected' : ''}>Transferencia</option>-->
                                    </select>                                    
                                </div><!-- /.form-group -->
                            </div>
                            <div class="col-md-2">
                                <div class="form-group">
                                    <label >Fecha de Movimiento (inicio):</label>
                                    <div class="input-group">
                                        <div class="input-group-addon">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                        <input type="text" name="startDate" class="form-control" id="startDate" value="${requestScope.SelectedStartDate}"/>
                                    </div>
                                </div><!-- /.form-group -->
                            </div>
                            <div class="col-md-2">
                                <div class="form-group">
                                    <label >Fecha de Movimiento (fin):</label>
                                    <div class="input-group">
                                        <div class="input-group-addon">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                        <input type="text" name="endDate" class="form-control" id="endDate" value="${requestScope.SelectedEndDate}"/>
                                    </div> 
                                </div><!-- /.form-group -->
                            </div>
                        </div>

                    </div><!-- /.box-body -->
                    <div class="box-footer">
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary"><i class="fa fa-search"></i> Buscar</button>
                            <input type="hidden" name="Action" value="Search"/>
                        </div><!-- /.form-group -->                        
                    </div>
                </div><!-- /.box -->
            </form>



            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title"> Historial de Movimientos de Inventario</h3>
                    <div class="box-tools pull-right">
                        <div class="pull-left"></div>                        
                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div>
                </div><!-- /.box-header -->
                <div class="box-body">
                    <div class="table-responsive">
                        <table id="dtable" class="table table-bordered table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>Producto</th>
                                    <th>Referencia</th>
                                    <th>Movimiento</th>
                                    <th>Transportista</th>
                                    <th>Trailer / No Caja</th>
                                    <th>Linea</th>
                                    <th>Rampa</th>
                                    <th>Nombre de Chofer</th>
                                    <th>Sello</th>
                                    <th>Destino</th>
                                    <th>Usuario</th>
                                    <th>Fecha de Movimiento</th>
                                    <th>Comentarios</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${WarehouseHistory_Table}" var="row" varStatus="status">
                                    <tr>
                                        <td>${row.Product}</td>
                                        <td>${row.ReferenceNumber}</td>
                                        <td>${row.Movement}</td>
                                        <td>${row.Trucking}</td>
                                        <td>${row.Trailer}</td>
                                        <td>${row.Line}</td>
                                        <td>${row.Dock}</td>
                                        <td>${row.DriverName}</td>
                                        <td>${row.Seal}</td>
                                        <td>${row.Destination}</td>
                                        <td>${row.User}</td>
                                        <td>${row.InsertDate}</td>
                                        <td>${row.Comments}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table> 
                    </div>
                </div><!-- /.box-body -->
                <div class="box-footer"></div>
            </div>


            <script>
                $('#dtable').DataTable({
                    "paging": false,
                    "lengthChange": false,
                    "searching": true,
                    "ordering": false,
                    "info": false,
                    "autoWidth": false,
                    "dom": 'lfrtip',
                    "tableTools": {
                        "sSwfPath": "../plugins/datatables/extensions/TableTools/swf/copy_csv_xls_pdf.swf"
                    }
                });

            </script>


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
        </section><!-- /.content -->
    </div><!-- /.content-wrapper -->
</div>