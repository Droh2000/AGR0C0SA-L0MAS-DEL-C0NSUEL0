<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<link rel="stylesheet" href="../plugins/iCheck/all.css">
<script src="https://cdn.jsdelivr.net/npm/signature_pad@5.0.10/dist/signature_pad.umd.min.js"></script>
<style>
    label, p, ul, ol {
      color: black;
      display: inline-block;
      opacity: 1;
    }
</style>
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
        <div class="box-header with-border bg-green">
        </div><!-- /.box-header -->
        <div class="box-body bg-green">
            <!-- Search Meeting -->
            <form name="formsearch" role="form" action="driverAgreementWarehouse.do" method="post">
                <div class="box box-success">
                    <div class="box-header with-border text-center">
                        <br>
                        <h3 class="box-title">ONION WAREHOUSE - DRIVER AGREEMENT</h3>
                        <br>
                    </div><!-- /.box-header -->
                    <div class="box-body offset-md-4 text-center">
                        <div class="col-md-4"></div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <img src="../images/LogoNationaOnionGocar.jpg" class="center-block" alt="Logo National Onion Gocar" width="450" height="250">
                                
                                <div class="text-left">
                                    <p><b>Our Goal:</b> We aim to provide you with a positive warehouse experience by ensuring efficient loading and unloading. To achieve this, we ask for your full cooperation. Please read and follow the instructions below:</p>
                                    <p><b>Unloading:</b></p>
                                    <ul>
                                        <li>If your load has shifted or is on the floor, you must restack the products properly. If not, you will not be offloaded, and a lumper charge will be assessed.</li>
                                    </ul>
                                    <p><b>Loading:</b></p>
                                    <ol>
                                        <li>All trailers must be clean, dry, and free of holes before backing up to the dock. Please do not leave trash in the parking lot?trash cans and brooms are available if needed.</li>
                                        <li>You must have a minimum of two load locks or straps to secure the freight properly.</li>
                                        <li>Dry vans must have proper large produce vents at the front and back doors, and all metal roofs must be insulated for onions and produce.</li>
                                    </ol>
                                    <p><b>Loading & Unloading Procedures:</b></p>
                                    <ol>
                                        <li>Check in with the correct information, including your PU# (CJ#, CF#, DI#, OX#, JC#, CO#) and, if unloading, your 4-digit folio#.</li>
                                        <li>Park in the staging area until assigned a dock door (we will call you).</li>
                                        <li>Once backed up to the dock, we will begin loading/unloading. Please turn off your engine. The red safety hose will be disconnected to prevent forward movement and ensure safety.</li>
                                        <li>After loading or unloading, we will notify you to collect your BOL. At this time, the red hose can be reconnected.</li>
                                        <li>For loading: please brace your load, and we will provide a seal for your shipment.</li>
                                    </ol>
                                    <p><b>Facility Rules:</b></p>
                                    <ol>
                                        <li>For safety reasons, drivers are not allowed inside the warehouse unless special permission is given by management.</li>
                                        <li>No illegal substances or alcohol are permitted anywhere on the premises.</li>
                                        <li>Drinking (non-alcoholic), eating, and smoking are permitted only in designated areas.</li>
                                    </ol>
                                    <p>We take pride in maintaining a well-organized warehouse and clean facility for your comfort. Please help us keep it that way by cleaning up after yourself. We hope you leave with a positive impression.</p>
                                </div>
                                <p><b>Have an excellent day and drive safely!</b></p>
                            </div>    
                            <div class="form-group text-left">
                                <label>PU #</label>
                                <input name="pu" type="text" class="form-control pull-right" id="pu" required value=""/>
                            </div>
                            <div class="form-group text-left">
                                <label>Destination/Delivery City</label>
                                <input name="destinationDeliveryCity" type="text" class="form-control pull-right" id="destinationDeliveryCity" required value=""/>
                            </div>
                            <div class="form-group text-left">
                                <label>Type Of Trailer</label>
                                <div class="checkbox">
                                  <label><input type="checkbox" name="typeOfTrailer_Reefer" value="Reefer"> Reefer</label>
                                </div>
                                <div class="checkbox">
                                  <label><input type="checkbox" name="typeOfTrailer_VentedVan" value="Vented Van"> Vented Van</label>
                                </div>
                                <div class="checkbox">
                                  <label><input type="checkbox" name="typeOfTrailer_Flatbed" value="Flatbed"> Flatbed</label>
                                </div>
                                <div class="checkbox">
                                  <label><input type="checkbox" name="typeOfTrailer_StepDeck" value="Step deck"> Step deck</label>
                                </div>
                                <div class="checkbox">
                                  <label><input type="checkbox" name="typeOfTrailer_Conestoga" value="Conestoga"> Conestoga</label>
                                </div>
                            </div>
                            <div class="form-group text-left">
                                <label>First Name</label>
                                <input name="firstName" type="text" class="form-control pull-right" id="firstName" required value=""/>
                            </div> 
                            <div class="form-group text-left">
                                <label>Last Name</label>
                                <input name="lastName" type="text" class="form-control pull-right" id="lastName" required value=""/>
                            </div>
                            
                            <div class="form-group text-left">
                                <label>Phone</label>
                                <input name="phone" type="text" class="form-control pull-right" id="phone" required value=""/>
                            </div>
                            <div class="form-group text-left">
                                <label>Driver License #</label>
                                <input name="driverLicense" type="text" class="form-control pull-right" id="driverLicense" required value=""/>
                            </div>
                            <div class="form-group text-left">
                                <label>State Issued DL</label>
                                <input name="sateIssuedDL" type="text" class="form-control pull-right" id="sateIssuedDL" required value=""/>
                            </div>
                            <div class="form-group text-left">
                                <label>DOT #</label>
                                <input name="dot" type="text" class="form-control pull-right" id="dot" required value=""/>
                            </div>
                            <div class="form-group text-left">
                                <label>Trucking Co</label>
                                <input name="truckingCo" type="text" class="form-control pull-right" id="truckingCo" required value=""/>
                            </div>
                            <div class="form-group text-left">
                                <label>Truck #</label>
                                <input name="truck" type="text" class="form-control pull-right" id="truck" required value=""/>
                            </div>
                            <div class="form-group text-left">
                                <label>Truck plates #</label>
                                <input name="truckPlates" type="text" class="form-control pull-right" id="truckPlates" required value=""/>
                            </div>
                            <div class="form-group text-left">
                                <label>Trailer #</label>
                                <input name="trailer" type="text" class="form-control pull-right" id="trailer" required value=""/>
                            </div>
                            <div class="form-group text-left">
                                <label>Trailer plates #</label>
                                <input name="trailerPlates" type="text" class="form-control pull-right" id="trailerPlates" required value=""/>
                            </div>
                            <div class="form-group text-left">
                                <label>Empty weight (TARE)</label>
                                <input name="emptyWeightTare" type="text" class="form-control pull-right" id="emptyWeightTare" required value=""/>
                            </div>
                            <div class="form-group">
                                <p><b>GOCA / NATIONAL ONION WAREHOUSE:</b></p>
                                <p>For safety reasons, the red air hose will be disconnected to prevent the trailer from moving and causing an accident. Personnel will be designated to disconnect and reconnect the hose. The driver is not authorized to connect the hose or move the trailer unless specifically instructed to do so. The driver will be held responsible for any accidents caused by failing to follow these instructions.</p>
                            </div>
                            <div class="form-group text-left">
                                <p><b>Signature</b></p>
                                <canvas id="signature-pad" width="456" height="180" style="border:1px solid #000;"></canvas>
                                <button type="button" class="btn btn-danger" onclick="limpiarFirma()">Clean</button>
                                <input type="hidden" name="firmaBase64" id="firmaBase64">
                            </div>
                        </div>      
                    </div><!-- /.box-body -->
                    <div class="box-footer text-center">
                        <button type="submit" class="btn btn-primary"><i class="fa fa-save"></i> Save</button>
                        <input type="hidden" name="Action" value="Save"/> 
                    </div>
                </div><!-- /.box -->
            </form>
        </div><!-- /.box-body -->
    </div>
</section><!-- /.content -->


<!-- iCheck 1.0.1 -->
<script src="../plugins/iCheck/icheck.min.js"></script>

<script>
    // Validar que si seleccionen los checkbox
    document.querySelector("form").addEventListener("submit", function (e) {
        const checkboxes = document.querySelectorAll('input[name^="typeOfTrailer_"]');
        const oneChecked = Array.from(checkboxes).some(chk => chk.checked);
        if (!oneChecked) {
          e.preventDefault();
          alert("Please select the trailer type.");
        }
    });
    
    // Logica del area para la Firma digital
    var canvas = document.getElementById("signature-pad");
    var signaturePad = new SignaturePad(canvas);
    
    document.querySelector("form").addEventListener("submit", function (e) {
        if (!signaturePad.isEmpty()) {
            document.getElementById("firmaBase64").value = signaturePad.toDataURL(); // data:image/png;base64,...
        } else {
            e.preventDefault();
            alert("Please sign before continuing.");
        }    
    });
    
    function limpiarFirma(){
        signaturePad.clear();
    }
</script>

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
