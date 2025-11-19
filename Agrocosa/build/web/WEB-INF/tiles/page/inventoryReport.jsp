<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
    });


</script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Reporte de Inventario
            <small>Consulta de inventario por localidades y producto</small>
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
        <form name="formsearch" role="form" action="inventoryReport.do" method="post">
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title">Filtros de Busqueda</h3>
                </div><!-- /.box-header -->
                <div class="box-body">

                    <div class="col-md-2">
                        <div class="form-group">
                            <label >Producto</label>
                            <select name="productName" id="productName" class="form-control">
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
                            <select name="storageName" id="storageName" class="form-control">
                                <option value=""></option>
                                <c:forEach items="${Storage_HashMap}" var="item">
                                    <option value="${item.value}"  ${requestScope.SelectedStorageName == item.value  ? 'selected' : ''}>${item.value}</option>
                                </c:forEach>
                            </select> 
                        </div>                        
                    </div>
                    <div class="col-md-2">                        
                        <div class="form-group">
                            <label >Sublocalidad</label>
                            <select name="substorageName" id="substorageName" class="form-control">                                
                                <c:if test="${requestScope.SelectedSubstorageName ne ''}"> 
                                    <option value=""></option>
                                    <c:forEach items="${Substorage_HashMap}" var="item">
                                        <option value="${item.value}"  ${requestScope.SelectedSubstorageName == item.value  ? 'selected' : ''}>${item.value}</option>
                                    </c:forEach>
                                </c:if>
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
                <h3 class="box-title">Inventario</h3>
                <div class="box-tools pull-right">
                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                </div>
            </div><!-- /.box-header -->
            <div class="box-body">
                <div class="table-responsive">
                    <table id="dtable" class="table table-bordered table-striped table-hover">
                        <thead>
                            <tr>    
                                <th>Localidad</th>
                                <th>Sublocalidad</th>
                                <th>Producto</th>
                                <th>Tamaño</th>
                                <th>Color</th>
                                <th>Cantidad</th>
                            </tr>
                        </thead>
                        <tfoot>
                            <tr>                                 
                                <th colspan="5" style="text-align: right;">Total de Pallets</th>
                                <th>${requestScope.Total}</th>
                            </tr>
                        </tfoot>
                        <tbody>

                            <c:forEach items="${Inventory_Table}" var="row" varStatus="status">
                                <tr>                                 
                                    <td>${row.StorageName}</td>
                                    <td>${row.SubstorageName}</td>
                                    <td>${row.ProductName}</td>
                                    <td>${row.Size}</td>
                                    <td>${row.Color}</td>
                                    <td>${row.PalletQty}</td>
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
