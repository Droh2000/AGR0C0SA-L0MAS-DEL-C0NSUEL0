<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <!--<meta http-equiv="refresh" content="60"/>-->
        <title><tiles:getAsString name="title"/></title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport"/>
        <!-- Bootstrap 3.3.5 -->
        <link rel="stylesheet" href="../bootstrap/css/bootstrap.min.css"/>
        <!-- Font Awesome -->
        <link href="../plugins/font-awesome-4.3.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />        
        <!-- Ionicons -->
        <link href="../plugins/ionicons-2.0.1/css/ionicons.min.css" rel="stylesheet" type="text/css" />         
        <!-- Theme style -->
        <link rel="stylesheet" href="../dist/css/AdminLTE.min.css"/>        
        <link rel="shortcut icon" href="../images/asticon.jpg" type="image/jpeg" />
        <script src="../plugins/jQuery/jQuery-2.1.4.min.js"></script>
        <script src="../bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
        <script src="../dist/js/app.min.js" type="text/javascript"></script>

        <!-- AdminLTE Skins. Choose a skin from the css/skins
             folder instead of downloading all of them to reduce the load. -->
        <link rel="stylesheet" href="../dist/css/skins/_all-skins.min.css"/>
        <!--DataTables-->
        <link href="../plugins/datatables/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />
        <link href="../plugins/datatables/extensions/TableTools/css/dataTables.tableTools.css" rel="stylesheet" type="text/css" />
        <script src="../plugins/datatables/jquery.dataTables.min.js" type="text/javascript"></script>
        <script src="../plugins/datatables/dataTables.bootstrap.min.js" type="text/javascript"></script>
        <script src="../plugins/datatables/extensions/TableTools/js/dataTables.tableTools.js" type="text/javascript"></script> 
        <!--FormValidator-->
        <script src="../plugins/validator/validator.min.js" type="text/javascript"></script>

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
            <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
    </head>
    <body class="skin-green-light">
        <div>
            <div>
                <tiles:insertAttribute name="publicheader"/>
            </div>
            <div>
                <tiles:insertAttribute name="publicbody"/>
            </div>
        </div>
    </body>
</html>
