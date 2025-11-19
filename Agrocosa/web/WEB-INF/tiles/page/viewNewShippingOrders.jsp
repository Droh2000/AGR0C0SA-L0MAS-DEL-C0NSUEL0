<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<script type="text/javascript">
    $(function () {
        $("#btnBack").click(function () {
            $("#formBack").submit();
        });


        $('.confirmFormBtn').on("click", function () {
            var id = $(this).attr('id');
            $("#idShippingOrder4Delete").val(id);
            $('#confirmModal').modal('show');
        });

        $('.detailFormBtn').on("click", function () {
            var id = $(this).attr('id');
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

    });

</script>


<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Ordenes de Embarque
            <small>Consulta de ordenes de embarque nuevas</small>
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
                <h3 class="box-title"> Ordenes nuevas de embarque</h3>
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
                                <th>Fecha de Orden</th>
                                <th>Estatus</th>
                                <th>Creada por</th>
                                <th>Comentarios</th>                                    
                                <th>Cancelar</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${ShippingOrders_Table}" var="row" varStatus="status">
                                <tr>
                                    <td>
                                        <button type="button" name="${row.idShippingOrder}" id="${row.idShippingOrder}" class="btn btn-primary xxs detailFormBtn" title ="Consultar" onclick="">
                                            <i class="fa fa-search-plus"></i>
                                        </button>                                                                                          
                                    </td>
                                    <td>${row.idShippingOrder}</td>
                                    <td>${row.CustomerName}</td>
                                    <td>${row.ShippingDate}</td>
                                    <td>${row.Status}</td>
                                    <td>${row.UserName}</td>
                                    <td>${row.Comments}</td>                                        
                                    <td>
                                        <button type="button" name="${row.idShippingOrder}" id="${row.idShippingOrder}" class="btn btn-primary xxs confirmFormBtn" title ="Borrar" onclick="">
                                            <i class="fa fa-trash"></i>
                                        </button>                                                
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table> 
                </div>
            </div><!-- /.box-body -->
            <div class="box-footer">
                <c:if test="${requestScope.ShowBackButton eq 'YES'}">
                    <button type="button" id="btnBack" class="btn btn-primary pull-left"><i class="fa fa-backward"></i> Regresar</button>
                </c:if>
            </div>
        </div>


        <div class="hide">
            <form name="formBack" id="formBack" action="newShippingOrder.do" method="post"></form>                        
        </div>

        <div class="modal fade" id="confirmModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <form name="formConfirm" action="viewNewShippingOrders.do" method="post">
                        <div class="modal-header alert-warning">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel"><i class="icon fa fa-trash"></i> Cancelar Orden de Embarque</h4>
                        </div>
                        <div class="modal-body">
                            <p><h3>Esta usted seguro de cancelar esta orden de embarque?</h3></p>
                            <div class="form-group">
                                <label for="comment">Comentarios:</label>
                                <textarea name="comments" class="form-control" rows="5" id="comments"></textarea>
                            </div>
                            <input type="hidden" name="idShippingOrder" id="idShippingOrder4Delete" value=""/>
                            <input type="hidden" name="Action" id="Action" value="CancelShippingOrder"/>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary pull-left" data-dismiss="modal">No</button>
                            <button type="submit" id="btnConfirmYes" class="btn btn-primary pull-right">Si</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>



        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header alert-success">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel"> Orden de Embarque</h4>
                    </div>

                    <div class="modal-body"></div>

                    <div class="modal-footer">
                        <span id="msg" class="pull-left" style="color: red;"></span>
                        <button type="button" class="btn btn-primary" data-dismiss="modal">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>


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
