<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->

<script type="text/javascript">
    $(function () {

        var selectedId = -1;


        $("#btnBack").click(function () {
            $("#formBack").submit();
        });

        $.ajaxSetup({
            cache: false
        });


        $('.scanBtn').on("click", function () {
            var id = $(this).attr('id');
            selectedId = id;
            $('#pn').val("");
            $('#qty').val("");
            $('#qtyInventory').val("");


            $.ajax({
                url: 'ajaxGetProductNameAndQuantity.do',
                type: 'POST',
                data: jQuery.param({idShippingOrderDetail: id}),
                success: function (result) {
                    var params = result.split("|");
                    $('#pn').val(params[0]);
                    $('#qty').val(params[1]);
                    $('#qtyInventory').val(params[2]);
                }
            });


            $.ajax({
                url: 'ajaxInventoryByProductName.do',
                type: 'POST',
                data: jQuery.param({idShippingOrderDetail: id}),
                success: function (result) {
                    $('#myModal').find('.modal-body').html(result);
                    $('#myModal').modal('show');
                }
            });
        });

        $("#buttonClass").click(function () {
            getValueUsingClass();
            $('#myModal').modal('hide');
        });


        $("#scanButton").click(function () {
            $('#palletData').val('');
            $('#idShippingOrder').val($("#selectedId").text());
            $('#scanModal').modal('show');
        });

        $("#buttonSaveScan").click(function () {
            $('#formSaveScan').submit();
        });



        function getValueUsingClass() {
            var chkArray = [];

            $(".chk:checked").each(function () {
                chkArray.push($(this).val());
            });

            var selected;
            selected = chkArray.join(',');

            if (selected.length > 0) {
                $('#ta' + selectedId).val(selected);
            }
        }



    });

</script>
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Escanear Pallets
            <small>Escanear Pallets para embarcar</small>
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


        <c:if test="${requestScope.ShippingOrders_Table ne null}">

            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title"> Ordenes de Embarque</h3>
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
                                    <th>Numero de Orden</th>                                    
                                    <th>Cliente</th>
                                    <th>Pickup#</th>
                                    <th>Fecha de Orden</th>
                                    <th>Estatus</th>
                                    <th>Creada por</th>
                                    <th>Comentarios</th>                                    
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${ShippingOrders_Table}" var="row" varStatus="status">
                                    <tr>
                                        <td><span id="selectedId">${row.idShippingOrder}</span></td>
                                        <td>${row.CustomerName}</td>
                                        <td>${row.PickupNumber}</td>
                                        <td>${row.ShippingDate}</td>
                                        <td>${row.Status}</td>
                                        <td>${row.UserName}</td>
                                        <td>${row.Comments}</td>                                        
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table> 
                    </div>
                </div><!-- /.box-body -->
                <div class="box-footer">
                    <button type="button" id="btnBack" class="btn btn-primary pull-left"><i class="fa fa-backward"></i> Regresar</button>
                </div>
            </div>
        </c:if>   


        <c:if test="${requestScope.ShippingOrderDetail_Table ne null}">
            <form name="formSeach" role="form" id="formSeach" action="scanShippingOrder.do" method="post">
                <div class="box box-success">
                    <div class="box-header with-border">
                        <h3 class="box-title"> Productos</h3>
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
                                        <th>Inventario</th>
                                        <th>Producto</th>                                    
                                        <th>Color</th>
                                        <th>Tamaño</th>
                                        <th>Cantidad</th>
                                        <!--<th>Pallets</th>-->
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${ShippingOrderDetail_Table}" var="row" varStatus="status">
                                        <tr>
                                            <td>
                                                <button type="button" name="b${row.idShippingOrderDetail}" id="${row.idShippingOrderDetail}" class="btn btn-primary xxs scanBtn" title ="inventario" >
                                                    <i class="fa fa-search-plus"></i>
                                                </button>                                                
                                            </td>
                                            <td>${row.ProductName}</td>
                                            <td>${row.Color}</td>
                                            <td>${row.Size}</td>
                                            <td>${row.Quantity}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table> 
                        </div>
                    </div><!-- /.box-body -->
                    <div class="box-footer">
                        <c:if test="${requestScope.Saved ne 'Yes'}">
                            <button type="button" id="scanButton" class="btn btn-primary scan pull-right"><i class="fa fa-barcode"></i> Escanear</button>
                        </c:if>                        
                    </div>
                </div>
            </form>
        </c:if> 


        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header alert-success">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel"> Inventario</h4>
                    </div>

                    <div style="padding-left: 50px;">
                        <div class="col-md-4">
                            <div class="form-group">
                                <label >Producto:</label>
                                <input name="pn" type="text" class="form-control" id="pn" value="" readonly/>
                            </div><!-- /.form-group -->
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label >Cantidad Requerida:</label>
                                <input name="qty" type="text" class="form-control" id="qty" value="" readonly/>
                            </div><!-- /.form-group -->
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label >Total Inventario:</label>
                                <input name="qtyInventory" type="text" class="form-control" id="qtyInventory" value="" readonly/>
                            </div><!-- /.form-group -->
                        </div>
                    </div>                              

                    <div class="modal-body"></div>

                    <div class="modal-footer">
                        <span id="msg" class="pull-left" style="color: red;"></span>
                        <button type="button" class="btn btn-primary pull-left" data-dismiss="modal">Cerrar</button>
                        <button type="button" class="btn btn-primary pull-right" id="buttonClass">Aceptar</button>
                    </div>
                </div>
            </div>
        </div>



        <div class="hide">
            <form name="formBack" id="formBack" action="scanShippingOrder.do" method="post">
                <input type="hidden" name="Action" value="Search"/>
                <input type="hidden" name="customer" value="${requestScope.SelectedCustomer}"/>
                <input type="hidden" name="status" id="status" value="Nueva"/>
            </form>                        
        </div>



        <div class="modal fade" id="scanModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <form name="formSaveScan" id="formSaveScan" action="scanShippingOrderMaterial.do" method="post">
                        <div class="modal-header alert-success">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel"> Escanear Pallets</h4>
                        </div>
                        <div class="modal-body">

                            <label >Escanear Pallet ID</label>
                            <div class="input-group">
                                <div style="max-height: 600px; overflow-y: auto;">
                                    <textarea rows="50" cols="30" name="palletData" id="palletData" required>${requestScope.PalletData}</textarea>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <span id="msg" class="pull-left" style="color: red;"></span>
                            <button type="button" class="btn btn-primary pull-left" data-dismiss="modal">Cerrar</button>
                            <button type="button" class="btn btn-primary pull-right" id="buttonSaveScan">Guardar</button>
                            <input type="hidden" name="Action" value="Save"/>
                            <input type="hidden" name="idShippingOrder" id="idShippingOrder" value="Save"/>
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

    </section><!-- /.content -->
</div><!-- /.content-wrapper -->
