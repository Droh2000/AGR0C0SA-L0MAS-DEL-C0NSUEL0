<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<script>
    $(document).ready(function () {
        var t = $('#dtable').DataTable();
        var idRow = -1;
        var counter = 1;
        var cropTypeName;
        var cropName;
        var storageName;
        var substorageName;
        var qty;

        $('#formMaterial').submit(function (e) {
            e.preventDefault();
            
            cropTypeName = $("#cropTypeName").val();
            cropName = $("#cropName").val();
            storageName = $("#storageName").val();
            substorageName = $("#substorageName").val();            
            qty = $("#quantity").val();

            t.row.add([
                cropTypeName,
                cropName,
                storageName,
                substorageName,
                qty,
                'x'
            ]).draw(false);
            $('#cropTypeName').val(null).trigger('change');
            $('#cropName').val(null).trigger('change');
            $('#substorageName').val(null).trigger('change');
            $("#quantity").val("");

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
                $("#comments4Save").val($("#comments").val());

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

        $("#cropTypeName").change(function () {
            if ($("#cropTypeName").val().length > 0) {
                var v = $("#cropTypeName").val();
                var param = 'cropTypeName=' + v;
                $.ajax({
                    url: 'ajaxCropName.do',
                    type: 'POST',
                    data: param,
                    success: function (result) {
                        $('#cropName').children().remove();
                        $("#cropName").append(result);
                    }
                });
            } else {
                $('#cropName').children().remove();
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
            Salida de Bines o Cajones
            <small>Salida de Bines o Cajones de Bodega de Secado</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-caret-right"></i> Secado</a></li>
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
                <h3 class="box-title">Salida de Bines</h3>
                <div class="box-tools pull-right">
                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                </div>
            </div><!-- /.box-header -->
            <div class="box-body">
                <form name="formMaterial" id="formMaterial" action="" method="post">
                    <div class="row">
                        <div class="col-md-2">
                            <div class="form-group">
                                <label>Cultivo</label>
                                <select name="cropTypeName" id="cropTypeName" class="form-control">
                                    <option value=""></option>
                                    <c:forEach items="${CropType_HashMap}" var="item">
                                        <option value="${item.value}"  ${requestScope.SelectedCropTypeName == item.value  ? 'selected' : ''}>${item.value}</option>
                                    </c:forEach>
                                </select>                              
                            </div>
                        </div>
                        <div class="col-md-2">
                            <div class="form-group">
                                <label>Variedad</label>
                                <select name="cropName" id="cropName" class="form-control">
                                    <c:if test="${requestScope.SelectedCropName ne ''}"> 
                                        <option value=""></option>
                                        <c:forEach items="${Crop_HashMap}" var="item">
                                            <option value="${item.value}"  ${requestScope.SelectedCropName == item.value  ? 'selected' : ''}>${item.value}</option>
                                        </c:forEach>
                                    </c:if>
                                </select>                              
                            </div>  
                        </div>
                        <div class="col-md-2">
                            <div class="form-group">
                                <label >Localidad</label>
                                <select name="storageName" id="storageName" class="form-control">
                                    <c:forEach items="${Storage_HashMap}" var="item">
                                        <option value="${item.value}"  ${requestScope.SelectedStorageName == item.value  ? 'selected' : ''}>${item.value}</option>
                                    </c:forEach>
                                </select> 
                            </div>                        
                        </div>
                        <div class="col-md-2">                        
                            <div class="form-group">
                                <label >Sublocalidad</label>
                                <select name="substorageName" id="substorageName" class="form-control">                                
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
                                <label style="color:white">-</label><br>
                                <button type="submit" class="btn btn-primary" id="addButton"><i class="fa fa-plus"></i> Agregar</button>
                            </div><!-- /.form-group -->                        
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-10">
                            <div class="form-group">
                                <label for="comment">Comentarios:</label>
                                <textarea name="comments" class="form-control" rows="5" id="comments"></textarea>
                            </div>
                        </div>
                    </div>
                </form>
            </div><!-- /.box-body -->
            <div class="box-footer"></div>
        </div><!-- /.box -->

        <div class="box box-success">
            <div class="box-header with-border">
                <h3 class="box-title">Bines</h3>
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
                                <th>Cultivo</th>
                                <th>Variedad</th>
                                <th>Localidad</th>
                                <th>Sublocalidad</th>
                                <th>Cantidad</th>
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
            <form name="formSaveData" id="formSaveData" action="outBinInventory.do" method="post">
                <input type="hidden" name="dataTable4Save" id="dataTable4Save" value=""/>
                <input type="hidden" name="comments" id="comments4Save" value=""/>
                <input type="hidden" name="Action" id="Action"  value="SaveTableData"/>
            </form>                        
        </div>

        <div class="hide">            
            <form name="formViewAllData" id="formViewAllData" action="binInventoryReport.do" method="post">
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
                        "data": null,
                        "defaultContent": "<button class='btn btn-primary xxs'><i class='fa fa-trash'></i></button>"
                    }]
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
