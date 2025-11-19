<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script src="../plugins/REDIPS_drag/redips-drag-min.min.js" type="text/javascript"></script>
<script src="../plugins/REDIPS_drag/redips-drag-source.js" type="text/javascript"></script>


<style>
    /* drag objects (DIV inside table cells) */
    .redips-drag {
        cursor: move;
        margin: auto;
        margin-bottom: 5px;
        background-color: white;
        text-align: center;
        font-size: 18pt; /* needed for cloned object */
        width: 100px;
        height: 35px;
        line-height: 35px;
        /* round corners */
        border-radius: 4px; /* Opera, Chrome */
        -moz-border-radius: 4px; /* FF */
    }

    .x-drag {
        cursor: move;
        margin: auto;
        margin-bottom: 5px;
        background-color: white;
        text-align: center;
        font-size: 18pt; /* needed for cloned object */
        width: 100px;
        height: 35px;
        line-height: 35px;
        /* round corners */
        border-radius: 4px; /* Opera, Chrome */
        -moz-border-radius: 4px; /* FF */
    }

    /* define main container (lime) */
    div#redips-drag {
        /*border: 1px solid lime;*/
        width: 100%;
        height: 100%;
        padding: 10px;
        display: flex;
    }
    /* table styles */
    div#redips-drag table {
        background-color: #fff;
        border-collapse: collapse;
        margin: 10px;
    }

    /* left table container (red) */
    #left {
        /*border: 1px solid red;*/
        width: 120px;
        height: 400px;
        /*float: left;*/
        margin-right: 13px;
    }

    /* right table container (blue) */
    #right {
        /*border: 1px solid SteelBlue;*/
        width: 100%;
        /*padding-left: auto;*/
        /*padding-right: 0px;*/
        /* align div to the left */
        /*margin-right: auto;*/
        /*overflow: hidden;*/
    }

    /* left table cells */
    #table1 td {
        border: 1px #AEB6BF solid;
        text-align: center;
        font-size: 10pt;
    }

    /* right table cells */
    #table2 td {
        border: 1px #AEB6BF solid;
        height: 50px;
        width: 400px;
        text-align: center;
        font-size: 10pt;
        padding: 2px;

    }

    /* green objects */
    .green {
        border: 2px solid #499B33;
    }

    .gray {
        border: 2px solid #499B33;
        background-color: #f7f7f7;
    }



    /* set height for right container TD (contains DIV elements) */
    .rightContainer {
        padding-top: 7px;
        vertical-align: top;
        height: 270px;
    }

    .redips-mark {
        color: black;
        background-color: #ecf0f5;
        font-weight: bold;
    }

    /* set styles for body (fonts etc) */
    /*    body {
            font-family: arial;
        }*/
</style>


<script type="text/javascript">

    $(function () {

        $('#saveButton').on("click", function () {
            var rd = REDIPS.drag;
            var tableContent = rd.saveContent('table2', 'json');
            
            $("#tableData").val(tableContent);
            $('#saveModal').modal('show');
        });
    });



</script>


<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Recibo de Embarque
            <small>Recibo de Embarques en el Crossdock</small>
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

        <div class="row">
            <div class="col-md-2">
                <!-- Search Meeting -->
                <form name="formsearch" role="form" action="receiptStockTransferOrder.do" method="post">
                    <div class="box box-success">
                        <div class="box-header with-border">
                            <h3 class="box-title">Buscar</h3>
                        </div><!-- /.box-header -->
                        <div class="box-body">
                            <div class="form-group">
                                <label >Proforma</label>
                                <input name="proforma" type="text" class="form-control pull-right" id="proforma" required value=""/>
                            </div>
                        </div><!-- /.box-body -->
                        <div class="box-footer">
                            <button type="submit" class="btn btn-primary"><i class="fa fa-search"></i> Buscar</button>
                            <input type="hidden" name="Action" value="Search"/> 
                        </div>
                    </div><!-- /.box -->
                </form>        
            </div>
            <div class="col-md-10">
                <div class="box box-success">
                    <div class="box-header with-border">

                        <h3 class="box-title">Embarque</h3>
                        <div class="box-tools pull-right">
                            <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                        </div>
                    </div>
                    <div class="box-body box-profile">
                        <div class="table-responsive">
                            <table id="dtable" class="table table-bordered table-striped table-hover">
                                <thead>
                                    <tr>                        
                                        <th>Embarque ID</th>
                                        <th>Transportista</th>                                            
                                        <th>Nombre de Chofer</th>                                            
                                        <th>Placas</th>
                                        <th>Proforma</th>
                                        <th>Estatus</th>
                                        <th>Fecha de Embarque</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${StockTransferOrderInfo_Table}" var="row" varStatus="status">
                                        <tr>
                                            <td>${row.idStockTransferOrder}</td>
                                            <td>${row.TransportCompany}</td>
                                            <td>${row.TruckDriverName}</td>
                                            <td>${row.TruckPlate}</td>
                                            <td>${row.Proforma}</td>
                                            <td>${row.Status}</td>
                                            <td>${row.ShipDate}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table> 
                        </div>
                    </div><!-- /.box-body -->
                    <div class="box-footer"></div>
                </div>
            </div>
        </div>

        <div class="box box-success">
            <div class="box-header with-border">
                <h3 class="box-title">Bodega</h3>
            </div>
            <div class="box-body box-profile">
                <div id="redips-drag" >



                    <!-- left container -->
                    <div id="left">
                        <!-- table1 -->
                        <table id="table1" class="table-responsive">
                            <colgroup>
                                <col width="200"/>
                            </colgroup>
                            <tbody>
                                <tr>
                                    <td class="rightContainer">
                                        <c:forEach items="${StockTransferOrderDetailInfo_Table}" var="row" varStatus="status">
                                            <div id="${row.idStockTransferOrder}|${row.ProductName}" class="redips-drag green">${row.ProductName}-${row.PalletQty}</span></div>
                                            </c:forEach>
                                    </td>
                                </tr>                                    
                            </tbody>
                        </table>
                    </div>


                    <!-- right container -->
                    <div id="right">
                        <!-- table2 -->
                        <table id="table2" class="table-responsive">
                            <colgroup>  
                                <c:forEach items="${Storage_Table}" var="row" varStatus="status">
                                    <col width="400"/>
                                </c:forEach>
                            </colgroup>
                            <tr>
                                <c:forEach items="${Storage_Table}" var="row" varStatus="status">
                                    <td class="redips-mark">${row.SubstorageName}</td>
                                </c:forEach>
                            </tr>
                            <tbody>
                                <tr>
                                    <c:forEach items="${Inventory_Table}" var="row" varStatus="status">
                                        <td>
                                            ${row.TableValue}
                                        </td>
                                    </c:forEach>
                                </tr>                                
                            </tbody>
                        </table>
                    </div>

                </div>



            </div><!-- /.box-body -->
            <div class="box-footer text-center">
                <button type="button" id="saveButton" class="btn btn-primary"><i class="fa fa-save"></i> Guardar</button>
                <input type="hidden" name="Action" value="Save"/> 
            </div>
        </div>

    </section><!-- /.content -->
</div><!-- /.content-wrapper -->

<script>
    // create container needed for methods below
    var redips = {};

// initialization
    redips.init = function () {
        // reference to the REDIPS.drag library
        var rd = REDIPS.drag;
        // initialization
        rd.init();
        // dragged elements can be placed to the empty cells only
        //rd.dropMode = 'single';
        // set hover color
        rd.hover.colorTd = '#9BB3DA';
        // when DIV element is double clicked return it to the left table
        /* rd.event.dblClicked = function () {
         var id = rd.obj.id, // set dblclicked DIV id
         pos = rd.getPosition(); // get element position
         // move element if source position is second (right) table
         if (pos[0] === 1) {
         // move DIV element to the left table
         rd.moveObject({
         id: id, // DIV element id
         target: [0, 1, 0] // target position (first table, second row, first cell)
         });
         }
         }; */
    }

// add onload event listener
    if (window.addEventListener) {
        window.addEventListener('load', redips.init, false);
    }
    else if (window.attachEvent) {
        window.attachEvent('onload', redips.init);
    }
</script>


<div class="modal fade" id="saveModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form name="formSave" action="receiptStockTransferOrder.do" method="post">
                <div class="modal-header alert-success">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel"><i class="icon fa fa-save"></i> Guardar Recibo de Producto</h4>
                </div>
                <div class="modal-body">
                    <p><h3>Esta usted seguro de recibir estos productos ?</h3></p>
                    <input type="hidden" name="tableData" id="tableData" value=""/>
                    <input type="hidden" name="Action" id="Action" value="Save"/>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary pull-left" data-dismiss="modal">Cancelar</button>
                    <button type="submit" id="btnConfirmYes" class="btn btn-primary pull-right">Guardar</button>
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
