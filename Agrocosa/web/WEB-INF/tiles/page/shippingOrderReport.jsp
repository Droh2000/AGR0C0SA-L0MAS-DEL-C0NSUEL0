<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<script type="text/javascript">
    $(function () {
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
             Reporte de Embarques al Cliente
            <small>Consulta de ordenes de embarque</small>
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
        <form name="formSearch" role="form" action="shippingOrderReport.do" method="post">
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
                                <label >Num de Orden:</label>
                                <input class="form-control" placeholder="Num Orden" type="text" data-title="No puede estar vacio" name="idShippingOrder" size ="5" value="${requestScope.SelectedIdShippingOrder}"/>
                            </div><!-- /.form-group -->
                        </div>
                        <div class="col-md-2">
                            <div class="form-group">
                                <label >Pickup#:</label>
                                <input class="form-control" placeholder="Pickup#" type="text" data-title="No puede estar vacio" name="pickupNumber" size ="5" value="${requestScope.SelectedPickupNumber}"/>
                            </div><!-- /.form-group -->
                        </div>
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
                        <div class="col-md-2">
                            <div class="form-group">
                                <label >Estatus:</label>
                                <select name="status" id="status" class="form-control" data-title="Estatus">
                                    <option value=""></option>
                                    <option value="Nueva"  ${requestScope.SelectedStatus == 'Nueva'  ? 'selected' : ''}>Nueva</option>
                                    <option value="Embarcada"  ${requestScope.SelectedStatus == 'Embarcada'  ? 'selected' : ''}>Embarcada</option>
                                    <option value="Cancelada"  ${requestScope.SelectedStatus == 'Cancelada'  ? 'selected' : ''}>Cancelada</option>
                                </select>                                    
                            </div><!-- /.form-group -->
                        </div>

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
                    <h3 class="box-title"> Ordenes de embarque</h3>
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
                                    <th>Pickup#</th>                                    
                                    <th>Cliente</th>
                                    <th>Fecha de Orden</th>
                                    <th>Estatus</th>
                                    <th>Creada por</th>
                                    <th>Comentarios</th>                                    
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
                                        <td>${row.PickupNumber}</td>
                                        <td>${row.CustomerName}</td>                                        
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
                <div class="box-footer"></div>
            </div>
        </c:if>        


        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header alert-success">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel"> Orden de Embarque al Cliente</h4>
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
