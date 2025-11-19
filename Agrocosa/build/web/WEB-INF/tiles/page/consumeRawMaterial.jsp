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


<script>
    $(document).ready(function () {
        var t = $('#dtable').DataTable();
        var idRow = -1;
        var counter = 1;
        var rawMaterialTypeName;
        var rawMaterialName;
        var storageName;
        var substorageName;
        var qty;
        var uom;

        $('#formMaterial').submit(function (e) {
            e.preventDefault();

            rawMaterialTypeName = $("#rawMaterialTypeName").val();
            rawMaterialName = $("#rawMaterialName").val();
            storageName = $("#storageName").val();
            substorageName = $("#substorageName").val();
            qty = $("#quantity").val();
            uom = $("#uom").val();
            
            t.row.add([
                rawMaterialTypeName,
                rawMaterialName,
                storageName,
                substorageName,
                qty,
                uom,
                'x'
            ]).draw(false);
            $('#rawMaterialTypeName').val(null).trigger('change');
            $('#rawMaterialName').val(null).trigger('change');
            $('#substorageName').val(null).trigger('change');
            $("#quantity").val("");
            $("#uom").val("");

            counter++;
        });


        $('#dtable tbody').on('click', 'button', function () {
            idRow = t.row($(this).parents('tr')).index();
            $('#confirmModal').modal('show');
        });


        $('#btnConfirmYes').on("click", function () {
            t.row(idRow).remove().draw();
            counter--;
            $('#confirmModal').modal('hide');
        });

        $('#btnViewInventory').on("click", function () {
            $("#formViewAllData").submit();
        });

        $('#btnSave').on("click", function () {
            var data = t.rows().data();
            if (data.length > 0) {
                var data4Save = convertTableToArrayObject();
                $("#dataTable4Save").val(JSON.stringify(data4Save));

                $("#formSaveData").submit();
            }
        });

        $("#storageName").change(function () {
            if ($("#storageName").val().length > 0) {
                var v = $("#storageName").val();
                var param = 'storageName=' + v;
                $.ajax({
                    url: 'ajaxSubstorage.do',
                    type: 'POST',
                    data: param,
                    success: function (result) {
                        $('#substorageName').children().remove();
                        $("#substorageName").append(result);
                    }
                });
            } else {
                $('#substorageName').children().remove();
            }
        });

        $("#rawMaterialTypeName").change(function () {
            if ($("#rawMaterialTypeName").val().length > 0) {
                var v = $("#rawMaterialTypeName").val();
                var param = 'rawMaterialTypeName=' + v;

                $.ajax({
                    url: 'ajaxRawMaterial.do',
                    type: 'POST',
                    data: param,
                    success: function (result) {
                        $('#rawMaterialName').children().remove();
                        $("#rawMaterialName").append(result);
                    }
                });
            } else {
                $('#rawMaterialName').children().remove();
            }
        });


        $("#rawMaterialName").change(function () {
            if ($("#rawMaterialName").val().length > 0) {
                var v = $("#rawMaterialName").val();
                var param = 'rawMaterialName=' + v;
                $.ajax({
                    url: 'ajaxDefaultUOM.do',
                    type: 'POST',
                    data: param,
                    success: function (result) {
                        $("#uom").val(result);
                    }
                });
            } else {
                $('#uom').val("");
            }
        });


    });

    function convertTableToArrayObject() {
        var dataObjects = [];
        var table = $('#dtable').DataTable();
        var data = table.rows().data();

        for (var i = 0; i < data.length; i++) {
            dataObjects.push(data[i]);
        }
        return dataObjects;
    }

    $.ajaxSetup({
        cache: false
    });

</script>


<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Salida de Insumos
            <small>Consumo de insumos</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-caret-right"></i> Insumos</a></li>
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
                <h3 class="box-title">Salida de Insumos</h3>
                <div class="box-tools pull-right">
                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                </div>
            </div><!-- /.box-header -->
            <div class="box-body">
                <form name="formMaterial" id="formMaterial" action="" method="post">
                    <div class="row">
                        <div class="col-md-2">
                            <div class="form-group">
                                <label>Tipo de Insumo</label>
                                <select name="rawMaterialTypeName" id="rawMaterialTypeName" class="form-control" required>
                                    <option value=""></option>
                                    <c:forEach items="${RawMaterialType_HashMap}" var="item">
                                        <option value="${item.value}"  ${requestScope.SelectedRawMaterialTypeName == item.value  ? 'selected' : ''}>${item.value}</option>
                                    </c:forEach>
                                </select>                              
                            </div>
                        </div>
                        <div class="col-md-2">
                            <div class="form-group">
                                <label>Insumo</label>
                                <select name="rawMaterialName" id="rawMaterialName" class="form-control" required>
                                    <c:if test="${requestScope.SelectedRawMaterialName ne ''}"> 
                                        <option value=""></option>
                                        <c:forEach items="${RawMaterialName_HashMap}" var="item">
                                            <option value="${item.value}"  ${requestScope.SelectedRawMaterialName == item.value  ? 'selected' : ''}>${item.value}</option>
                                        </c:forEach>
                                    </c:if>
                                </select>                              
                            </div> 
                        </div>
                        <div class="col-md-2">
                            <div class="form-group">
                                <label >Localidad</label>
                                <select name="storageName" id="storageName" class="form-control" required>
                                    <c:forEach items="${Storage_HashMap}" var="item">
                                        <option value="${item.value}"  ${requestScope.SelectedStorageName == item.value  ? 'selected' : ''}>${item.value}</option>
                                    </c:forEach>
                                </select> 
                            </div>                        
                        </div>
                        <div class="col-md-2">                        
                            <div class="form-group">
                                <label >Sublocalidad</label>
                                <select name="substorageName" id="substorageName" class="form-control" required>                                
                                    <c:if test="${requestScope.SelectedSubstorageName ne ''}"> 
                                        <option value=""></option>
                                        <c:forEach items="${Substorage_HashMap}" var="item">
                                            <option value="${item.value}"  ${requestScope.SelectedSubstorageName == item.value  ? 'selected' : ''}>${item.value}</option>
                                        </c:forEach>
                                    </c:if>
                                </select>                                                                                    
                            </div>                       
                        </div>
                        <div class="col-md-2">
                            <div class="form-group">
                                <label >Cantidad</label>
                                <input type="text" name="quantity" id="quantity" class="form-control numeric" placeholder="Cantidad" size="5" data-title="Este campo es Requerido" required/> 
                            </div><!-- /.form-group -->                        
                        </div>
                        <div class="col-md-2">
                            <div class="form-group">
                                <label >Unidad de Medida</label>
                                <input type="text" name="uom" id="uom" class="form-control numeric" placeholder="" data-title="" required readonly/> 
                            </div>
                        </div>
                        <div class="col-md-2">
                            <div class="form-group">
                                <label style="color:white">-</label><br>
                                <button type="submit" class="btn btn-primary" id="addButton"><i class="fa fa-plus"></i> Agregar</button>
                            </div><!-- /.form-group -->                        
                        </div>
                    </div>                    
                </form>
            </div><!-- /.box-body -->
            <div class="box-footer"></div>
        </div><!-- /.box -->

        <div class="box box-success">
            <div class="box-header with-border">
                <h3 class="box-title">Insumos Seleccionados</h3>
                <div class="box-tools pull-right">
                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    <button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
                </div>
            </div><!-- /.box-header -->
            <div class="box-body">
                <div class="table-responsive">

                    <table id="dtable" class="table table-bordered table-striped table-hover">
                        <thead>
                            <tr>                       
                                <th>Tipo de Insumo</th>
                                <th>Insumo</th>
                                <th>Localidad</th>
                                <th>Sublocalidad</th>
                                <th>Cantidad</th>
                                <th>Unidad de Medida</th>
                                <th>Borrar</th>
                            </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table> 
                </div>
            </div><!-- /.box-body -->
            <div class="box-footer clearfix">
                <button type="button" class="btn btn-primary pull-left"  id="btnViewInventory" ><i class="fa fa-search-plus"></i> Ver Inventario</button>
                <button type="button" class="btn btn-primary pull-right" id="btnSave" ><i class="fa fa-save"></i> Guardar</button>
            </div><!-- /.box-footer -->
        </div><!-- /.box -->

        <div class="modal fade" id="confirmModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header alert-warning">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel"><i class="icon fa fa-trash"></i> Borrar Registro</h4>
                    </div>
                    <div class="modal-body">
                        <h3>Esta usted seguro de borrar este registro?</h3>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary pull-left" data-dismiss="modal">No</button>
                        <button type="button" id="btnConfirmYes" class="btn btn-primary pull-right">Si</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="hide">            
            <form name="formSaveData" id="formSaveData" action="consumeRawMaterial.do" method="post">
                <input type="hidden" name="dataTable4Save" id="dataTable4Save" value=""/>
                <input type="hidden" name="Action" id="Action"  value="SaveTableData"/>
            </form>                        
        </div>

        <div class="hide">            
            <form name="formViewAllData" id="formViewAllData" action="rawMaterialInventoryReport.do" method="post">
                <input type="hidden" name="Action" id="Action" value="Search"/>
            </form>                        
        </div>



        <script>
            $('#dtable').DataTable({
                "paging": false,
                "lengthChange": false,
                "searching": false,
                "ordering": false,
                "info": false,
                "autoWidth": false,
                "dom": 'T<"clear">lfrtip',
                "tableTools": {
                    "sSwfPath": "../plugins/datatables/extensions/TableTools/swf/copy_csv_xls_pdf.swf"
                },
                "columnDefs": [
                    {
                        "targets": 0,
                        "visible": true
                    },
                    {
                        "targets": 1,
                        "visible": true
                    },
                    {
                        "targets": 2,
                        "visible": true
                    },
                    {
                        "targets": 3,
                        "visible": true
                    },
                    {
                        "targets": 4,
                        "visible": true
                    },
                    {
                        "targets": 5,
                        "visible": true
                    },
                    {
                        "targets": 6,
                        "data": null,
                        "defaultContent": "<button class='btn btn-primary xxs'><i class='fa fa-trash'></i></button>"
                    }]
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
