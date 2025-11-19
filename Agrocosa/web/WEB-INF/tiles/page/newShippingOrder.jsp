<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script src="../plugins/select2/select2.js" type="text/javascript"></script>
<link href="../plugins/select2/select2.css" rel="stylesheet" type="text/css" />

<script>
    $(document).ready(function () {
        var t = $('#dtable').DataTable();
        var idRow = -1;
        var counter = 1;
        var customer;
        var material;
        var qty;

        $('#formMaterial').submit(function (e) {
            e.preventDefault();
            customer = $("#customer").val();
            material = $("#productName").val();
            qty = $("#quantity").val();

            t.row.add([
                customer,
                material,
                qty,
                'x'
            ]).draw(false);
            $('#productName').val(null).trigger('change');
            $("#quantity").val("");

            counter++;
        });


        $('#dtable tbody').on('click', 'button', function () {
            idRow = t.row($(this).parents('tr')).index();
            $('#confirmModal').modal('show');
        });


        $('#btnConfirmYes').on("click", function () {
            t.row(idRow).remove().draw();
            counter--;
            $('#confirmModal').modal('hide');
        });


        $('#btnViewOrders').on("click", function () {
            $("#formViewAllData").submit();
        });


        $('#btnSaveOrders').on("click", function () {
            var data = t.rows().data();
            if (data.length > 0) {
                var data4Save = convertTableToArrayObject();
                $("#dataTable4Save").val(JSON.stringify(data4Save));

                $("#comments4Save").val($("#comments").val());
                $("#customer4Save").val($("#customer").val());
                $("#pickupNumber4Save").val($("#pickupNumber").val());

                $("#formSaveData").submit();
            }
        });

    });

    function convertTableToArrayObject() {
        var dataObjects = [];
        var table = $('#dtable').DataTable();
        var data = table.rows().data();

        for (var i = 0; i < data.length; i++) {
            dataObjects.push(data[i]);
        }
        return dataObjects;
    }

    $.ajaxSetup({
        cache: false
    });

    $(function () {

        $("#customer").change(function () {
            $('#productName').val(null).trigger('change');
            $("#quantity").val("");
        });


    });





</script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Orden de Embarque
            <small>Crear nueva orden de embarque</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-caret-right"></i> Bodega</a></li>
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


        <div class="box box-success">
            <div class="box-header with-border">
                <h3 class="box-title">Orden de Embarque</h3>
                <div class="box-tools pull-right">
                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                </div>
            </div><!-- /.box-header -->
            <div class="box-body">
                <form name="formMaterial" id="formMaterial" action="" method="post">
                    <div class="row">
                        <div class="col-md-2">
                            <div class="form-group">
                                <label >Cliente:</label>
                                <select name="customer" id="customer" class="form-control" data-title="Cliente" required>
                                    <option value=""></option>
                                    <c:forEach items="${Customer_HashMap}" var="item">
                                        <option value="${item.value}"  ${requestScope.SelectedCustomer == item.value  ? 'selected' : ''}>${item.value}</option>
                                    </c:forEach>
                                </select> 
                            </div><!-- /.form-group -->
                        </div>
                        <div class="col-md-2">
                            <div class="form-group">
                                <label >Producto:</label>
                                <select name="productName" id="productName" class="form-control">
                                    <option value=""></option>
                                    <c:forEach items="${PalletProduct_HashMap}" var="item">
                                        <option value="${item.value}"  ${requestScope.SelectedProductName == item.value  ? 'selected' : ''}>${item.value}</option>
                                    </c:forEach>
                                </select>
                            </div><!-- /.form-group -->
                        </div>
                        <div class="col-md-2">
                            <div class="form-group">
                                <label >Cantidad:</label>
                                <input type="text" name="quantity" id="quantity" class="form-control numeric" placeholder="Cantidad" size="5" data-title="Este campo es Requerido" required/> 
                            </div><!-- /.form-group -->                        
                        </div>
                        <div class="col-md-2">
                            <div class="form-group">
                                <label style="color:white">-</label><br>
                                <button type="submit" class="btn btn-primary" id="addButton"><i class="fa fa-plus"></i> Agregar</button>
                            </div><!-- /.form-group -->                        
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-2">
                            <div class="form-group">
                                <label >Pickup #:</label>
                                <input type="text" name="pickupNumber" id="pickupNumber" class="form-control" placeholder="Pickup #" size="5" data-title="Este campo es Requerido" required/> 
                            </div><!-- /.form-group -->      
                        </div>
                    </div>    
                    <div class="row">
                        <div class="col-lg-10">
                            <div class="form-group">
                                <label for="comment">Comentarios:</label>
                                <textarea name="comments" class="form-control" rows="5" id="comments"></textarea>
                            </div>
                        </div>
                    </div>
                </form>
            </div><!-- /.box-body -->
            <div class="box-footer"></div>
        </div><!-- /.box -->

        <div class="box box-success">
            <div class="box-header with-border">
                <h3 class="box-title">Materiales</h3>
                <div class="box-tools pull-right">
                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    <button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
                </div>
            </div><!-- /.box-header -->
            <div class="box-body">
                <div class="table-responsive">

                    <table id="dtable" class="table table-bordered table-striped table-hover">
                        <thead>
                            <tr>                       
                                <th>Cliente</th>
                                <th>Producto</th>
                                <th>Cantidad</th>
                                <th>Borrar</th>
                            </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table> 
                </div>
            </div><!-- /.box-body -->
            <div class="box-footer clearfix">
                <button type="button" class="btn btn-primary pull-left"  id="btnViewOrders" ><i class="fa fa-search-plus"></i> Ver ordenes nuevas</button>
                <button type="button" class="btn btn-primary pull-right" id="btnSaveOrders" ><i class="fa fa-save"></i> Guardar</button>
            </div><!-- /.box-footer -->
        </div><!-- /.box -->

        <div class="modal fade" id="confirmModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header alert-warning">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel"><i class="icon fa fa-trash"></i> Borrar Material</h4>
                    </div>
                    <div class="modal-body">
                        <h3>Esta usted seguro de borrar este registro?</h3>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary pull-left" data-dismiss="modal">No</button>
                        <button type="button" id="btnConfirmYes" class="btn btn-primary pull-right">Si</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="hide">            
            <form name="formSaveData" id="formSaveData" action="newShippingOrder.do" method="post">
                <input type="hidden" name="dataTable4Save" id="dataTable4Save" value=""/>
                <input type="hidden" name="comments" id="comments4Save" value=""/>
                <input type="hidden" name="customer" id="customer4Save" value=""/>
                <input type="hidden" name="pickupNumber" id="pickupNumber4Save" value=""/>
                <input type="hidden" name="Action" id="Action"  value="SaveTableData"/>
            </form>                        
        </div>

        <div class="hide">            
            <form name="formViewAllData" id="formViewAllData" action="viewNewShippingOrders.do" method="post">
                <input type="hidden" name="Action" id="Action" value="viewAllShippingOrder"/>
            </form>                        
        </div>



        <script>
            (function ($) {
                $(".select2").select2();
            })(jQuery);

            $('#dtable').DataTable({
                "paging": false,
                "lengthChange": false,
                "searching": false,
                "ordering": false,
                "info": false,
                "autoWidth": false,
                "dom": 'T<"clear">lfrtip',
                "tableTools": {
                    "sSwfPath": "../plugins/datatables/extensions/TableTools/swf/copy_csv_xls_pdf.swf"
                },
                "columnDefs": [
                    {
                        "targets": 0,
                        "visible": true
                    },
                    {
                        "targets": 1,
                        "visible": true
                    },
                    {
                        "targets": 2,
                        "visible": true
                    },
                    {
                        "targets": 3,
                        "data": null,
                        "defaultContent": "<button class='btn btn-primary xxs'><i class='fa fa-trash'></i></button>"
                    }]
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
