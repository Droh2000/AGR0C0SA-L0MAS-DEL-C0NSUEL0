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
//            alert(id);
            $("#proformamodal").val($("#proforma" + id).text());
            $("#factura").val($("#factura" + id).text());
            $("#entry").val($("#entry" + id).text());
            $("#scaccode").val($("#scaccode" + id).text());
            $("#trucking").val($("#trucking" + id).text());
            $("#trailer").val($("#trailer" + id).text());
            $("#sello").val($("#sello" + id).text());
            $("#gate").val($("#gate" + id).text());
            $("#drivername").val($("#drivername" + id).text());
            $("#comments").val($("#comments" + id).text());
            $("#today").val($("#today" + id).text());
            $("#idShipping").val(id);

            $('#myModal').modal('show');
        });


    });

</script>

<div class="" >
    <div style="content-wrapper">
        <section class="content-header">
            <h1>
                Edit Shipments
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
            <form name="formSearch" role="form" action="shippingEditReport.do" method="post">
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
                                    <label >Proforma :</label>
                                    <input class="form-control" placeholder="proforma" type="text"  name="proforma" size ="5" value="${requestScope.SelectedProforma}"/>
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


            <c:if test="${requestScope.ShippingEditReport_Table ne null}">

                <div class="box box-success">
                    <div class="box-header with-border">
                        <h3 class="box-title"> Edit Shipments</h3>
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
                                        <th>Proforma</th>
                                        <th>Invoice</th>
                                        <th>Entry</th>
                                        <th>ScacCode</th>
                                        <th>Trucking</th>
                                        <th>Trailer</th>
                                        <th>Seal</th>
                                        <th>Concept</th>
                                        <th>Dock</th>
                                        <th>Line</th>
                                        <th>Driver Name</th>
                                        <th>Comments</th>
                                        <th>Status</th>
                                        <th>Insert Date</th>
                                        <th>Today</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${ShippingEditReport_Table}" var="row" varStatus="status">
                                        <tr>
                                            <td id="proforma${row.idShipping}">${row.Proforma}</td>
                                            <td id="factura${row.idShipping}">${row.Factura}</td>
                                            <td id="entry${row.idShipping}">${row.Entry}</td>
                                            <td id="scaccode${row.idShipping}">${row.ScacCode}</td>
                                            <td id="trucking${row.idShipping}">${row.Trucking}</td>
                                            <td id="trailer${row.idShipping}">${row.Trailer}</td>
                                            <td id="sello${row.idShipping}">${row.Seal}</td>
                                            <td>${row.Carga}</td>
                                            <td id="gate${row.idShipping}">${row.Gate}</td>
                                            <td>${row.Line}</td>
                                            <td id="drivername${row.idShipping}">${row.DriverName}</td>
                                            <td id="comments${row.idShipping}">${row.Comments}</td>
                                            <td>${row.Status}</td>
                                            <td>${row.InsertDate}</td>
                                            <td id="today${row.idShipping}">${row.TodayCol}</td>
                                            <td>
                                                <button type="button" name="${row.idShipping}" id="${row.idShipping}" class="btn btn-primary xxs detailFormBtn" title ="Modify" onclick="">
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
                        <form name="formClose" action="shippingEditReport.do" method="post">
                            <div class="modal-header alert-success">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title" id="myModalLabel"> Modify Shipping</h4>
                            </div>

                            <div class="modal-body">
                                <input type="hidden" name="Action" id="Action" value="ModifyShipping"/>
                                <input type="text" name="idShipping" id="idShipping" value=""/>

                                <div class="form-group">
                                    <label>Proforma :</label>
                                    <input name="proformamodal" type="text" class="form-control pull-right" id="proformamodal" value="" />
                                </div>

                                <div class="form-group">
                                    <label>Invoice :</label>
                                    <input name="factura" type="text" class="form-control pull-right" id="factura" value="" />
                                </div>

                                <div class="form-group">
                                    <label>Entry :</label>
                                    <input name="entry" type="text" class="form-control pull-right" id="entry" value="" />
                                </div>

                                <div class="form-group">
                                    <label>Scac Code :</label>
                                    <input name="scaccode" type="text" class="form-control pull-right" id="scaccode" value="" />
                                </div>

                                <div class="form-group">
                                    <label>Trukcing :</label>
                                    <input name="trucking" type="text" class="form-control pull-right" id="trucking" value="" />
                                </div>

                                <div class="form-group">
                                    <label>Trailer : </label>
                                    <input name="trailer" type="text" class="form-control pull-right" id="trailer" value="" />
                                </div>

                                <div class="form-group">
                                    <label>Seal : </label>
                                    <input name="sello" type="text" class="form-control pull-right" id="sello" value="" />
                                </div>

                                <div class="form-group">
                                    <label>Dock : </label>
                                    <input name="gate" type="text" class="form-control pull-right" id="gate" value="" />
                                </div>

                                <div class="form-group">
                                    <label>Driver Name :</label>
                                    <input name="drivername" type="text" class="form-control pull-right" id="drivername" value="" />
                                </div>

                                <div class="form-group">
                                    <label>Today : </label>
                                    <input name="today" type="text" class="form-control pull-right" id="today" value="" />
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