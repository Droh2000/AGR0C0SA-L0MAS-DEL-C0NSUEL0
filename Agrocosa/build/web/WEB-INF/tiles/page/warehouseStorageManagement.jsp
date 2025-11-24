<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
<link rel="stylesheet" href="../plugins/iCheck/all.css">

<!-- select -->
<script src="../plugins/select2/select2.js" type="text/javascript"></script>
<link href="../plugins/select2/select2.css" rel="stylesheet" type="text/css" />
<link href="../plugins/select2/select2-bootstrap.css" rel="stylesheet" type="text/css" />


<script src="../plugins/jasny/js/jasny-bootstrap.min.js" type="text/javascript"></script>
<script>
    $("#fileSelector").submit(function (e) {
        waitingDialog.show();
    });
</script>
<link href="../plugins/jasny/css/jasny-bootstrap.min.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">



    function validateSelectedItem()
    {
        for (i = 0; i < document.paramsForm.elements.length; i++)
        {
            if (document.paramsForm.elements[i].type === "checkbox")
            {
                if (document.paramsForm.elements[i].checked)
                    return true;
            }
        }

        $("#primaryAlertText").html('Por favor seleccione un registro para utilizar esta opcion.');
        setTimeout(function () {
            $('#primaryAlert').modal();
        }, 100);

        return false;
    }

    $(function () {
        $('.btnDelete').on("click", function () {
            if (validateSelectedItem()) {
                $('#confirmModal').modal('show');
            }
        });

        $('#btnConfirmYes').on("click", function () {
            $('.box-footer').append('<input type="hidden" class="hidden" name="Method" value = "Delete" />');
            $("#paramsForm").submit();
        });
    });



</script>
<style>
    .select2-container .select2-selection--single {
        height: 34px !important;      /* altura estándar de form-control */
        padding: 6px 12px !important; /* mismo padding de Bootstrap */
    }

    .select2-container--default .select2-selection--single .select2-selection__rendered {
        line-height: 22px !important; /* centra el texto */
    }

    .select2 {
        width: 100% !important;
    }
</style>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Administración de Localidades de Inventario
            <small>Configuración de catálogo de Localidades</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-caret-right"></i> Configuración</a></li>
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
                        <script>
                            $(".alert-success").fadeTo(2000, 1000).slideUp(1000, function () {
                                $(".alert-success").slideUp(1000);
                            });
                        </script>
                    </div>
                </c:if>
            </c:when>
            <c:when test="${requestScope.RESPONSE_CODE == 'FAIL'}">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                    <h4><i class="icon fa fa-ban"></i> Error !</h4>
                    <c:out value="${requestScope.RESPONSE_MESSAGE}" /><br />
                    <c:out value="${requestScope.RESPONSE_DETAIL}" />
                    <script>
                        $(".alert-danger").fadeTo(2000, 1000).slideUp(1000, function () {
                            $(".alert-danger").slideUp(1000);
                        });
                    </script>
                </div>

            </c:when>
        </c:choose>

        <form name="paramsForm" id="paramsForm" method="POST" action="warehouseStorageManagement.do">
            <div class="box box-solid">
                <div class="box-header with-border">
                    <h3 class="box-title">Localidades</h3>
                </div>


                <div class="box-body" id="info" > 
                    <c:forEach items="${Storage_Table}" var="row" varStatus="status">
                        <c:choose>
                            <c:when test="${requestScope.Method == 'Edit' and requestScope.ID == row.idWarehouseStorage}">
                                <div class="row">

                                    <div class="col-md-4">
                                        <div class="form-group">
                                            <label>Localidad</label>                                                
                                            <input name="WarehouseStorageName" type="text" class="form-control pull-right" id="WarehouseStorageName" required value="${row.WarehouseStorageName}" />
                                        </div>                                        
                                    </div>
                                        
                                    <!-- Selector para elegir el tipo de Inventario al que corresponde la localidad -->
                                    <div class="col-md-4">
                                        <div class="form-group">
                                            <label>Almacen Correspondiente</label>                                                                                                                 
                                            <select name="WarehouseType" id="WarehouseType" class="form-control select2" data-title="No puede estar vacio" required >
                                                <option value=""></option>
                                                <c:forEach items="${WearhouseType_HashMap}" var="item">
                                                    <option value="${item.key}"  ${row.WarehouseName == item.value  ? 'selected' : ''}>${item.value}</option>
                                                </c:forEach>
                                            </select>
                                        </div>    
                                    </div> 

                                    <div class="col-md-4">
                                        <div class="form-group">
                                            <label>Fecha de Registro</label>                                                
                                            <div class="input-group">
                                                <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                                <input type="text" name="StartDate" id="StartDate" class="form-control" value="${row.InsertDate}" readonly />
                                            </div>
                                        </div><!-- /.form group -->

                                        <div class="form-group">
                                            <label>Fecha de Modificación</label>                                                
                                            <div class="input-group">
                                                <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                                <input type="text" name="EndDate" id="EndDate" class="form-control" value="${row.ModifiedDate}" readonly />
                                            </div>
                                        </div><!-- /.form group -->
                                    </div>
                                </div>

                                <input type="hidden" name="ID" title="${row.idWarehouseStorage}" value="${row.idWarehouseStorage}"/>
                            </c:when>
                        </c:choose>
                    </c:forEach>
                </div>



                <div class="box-footer">
                    <c:if test="${requestScope.Storage_Table ne null}">
                        <c:if test="${requestScope.Method ne null and requestScope.Action ne 'Done'}">&nbsp;
                            <button type="submit" class="btn btn-success" title ="Guardar" name="Method" id="btnSubmit" value="Save" onclick="">
                                <i class="fa fa-save"></i> Guardar
                            </button>
                            <button type="Submit" class="btn btn-success" title ="Cancelar" name="Method" id="btnSubmit"  onclick="location.href = 'warehouseStorageManagement.do'">
                                <i class="fa fa-remove"></i> Cancelar
                            </button>                            
                        </c:if>

                        <c:if test="${requestScope.Method eq null or requestScope.Action eq 'Done'}"> &nbsp;
                            <button type="submit" class="btn btn-success" title ="Agregar" name="Method" id="btnSubmit" value="Add">
                                <i class="fa fa-plus-square"></i> Agregar
                            </button>
                            <button type="submit" class="btn btn-success" title ="Editar" name="Method" id="btnSubmit" value="Edit" onclick="return validateSelectedItem();">
                                <i class="fa fa-edit"></i> Editar
                            </button>
                            <button type="button" class="btn btn-success btnDelete" title ="Eliminar" name="Method" id="btnSubmit" value="Delete">
                                <i class="fa fa-minus-square"></i> Eliminar
                            </button>
                        </c:if>
                    </c:if>
                </div>
            </div>


            <c:if test="${requestScope.Storage_Table ne null}">
                <div class="box box-solid">
                    <div class="box-header with-border">
                        <h3 class="box-title">Localidades</h3>
                    </div>
                    <div class="box-body" id="info" > 
                        <div class="table-responsive">

                            <table id="dtable" class="table table-bordered table-striped table-hover">
                                <thead>
                                    <tr>
                                        <th></th>
                                        <th>Localidad</th>
                                        <th>Almacen Correspondiente</th>
                                        <th>Usuario</th>
                                        <th>Fecha de Registro</th>
                                        <th>Fecha de Modificación</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${Storage_Table}" var="row" varStatus="status">
                                        <tr>
                                            <td>
                                                <c:if test="${requestScope.Method ne 'Edit' or (requestScope.ID == row.idWarehouseStorage and requestScope.ID ne 'new' ) }">
                                                    <input type="checkbox" class="minimal-red" name="ID" title="${row.idWarehouseStorage}" value="${row.idWarehouseStorage}"
                                                           ${requestScope.ID == row.idWarehouseStorage and requestScope.Method != 'Save' ? 'checked="checked"' : ''} onclick="radio(this);"/>
                                                </c:if>
                                            </td>
                                            <td>${row.WarehouseStorageName}</td>
                                            <td>${row.WarehouseName}</td>
                                            <td>${row.UserName}</td>
                                            <td>${row.InsertDate}</td>
                                            <td>${row.ModifiedDate}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table> 
                        </div>
                    </div>
                </div>
            </c:if>

        </form>

        <script>
            $('#dtable').DataTable({
                "paging": false,
                "lengthChange": false,
                "searching": true,
                "ordering": true,
                "info": false,
                "autoWidth": false,
                "dom": 'frtip',
                "columns": [
                    {"width": "2%"},
                    null,
                    null,
                    null,
                    null,
                    null
                ],
                "language": {
                    "search": "Buscar:"
                }
            });
            </script>


        <!-- iCheck 1.0.1 -->
        <script src="../plugins/iCheck/icheck.min.js"></script>

        <script>
            //iCheck for checkbox and radio inputs
            $('input[type="checkbox"].minimal, input[type="radio"].minimal').iCheck({
                checkboxClass: 'icheckbox_minimal-blue',
                radioClass: 'iradio_minimal-blue'
            });
            //Red color scheme for iCheck
            $('input[type="checkbox"].minimal-red, input[type="radio"].minimal-red').iCheck({
                checkboxClass: 'icheckbox_minimal-red',
                radioClass: 'iradio_minimal-red'
            });
            //Flat red color scheme for iCheck
            $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
                checkboxClass: 'icheckbox_flat-green',
                radioClass: 'iradio_flat-green'
            });

        </script> 


        <div id="primaryAlert" class="modal fade" role="dialog">
              <div class="modal-dialog">
                <div class="modal-content" style="text-align: center; padding:20px;">
                    <h2 class="modal-title titles" id="primaryAlertTitle" >Mensaje</h2><br/>
                    <h4 id="primaryAlertText"></h4><br/>
                    <button id="closePrimaryAlert" type="button" class="btn btn-success" data-dismiss="modal">Entendido</button>
                </div>
            </div>
        </div>

        <div class="modal fade" id="confirmModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog" role="document">
                <div class="modal-content" style="text-align: center; padding:20px;">
                    <h2 class="modal-title titles" id="primaryAlertTitle" >Mensaje</h2><br/>
                    <div class="modal-body">
                        <h3>Esta usted seguro de eliminar este registro?</h3>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-success pull-left" data-dismiss="modal">Cancelar</button>
                        <button type="button" id="btnConfirmYes" class="btn btn-success pull-right">Aceptar</button>
                    </div>                    
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