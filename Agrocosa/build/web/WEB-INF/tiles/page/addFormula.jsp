<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script src="../plugins/select2/select2.js" type="text/javascript"></script>
<link href="../plugins/select2/select2.css" rel="stylesheet" type="text/css" />

<script>
    $.ajaxSetup({
        cache: false
    });




    $(document).ready(function () {
        var t = $('#dtable').DataTable();
        var idRow = -1;
        var component;
        var uom;
        var qty;
        var selId = 0;

        $('#formMaterial').submit(function (e) {
            e.preventDefault();
            component = $("#component").val();
            uom = $("#uom").val();
            qty = $("#quantity").val();

            t.row.add([
                component,
                qty,
                uom,
                'x',
                'x',
                '0'
            ]).draw(false);
            $('#component').val("");
            $("#quantity").val("");
            $('#uom').val(null).trigger('change');

        });


        $('#dtable tbody').on('click', 'button', function () {
            idRow = t.row($(this).parents('tr')).index();
            if ($(this).attr('name') === 'b1') {
                selId = t.cell(idRow, 5).data();//row,col
                $('#idFormulaDetail').val(selId);
                
                var ar = [];
                <c:forEach items="${VegetalNutritionDetail_Table}" var="item">
                    ar.push(['${item.idFormulaDetail}', '${item.VegetalNutrition}', '${item.Portion}']);
                </c:forEach>

                $('#savevegetalNutrition').trigger("reset");

                for (var i = 0, len = ar.length; i < len; i++) {
                    if (ar[i][0] === selId) {
                        $("#" + ar[i][1]).val(ar[i][2]);
                    }
                }

                $('#vegetalNutritionModal').modal('show');
            } else {
                $('#confirmModal').modal('show');
            }
        });


        $('#btnConfirmYes').on("click", function () {

            selId = t.cell(idRow, 5).data();//row,col

            if (selId !== "0") {
                //Call Ajax to delete id
                $.ajax({
                    url: 'ajaxDeleteFormulaDetail.do',
                    type: 'POST',
                    data: jQuery.param({idFormulaDeatail: selId}),
                    success: function (result) {
                        //show message
                    }
                });
            }

            t.row(idRow).remove().draw();
            $('#confirmModal').modal('hide');
        });




        $('#btnSaveOrders').on("click", function () {
            var data = t.rows().data();
            if (data.length > 0) {
                var data4Save = convertTableToArrayObject();
                $("#dataTable4Save").val(JSON.stringify(data4Save));
                $("#formSaveData").submit();
            }
        });

        $("#btnBack").click(function () {
            $("#formBack").submit();
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

</script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Formula
            <small>Compuestos de Formulas</small>
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
                <h3 class="box-title">Compuestos de Formula : &nbsp; ${requestScope.FormulaName}</h3>
                <div class="box-tools pull-right">
                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                </div>
            </div><!-- /.box-header -->
            <div class="box-body">
                <form name="formMaterial" id="formMaterial" action="" method="post">
                    <div class="row">
                        <div class="col-md-2">
                            <div class="form-group">
                                <label >Compuesto</label>
                                <input type="text" name="component" id="component" class="form-control" placeholder="Componente" data-title="Este campo es Requerido" required/> 
                            </div><!-- /.form-group -->
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
                                <select name="uom" id="uom" class="form-control" data-title="Unida de Medida" required>
                                    <option value=""></option>
                                    <c:forEach items="${UOM_HashMap}" var="item">
                                        <option value="${item.value}"  ${requestScope.SelectedUOM == item.value  ? 'selected' : ''}>${item.value}</option>
                                    </c:forEach>
                                </select>                                 
                            </div><!-- /.form-group -->
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
                <h3 class="box-title">Compuestos</h3>
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
                                <th>Compuesto</th>
                                <th>Cantidad</th>
                                <th>Unidad de Medida</th>
                                <th>Porcentajes NPK</th>
                                <th>Borrar</th>
                                <th>id</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${FormulaDetail_Table}" var="row" varStatus="status">
                                <tr>
                                    <td>${row.ComponentName}</td>
                                    <td>${row.Quantity}</td>
                                    <td>${row.UOM}</td>
                                    <td>x</td>
                                    <td>x</td>
                                    <td>${row.idFormulaDetail}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table> 
                </div>
            </div><!-- /.box-body -->
            <div class="box-footer clearfix">
                <button type="button" id="btnBack" class="btn btn-primary pull-left"><i class="fa fa-backward"></i> Regresar</button>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <button type="button" class="btn btn-primary pull-right" id="btnSaveOrders" ><i class="fa fa-save"></i> Guardar</button>
            </div><!-- /.box-footer -->
        </div><!-- /.box -->

        <div class="modal fade" id="confirmModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header alert-warning">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel"><i class="icon fa fa-trash"></i> Borrar Material</h4>
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
            <form name="formSaveData" id="formSaveData" action="addFormula.do" method="post">
                <input type="hidden" name="dataTable4Save" id="dataTable4Save" value=""/>
                <input type="hidden" name="idFormula" id="idFormula" value="${requestScope.idFormula}"/>
                <input type="hidden" name="Action" id="Action"  value="SaveTableData"/>
            </form>                        
        </div>


        <div class="hide">
            <form name="formBack" id="formBack" action="formulaManagement.do" method="post">
            </form>                        
        </div>



        <div class="modal fade" id="vegetalNutritionModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog" role="document">
                <form class="form-horizontal" name="savevegetalNutrition" id="savevegetalNutrition" method="POST" action="addFormula.do">
                    <div class="modal-content">
                        <div class="modal-header alert-warning">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel"> Elementos de Nutricion Vegetal</h4>
                        </div>
                        <div class="modal-body">

                            <c:forEach items="${VegetalNutrition_HashMap}" var="item" varStatus="loop">
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">${item.value}</label>
                                    <div class="col-sm-6">
                                        <input type="text" name="${item.value}" id="${item.value}" class="form-control" placeholder="" data-title=""/> 
                                    </div>
                                </div><!-- /.form-group -->
                            </c:forEach>

                        </div>
                        <div class="modal-footer">
                            <input type="hidden" name="idFormulaDetail" id="idFormulaDetail" value=""/>
                            <input type="hidden" name="Action" id="Action"  value="saveVegetalNutrition"/>
                            <input type="hidden" name="idFormula" id="idFormula" value="${requestScope.idFormula}"/>
                            
                            <button type="button" class="btn btn-primary pull-left" data-dismiss="modal">No</button>
                            <button type="submit" id="btnSubmitSave" class="btn btn-primary pull-right">Guardar</button>
                        </div>
                    </div>
                </form>
            </div>
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
                        "data": null,
                        "defaultContent": "<button name='b1' class='btn btn-primary xxs'><i class='fa fa-plus'></i></button>"
                    },
                    {
                        "targets": 4,
                        "data": null,
                        "defaultContent": "<button name='b2' class='btn btn-primary xxs'><i class='fa fa-trash'></i></button>"
                    },
                    {
                        "targets": 5,
                        "visible": false
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
