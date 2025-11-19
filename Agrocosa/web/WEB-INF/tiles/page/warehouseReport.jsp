<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<div class="" >
    <div style="content-wrapper">
        <section class="content-header">
            <h1>
                Reporte de Inventario
                <small>Consulta de Inventario</small>
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
            <form name="formSearch" role="form" action="warehouseReport.do" method="post">
                <div class="box box-success">
                    <div class="box-header with-border">
                        <h3 class="box-title">Filtros de Busqueda</h3>
                    </div><!-- /.box-header -->
                    <div class="box-body">

                        <div class='row'>
                            <div class="col-md-2">
                                <div class="form-group">
                                    <label >Producto:</label>
                                    <input class="form-control" placeholder="product" type="text"  name="product" value="${requestScope.SelectedProduct}"/>
                                </div> 
                            </div>
                            <div class="col-md-2">
                                <div class="form-group">
                                    <label >Linea:</label>
                                    <input class="form-control" placeholder="line" type="text"  name="line" size ="5" value="${requestScope.SelectedLine}"/>
                                </div><!-- /.form-group -->
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


            <div class='row'>
                <div class="col-md-4">
                    <div class="box box-success">
                        <div class="box-header with-border">
                            <h3 class="box-title"> Inventario Total</h3>
                        </div><!-- /.box-header -->
                        <div class="box-body">

                            <c:set var="sumTotal" value="${0}"/> 

                            <div class="table-responsive">
                                <table id="dtable" class="table table-bordered table-striped table-hover">
                                    <thead>
                                        <tr>
                                            <th>Producto</th>
                                            <th>Linea</th>
                                            <th>Cantidad</th>
                                        </tr>
                                    </thead>                                    
                                    <tbody>
                                        <c:forEach items="${WarehouseReport_Table}" var="row" varStatus="status">
                                            <tr>
                                                <td>${row.Product}</td>
                                                <td>${row.Linea}</td>
                                                <td>${row.Qty}</td>
                                                <c:set var="sumTotal" value="${sumTotal + row.Qty}"/>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <th colspan="2" style="text-align: right;">Total</th>
                                            <th class="text-left"><fmt:formatNumber value="${sumTotal}" type="number" maxFractionDigits="0"/></th>                                    
                                        </tr>
                                    </tfoot>
                                </table> 
                            </div>
                        </div><!-- /.box-body -->
                    </div>
                </div>
                <div class="col-md-4"> 
                    <div class="box box-success">
                        <div class="box-header with-border">
                            <h3 class="box-title"> Inventario por Producto</h3>
                        </div><!-- /.box-header -->
                        <div class="box-body">

                            <c:set var="sumTotal" value="${0}"/> 

                            <div class="table-responsive">
                                <table id="dtable" class="table table-bordered table-striped table-hover">
                                    <thead>
                                        <tr>
                                            <th>Producto</th>
                                            <th>Cantidad</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${WarehouseProductReport_Table}" var="row" varStatus="status">
                                            <tr>
                                                <td>${row.Product}</td>
                                                <td>${row.Qty}</td>
                                                <c:set var="sumTotal" value="${sumTotal + row.Qty}"/>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <th style="text-align: right;">Total</th>
                                            <th class="text-left"><fmt:formatNumber value="${sumTotal}" type="number" maxFractionDigits="0"/></th>                                    
                                        </tr>
                                    </tfoot>
                                </table> 
                            </div>
                        </div><!-- /.box-body -->
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="box box-success">
                        <div class="box-header with-border">
                            <h3 class="box-title"> Inventario por Linea</h3>
                        </div><!-- /.box-header -->
                        <div class="box-body">

                            <c:set var="sumTotal" value="${0}"/>

                            <div class="table-responsive">
                                <table id="dtable" class="table table-bordered table-striped table-hover">
                                    <thead>
                                        <tr>
                                            <th>Linea</th>
                                            <th>Cantidad</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${WarehouseLineReport_Table}" var="row" varStatus="status">
                                            <tr>
                                                <td>${row.Linea}</td>
                                                <td>${row.Qty}</td>
                                                <c:set var="sumTotal" value="${sumTotal + row.Qty}"/>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <th style="text-align: right;">Total</th>
                                            <th class="text-left"><fmt:formatNumber value="${sumTotal}" type="number" maxFractionDigits="0"/></th>                                    
                                        </tr>
                                    </tfoot>
                                </table> 
                            </div>
                        </div><!-- /.box-body -->
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
</div>