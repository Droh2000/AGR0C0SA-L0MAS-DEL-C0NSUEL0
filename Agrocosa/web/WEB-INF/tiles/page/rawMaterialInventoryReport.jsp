<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script>
    $(document).ready(function () {
        
        $("#rawMaterialTypeName").change(function () {
            if ($("#rawMaterialTypeName").val().length > 0) {
                var v = $("#rawMaterialTypeName").val();
                var param = 'rawMaterialTypeName=' + v;
                
                $.ajax({
                    url: 'ajaxRawMaterial.do',
                    type: 'POST',
                    data: param,
                    success: function (result) {
                        $('#rawMaterialName').children().remove();
                        $("#rawMaterialName").append(result);
                    }
                });
            } else {
                $('#rawMaterialName').children().remove();
            }
        });
    });
</script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Reporte de Inventario de Insumos
            <small>Consulta de inventario de Insumos por localidades</small>
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
        <form name="formsearch" role="form" action="rawMaterialInventoryReport.do" method="post">
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title">Filtros de Busqueda</h3>
                </div><!-- /.box-header -->
                <div class="box-body">
                    <div class="col-md-2">
                        <div class="form-group">
                            <label>Tipo de Insumo</label>
                            <select name="rawMaterialTypeName" id="rawMaterialTypeName" class="form-control">
                                <option value=""></option>
                                <c:forEach items="${RawMaterialType_HashMap}" var="item">
                                    <option value="${item.value}"  ${requestScope.SelectedRawMaterialTypeName == item.value  ? 'selected' : ''}>${item.value}</option>
                                </c:forEach>
                            </select>                              
                        </div>
                    </div>
                    <div class="col-md-2">
                        <div class="form-group">
                            <label>Insumo</label>
                            <select name="rawMaterialName" id="rawMaterialName" class="form-control">
                                <c:if test="${requestScope.SelectedRawMaterialName ne ''}"> 
                                    <option value=""></option>
                                    <c:forEach items="${RawMaterialName_HashMap}" var="item">
                                        <option value="${item.value}"  ${requestScope.SelectedRawMaterialName == item.value  ? 'selected' : ''}>${item.value}</option>
                                    </c:forEach>
                                </c:if>
                            </select>                              
                        </div>
                    </div>
                    <div class="col-md-2">
                        <div class="form-group">
                            <label >Localidad</label>
                            <select name="storageName" id="storageName" class="form-control">
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
                <h3 class="box-title">Inventario de Insumos</h3>
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
                                <th>Tipo de Insumo</th>
                                <th>Insumo</th>
                                <th>Cantidad</th>
                                <th>Unidad de Medida</th>
                            </tr>
                        </thead>
                        <tbody>

                            <c:forEach items="${RawMaterialInventory_Table}" var="row" varStatus="status">
                                <tr>                                 
                                    <td>${row.StorageName}</td>
                                    <td>${row.SubstorageName}</td>
                                    <td>${row.RawMaterialTypeName}</td>
                                    <td>${row.RawMaterialName}</td>
                                    <td>${row.Qty}</td>
                                    <td>${row.UOM}</td>
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
