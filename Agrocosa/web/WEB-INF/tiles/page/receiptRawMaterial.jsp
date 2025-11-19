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
</script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Recibo de Insumos
            <small>Recibir insumos y asignar ubicación</small>
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


        <!-- Search Meeting -->
        <form name="formsearch" role="form" action="receiptRawMaterial.do" method="post">
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title">Seleccionar insumos para dar entrada</h3>
                </div><!-- /.box-header -->
                <div class="box-body">
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
                        <div class="form-group">
                            <label >Unidad de Medida</label>
                            <input type="text" name="uom" id="uom" class="form-control numeric" placeholder="" data-title="" required readonly/> 
                        </div> 
                    </div>
                    <div class="col-lg-10">
                        <div class="form-group">
                            <label for="comment">Comentarios:</label>
                            <textarea name="comments" class="form-control" rows="5" id="comments"></textarea>
                        </div>
                    </div>
                </div>


                <div class="box-footer">
                    <button type="submit" class="btn btn-primary"><i class="fa fa-save"></i> Guardar</button>
                    <input type="hidden" name="Action" value="Save"/>    
                </div>
            </div><!-- /.box -->
        </form>        


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
