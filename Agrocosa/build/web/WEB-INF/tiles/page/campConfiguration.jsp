<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script src="../plugins/select2/select2.js" type="text/javascript"></script>
<link href="../plugins/select2/select2.css" rel="stylesheet" type="text/css" />

<style>
    .select2{
        width:100%!important;
    }
</style>
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Campos
            <small>Configuracion de campos de cultivos</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-caret-right"></i> Campos</a></li>
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
        <form name="formsearch" role="form" action="campConfiguration.do" method="post">
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title">Buscar Campos</h3>
                </div><!-- /.box-header -->
                <div class="box-body">



                    <div class="col-sm-6">
                        <div class="form-group">
                            <label >Nombre del Campo</label>
                            <select name="CampName" id="CampName" class="form-control select2" data-title="Seleccione el campo" required >
                                <option value=""></option>
                                <c:forEach items="${Camp_HashMap}" var="item">
                                    <option value="${item.value}"  ${requestScope.SelectedCampName == item.value  ? 'selected' : ''}>${item.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>


                </div><!-- /.box-body -->
                <div class="box-footer">
                    <button type="submit" class="btn btn-primary"><i class="fa fa-search"></i> Buscar</button>
                    <input type="hidden" name="Action" value="SearchCamp"/>    
                </div>
            </div><!-- /.box -->
        </form>        

        <div class="box box-success">
            <div class="box-header with-border">
                <h3 class="box-title">Campos</h3>
                <div class="box-tools pull-right">
                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                </div>
            </div><!-- /.box-header -->
            <div class="box-body">
                <div class="table-responsive">
                    <table id="dtable" class="table table-bordered table-striped table-hover">
                        <thead>
                            <tr>    
                                <th>Nombre</th>
                                <th>Num. Secciones</th>
                                <th>Clima</th>
                                <th>Precipitacion</th>
                                <th>Configurar</th>
                            </tr>
                        </thead>
                        <tbody>

                            <c:forEach items="${Camp_Table}" var="row" varStatus="status">
                                <tr>                                 
                                    <td>${row.CampName}</td>
                                    <td>${row.SectionQty}</td>
                                    <td>${row.Weather}</td>
                                    <td>${row.Precipitation}</td>
                                    <td>
                                        <form name="editform" action="editCamp.do" method="post">
                                            <button type="submit" class="btn btn-primary xxs" title ="Edit">
                                                <i class="fa fa-edit"></i>
                                            </button>
                                            <input type="hidden" name="idCamp" value="${row.idCamp}"/>
                                        </form>
                                    </td>                                        
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table> 
                </div>

            </div><!-- /.box-body -->
        </div>


        <script>
            $(document).ready(function () {
                $("#CampName").select2();
                
                $('#dtable').DataTable({
                    "order": [[0, "desc"], [1, "asc"]],
                    "paging": true,
                    "lengthChange": false,
                    "searching": true,
                    "ordering": false,
                    "info": false,
                    "autoWidth": false,
                    "dom": 'T<"clear">lfrtip',
                    "tableTools": {
                        "sSwfPath": "../plugins/datatables/extensions/TableTools/swf/copy_csv_xls_pdf.swf"
                    }
                });

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
