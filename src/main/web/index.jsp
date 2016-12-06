<%--
  Created by IntelliJ IDEA.
  User: chj
  Date: 2016/11/14
  Time: 13:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>metadata</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
    <script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        function loadCamera() {
            $.ajax({
                type: "GET",
                url: "/metadata/v1/camera",
                async: false,
                success: function (data) {
                    for (var key in data) {
                        $("#camera").append("<button  type=\"button\" class=\"btn btn-primary\" onclick='showType(" + data[key] + ")'>" + data[key] + "</button>")
                    }
                },
                dataType: "json"
            });
        }

        function showType(geohash) {
            $("#cameratype").html("")
            $("#time").html("")
            $("#img").html("")
            $("#commit").html("")
            $("#commit2").html("")
            $.ajax({
                type: "GET",
                url: "/metadata/v1/type/" + geohash,
                async: false,
                success: function (data) {
                    $("#time").html("")
                    $("#cameratype").append(" <label for=\"name\">选择属性</label>")
                    $("#cameratype").append("<select class=\"form-control\"  id=\"shuxing\">")
                    $("#cameratype").append("</select >")
                    var Data = data;
                    var flag = false

                    for (var key in Data) {
                        flag = true
                        $("#shuxing").append("<option>" + key + ":" + Data[key] + "</option>");
                    }

                    if (flag) {
                        $("#time").append(" StartTime: <input type=\"datetime-local\" name=\"starttime\" id=\"starttime\"/><br />")
                        $("#time").append(" EndTime: <input type=\"datetime-local\" name=\"endtime\" id=\"endtime\"/><br />")
                        $("#commit").append(" <button  type=\"button\" class=\"btn btn-primary\" onclick='showImg(" + geohash + ",1)'> commit</button><br />")

                        $("#commit2").append(" <input  type=\"text\" id=\"text\"> commit2</input><br />")
                        $("#commit2").append(" <button  type=\"button\" class=\"btn btn-primary\" onclick='showImg2(" + geohash + ",1)'> commit2</button>")

                    }

                },
                dataType: "json"
            });
        }


        function showImg(geohash, page) {
            var start = document.getElementById('starttime');
            var starttime = new Date(start.value).getTime() / 1000 - 28800;
            var end = document.getElementById('endtime');
            var endtime = new Date(end.value).getTime() / 1000 - 28800;//两边好像不统一，这边快28800

            var name = document.getElementById("shuxing").value.split(":")[0];

            $.ajax({
                type: "GET",
                url: "/metadata/v1/rowkeys?typename=" + name + "&starttime=" + starttime + "&endtime=" + endtime + "&geohash=" + geohash + "&page=" + page,
                async: false,
                success: function (data) {
                    $("#img").html("")
                    var flag = true
                    for (var key in data) {
                        if (flag) {
                            $("#img").append("<p>共有" + data[key] + "条满足条件的结果</p>")
                            $("#img").append("<button  type=\"button\" class=\"btn btn-primary\" onclick=\"download(" + "\'" + name + "\'" + "," + starttime + "," + +endtime + "," + +geohash + ")\"> 下载全部照片</button>")
                            $("#img").append("<ul class=\"pager\">" +
                                    "<li class=\"previous disabled\"><a onclick=\"showImg(" + geohash + "," + (page - 1) + ")\">&larr; Older</a></li>" +
                                    "<li class=\"next\"><a onclick=\"showImg(" + geohash + "," + (page + 1) + ")\">Newer &rarr;</a></li>" +
                                    "</ul>")
                            flag = false
                        }
                        else {
                            $("#img").append("<div class=\"col-sm-4\">" +
                                    " <a href=\"#\" class=\"thumbnail\">" +
                                    " <img src=\"http://localhost:8080/metadata/v1/image?rowkey=" + data[key].split("`")[0] + " \"border=0 width=\"300\" height=\"300\"/>" +
                                    "</a><p>data: " + data[key].split("`")[1] + "</p></div>")
                        }
                    }
                },
                dataType: "json"
            });
        }

        function download(name, starttime, endtime, geohash) {
            window.open("metadata/v1/download?typename=" + name + "&starttime=" + starttime + "&endtime=" + endtime + "&geohash=" + geohash)
        }

        function showImg2(geohash, page) {
            var start = document.getElementById('starttime');
            var starttime = new Date(start.value).getTime() / 1000 - 28800;
            var end = document.getElementById('endtime');
            var endtime = new Date(end.value).getTime() / 1000 - 28800;//两边好像不统一，这边快28800

            var name = document.getElementById("shuxing").value.split(":")[0];
            var keyword = document.getElementById("text").value;
            $("#img").html("")
            $.ajax({
                type: "GET",
                url: "/metadata/v1/rowkeysfromMap?keyword=" + keyword + "&typename=" + name + "&starttime=" + starttime + "&endtime=" + endtime + "&geohash=" + geohash,
                async: false,
                success: function (data) {
                    for (var key in data) {
                        $("#img").append("<div class=\"col-sm-4\">" +
                                " <a href=\"#\" class=\"thumbnail\">" +
                                " <img src=\"http://localhost:8080/metadata/v1/image?rowkey=" + data[key] + " \"border=0 width=\"300\" height=\"300\"/>" +
                                "</a> </div>")
                    }
                },
                dataType: "json"
            });
        }
    </script>
</head>

<body onload="loadCamera()">
<div class="container">
    <nav class="navbar navbar-inverse" role="navigation">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">视频元数据检索系统</a>
        </div>
    </nav>
    <div class="container">
        <div class="container" id="camera">
            <h4>Camera id:</h4>
        </div>
        <div class="container" id="cameratype">
        </div>

        <div class="container" id="time"></div>
        <div class="container" id="commit"></div>
        <div class="container" id="commit2"></div>
        <div class="container" id="img"></div>

    </div>
</div>
</body>
</html>
