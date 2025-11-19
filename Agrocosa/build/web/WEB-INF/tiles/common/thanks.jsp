<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
    h3, label, p, ul, ol {
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
                                <br>
                                <h3>Thank you! Your response has been submitted.</h3>
                                <br>
                                <img src="../images/LogoNationaOnionGocar.jpg" class="center-block" alt="Logo National Onion Gocar" width="450" height="250">
                                <h3>Have an excellent day</h3>
                            </div>
                        </div>      
                    </div><!-- /.box-body -->
                    <div class="box-footer text-center">
                    </div>
                </div><!-- /.box -->
            </form>
        </div><!-- /.box-body -->
    </div>
</section><!-- /.content -->

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
