<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script src="../plugins/select2/select2.js" type="text/javascript"></script>
<link href="../plugins/select2/select2.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
    $(function () {
        var id;
        $('.detailFormBtn').on("click", function () {
            id = $(this).attr('id');
            $("#selectedId").val(id);

            $("#pickup").val($("#p" + id).text());
            $("#pickuporiginal").val($("#p" + id).text());
            $("#trailerType").val($("#t" + id).text());
            $("#trailerNumber").val($("#tn" + id).text());
            $("#weight").val($("#w" + id).text());
            $("#qtyinput").val($("#qty" + id).text());
            $("#driverName").val($("#dn" + id).text());
            $("#driverTel").val($("#dt" + id).text());
            $("#trucking").val($("#tr" + id).text());
            $("#registrar").val($("#st" + id).text());
            $("#dock").val($("#dock" + id).text());
            $("#line").val($("#line" + id).text());
            $("#producto").val($("#ch" + id).text());
            $("#destino").val($("#destino" + id).text());

            $(".trtablemodal").remove();


            $.ajax({
                url: 'ajaxRetriveDeliveryProduct.do',
                type: 'POST',
                data: jQuery.param({idDelivery: id}),
                success: function (result) {
                    $("#tbodymodal").append(result);
                },
                error: function () {
//                    $("#tbodymodal").remove();
                }
            });

            $('#myModal').modal('show');
        });


//        $('#btnConfirmYes').on("click", function () {
//            alert("enviar datos");
//        });


        $("#qtyinput").focusout(function () {
            if ($(this).val() != $("#qty" + id).text()) {
                if (!confirm("Are you sure to update the Quantity Number?")) {
                    $(this).val($("#qty" + id).text());
                    $(this).css({"background": "white"});
                    $(this).css({"color": "black"});
                } else {
                    $(this).css({"background": "red"});
                    $(this).css({"color": "white"});
                }
            } else {
                $(this).css({"background": "white"});
                $(this).css({"color": "black"});
            }


        })

        $("#proformainput").change(function () {
            if ($("#proformainput").val().length > 0) {
                $.ajax({
                    url: 'ajaxConceptByProform.do',
                    type: 'POST',
                    data: jQuery.param({proformainput: $("#proformainput").val()}),
                    success: function (result) {
                        $("#productinput").html(result);
                        $("#line").html("");
                    },
                    error: function () {
                        $("#productinput").val("error");
                    }
                });
            } else {
                $("#productinput").html("");
                $("#line").html("");
            }
        });

        $("#productinput").change(function () {
            if ($("#productinput").val().length > 0) {
                $.ajax({
                    url: 'ajaxLineByProduct.do',
                    type: 'POST',
                    data: jQuery.param({productinput: $("#productinput").val(), proformainput: $("#proformainput").val()}),
                    success: function (result) {
                        $("#line").html(result);
                    },
                    error: function () {
                        $("#line").val("error");
                    }
                });
            } else {
                $("#line").html("");
            }
        });

        $(".agregarproducto").click(function () {
            var idDelivery = $("#selectedId").val();
            var proforma = $("#proformainput").val();
            var linea = $("#line").val();
            var producto = $("#productinput").val();
            var qty = $("#qtypalletsinput").val();

            if (proforma == "") {
                alert("You must select Proforma");
                return;
            }
            if (producto == "") {
                alert("You must select a Product");
                return;
            }
            if (linea == "") {
                alert("You must select a Line");
                return;
            }
            if (qty == "") {
                alert("You must write Quantity of Pallets");
                return;
            }

            $.ajax({
                url: 'ajaxSaveDeliveryProduct.do',
                type: 'POST',
                data: jQuery.param({proforma: proforma, producto: producto, linea: linea, qty: qty, idDelivery: idDelivery}),
                success: function (result) {
                    if(result==0){
                        alert("Quantity not Available");
                        return;
                    }
                    
//                    $("#line").html(result);
                    $("#tbodymodal").append("<tr id='tr" + result + "' class='trtablemodal'><td><a onclick='BorrarProducto(" + result + ")' id='DeleteRow" + idDelivery + "' class='deleteRowClass'>Delete</a></td><td>" + proforma + "</td><td>" + producto + "</td><td>" + linea + "</td><td>" + qty + "</td></tr>");
                    $("#proformainput").val("");
                    $("#line").val("");
                    $("#productinput").val("");
                    $("#qtypalletsinput").val("");
                },
                error: function () {
//                    $("#line").val("error");
                }
            });
//            alert("Guardar datos.");
        });


    });


    function BorrarProducto(idProducto) {

//        USA EL MISMO SERVLET PARA AGREGAR PERO DIFERENTE TRANSACCION
        $.ajax({
            url: 'ajaxSaveDeliveryProduct.do',
            type: 'POST',
            data: jQuery.param({idDeliveryProduct: idProducto}),
            success: function (result) {
                $("#tr" + idProducto).remove();
            },
            error: function () {
                
            }
        });
    }



</script>
<div class="" >
    <div style="content-wrapper">
        <section class="content-header">
            <h1>
                Deliveries
                <small>Product Delivery</small>
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




            <c:if test="${requestScope.DeliveryReport_Table ne null}">

                <div class="box box-success">
                    <div class="box-header with-border">
                        <h3 class="box-title"> Entregas</h3>
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
                                        <th>Deliver</th>
                                        <th>Number</th>  
                                        <th>Pickup</th>                                    
                                        <!--<th>Pickup Confirm</th>-->                                    
                                        <th>Customer</th>                                    
                                        <th>Trailer Type</th>                                    
                                        <!--<th>Trailer Type Confirm</th>-->                                    
                                        <th>Trailer Number</th>                                    
                                        <!--<th>Trailer Number Confirm</th>-->                                    
                                        <th>Quantity</th>
                                        <!--<th>Quantity Confirm</th>-->
                                        <th>Concept</th>
                                        <th>Weight</th>
                                        <!--<th>Weight Confirm</th>-->
                                        <th>Measure</th>                                    
                                        <th>Status</th>                                    
                                        <th>Confirm Concept</th>                                    
                                        <th>Confirmation Data</th>                                  
                                        <th>Driver Name</th>                                    
                                        <th>Driver Phone</th>                                    
                                        <th>Trucking</th>                                    
                                        <th>Dock</th>                                    
                                        <th>Line</th>                                    
                                        <th>Commentss</th>                                    
                                        <th>Destiny</th>                                    
                                        <th>Insert Date</th>                                    
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${DeliveryReport_Table}" var="row" varStatus="status">
                                        <tr>
                                            <td>
                                                <button type="button" name="${row.idDelivery}" id="${row.idDelivery}" class="btn btn-primary xxs detailFormBtn" title ="Entregar" onclick="">
                                                    <i class="fa fa-truck"></i>
                                                </button>
                                            </td>
                                            <td>${row.idDelivery}</td>
                                            <td id="p${row.idDelivery}">${row.PickupNumber}</td>
                                            <!--<td id="pc${row.idDelivery}">${row.ConfirmPickup}</td>-->
                                            <td>${row.Customer}</td>
                                            <td id="t${row.idDelivery}">${row.TrailerType}</td>
                                            <!--<td id="tc${row.idDelivery}">${row.ConfirmTrailer}</td>-->
                                            <td id="tn${row.idDelivery}">${row.TrailerNumber}</td>
                                            <!--<td id="tnc${row.idDelivery}">${row.ConfirmTrailerNumber}</td>-->
                                            <td id="qty${row.idDelivery}">${row.Quantity}</td>
                                            <!--<td id="qtyc${row.idDelivery}">${row.ConfirmQty}</td>-->
                                            <td id="ch${row.idDelivery}">${row.Charge}</td>
                                            <td id="w${row.idDelivery}">${row.Weight}</td>
                                            <!--<td id="wc${row.idDelivery}">${row.ConfirmWeight}</td>-->
                                            <td>${row.UOM}</td>
                                            <td id="st${row.idDelivery}">${row.Status}</td>
                                            <td>${row.Productos}</td>
                                            <td>PickUp :${row.ConfirmPickup}<br />Trailer Type :${row.ConfirmTrailer}<br />Trailer Number :${row.ConfirmTrailerNumber}<br />Weight :${row.ConfirmWeight}<br />Quantity :${row.ConfirmQty}</td>
                                            <td id="dn${row.idDelivery}">${row.DriverName}</td>
                                            <td id="dt${row.idDelivery}">${row.DriverTel}</td>
                                            <td id="tr${row.idDelivery}">${row.Trucking}</td>
                                            <td id="dock${row.idDelivery}">${row.Dock}</td>
                                            <td id="line${row.idDelivery}">${row.Line}</td>
                                            <td>${row.Comments}</td>
                                            <td id="destino${row.idDelivery}">${row.Destination}</td>
                                            <td>${row.InsertDate}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table> 
                        </div>
                    </div><!-- /.box-body -->
                    <div class="box-footer"></div>
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
                    "dom": 'lfrtip',
                    "tableTools": {
                        "sSwfPath": "../plugins/datatables/extensions/TableTools/swf/copy_csv_xls_pdf.swf"
                    }
                });

            </script>

            <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
                <div class="modal-dialog modal-lg" role="document">
                    <div class="modal-content">
                        <form name="formClose" action="deliveryReport.do" method="post">
                            <div class="modal-header alert-success">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title" id="myModalLabel"> Product Delivery</h4>
                            </div>

                            <div class="modal-body">
                                <input type="hidden" name="Action" id="Action" value="CloseDelivery"/>

                                <div class="form-group">
                                    <label>Pickup: </label>
                                    <input name="pickup" type="text" class="form-control pull-right" id="pickup" required value="" />
                                </div>

                                <input name="pickuporiginal" type="hidden" class="form-control pull-right" id="pickuporiginal" value="" />
                                <input name="producto" type="hidden" class="form-control pull-right" id="producto" value="" />
                                <input name="destino" type="hidden" class="form-control pull-right" id="destino" value="" />

                                <div class="form-group">
                                    <label>Number: </label>
                                    <input name="idDelivery" type="text" class="form-control pull-right" id="selectedId" value="" readonly/>
                                </div>
                                <div class="form-group">
                                    <label>Action:  </label>
                                    <select name="registrar" id="registrar" class="form-control pull-right" required>
                                        <option>Arrived</option>
                                        <option>Loading</option>
                                        <option>Loaded</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>Driver Name: </label>
                                    <input name="driverName" type="text" class="form-control pull-right" id="driverName" required value="" />
                                </div>

                                <div class="form-group">
                                    <label>Driver Phone: </label>
                                    <input name="driverTel" type="text" class="form-control pull-right" id="driverTel" required value="" />
                                </div>

                                <div class="form-group">
                                    <label>Trailer Number </label>
                                    <input name="trailerNumber" type="text" class="form-control pull-right" id="trailerNumber" required value="" />
                                </div>

                                <div class="form-group">
                                    <label>Trailer Type: </label>
                                    <!--<input name="trailerType" type="text" class="form-control pull-right" id="trailerType" required value="" />-->
                                    <select name="trailerType" id="trailerType" class="form-control pull-right" required>
                                        <option></option>
                                        <option>Flat bed</option>
                                        <option>Reefer</option>
                                        <option>Dryvan</option>
                                        <option>Low boy</option>
                                        <option>Step deck</option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Weight: </label>
                                    <input name="weight" type="text" class="form-control pull-right" id="weight" required value="" />
                                </div>

                                <div class="form-group">
                                    <label>Trucking: </label>
                                    <input name="trucking" type="text" class="form-control pull-right" id="trucking" required value="" />
                                </div>

                                <div class="form-group">
                                    <label>Dock: </label>
                                    <select name="dock" type="text" class="form-control pull-right" id="dock">
                                        <option></option>
                                        <option>1</option>
                                        <option>2</option>
                                        <option>3</option>
                                        <option>4</option>
                                        <option>5</option>
                                        <option>6</option>
                                        <option>7</option>
                                        <option>8</option>
                                        <option>9</option>
                                        <option>10</option>
                                        <option>11</option>
                                        <option>12</option>
                                    </select>
                                    <!--<input name="dock" type="text" class="form-control pull-right" id="dock" value="" />-->
                                </div>

                                <div class="form-group">
                                    <label>Proforma : </label>
                                    <select name="proformainput" id="proformainput" class="form-control" data-title="Proforma">
                                        <option value=""></option>
                                        <c:forEach items="${Proforma_HashMap}" var="item">
                                            <option value="${item.value}">${item.value}</option>
                                        </c:forEach>
                                    </select> 
                                </div>

                                <div class="form-group">
                                    <label>Product : </label>
                                    <!--<input name="productinput" type="text" class="form-control pull-right" id="productinput" required value="" />-->
                                    <select name="productinput" type="text" class="form-control pull-right" id="productinput">
                                        <option></option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Line: </label>
                                    <!--<input name="line" type="text" class="form-control pull-right" id="line" value="" />-->
                                    <select name="line" type="text" class="form-control pull-right" id="line">
                                        <option></option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Qty Pallets : </label>
                                    <input name="qtypalletsinput" type="text" class="form-control pull-right" id="qtypalletsinput" value="" />
                                </div>

                                <div class="form-group">
                                    <label>Quantity : </label>
                                    <input name="qtyinput" type="text" class="form-control pull-right" id="qtyinput" required value="" />
                                </div>

                                <button style="width:100%;" type="button" class="btn btn-primary agregarproducto">Add Product</button>

                                <div class="form-group">
                                    <table class="table table-responsive" id="tableproducts">
                                        <thead>
                                            <tr>
                                                <th></th>
                                                <th>Proforma</th>
                                                <th>Product</th>
                                                <th>Line</th>
                                                <th>Quantity</th>
                                            </tr>
                                        </thead>
                                        <tbody id="tbodymodal">
                                        </tbody>
                                    </table>
                                </div>

                                <div class="form-group">
                                    <label>Comments </label>
                                    <textarea name="comments" class="form-control" rows="5" id="comments"></textarea>
                                </div>

                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary pull-left" data-dismiss="modal">Close</button>
                                <button type="submit" id="btnConfirmYes" class="btn btn-primary pull-right">Save</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <script>
                (function ($) {
                    $(".select2").select2();
                })(jQuery);
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