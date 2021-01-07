<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>login</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
    <div>
        <button class="btn-primary" id="addButton" onclick="showAdd()">新增病人</button>
        <div id="addPatient" style="display: none">
            <form action="/Database_PJ_war_exploded/emergency/registerPatient" method="post" onsubmit="return addCheck()">
                <input name="name" type="text" placeholder="name" id="name">
                <input name="address" type="text" placeholder="address" id="address">
                <select name="gender" id="gender">
                    <option value="male">男性</option>
                    <option value="female">女性</option>
                </select>
                <input name="telephone" type="text" placeholder="telephone" id="telephone">
                <select name="evaluation" id="evaluation">
                    <option value="1">轻症</option>
                    <option value="2">重症</option>
                    <option value="3">危重症</option>
                </select>
                <button type="submit">提交</button>
                <div id="attention" style="display: none">请填入正确信息！</div>
            </form>
        </div>
    </div>
    <div>
        <button class="btn-primary" id="queryButton" onclick="showQuery()">查询病人情况</button>
        <div id="queryPatient" style="display: none">
            <div>查询特征</div>
            <select id="Indentity">
                <option value=""></option>
                <option value="Area">隔离区域</option>
                <option value="LifeStatus">生命状态</option>
                <option value="Evaluation">病情评级</option>
            </select>
            <select id="subIndentity" style="display: none"></select>
            <button onclick="query()">查询</button>
            <div id="queryAttention" style="display: none">请选择正确的查询条件</div>
            <div id="queryResult" style="display: none">
                <table class="table" id="resultTable"></table>
            </div>
        </div>
    </div>
</body>
<script>
    function showAdd() {
        if($('#addPatient')[0].style.display === 'none' || $('#addPatient')[0].style.display == null){
            $('#addPatient').css("display", "block");
        }else {
            $('#addPatient').css("display", "none");
        }
    }

    function addCheck() {
        if($('#name').val() === "" || $('#address').val() === "" || $('#gender').val() === "" ||
                $('#telephone').val() === "" || $('#evaluation').val() === ""){
            $('#attention').css("display", "block");
            return false;
        }
        return true;
    }

    function showQuery() {
        if($('#queryPatient')[0].style.display === 'none' || $('#queryPatient')[0].style.display == null){
            $('#queryPatient').css("display", "block");
        }else {
            $('#queryPatient').css("display", "none");
        }
    }

    $('#Indentity').change(function () {
        if($('#Indentity').val() == "Area"){
            $('#subIndentity').empty();
            $('#subIndentity').append("<option value='1'>轻症区</option>");
            $('#subIndentity').append("<option value='2'>重症区</option>");
            $('#subIndentity').append("<option value='3'>危重症区</option>");
            $('#subIndentity').append("<option value='4'>隔离区</option>");
            $('#subIndentity').css("display", "block");
        }
        else if($('#Indentity').val() == "LifeStatus"){
            $('#subIndentity').empty();
            $('#subIndentity').append("<option value='1'>已治愈</option>");
            $('#subIndentity').append("<option value='2'>住院</option>");
            $('#subIndentity').append("<option value='3'>死亡</option>");
            $('#subIndentity').css("display", "block");
        }
        else if($('#Indentity').val() == "Evaluation"){
            $('#subIndentity').empty();
            $('#subIndentity').append("<option value='1'>轻症</option>");
            $('#subIndentity').append("<option value='2'>重症</option>");
            $('#subIndentity').append("<option value='3'>危重症</option>");
            $('#subIndentity').css("display", "block");
        }
        else {
            $('#subIndentity').empty();
            $('#subIndentity').css("display", "none");
        }
    });
    
    function query() {
        if($('#Indentity').val() == ""){
            $('#queryAttention').css("display", "block");
            return;
        }else {
            $('#queryAttention').css("display", "none");
            var p = $('#Indentity').val();
            p = p.replace(p[0], p[0].toLowerCase());
            var d = {};
            d[p] = $('#subIndentity').val();
            $.post('/Database_PJ_war_exploded/emergency/queryPatientsBy' + $('#Indentity').val(), d,
                function (result) {
                    result2Table(JSON.parse(result)["patients"]);
                }
            );
        }
    }

    var id2Area = ['', '轻症区', "重症区", "危重症区", "隔离区"];
    var id2Status = ['', "已治愈", "住院", "死亡"];
    var id2Evaluation = ['', "轻症", "重症", "危重症"];

    function result2Table(data) {
        $('#resultTable').empty();
        $('#resultTable').append(
            "<tr>" +
            "<th>姓名</th>" +
            "<th>性别</th>" +
            "<th>电话号码</th>" +
            "<th>家庭地址</th>" +
            "<th>病情评级</th>" +
            "<th>生命状态</th>" +
            "<th>区域</th>" +
            "<th>负责护士</th>" +
            "</tr>"
        );
        for(i=0; i<data.length; i++){
            var p = data[i];
            console.log(p);
            $('#resultTable').append(
                "<tr>" +
                "<td>" + p["name"] + "</td>" +
                "<td>" + p["gender"] + "</td>" +
                "<td>" + p["telephone"] + "</td>" +
                "<td>" + p["address"] + "</td>" +
                "<td>" + id2Evaluation[p["evaluation"]] + "</td>" +
                "<td>" + id2Status[p["lifeStatus"]] + "</td>" +
                "<td>" + id2Area[p["areaId"]] + "</td>" +
                "<td>" + p["nurseName"] + "</td>" +
                "</tr>"
            );
        }
        $('#queryResult').css("display", "block");
    }
</script>
</html>
