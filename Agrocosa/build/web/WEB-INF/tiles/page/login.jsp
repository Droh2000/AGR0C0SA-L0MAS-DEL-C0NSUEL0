<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title>Agrocosa</title>
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
        <!-- iCheck -->
        <link rel="stylesheet" href="../plugins/iCheck/square/blue.css"/>

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
            <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->

        <style>
            /* CSS used here will be applied after bootstrap.css */

            body { 
                background: url('../images/bg_login.jpg') no-repeat center center fixed; 
                -webkit-background-size: cover;
                -moz-background-size: cover;
                -o-background-size: cover;
                background-size: cover;
            }

            .panel-default {
                opacity: 0.9;
                margin-top:30px;
            }
            .form-group.last {
                margin-bottom:0px;
            }            
        </style>

    </head>
    <body class="hold-transition skin-green-light sidebar-mini">        
        <div class="container">
            <div class="row">
                <div class="col-md-4 col-md-offset-4">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="social-auth-links text-center">
                                <img src="../images/logo.png" alt="logo"/>
                            </div>
                        </div>
                        <div class="panel-body">
                            <form action="authentication" method="post">
                                <div class="form-group">
                                    <label for="inputEmail3" class="col-sm-3 control-label">
                                        Usuario</label>
                                    <div class="col-sm-9">
                                        <input type="text" name ="username" autofocus class="form-control" placeholder="usuario" required="">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="inputPassword3" class="col-sm-3 control-label">
                                        Contraseña</label>
                                    <div class="col-sm-9">
                                        <input type="password" name="password" class="form-control" placeholder="contaseña" required="">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-offset-3 col-sm-9">
                                        <div class="checkbox">
                                            <label>&nbsp;</label>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group last">
                                    <div class="col-sm-offset-3 col-sm-9 pull-right">
                                        <button type="submit" class="btn btn-success btn-sm pull-right">Entrar</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="panel-footer"></div>
                    </div>
                </div>
            </div>
        </div>

        <c:choose>
            <c:when test="${sessionScope.error ne null}">
                <div id="msg" class="alert alert-danger" role="alert">
                    <center><c:out value="${sessionScope.error}" /></center>
                </div>
            </c:when>
        </c:choose>


        <!-- jQuery 2.1.4 -->
        <script src="../plugins/jQuery/jQuery-2.1.4.min.js"></script>
        <!-- Bootstrap 3.3.5 -->
        <script src="../bootstrap/js/bootstrap.min.js"></script>
        <!-- iCheck -->
        <script src="../plugins/iCheck/icheck.min.js"></script>
        <script>
            $(function () {
                $('input').iCheck({
                    checkboxClass: 'icheckbox_square-blue',
                    radioClass: 'iradio_square-blue',
                    increaseArea: '20%' // optional
                });
            });
        </script>
    </body>
</html>
