<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Docudile</title>

    <link rel="stylesheet" href="${"/resources/css/bootstrap.min.css"}">
    <link rel="stylesheet" href="${"/resources/bootflat/css/bootflat.css"}">
    <link rel="stylesheet" href="${"/resources/css/bootstrap-treenav.css"}">
    <link rel="stylesheet" href="${"/resources/fonts/font-awesome/css/font-awesome.min.css"}">
    <link rel="stylesheet" href="${"/resources/css/site.css"}">

    <link rel="icon"
          type="image/png"
          href="${"/resources/img/logo.png"}">
</head>
<body>
<header>
    <nav class="navbar dd-home-navbar">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                        data-target="#bs-example-navbar-collapse-2" aria-expanded="false"><span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
                </button>
                <a href="#" class="dd-home-navbar-logo pull-left"><img src="${"/resources/img/logo-inverted.png"}"></a>
                <a class="navbar-brand dd-brand" href="#">docu dile</a></div>
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-2">
                <form class="navbar-form navbar-left dd-search" role="search">
                    <div class="form-search search-only">
                        <i class="search-icon glyphicon glyphicon-search"></i>
                        <input type="text" class="form-control search-query">
                    </div>
                </form>
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav navbar-right dd-nav-links">
                        <li><p>Paul Ryan</p></li>
                        <li><a href="#"><i class="fa fa-cog"></i></a></li>
                        <li><a href="#"><i class="fa fa-user"></i></a></li>
                    </ul>
                </div>
            </div>
        </div>
    </nav>
</header>

<main>
    <div class="container-fluid dd-breadcrumbs">
        <div class="row">
            <div class="col-sm-2">

            </div>
            <div class="col-sm-7">
                <ol class="breadcrumb">
                    <li><a href="#">Docudile</a></li>
                    <li><a href="#">Memo</a></li>
                    <li class="active"><a href="#">2016</a></li>
                    <li class="active">MOA</li>
                </ol>
            </div>
            <div class="col-sm-3">

            </div>
        </div>
    </div>
    <div class="container-fluid">
        <div class="row">
            <div class="col-sm-2 dd-navtree">
                <ul class="nav nav-pills nav-stacked nav-tree" id="myTree" data-toggle="nav-tree" data-nav-tree-expanded="fa fa-folder-open-o" data-nav-tree-collapsed="fa fa-folder-o">
                    <li>
                        <a href="#">Docudile</a>
                        <ul class="nav nav-pills nav-stacked nav-tree">
                            <li>
                                <a href="#">Memo</a>
                                <ul class="nav nav-pills nav-stacked nav-tree">
                                    <li>
                                        <a href="#">2015</a>
                                        <ul class="nav nav-pills nav-stacked nav-tree">
                                            <li>
                                                <a href="#">MOA</a>
                                            </li>
                                        </ul>
                                    </li>
                                    <li>
                                        <a href="#">2016</a>
                                        <ul class="nav nav-pills nav-stacked nav-tree">
                                            <li class="active">
                                                <a href="#">MOA</a>
                                            </li>
                                        </ul>
                                    </li>
                                </ul>
                            </li>
                            <li>
                                <a href="#">Letter</a>
                                <ul class="nav nav-pills nav-stacked nav-tree">
                                    <li>
                                        <a href="#">2015</a>
                                    </li>
                                    <li>
                                        <a href="#">2016</a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
            <div class="col-sm-7 dd-filebox">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th class="col-sm-6">Name</th>
                        <th class="col-sm-2">Owner</th>
                        <th class="col-sm-2">Last modified</th>
                        <th class="col-sm-2"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td><i class="fa fa-file-image-o"></i> 021416 Moa.docx</td>
                        <td>me</td>
                        <td>Feb 14, 2015</td>
                        <td><a href="#"><i class="fa fa-trash-o"></i></a></td>
                    </tr>
                    <tr>
                        <td><i class="fa fa-file-image-o"></i> 021416 Moa.docx</td>
                        <td>me</td>
                        <td>Feb 14, 2015</td>
                        <td><a href="#"><i class="fa fa-trash-o"></i></a></td>
                    </tr>
                    <tr>
                        <td><i class="fa fa-file-image-o"></i> 021416 Moa.docx</td>
                        <td>me</td>
                        <td>Feb 14, 2015</td>
                        <td><a href="#"><i class="fa fa-trash-o"></i></a></td>
                    </tr>
                    <tr>
                        <td><i class="fa fa-file-image-o"></i> 021416 Moa.docx</td>
                        <td>me</td>
                        <td>Feb 14, 2015</td>
                        <td><a href="#"><i class="fa fa-trash-o"></i></a></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="col-sm-3">

            </div>
        </div>
    </div>
</main>

<script rel="script" src="${"/resources/js/jquery-2.1.3.min.js"}"></script>
<script rel="script" src="${"/resources/js/bootstrap.min.js"}"></script>
<script rel="script" src="${"/resources/js/bootstrap-treenav.js"}"></script>
</body>
</html>