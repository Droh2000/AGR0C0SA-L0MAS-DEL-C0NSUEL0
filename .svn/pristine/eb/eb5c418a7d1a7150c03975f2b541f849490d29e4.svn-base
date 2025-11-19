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

<script type="text/javascript">

    function radio(clicked) {
        var form = clicked.form;
        var checkboxes = form.elements[clicked.name];

        //alert(checkboxes.length);
        if (!clicked.checked) {
            clicked.parentNode.parentNode.className = "";
            return false;
        }

        for (i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i] !== clicked) {
                checkboxes[i].checked = false;
                checkboxes[i].parentNode.parentNode.className = "";
            }
        }

        // highlight the row
        clicked.parentNode.parentNode.className = "over";
    }

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
        alert('Por favor seleccione un registro para utilizar esta opcion.');
        return false;
    }

    function confirmSave()
    {
        var answer = confirm("Esta usted seguro de querer guardar este registro?");
        if (answer)
        {
            return true;
        } else {
            return false;
        }
    }

    function checkIt(evt) {
        evt = (evt) ? evt : window.event;
        var charCode = (evt.which) ? evt.which : evt.keyCode;
        if (charCode > 31 && (charCode < 48 || charCode > 57))
        {
            //status = "This field accepts numbers only.";
            return false;
        }
        return true;
    }
    function confirmDelete()
    {
        if (validateSelectedItem())
        {
            var answer = confirm("Esta usted seguro de eliminar este registro?");
            if (answer)
                return true;
            else
                return false;
        }
        else
            return false;
    }

</script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Administración de Conversiones de Unidades de Medidas
            <small>Configuración de conversion de unidades de medida</small>
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

        <form name="paramsForm" method="POST" action="uomConversionManagement.do"  onsubmit="">
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title">Conversiones de Unidades de Medida</h3>
                </div>


                <div class="box-body" id="info" > 
                    <c:forEach items="${UomConversionInfo_Table}" var="row" varStatus="status">
                        <c:choose>
                            <c:when test="${requestScope.Method == 'Edit' and requestScope.ID == row.idUomConversion}">
                                <div class="row">


                                    <div class="col-md-2">
                                        <div class="form-group">
                                            <label>De Unidad de Medida </label>    
                                            <div class="input-group">   
                                                <select name="UomFrom" id="UomFrom" class="form-control" required>
                                                    <option value=""></option>
                                                    <c:forEach items="${UOM_HashMap}" var="item">
                                                        <option value="${item.value}"  ${row.UomFrom == item.value  ? 'selected' : ''}>${item.value}</option>
                                                    </c:forEach>
                                                </select>  
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label>A Unidad de Medida</label>                                                
                                            <div class="input-group">   
                                                <select name="UomTo" id="UomTo" class="form-control" required>
                                                    <option value=""></option>
                                                    <c:forEach items="${UOM_HashMap}" var="item">
                                                        <option value="${item.value}"  ${row.UomTo == item.value  ? 'selected' : ''}>${item.value}</option>
                                                    </c:forEach>
                                                </select>  
                                            </div>
                                        </div>                                        
                                        <div class="form-group">
                                            <label>Equivalencia</label>                                                
                                            <div class="input-group">
                                                <input name="Conversion" type="text" class="form-control pull-right" id="Conversion" required value="${row.Conversion}" />
                                            </div>                                  
                                        </div>
                                    </div>
                                    <div class="col-md-2">
                                        <div class="form-group">
                                            <div class="input-group">
                                                <div class="input-group-addon">
                                                    <i class="fa fa-user"></i>
                                                </div>
                                                <input type="text" name="User" id="User" class="form-control" value="${row.User}" readonly />
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label>Fecha de Registro:</label>
                                            <div class="input-group">
                                                <div class="input-group-addon">
                                                    <i class="fa fa-calendar"></i>
                                                </div>
                                                <input type="text" name="InsertDate" id="InsertDate" class="form-control" value="${row.InsertDate}" readonly />
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label>Fecha de Modificación:</label>
                                            <div class="input-group">
                                                <div class="input-group-addon">
                                                    <i class="fa fa-calendar"></i>
                                                </div>
                                                <input type="text" name="ModifiedDate" id="ModifiedDate" class="form-control" value="${row.ModifiedDate}" readonly />
                                            </div>                                
                                        </div>                                
                                    </div>
                                </div>
                                <input type="hidden" name="ID" title="${row.idUomConversion}" value="${row.idUomConversion}"/>
                            </c:when>
                        </c:choose>
                    </c:forEach>
                </div>


                <div class="box-footer">
                    <c:if test="${requestScope.UomConversionInfo_Table ne null}">
                        <c:if test="${requestScope.Method ne null and requestScope.Action ne 'Done'}">&nbsp;
                            <button type="submit" class="btn btn-primary" title ="Guardar" name="Method" id="btnSubmit" value="Save" onclick="">
                                <i class="fa fa-save"></i> Guardar
                            </button>
                            <button type="Submit" class="btn btn-primary" title ="Cancelar" name="Method" id="btnSubmit"  onclick="location.href = 'uomConversionManagement.do'">
                                <i class="fa fa-remove"></i> Cancelar
                            </button>                            
                        </c:if>

                        <c:if test="${requestScope.Method eq null or requestScope.Action eq 'Done'}"> &nbsp;
                            <button type="submit" class="btn btn-primary" title ="Agregar" name="Method" id="btnSubmit" value="Add">
                                <i class="fa fa-plus-square"></i> Agregar
                            </button>
                            <button type="submit" class="btn btn-primary" title ="Editar" name="Method" id="btnSubmit" value="Edit" onclick="return validateSelectedItem();">
                                <i class="fa fa-edit"></i> Editar
                            </button>
                            <button class="btn btn-primary" title ="Eliminar" name="Method" id="btnSubmit" value="Delete" onclick="return confirmDelete();">
                                <i class="fa fa-minus-square"></i> Eliminar
                            </button>
                        </c:if>
                    </c:if>
                </div>

            </div>

            <c:if test="${requestScope.UomConversionInfo_Table ne null}">
                <div class="box box-success">
                    <div class="box-body" id="info" > 
                        <div style="margin-top: 10px;"> 
                            <c:if test="${requestScope.Method eq null or requestScope.Action eq 'Done'}"> &nbsp;

                            </c:if>
                        </div>
                        <div class="table-responsive">

                            <table id="dtable" class="table table-bordered table-striped table-hover">
                                <thead>
                                    <tr>
                                        <th></th>
                                        <th>De Unidad de Medida</th>
                                        <th>A Unidad de Medida</th>
                                        <th>Equivalencia</th>
                                        <th>Registado Por</th>
                                        <th>Fecha de Registro</th>
                                        <th>Fecha de Modificacion</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${UomConversionInfo_Table}" var="row" varStatus="status">
                                        <tr>
                                            <td>
                                                <c:if test="${requestScope.Method ne 'Edit' or (requestScope.ID == row.idUomConversion and requestScope.ID ne 'new' ) }">
                                                    <input type="checkbox" class="minimal-red" name="ID" title="${row.idUomConversion}" value="${row.idUomConversion}"
                                                           ${requestScope.ID == row.idUomConversion and requestScope.Method != 'Save' ? 'checked="checked"' : ''} onclick="radio(this);"/>
                                                </c:if>
                                            </td>
                                            <td>${row.UomFrom}</td>
                                            <td>${row.UomTo}</td>
                                            <td>${row.Conversion}</td>
                                            <td>${row.User}</td>
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
                "dom": 'T<"clear">lfrtip',
                "tableTools": {
                    "sSwfPath": "../plugins/datatables/extensions/TableTools/swf/copy_csv_xls_pdf.swf"
                },
                "columns": [
                    {"width": "2%"},
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                ]
            });
            $('form').bootstrap3Validate(function (e, data) {
            });</script>


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
