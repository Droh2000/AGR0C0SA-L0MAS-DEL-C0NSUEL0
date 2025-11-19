<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- daterange picker -->
<link href="../plugins/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />

<script>

    $(function () {

        var id = 0;
        $('.detailFormBtn').on("click", function () {
            id = $(this).attr('id');
            
            $("#pickupConfirmed").val($("#pickupconfirmed" + id).text());
            $("#driverName").val($("#drivername" + id).text());
            $("#driverTel").val($("#drivertel" + id).text());
            $("#trailerNumberConfirmed").val($("#trailernumberconfirmed" + id).text());
            $("#trailerTypeConfirmed").val($("#trailertypeconfirmed" + id).text());
            $("#weightConfirmed").val($("#weightconfirmed" + id).text());
            $("#trucking").val($("#trucking" + id).text());
            $("#qtyinputConfirmed").val($("#qtyconfirmed" + id).text());
            
            $("#pickup").val($("#pickup" + id).text());
            $("#trailertype").val($("#trailertype" + id).text());
            $("#trailernumber").val($("#trailernumber" + id).text());
            $("#qtyinput").val($("#qty" + id).text());
            $("#weight").val($("#weight" + id).text());
            $("#uom").val($("#uom" + id).text());
            $("#dock").val($("#dock" + id).text());
            $("#destiny").val($("#destiny" + id).text());
            $("#comments").val($("#comments" + id).text());
            
            
            $("#idDelivery").val(id);


//            $(".trtablemodal").remove();
//            $.ajax({
//                url: 'ajaxRetriveDeliveryProduct.do',
//                type: 'POST',
//                data: jQuery.param({idDelivery: id}),
//                success: function (result) {
//                    $("#tbodymodal").append(result);
//                },
//                error: function () {
////                    $("#tbodymodal").remove();
//                }
//            });

            $('#myModal').modal('show');
        });


    });

</script>

<div class="" >
    <div style="content-wrapper">
        <section class="content-header">
            <h1>
                Edit Delivery
                <small></small>
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
            <form name="formSearch" role="form" action="deliveryEditReport.do" method="post">
                <div class="box box-success">
                    <div class="box-header with-border">
                        <h3 class="box-title">Search Filter</h3>
                        <div class="box-tools pull-right">
                            <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                        </div>                    
                    </div><!-- /.box-header -->
                    <div class="box-body">
                        <div class='row'>

                            <div class="col-md-2">
                                <div class="form-group">
                                    <label >Pickup</label>
                                    <input class="form-control" placeholder="pickup" type="text"  name="pickup" size ="5" value="${requestScope.SelectedPickup}"/>
                                </div> 
                            </div>

                        </div>

                    </div><!-- /.box-body -->
                    <div class="box-footer">
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary"><i class="fa fa-search"></i> Search</button>
                            <input type="hidden" name="Action" value="Search"/>
                        </div><!-- /.form-group -->                        
                    </div>
                </div><!-- /.box -->
            </form>


            <c:if test="${requestScope.DeliveryEditReport_Table ne null}">

                <div class="box box-success">
                    <div class="box-header with-border">
                        <h3 class="box-title"> Edit Delivery</h3>
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
                                        <th>Pickup</th>
                                        <th>Trailer Type</th>
                                        <th>Trailer Number</th>
                                        <th>Quantity</th>
                                        <th>Load</th>
                                        <th>Weight</th>
                                        <th>Measure Unit</th>
                                        <th>Registry Date</th>
                                        <th>Status</th>
                                        <th>Pickup Confirmed</th>
                                        <th>Trailer Confirmed</th>
                                        <th>Trailer Number Confirmed</th>
                                        <th>Weight Confirmed</th>
                                        <th>Qty Confirmed</th>
                                        <th>Trucking</th>
                                        <th>Dock</th>
                                        <th>Driver Name</th>
                                        <th>Driver Phone</th>
                                        <th>Comments</th>
                                        <th>Destiny</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${DeliveryEditReport_Table}" var="row" varStatus="status">
                                        <tr>
                                            <td id="pickup${row.idDelivery}">${row.PickupNumber}</td>
                                            <td id="trailertype${row.idDelivery}">${row.TrailerType}</td>
                                            <td id="trailernumber${row.idDelivery}">${row.TrailerNumber}</td>
                                            <td id="qty${row.idDelivery}">${row.Quantity}</td>
                                            <td>${row.Charge}</td>
                                            <td id="weight${row.idDelivery}">${row.Weight}</td>
                                            <td id="uom${row.idDelivery}">${row.UOM}</td>
                                            <td>${row.InsertDate}</td>
                                            <td>${row.Status}</td>
                                            <td id="pickupconfirmed${row.idDelivery}">${row.ConfirmPickup}</td>
                                            <td id="trailertypeconfirmed${row.idDelivery}">${row.ConfirmTrailer}</td>
                                            <td id="trailernumberconfirmed${row.idDelivery}">${row.ConfirmTrailerNumber}</td>
                                            <td id="weightconfirmed${row.idDelivery}">${row.ConfirmWeight}</td>
                                            <td id="qtyconfirmed${row.idDelivery}">${row.ConfirmQty}</td>
                                            <td id="trucking${row.idDelivery}">${row.Trucking}</td>
                                            <td id="dock${row.idDelivery}">${row.Dock}</td>
                                            <td id="drivername${row.idDelivery}">${row.DriverName}</td>
                                            <td id="drivertel${row.idDelivery}">${row.DriverTel}</td>
                                            <td id="comments${row.idDelivery}">${row.Comments}</td>
                                            <td id="destiny${row.idDelivery}">${row.Destination}</td>
                                            <td>
                                                <button type="button" name="${row.idDelivery}" id="${row.idDelivery}" class="btn btn-primary xxs detailFormBtn" title ="Modify" onclick="">
                                                    <i class="fa fa-truck"></i>
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
                        <form name="formClose" action="deliveryEditReport.do" method="post">
                            <div class="modal-header alert-success">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title" id="myModalLabel"> Modify Delivery</h4>
                            </div>

                            <div class="modal-body">
                                <input type="hidden" name="Action" id="Action" value="ModifyDelivery"/>
                                <input type="hidden" name="idDelivery" id="idDelivery" value=""/>

                                <div class="form-group">
                                    <label>Pickup :</label>
                                    <input name="pickup" type="text" class="form-control pull-right" id="pickup" value="" />
                                </div>
                                
                                <div class="form-group">
                                    <label>Trailer Type :</label>
                                    <select name="trailertype" id="trailertype" class="form-control pull-right">
                                        <option></option>
                                        <option>Flat bed</option>
                                        <option>Reefer</option>
                                        <option>Dryvan</option>
                                        <option>Low boy</option>
                                        <option>Step deck</option>
                                    </select>
                                </div>
                                
                                <div class="form-group">
                                    <label>Trailer Number :</label>
                                    <input name="trailernumber" type="text" class="form-control pull-right" id="trailernumber" value="" />
                                </div>
                                
                                <div class="form-group">
                                    <label>Quantity :</label>
                                    <input name="qtyinput" type="text" class="form-control pull-right" id="qtyinput" value="" />
                                </div>
                                
                                <div class="form-group">
                                    <label>Weight :</label>
                                    <input name="weight" type="text" class="form-control pull-right" id="weight" value="" />
                                </div>
                                
                                <div class="form-group">
                                    <label>Measure Unit :</label>
                                    <input name="uom" type="text" class="form-control pull-right" id="uom" value="" />
                                </div>
                                
                                <div class="form-group">
                                    <label>Driver Name: </label>
                                    <input name="driverName" type="text" class="form-control pull-right" id="driverName" value="" />
                                </div>

                                <div class="form-group">
                                    <label>Driver Phone: </label>
                                    <input name="driverTel" type="text" class="form-control pull-right" id="driverTel" value="" />
                                </div>
                                
                                <div class="form-group">
                                    <label>Pickup Confirmed : </label>
                                    <input name="pickupConfirmed" type="text" class="form-control pull-right" id="pickupConfirmed" value="" />
                                </div>

                                <div class="form-group">
                                    <label>Trailer Number Confirmed :</label>
                                    <input name="trailerNumberConfirmed" type="text" class="form-control pull-right" id="trailerNumberConfirmed" value="" />
                                </div>

                                <div class="form-group">
                                    <label>Trailer Type Confirmed : </label>
                                    <!--<input name="trailerType" type="text" class="form-control pull-right" id="trailerType" required value="" />-->
                                    <select name="trailerTypeConfirmed" id="trailerTypeConfirmed" class="form-control pull-right">
                                        <option></option>
                                        <option>Flat bed</option>
                                        <option>Reefer</option>
                                        <option>Dryvan</option>
                                        <option>Low boy</option>
                                        <option>Step deck</option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Weight Confirmed : </label>
                                    <input name="weightConfirmed" type="text" class="form-control pull-right" id="weightConfirmed" value="" />
                                </div>

                                <div class="form-group">
                                    <label>Quantity Confirmed : </label>
                                    <input name="qtyinputConfirmed" type="text" class="form-control pull-right" id="qtyinputConfirmed" value="" />
                                </div>    
                                
                                <div class="form-group">
                                    <label>Trucking : </label>
                                    <input name="trucking" type="text" class="form-control pull-right" id="trucking" value="" />
                                </div>
                                
                                <div class="form-group">
                                    <label>Dock : </label>
                                    <input name="dock" type="text" class="form-control pull-right" id="dock" value="" />
                                </div>
                                
                                <div class="form-group">
                                    <label>Destiny : </label>
                                    <input name="destiny" type="text" class="form-control pull-right" id="destiny" value="" />
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