<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<header class="main-header">
    <!-- Logo -->
    <div class="logo">
        <!-- logo for regular state and mobile devices -->
        <span class="logo-lg"><img style="padding-top: 10px;" class="img-responsive" src="../images/header-text.png" alt="logo"/></span>
    </div>
    <!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <ul class="nav navbar-nav">
<!--                <li><a href="shippingReport.do">Shipments</a></li>
                <li><a href="deliveryReport.do">Deliveries</a></li>
                <li><a href="shippingHistoryReport.do">Shipping Report</a></li>
                <li><a href="deliveryHistoryReport.do">Delivery Report</a></li>
                <li><a href="warehouseReport.do">Inventory</a></li>
                <li><a href="warehouseHistoryReport.do">Inventory History</a></li>
                <li><a href="consolidateWarehousePallets.do">Consolidate Pallets</a></li>-->
                <!--<li><a href="${requestScope['javax.servlet.forward.context_path']}/public/logout">Exit</a></li>-->
                <li><a href="${requestScope['javax.servlet.forward.context_path']}/protected/main.do">Exit</a></li>
            </ul>
        </div>
    </nav>
</header>
