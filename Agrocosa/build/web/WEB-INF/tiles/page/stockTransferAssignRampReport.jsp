<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="">
    <div style="content-wrapper">
        <section class="content-header">
            <h1>
                Asignación de la rampa correspondiente al transportista
                <small>Manténgase atento a su asignación para saber dónde recibir la carga</small>
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
                    
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title">Lista de transportistas</h3>
                    <div class="box-tools pull-right">
                        <div class="pull-left"></div>                        
                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div>
                </div><!-- /.box-header -->
                <div class="box-body">
                    <c:if test="${not empty StockTransferAssignRamp_Table}">
                        <div class="table-responsive">
                            <table id="dtable" class="table table-bordered table-striped table-hover">
                                <thead>
                                    <tr>    
                                        <th>Id de Embarque</th>
                                        <th>Empresa Transportista</th>
                                        <th>Nombre del chofer</th>
                                        <th>No. Caja</th>
                                        <th>Placa Del Camion</th>
                                        <th>Rampa Asignada</th>
                                        <th>Puede Pasar</th>
                                        <th>Fecha</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${StockTransferAssignRamp_Table}" var="row" varStatus="status">
                                        <tr>                                 
                                            <td>${row.idStockTransferOrder}</td>
                                            <td>${row.TransportCompany}</td>
                                            <td>${row.TruckDriverName}</td>
                                            <td>${row.TruckBoxNumber}</td>
                                            <td>${row.TruckPlate}</td>
                                            <td>${row.Ramp}</td>
                                            <td>${row.CantPass}</td>
                                            <td>${row.InsertDate}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table> 
                        </div>
                    </c:if>
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