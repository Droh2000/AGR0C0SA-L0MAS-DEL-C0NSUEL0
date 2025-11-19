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

<script src="../plugins/bootstrap-duallistbox/jquery.bootstrap-duallistbox.min.js"></script>
<link rel="stylesheet" type="text/css" href="../plugins/bootstrap-duallistbox/bootstrap-duallistbox.css">
<script type="text/javascript">
    $.ajaxSetup({
        cache: false
    });

    var ajax_load = "";

    $(function () {




        $("#btnBack").click(function () {
            $("#formBack").submit();
        });





        $("#CropType").change(function () {

            if ($("#CropType").val().length > 0) {

                var type = $("#CropType").val();
                var param = 'CropType=' + type;

                $.ajax({
                    url: 'ajaxCropByCropType.do',
                    type: 'POST',
                    data: param,
                    success: function (result) {
                        $('#Crop').children().remove();
                        $("#Crop").append(result);
                    }
                });

            }
        });



        var t = $('#dtable').DataTable();

        var cropType;
        var crop;
        var qty;
        var selId = 0;

        $("#addButton").click(function () {
            cropType = $("#CropType").val();
            crop = $("#Crop").val();
            qty = $("#GrooveQty").val();
            

            t.row.add([
                cropType,
                crop,
                qty === "" ? 0:qty,
                'x',
                '0'
            ]).draw(false);
        });

        $('#dtable tbody').on('click', 'button', function () {
            idRow = t.row($(this).parents('tr')).index();
            $('#confirmModal').modal('show');
        });

        $('#btnConfirmYes').on("click", function () {
            selId = t.cell(idRow,4).data();//row,col
            
            if(selId !== "0"){
                //Call Ajax to delete id
                $.ajax({
                    url: 'ajaxDeleteSectionCrop.do',
                    type: 'POST',
                    data: jQuery.param({idSectionCrop: selId}),
                    success: function (result) {
                        //show message
                    }
                });                
            }
            
            t.row(idRow).remove().draw();
            $('#confirmModal').modal('hide');           
        });

        $('#btnSave').on("click", function () {
            var data = t.rows().data();
            if (data.length > 0) {
                var data4Save = convertTableToArrayObject();
                $("#dataTable4Save").val(JSON.stringify(data4Save));
                $("#formSaveData").submit();
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

</script>
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            <i class='fa fa-yelp'></i> Tipos de Cultivos 
            <small>Asignación de tipos de cultivos</small>
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


        <div class="box box-success">
            <div class="box-header with-border">Asignación de tipos de cultivos</div>
            <div class="box-body" > 
                <div class="row"> 
                    <div class="col-md-3">
                        <div class="box-body box-profile">
                            <h3 class="profile-username text-center">Seccion:&nbsp;&nbsp;${requestScope.SectionName}</h3>

                            <ul class="list-group list-group-unbordered">
                                <li class="list-group-item">
                                    <b>Superficie</b> <a class="pull-right">${requestScope.Area}</a>
                                </li>
                                <li class="list-group-item">
                                    <b>Tipo de Suelo</b> <a class="pull-right">${requestScope.LandTypeName}</a>
                                </li>
                                <li class="list-group-item">
                                    <b>Tipo de Cinta</b> <a class="pull-right">${requestScope.PipeTypeName}</a>
                                </li>
                            </ul>
                        </div><!-- /.box-body -->
                    </div>
                </div>

                <div class="row"> 
                    <div class="col-md-2">
                        <div class="form-group">
                            <label >Cultivo</label>
                            <select name="CropType" id="CropType" class="form-control" data-title="Seleccione el tipo de cultivo">
                                <option value="empty"></option>
                                <c:forEach items="${CropType_HashMap}" var="item">
                                    <option value="${item.value}"  ${requestScope.SelectedCropType == item.value  ? 'selected' : ''}>${item.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="col-md-2">
                        <div class="form-group">
                            <label >Variedad</label>
                            <select name="Crop" id="Crop" class="form-control" data-title="Variedad" required>                                    
                            </select> 
                        </div><!-- /.form-group --> 
                    </div>

                    <div class="col-md-2">
                        <div class="form-group">
                            <label >Cantidad de Surcos</label>
                            <input type="text" name="GrooveQty" id="GrooveQty" class="form-control numeric" placeholder="Cantidad de Surcos" size="5" /> 
                        </div><!-- /.form-group -->
                    </div>

                    <div class="col-md-2">
                        <div class="form-group">
                            <label style="color:white">-</label><br>
                            <span class="input-group-btn">
                                <button type="button" class="btn btn-primary" id="addButton"><i class="fa fa-plus"></i> Agregar</button>
                            </span>
                        </div><!-- /.form-group -->  
                    </div>
                </div>





                <div class="table-responsive">
                    <table id="dtable" class="table table-bordered table-striped table-hover">
                        <thead>
                            <tr>                        
                                <th>Cultivo</th>
                                <th>Variedad</th>
                                <th>Cantidad de Surcos</th>                                            
                                <th>Borrar</th>
                                <th>id</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${SectionCrop_Table}" var="row" varStatus="status">
                                <tr>
                                    <td>${row.CropTypeName}</td>
                                    <td>${row.CropName}</td>
                                    <td>${row.GrooveQty}</td>
                                    <td>x</td>
                                    <td>${row.idSectionCrop}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table> 
                </div>

            </div>
            <div class="box-footer">
                <div class="form-group">
                    <button type="button" id="btnBack" class="btn btn-primary pull-left"><i class="fa fa-backward"></i> Regresar</button>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <button type="button" id="btnSave" class="btn btn-primary"><i class="fa fa-save"></i> Guardar</button>                    
                </div><!-- /.form-group -->                        
            </div>
        </div>



        <div class="hide">
            <form name="formBack" id="formBack" action="editCamp.do" method="post">
                <input type="hidden" id="idCamp" name="idCamp" value="${requestScope.idCamp}"/>
            </form>                        
        </div>


        <div class="modal fade" id="confirmModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header alert-warning">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel"><i class="icon fa fa-trash"></i> Borrar Cultivo</h4>
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
            <form name="formSaveData" id="formSaveData" action="addCrop.do" method="post">
                <input type="hidden" name="selectedCrop" id="dataTable4Save" value=""/>
                <input type="hidden" name="Action" id="Action"  value="saveSectionCrop"/>
                <input type="hidden" name="idCampSection" id="idCampSection" value="${requestScope.idCampSection}"/>
                <input type="hidden" name="idCamp" id="idCamp" value="${requestScope.idCamp}"/>
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
                        "data": null,
                        "defaultContent": "<button class='btn btn-primary xxs'><i class='fa fa-trash'></i></button>"
                    },
                    {
                        "targets": 4,
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
