<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script src="../plugins/highcharts/js/highcharts.js" type="text/javascript"></script>
<script src="../plugins/highcharts/js/modules/exporting.js" type="text/javascript"></script>
<link href="../plugins/highcharts/css/highcharts.css" rel="stylesheet" type="text/css" />

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Tablero Electrónico
            <small></small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-caret-right"></i> Reportes</a></li>
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


        <div class="row">
            <div class="col-md-3 col-sm-6 col-xs-12">
                <div class="info-box">
                    <span class="info-box-icon bg-aqua"><i class="fa fa-flask"></i></span>
                    <div class="info-box-content">
                        <span class="info-box-text">Aplicaciones</span>
                        <span class="info-box-number">${requestScope.PendingApplicationQty}</span>
                    </div><!-- /.info-box-content -->
                </div><!-- /.info-box -->
            </div><!-- /.col -->
            <div class="col-md-3 col-sm-6 col-xs-12">
                <div class="info-box">
                    <span class="info-box-icon bg-green"><i class="fa fa-cubes"></i></span>
                    <div class="info-box-content">
                        <span class="info-box-text">Cosechas</span>
                        <span class="info-box-number">${requestScope.PendingPickingQty}</span>
                    </div><!-- /.info-box-content -->
                </div><!-- /.info-box -->
            </div><!-- /.col -->
            <div class="col-md-3 col-sm-6 col-xs-12">
                <div class="info-box">
                    <span class="info-box-icon bg-yellow"><i class="fa fa-sun-o"></i></span>
                    <div class="info-box-content">
                        <span class="info-box-text">Jornales</span>
                        <span class="info-box-number">${requestScope.PendingJournalQty}</span>
                    </div><!-- /.info-box-content -->
                </div><!-- /.info-box -->
            </div><!-- /.col -->
            <div class="col-md-3 col-sm-6 col-xs-12">
                <div class="info-box">
                    <span class="info-box-icon bg-red"><i class="fa fa-umbrella"></i></span>
                    <div class="info-box-content">
                        <span class="info-box-text">Riegos</span>
                        <span class="info-box-number">${requestScope.PendingIrrigationQty}</span>
                    </div><!-- /.info-box-content -->
                </div><!-- /.info-box -->
            </div><!-- /.col -->
        </div><!-- /.row -->



        <div class="row">
            <div class="col-md-3 col-sm-6 col-xs-12">
                <div class="box box-primary">
                    <div class="box-header">
                        <div class="pull-right box-tools">
                        </div>
                        <h3 class="box-title">
                        </h3>
                    </div>
                    <div class="box-body">
                        <div id="container"></div>

                        <script type="text/javascript">

                                    Highcharts.chart('container', {
                                    chart: {
                                    plotBackgroundColor: null,
                                            plotBorderWidth: null,
                                            plotShadow: false,
                                            type: 'pie'
                                    },
                                            title: {
                                            text: 'Aplicaciones'
                                            },
                                            tooltip: {
                                            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                                            },
                                            plotOptions: {
                                            pie: {
                                            allowPointSelect: true,
                                                    cursor: 'pointer',
                                                    dataLabels: {
                                                    enabled: true,
                                                            format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                                                            style: {
                                                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                                                            }
                                                    }
                                            }
                                            },
                                            series: [{
                                            name: 'Estatus',
                                                    colorByPoint: true,
                                                    data: [
                            <c:forEach items="${ApplicationChart_Table}" var="item">
                                                    {
                                                    name: '${item.TaskType}',
                                                            y: ${item.Perc}
                                                    },
                            </c:forEach>
                                                    ]
                                            }]
                                    });</script>                            
                    </div><!-- /.box-body-->                        
                </div>                


            </div><!-- /.col -->
            <div class="col-md-3 col-sm-6 col-xs-12">
                <div class="box box-primary">
                    <div class="box-header">
                        <div class="pull-right box-tools">
                        </div>
                        <h3 class="box-title">
                        </h3>
                    </div>
                    <div class="box-body">
                        <div id="container2"></div>

                        <script type="text/javascript">

                                    Highcharts.chart('container2', {
                                    chart: {
                                    plotBackgroundColor: null,
                                            plotBorderWidth: null,
                                            plotShadow: false,
                                            type: 'pie'
                                    },
                                            title: {
                                            text: 'Cosechas'
                                            },
                                            tooltip: {
                                            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                                            },
                                            plotOptions: {
                                            pie: {
                                            allowPointSelect: true,
                                                    cursor: 'pointer',
                                                    dataLabels: {
                                                    enabled: true,
                                                            format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                                                            style: {
                                                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                                                            }
                                                    }
                                            }
                                            },
                                            series: [{
                                            name: 'Estatus',
                                                    colorByPoint: true,
                                                    data: [
                            <c:forEach items="${PickingChart_Table}" var="item">
                                                    {
                                                    name: '${item.TaskType}',
                                                            y: ${item.Perc}
                                                    },
                            </c:forEach>
                                                    ]
                                            }]
                                    });</script>                            
                    </div><!-- /.box-body-->                        
                </div>

            </div><!-- /.col -->
            <div class="col-md-3 col-sm-6 col-xs-12">
                <div class="box box-primary">
                    <div class="box-header">
                        <div class="pull-right box-tools">
                        </div>
                        <h3 class="box-title">
                        </h3>
                    </div>
                    <div class="box-body">
                        <div id="container3"></div>

                        <script type="text/javascript">

                                    Highcharts.chart('container3', {
                                    chart: {
                                    plotBackgroundColor: null,
                                            plotBorderWidth: null,
                                            plotShadow: false,
                                            type: 'pie'
                                    },
                                            title: {
                                            text: 'Jornal'
                                            },
                                            tooltip: {
                                            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                                            },
                                            plotOptions: {
                                            pie: {
                                            allowPointSelect: true,
                                                    cursor: 'pointer',
                                                    dataLabels: {
                                                    enabled: true,
                                                            format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                                                            style: {
                                                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                                                            }
                                                    }
                                            }
                                            },
                                            series: [{
                                            name: 'Estatus',
                                                    colorByPoint: true,
                                                    data: [
                            <c:forEach items="${JournalChart_Table}" var="item">
                                                    {
                                                    name: '${item.TaskType}',
                                                            y: ${item.Perc}
                                                    },
                            </c:forEach>
                                                    ]
                                            }]
                                    });</script>                            
                    </div><!-- /.box-body-->                        
                </div>

            </div><!-- /.col -->
            <div class="col-md-3 col-sm-6 col-xs-12">
                <div class="box box-primary">
                    <div class="box-header">
                        <div class="pull-right box-tools">
                        </div>
                        <h3 class="box-title">
                        </h3>
                    </div>
                    <div class="box-body">
                        <div id="container4"></div>

                        <script type="text/javascript">

                                    Highcharts.chart('container4', {
                                    chart: {
                                    plotBackgroundColor: null,
                                            plotBorderWidth: null,
                                            plotShadow: false,
                                            type: 'pie'
                                    },
                                            title: {
                                            text: 'Riego'
                                            },
                                            tooltip: {
                                            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                                            },
                                            plotOptions: {
                                            pie: {
                                            allowPointSelect: true,
                                                    cursor: 'pointer',
                                                    dataLabels: {
                                                    enabled: true,
                                                            format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                                                            style: {
                                                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                                                            }
                                                    }
                                            }
                                            },
                                            series: [{
                                            name: 'Estatus',
                                                    colorByPoint: true,
                                                    data: [
                            <c:forEach items="${IrrigationChart_Table}" var="item">
                                                    {
                                                    name: '${item.TaskType}',
                                                            y: ${item.Perc}
                                                    },
                            </c:forEach>
                                                    ]
                                            }]
                                    });
                        </script>                            
                    </div><!-- /.box-body-->                        
                </div>

            </div><!-- /.col -->
        </div><!-- /.row -->


        <div class="row">
            <div class="col-md-3 col-sm-6 col-xs-12">
                <div class="box box-success">
                    <div class="box-header with-border">
                        <h3 class="box-title">Riego por Campos</h3>
                        <div class="box-tools pull-right">
                            <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                            <button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
                        </div>
                    </div><!-- /.box-header -->
                    <div class="box-body">
                        <div class="table-responsive">
                            <table class="table no-margin">
                                <thead>
                                    <tr>
                                        <th>Campo</th>
                                        <th>Cantidad</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${TaskByCamp_Table}" var="row" varStatus="status">
                                        <tr>
                                            <td>${row.CampName}</td>
                                            <td><span class="label label-success pull-right">${row.Qty}</span></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div><!-- /.box-body -->
                </div>
            </div>
            <div class="col-md-3 col-sm-6 col-xs-12">
                <div class="box box-success">
                    <div class="box-header with-border">
                        <h3 class="box-title">Riego por Secciones</h3>
                        <div class="box-tools pull-right">
                            <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                            <button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
                        </div>
                    </div><!-- /.box-header -->
                    <div class="box-body">
                        <div class="table-responsive">
                            <table class="table no-margin">
                                <thead>
                                    <tr>
                                        <th>Campo</th>
                                        <th>Cantidad</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${TaskBySection_Table}" var="row" varStatus="status">
                                        <tr>
                                            <td>${row.SectionName}</td>
                                            <td><span class="label label-success pull-right">${row.Qty}</span></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div><!-- /.box-body -->
                </div>
            </div>
            <div class="col-md-3 col-sm-6 col-xs-12">

            </div>
            <div class="col-md-3 col-sm-6 col-xs-12">

            </div>
        </div>





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
