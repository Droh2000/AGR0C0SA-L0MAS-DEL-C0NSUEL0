<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<link rel="stylesheet" href="../plugins/iCheck/all.css">

<script type="text/javascript">
    $(function () {
        $('.detailFormBtn').on("click", function () {
            var id = $(this).attr('id');
            id = id.replace("d", "");
            $.ajax({
                url: 'ajaxShippingOrderDetail.do',
                type: 'POST',
                data: jQuery.param({idShippingOrder: id}),
                success: function (result) {
                    $('#myModal').find('.modal-body').html(result);
                    $('#myModal').modal('show');
                }
            });
        });

        $('.truckBtn').on("click", function () {
            var id = $(this).attr('id');
            id = id.replace("t", "");
            $('#idShippingOrder4UpdateTuck').val(id);

            $('#truckBoxNumber').val("");
            $('#truckBoxPlate').val("");
            $('#truckPlate').val("");
            $('#truckDriverName').val("");
            $('#msgTruck').text("");

            $.ajax({
                url: 'ajaxGetShippingTruckInfo.do',
                type: 'POST',
                data: jQuery.param({idShippingOrder: id}),
                success: function (result) {
                    var params = result.split("|");
                    $('#truckBoxNumber').val(params[0]);
                    $('#truckBoxPlate').val(params[1]);
                    $('#truckPlate').val(params[2]);
                    $('#truckDriverName').val(params[3]);
                }
            });

            $('#modalTruck').modal('show');
        });

        $('.checklistBtn').on("click", function () {
            var id = $(this).attr('id');
            id = id.replace("r", "");
            $('#idShippingOrder4UpdateCheckList').val(id);


            $.ajax({
                url: 'ajaxGetShippingCheckListInfo.do',
                type: 'POST',
                data: jQuery.param({idShippingOrder: id}),
                success: function (result) {
                    $('#modalCheckList').find('.modal-body').html(result);
                    $('#modalCheckList').modal('show');
                }
            });



        });

        $('.scanBtn').on("click", function () {
            var id = $(this).attr('id');
            id = id.replace("s", "");
            $('#idShippingOrder4Update').val(id);
            $("#scanForm").submit();
        });


        $('#buttonSaveTruck').on("click", function () {
            var form = $('#truckForm');
            $.ajax({
                url: 'ajaxSaveShippingTruckInfo.do',
                type: 'POST',
                data: form.serialize(),
                success: function (result) {
                    $('#msgTruck').text(result);
                }
            });

        });


        $('#buttonSaveCheckList').on("click", function () {
            var form = $('#checkListForm');
            $.ajax({
                url: 'ajaxSaveShippingCheckListInfo.do',
                type: 'POST',
                data: form.serialize(),
                success: function (result) {
                    $('#msgCheckList').text(result);
                }
            });

        });


    });



</script>


<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Escanear Pallets
            <small>Escanear ordenes de embarque</small>
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

        <!-- Search Meeting -->
        <form name="formSearch" role="form" action="scanShippingOrder.do" method="post">
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
                                <label >Cliente:</label>
                                <select name="customer" id="customer" class="form-control" data-title="Cliente">
                                    <option value=""></option>
                                    <c:forEach items="${Customer_HashMap}" var="item">
                                        <option value="${item.value}"  ${requestScope.SelectedCustomer == item.value  ? 'selected' : ''}>${item.value}</option>
                                    </c:forEach>
                                </select>                                    
                            </div><!-- /.form-group -->
                        </div>
                        <input type="hidden" name="status" id="status" value="Nueva"/>        
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



        <c:if test="${requestScope.ShippingOrders_Table ne null}">

            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title"><i class="icon fa fa-file-o"></i> Ordenes de embarque para escanear</h3>
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
                                    <th>Consultar</th>
                                    <th>Numero de Orden</th>                                    
                                    <th>Cliente</th>
                                    <th>Pickup#</th>
                                    <th>Fecha de Orden</th>
                                    <th>Estatus</th>
                                    <th>Creada por</th>
                                    <th>Comentarios</th>                                    
                                    <th>Camión</th>                                    
                                    <th>Revisión</th>                                    
                                    <th>Escanear</th>                                    
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${ShippingOrders_Table}" var="row" varStatus="status">
                                    <tr>
                                        <td>
                                            <button type="button" name="d${row.idShippingOrder}" id="d${row.idShippingOrder}" class="btn btn-primary xxs detailFormBtn" title ="Consultar" onclick="">
                                                <i class="fa fa-search-plus"></i>
                                            </button>                                                                                          
                                        </td>
                                        <td>${row.idShippingOrder}</td>
                                        <td>${row.CustomerName}</td>
                                        <td>${row.PickupNumber}</td>
                                        <td>${row.ShippingDate}</td>
                                        <td>${row.Status}</td>
                                        <td>${row.UserName}</td>
                                        <td>${row.Comments}</td>  
                                        <td>
                                            <button type="button" name="t${row.idShippingOrder}" id="t${row.idShippingOrder}" class="btn btn-primary xxs truckBtn" title ="Camion" onclick="">
                                                <i class="fa fa-truck"></i>
                                            </button>                                                                                          
                                        </td>
                                        <td>
                                            <button type="button" name="r${row.idShippingOrder}" id="r${row.idShippingOrder}" class="btn btn-primary xxs checklistBtn" title ="Revision" onclick="">
                                                <i class="fa  fa-check-circle-o"></i>
                                            </button>                                                                                          
                                        </td>                                        
                                        <td>
                                            <button type="button" name="s${row.idShippingOrder}" id="s${row.idShippingOrder}" class="btn btn-primary xxs scanBtn" title ="Escanear" onclick="">
                                                <i class="fa fa-barcode"></i>
                                            </button>                                                                                          
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table> 
                    </div>
                </div><!-- /.box-body -->
                <div class="box-footer"></div>
            </div>
        </c:if>        


        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header alert-success">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel"><i class="icon fa fa-ship"></i> Orden de Embarque</h4>
                    </div>

                    <div class="modal-body"></div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" data-dismiss="modal">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>



        <div class="hide">            
            <form name="scanForm" id="scanForm" action="scanShippingOrderMaterial.do" method="post">
                <input type="hidden" name="idShippingOrder" id="idShippingOrder4Update" value=""/>
                <input type="hidden" name="customer" value="${requestScope.SelectedCustomer}"/>
                <input type="hidden" name="Action" value="Search"/>
            </form>                        
        </div>



        <div class="modal fade" id="modalTruck" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog" role="document">
                <form name="truckForm" id="truckForm" action="" method="post">
                    <div class="modal-content">
                        <div class="modal-header alert-success">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel"><i class="icon fa fa-truck"></i> Datos del Camion y Caja</h4>
                        </div>

                        <div class="modal-body">
                            <div class="form-group">
                                <label>No Caja</label>
                                <input name="truckBoxNumber" type="text" class="form-control pull-right" id="truckBoxNumber" required value=""/>
                            </div>  
                            <div class="form-group">
                                <label>Placa Caja</label>
                                <input name="truckBoxPlate" type="text" class="form-control pull-right" id="truckBoxPlate" required value=""/>
                            </div> 
                            <div class="form-group">
                                <label>Placa Tractor</label>
                                <input name="truckPlate" type="text" class="form-control pull-right" id="truckPlate" required value=""/>
                            </div> 
                            <div class="form-group">
                                <label>Nombre de Chofer</label>
                                <input name="truckDriverName" type="text" class="form-control pull-right" id="truckDriverName" required value=""/>
                            </div> 
                            <div class="form-group">
                                <label>&nbsp;&nbsp;</label>
                                <span id="msgTruck" class="pull-left" style="color: red;">&nbsp;&nbsp;</span>
                            </div>
                        </div>

                        <div class="modal-footer">                            
                            <input type="hidden" name="idShippingOrder" id="idShippingOrder4UpdateTuck" value=""/>
                            <button type="button" class="btn btn-primary pull-left" data-dismiss="modal">Cerrar</button>
                            <button type="button" class="btn btn-primary pull-right" id="buttonSaveTruck">Guardar</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>




        <div class="modal fade" id="modalCheckList" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog" role="document">
                <form name="checkListForm" id="checkListForm" action="" method="post">
                    <div class="modal-content">
                        <div class="modal-header alert-success">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel"><i class="icon fa fa-check-square-o"></i> Revisión de Caja</h4>
                        </div>

                        <div class="modal-body">

                        </div>

                        <div class="modal-footer">                        
                            <span id="msgCheckList" class="pull-left" style="color: red;"></span>
                            <input type="hidden" name="idShippingOrder" id="idShippingOrder4UpdateCheckList" value=""/>
                            <button type="button" class="btn btn-primary pull-left" data-dismiss="modal">Cerrar</button>
                            <button type="button" class="btn btn-primary pull-right" id="buttonSaveCheckList">Guardar</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>


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
