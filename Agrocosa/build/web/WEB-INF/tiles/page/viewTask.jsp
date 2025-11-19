<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- daterange picker -->
<link href="../plugins/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
    $(function () {

        $("#btnBack").click(function () {
            history.back(-1);
        });

    });

</script>
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Aplicacion Foliar
            <small>Por favor seleccione un registro para captura de producto</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-caret-right"></i> Aplicación Foliar</a></li>
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

            <div class="col-md-6">

                <div class="box box-success">
                    <div class="box-header">
                        <h3 class="box-title">Aplicación Foliar</h3>
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
                            <textarea name="comments" class="form-control" rows="5" id="comments" readonly>${requestScope.Comments}</textarea>
                        </div>
                    </div><!-- /.box-body -->
                    <div class="box-footer">
                        <button type="button" id="btnBack" class="btn btn-primary pull-left"><i class="fa fa-backward"></i> Regresar</button>
                    </div>
                </div><!-- /.box -->

                <div class="box box-success">
                    <div class="box-header with-border">
                        <h3 class="box-title">Comentarios</h3>
                        <div class="box-tools pull-right">
                            <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                        </div>
                    </div><!-- /.box-header -->
                    <div class="box-body">
                        <div class="table-responsive">
                            <table id="dtable" class="table table-bordered table-striped table-hover">
                                <thead>
                                    <tr>    
                                        <th>Comentario</th>
                                        <th>Registrado Por</th>
                                        <th>Fecha de Registro</th>
                                </thead>
                                <tbody>

                                    <c:forEach items="${Comments_Table}" var="row" varStatus="status">
                                        <tr>                                 
                                            <td>${row.Comments}</td>
                                            <td>${row.UserName}</td>
                                            <td>${row.fInsertDate}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table> 
                        </div>

                    </div><!-- /.box-body -->
                </div>
            </div>


            <div class="col-md-6">
                <c:if test="${requestScope.TaskProduct eq 'Yes'}">
                    <div class="box box-success">
                        <div class="box-header">
                            <h3 class="box-title">Datos Generales</h3>
                        </div>
                        <div class="box-body">
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label>Lote</label>                                                
                                    <div class="input-group">
                                        <input name="lot" type="text" class="form-control pull-right" id="lot" disabled value="${requestScope.SelectedLot}" />
                                    </div>                                  
                                </div>
                                <div class="form-group">
                                    <label>Hora Inicio</label>                                                
                                    <div class="input-group">
                                        <input name="startTime" type="text" class="form-control pull-right" id="startTime" disabled value="${requestScope.SelectedStartTime}" />
                                    </div>                                  
                                </div>
                                <div class="form-group">
                                    <label>&nbsp;</label>                                                
                                    <div class="input-group">
                                        &nbsp;
                                    </div>                                  
                                </div>                            
                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label>Valvula</label>                                                
                                    <div class="input-group">
                                        <input name="valve" type="text" class="form-control pull-right" id="valve" disabled value="${requestScope.SelectedValve}" />
                                    </div>                                  
                                </div>
                                <div class="form-group">
                                    <label>Hora Fin</label>                                                
                                    <div class="input-group">
                                        <input name="endTime" type="text" class="form-control pull-right" id="endTime" disabled value="${requestScope.SelectedEndTime}" />
                                    </div>                                  
                                </div>
                                <div class="form-group">
                                    <label>&nbsp;</label>                                                
                                    <div class="input-group">
                                        &nbsp;
                                    </div>                                  
                                </div>

                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label>Fecha</label>
                                    <div class="input-group">
                                        <div class="input-group-addon">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                        <input type="text" name="taskDate" class="form-control" id="taskDate" disabled value="${requestScope.SelectedTaskDate}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label>&nbsp;</label>                                                
                                    <div class="input-group">
                                        &nbsp;
                                    </div>                                  
                                </div>
                                <div class="form-group">
                                    <label>&nbsp;</label>                                                
                                    <div class="input-group">
                                        &nbsp;
                                    </div>                                  
                                </div>
                            </div>
                        </div><!-- /.box-body -->
                    </div><!-- /.box -->

                    <div class="box box-success">
                        <div class="box-header">
                            <h3 class="box-title">Datos del Producto</h3>
                        </div>
                        <div class="box-body">
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label>Nombre Comercial</label>                                                
                                    <div class="input-group">
                                        <input name="comercialName" type="text" class="form-control pull-right" id="comercialName" disabled value="${requestScope.SelectedComercialName}" />
                                    </div>                                  
                                </div>
                                <div class="form-group">
                                    <label>Caducidad</label>                                                
                                    <div class="input-group">
                                        <input name="caducity" type="text" class="form-control pull-right" id="caducity" disabled value="${requestScope.SelectedCaducity}" />
                                    </div>                                  
                                </div>
                                <div class="form-group">
                                    <label>Unidad</label>                                                
                                    <div class="input-group">
                                        <select name="uom" id="uom" class="form-control" data-title="Unida de Medida" disabled>
                                            <option value=""></option>
                                            <c:forEach items="${UOM_HashMap}" var="item">
                                                <option value="${item.value}"  ${requestScope.SelectedUOM == item.value  ? 'selected' : ''}>${item.value}</option>
                                            </c:forEach>
                                        </select>      
                                    </div>                                  
                                </div>
                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label>Ingrediente Activo</label>                                                
                                    <div class="input-group">
                                        <input name="ingredient" type="text" class="form-control pull-right" id="ingredient" disabled value="${requestScope.SelectedIngredient}" />
                                    </div>                                  
                                </div>                                
                                <div class="form-group">
                                    <label>Dosis</label>                                                
                                    <div class="input-group">
                                        <input name="dose" type="text" class="form-control pull-right" id="dose" disabled value="${requestScope.SelectedDose}" />
                                    </div>                                  
                                </div>                            
                                <div class="form-group">
                                    <label>&nbsp;</label>                                                
                                    <div class="input-group">
                                        &nbsp;
                                    </div>                                  
                                </div>
                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label>Lote</label>                                                
                                    <div class="input-group">
                                        <input name="lote" type="text" class="form-control pull-right" id="lote" disabled value="${requestScope.SelectedLote}" />
                                    </div>                                  
                                </div>
                                <div class="form-group">
                                    <label>Total Aplicado</label>                                                
                                    <div class="input-group">
                                        <input name="total" type="text" class="form-control pull-right" id="total" disabled value="${requestScope.SelectedTotal}" />
                                    </div>                                  
                                </div>
                                <div class="form-group">
                                    <label>&nbsp;</label>                                                
                                    <div class="input-group">
                                        &nbsp;
                                    </div>                                  
                                </div>
                            </div>
                        </div><!-- /.box-body -->
                    </div><!-- /.box -->

                    <div class="box box-success">
                        <div class="box-header">
                            <h3 class="box-title">Tiempos</h3>
                        </div>
                        <div class="box-body">
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label>Intervalo de Seguridad</label>                                                
                                    <div class="input-group">
                                        <input name="interval" type="text" class="form-control pull-right" id="interval" disabled value="${requestScope.SelectedInterval}" />
                                    </div>                                  
                                </div>
                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label>Periodo de Preentrada</label>                                                
                                    <div class="input-group">
                                        <input name="period" type="text" class="form-control pull-right" id="period" disabled value="${requestScope.SelectedPeriod}" />
                                    </div>                                  
                                </div>
                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label>&nbsp;</label>                                                
                                    <div class="input-group">
                                        &nbsp;
                                    </div>                                  
                                </div>
                            </div>
                        </div><!-- /.box-body -->
                    </div><!-- /.box -->
                </c:if> 
                <c:if test="${requestScope.TaskAspertion eq 'Yes'}">
                    <div class="box box-success">
                        <div class="box-header">
                            <h3 class="box-title">Datos Generales</h3>
                        </div>
                        <div class="box-body">
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label>Condiciones Día</label>                                                
                                    <div class="input-group">
                                        <input name="condition" type="text" class="form-control pull-right" id="condition" required value="${requestScope.SelectedCondition}" />
                                    </div>                                  
                                </div>
                                <div class="form-group">
                                    <label>Velocidad Viento Km/Hr</label>                                                
                                    <div class="input-group">
                                        <input name="velocity" type="text" class="form-control pull-right" id="velocity" required value="${requestScope.SelectedAirVelocity}" />
                                    </div>                                  
                                </div>
                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label>Temperatura C</label>                                                
                                    <div class="input-group">
                                        <input name="temperature" type="text" class="form-control pull-right" id="temperature" required value="${requestScope.SelectedTemperature}" />
                                    </div>                                  
                                </div>
                                <div class="form-group">
                                    <label>Dirección de Viento</label>                                                
                                    <div class="input-group">
                                        <input name="direction" type="text" class="form-control pull-right" id="direction" required value="${requestScope.SelectedDirection}" />
                                    </div>                                  
                                </div>
                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label>Humedad %H Relativa</label>                                                
                                    <div class="input-group">
                                        <input name="humidity" type="text" class="form-control pull-right" id="humidity" required value="${requestScope.SelectedHumidity}" />
                                    </div>                                  
                                </div>  
                                <div class="form-group">
                                    <label>Boquilla</label>                                                
                                    <div class="input-group">
                                        <input name="beak" type="text" class="form-control pull-right" id="beak" required value="${requestScope.SelectedBeak}" />
                                    </div>                                  
                                </div>
                            </div>
                        </div><!-- /.box-body -->
                    </div><!-- /.box -->

                    <div class="box box-success">
                        <div class="box-header">
                            <h3 class="box-title">Aspersora</h3>
                        </div>
                        <div class="box-body">
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label>Tipo Aspersora</label>                                                
                                    <div class="input-group">
                                        <input name="aspertionType" type="text" class="form-control pull-right" id="aspertionType" required value="${requestScope.SelectedAspertionType}" />
                                    </div>                                  
                                </div>
                            </div>                        
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label>Presión Lbs</label>                                                
                                    <div class="input-group">
                                        <input name="pressure" type="text" class="form-control pull-right" id="pressure" required value="${requestScope.SelectedPressure}" />
                                    </div>                                  
                                </div>                            
                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label>&nbsp;</label>                                                
                                    <div class="input-group">
                                        &nbsp;
                                    </div>                                  
                                </div>                            
                            </div>
                        </div><!-- /.box-body -->
                    </div><!-- /.box -->

                    <div class="box box-success">
                        <div class="box-header">
                            <h3 class="box-title">Tractor</h3>
                        </div>
                        <div class="box-body">
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label>Tipo de Tractor</label>                                                
                                    <div class="input-group">
                                        <input name="tractorType" type="text" class="form-control pull-right" id="tractorType" required value="${requestScope.SelectedTractorType}" />
                                    </div>                                  
                                </div>
                            </div>                        
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label>Velocidad</label>                                                
                                    <div class="input-group">
                                        <input name="tractorVelocity" type="text" class="form-control pull-right" id="tractorVelocity" required value="${requestScope.SelectedTractorVelocity}" />
                                    </div>                                  
                                </div>                            
                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <label>&nbsp;</label>                                                
                                    <div class="input-group">
                                        &nbsp;
                                    </div>                                  
                                </div>                            
                            </div>
                        </div><!-- /.box-body -->
                    </div><!-- /.box -->

                    <div class="box box-success">
                        <div class="box-header">
                            <h3 class="box-title">Otros</h3>
                        </div>
                        <div class="box-body">
                            <div class="row">
                                <div class="col-xs-4">
                                    <div class="form-group">
                                        <label>Gasto de Agua Lt/Ha</label>                                                
                                        <div class="input-group">
                                            <input name="expenditure" type="text" class="form-control pull-right" id="expenditure" required value="${requestScope.SelectedExpenditure}" />
                                        </div>                                  
                                    </div>
                                </div>
                                <div class="col-xs-4">
                                    <div class="form-group">
                                        <label>Responsable de la Aplicacion</label>                                                
                                        <div class="input-group">
                                            <input name="owner" type="text" class="form-control pull-right" id="owner" required value="${requestScope.SelectedOwner}" />
                                        </div>                                  
                                    </div>                    
                                </div>
                                <div class="col-xs-4">
                                    <div class="form-group">
                                        <label>&nbsp;</label>                                                
                                        <div class="input-group">
                                            &nbsp;
                                        </div>                                  
                                    </div>                            
                                </div>
                            </div>
                            <div class="form-group">
                                <label>Comentarios</label>
                                <textarea name="comments" class="form-control" rows="5" id="comment">${requestScope.SelectedComments}</textarea>
                            </div>
                        </div><!-- /.box-body -->
                    </div><!-- /.box -->
                </c:if>    

            </div>

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
