<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- daterange picker -->
<link href="../plugins/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
    $(function () {

        $("#btnBack").click(function () {
            $("#formBack").submit();
        });

        $(".chk").click(function () {
            var id = $(this).attr('id');
            if ($(this).is(":checked")) {
                $("#lote" + id).prop('required', true);
                $("#caducity" + id).prop('required', true);
                $("#total" + id).prop('required', true);
            } else {
                $("#lote" + id).prop('required', false);
                $("#caducity" + id).prop('required', false);
                $("#total" + id).prop('required', false);
            }
        });

    });
</script>
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Riego
            <small>Por favor seleccione un registro para captura de producto</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-caret-right"></i> Riego</a></li>
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
            <div class="col-md-12">
                <div class="box box-success">
                    <div class="box-header">
                        <h3 class="box-title">Riego</h3>
                    </div>
                    <div class="box-body">
                        <div class="box-body box-profile">
                            <h3 class="profile-username text-center">${requestScope.TaskType}</h3>

                            <ul class="list-group list-group-unbordered">
                                <li class="list-group-item">
                                    <b>Num de Tarea</b> <a class="pull-right">${requestScope.idTask}</a>
                                </li>
                                <li class="list-group-item">
                                    <b>Campo</b> <a class="pull-right">${requestScope.CampName}</a>
                                </li>
                                <li class="list-group-item">
                                    <b>Secciones</b> <a class="pull-right">${requestScope.SectionName}</a>
                                </li>
                                <li class="list-group-item">
                                    <b>Formula</b> <a class="pull-right">${requestScope.LaborTypeName}</a>
                                </li>
                                <li class="list-group-item">
                                    <b>Fecha de Tarea</b> <a class="pull-right">${requestScope.TaskDate}</a>
                                </li>
                                <li class="list-group-item">
                                    <b>Supervisor</b> <a class="pull-right">${requestScope.SupervisorName}</a>
                                </li>
                                <li class="list-group-item">
                                    <b>Bomba Primaria</b> <a class="pull-right">${requestScope.BombPrimary}</a>
                                </li>
                                <li class="list-group-item">
                                    <b>Bomba Secundaria</b> <a class="pull-right">${requestScope.BombSecondary}</a>
                                </li>
                            </ul>
                        </div><!-- /.box-body -->
                        <div class="form-group">
                            <label>Comentarios</label>
                            <textarea name="commentsTask" class="form-control" rows="5" id="commentsTask" readonly>${requestScope.Comments}</textarea>
                        </div>
                    </div><!-- /.box-body -->
                    <div class="box-footer">
                        <button type="button" id="btnBack" class="btn btn-primary pull-left"><i class="fa fa-backward"></i> Regresar</button>
                    </div>
                </div><!-- /.box -->



                <form name="saveForm" id="saveForm"  method="POST" action="irrigationInfo.do">

                    <div class="box box-success">
                        <div class="box-header">
                            <h3 class="box-title">Formulas</h3>
                        </div>
                        <div class="box-body">

                            <div class="table-responsive">
                                <table id="dtable" class="table table-bordered table-striped table-hover">
                                    <thead>
                                        <tr>    
                                            <th>Formula</th>
                                            <th>Total Aplicado</th>
                                        </tr>
                                    </thead>
                                    <tbody>

                                        <c:forEach items="${Formula_Table}" var="row" varStatus="status">
                                            <tr>                                 
                                                <td>${row.LaborTypeName}</td>
                                                <td>
                                                    <input name="totalFormula${row.idTaskLabor}" type="text" class="form-control pull-right"  required id="totalFormula${row.idTaskLabor}" value="${row.Total}"/>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table> 
                            </div>
                        </div><!-- /.box-body -->
                        <div class="box-footer"></div>
                    </div><!-- /.box -->

                    <div class="box box-success">
                        <div class="box-header">
                            <h3 class="box-title">Datos del Producto</h3>
                        </div>
                        <div class="box-body">

                            <div class="table-responsive">
                                <table id="dtable" class="table table-bordered table-striped table-hover">
                                    <thead>
                                        <tr>    
                                            <th>Seleccion</th>
                                            <th>Nombre Comercial</th>
                                            <th>Marca</th>
                                            <th>Ingrediente Activo</th>                                            
                                            <th>Dosis Minima</th>
                                            <th>Dosis Maxima</th>
                                            <th>Intervalo de Seguridad</th>
                                            <th>Periodo de Reentrada</th>
                                            <th>UOM</th>
                                            <th>Lote</th>
                                            <th>Caducidad</th>
                                            <th>Total Aplicado</th>
                                        </tr>
                                    </thead>
                                    <tbody>

                                        <c:forEach items="${AgrochemicalProduct_Table}" var="row" varStatus="status">
                                            <tr>                                 
                                                <td><input type="checkbox" class="chk" name="checkbox" id="${row.idAgrochemical}" ${row.Flag == '1' ? 'checked' : ''}/></td>
                                                <td>${row.AgrochemicalName}</td>
                                                <td>${row.Brand}</td>
                                                <td>${row.ActiveIngredient}</td>
                                                <td>${row.MinDose}</td>
                                                <td>${row.MaxDose}</td>
                                                <td>${row.SecurityInterval}</td>
                                                <td>${row.DelayPeriod}</td>
                                                <td>${row.UOM}</td>

                                                <td>
                                                    <input name="lote${row.idAgrochemical}" type="text" class="form-control pull-right"  id="lote${row.idAgrochemical}" value="${row.Lote}" />
                                                </td>
                                                <td>
                                                    <div class="input-group">
                                                        <div class="input-group-addon">
                                                            <i class="fa fa-calendar"></i>
                                                        </div>
                                                        <input name="caducity${row.idAgrochemical}" type="text" class="form-control input-sm mydate" id="caducity${row.idAgrochemical}"  value="${row.Caducity}" size="15" />
                                                    </div>
                                                </td>
                                                <td>
                                                    <input name="total${row.idAgrochemical}" type="text" class="form-control pull-right"  id="total${row.idAgrochemical}" value="${row.Total}"/>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table> 
                            </div>

                        </div><!-- /.box-body -->
                        <div class="box-footer"></div>
                    </div><!-- /.box -->

                    <div class="box box-success">
                        <div class="box-header">
                            <h3 class="box-title">Otros</h3>
                        </div>
                        <div class="box-body">                            
                            <div class="form-group">
                                <label>Comentarios</label>
                                <textarea name="comments" class="form-control" rows="5" id="comment">${requestScope.SelectedComments}</textarea>
                            </div>
                        </div><!-- /.box-body -->
                        <div class="box-footer">
                            <button type="submit" class="btn btn-primary pull-left" id="btnSave" ><i class="fa fa-save"></i> Guardar</button>
                            <input type="hidden" name="idTask" id="idTask" value="${requestScope.idTask}"/>
                            <input type="hidden" name="Action" id="Action"  value="saveTask"/>
                        </div>
                    </div><!-- /.box -->  

                </form> 
            </div>
        </div>

        <div class="modal fade" id="errorModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header alert-danger">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel"><i class="icon fa fa-times"></i> Productos</h4>
                    </div>
                    <div class="modal-body">
                        <p><h3>No ha elegido ningun producto, por favor seleccione los productos aplicados.</h3></p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary pull-right" data-dismiss="modal">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="hide">
            <form name="formBack" id="formBack" action="fillIrrigationInfo.do" method="post">

            </form>                        
        </div>

        <!-- date-range-picker -->
        <script src="../plugins/daterangepicker/moment.min.js" type="text/javascript"></script>
        <script src="../plugins/daterangepicker/daterangepicker.js" type="text/javascript"></script>

        <script type="text/javascript">
    $('#taskDate').daterangepicker({
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
    $('.mydate').daterangepicker({
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
