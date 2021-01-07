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
    <button class="btn-primary" onclick="showQuery()">查询病人情况</button>
    <div id="queryPatient" style="display: none">
        <div>查询条件</div>
        <select id="Indentity">
            <option value="AllPatientsInArea">区域内所有病人</option>
            <option value="PatientsWaitingToTransfer">等待转移病人</option>
            <option value="PatientsByLifeStatus">病人生命状态</option>
            <option value="PatientsWaitingToDischarge">等待出院病人</option>
        </select>
        <select id="subIndentity" style="display: none">
            <option value="1">轻症</option>
            <option value="2">重症</option>
            <option value="3">危重症</option>
        </select>
        <button onclick="submitQuery()">查询</button>
    </div>
    <div id="queryResult" style="display: none">
        <table class="table" id="resultTable"><</table>
    </div>
</div>

<div class="modal fade" id="Modal" tabindex="-1" role="dialog" aria-labelledby="ModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="ModalLabel"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" id="modalBody"></div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="modalOKButton">确定</button>
            </div>
        </div>
    </div>
</div>

<script>
    function showQuery() {
        if($('#queryPatient')[0].style.display === 'none' || $('#queryPatient')[0].style.display == null){
            $('#queryPatient').css("display", "block");
        }else {
            $('#queryPatient').css("display", "none");
        }
    };

    $('#Indentity').change(function () {
        if($('#Indentity').val() == "PatientsByLifeStatus"){
            $('#subIndentity').css("display", "block");
        }else {
            $('#subIndentity').css("display", "none");
        }
    });

    function submitQuery() {
        var d = {}
        if($('#Indentity') == "PatientsByLifeStatus"){
            d['lifeStatus'] = $('#subIndentity').val();
        }
        $.post(
            "/Database_PJ_war_exploded/doctor/query" + $('#Indentity').val(), d,
            function (result) {
                result2Table(JSON.parse(result)["patients"])
            }
        );
    };

    var id2Area = ['', '轻症区', "重症区", "危重症区", "隔离区"];
    var id2Status = ['', "已治愈", "住院", "死亡"];
    var id2Evaluation = ['', "轻症", "重症", "危重症"];
    var patientsIds = [];

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
            "<th>修改病情评级</th>" +
            "<th>修改生命状态</th>" +
            "<th>进行核酸检测</th>" +
            "<th>康复出院</th>" +
            "</tr>"
        );
        patientsIds = [''];
        for(i=0; i<data.length; i++){
            var p = data[i];
            patientsIds.push(data[i]["patientId"])
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
                "<td><button class='btn-primary' onclick='updateEvaluation($(this).parent().parent().index())'>修改病情评级</button></td>" +
                "<td><button class='btn-primary' onclick='updateLifeStatus($(this).parent().parent().index())'>修改生命状态</button></td>" +
                "<td><button class='btn-primary' onclick='addTest($(this).parent().parent().index()'>进行核酸检测</button></td>" +
                "<td><button class='btn-primary' onclick='outHospital($(this).parent().parent().index()'>康复出院</button></td>" +
                "</tr>"
            );
        }
        $('#queryResult').css("display", "block");
    };
    
    function updateEvaluation(index) {
        let id = patientsIds[index];
        $('#ModalLabel').text("修改病情评级");
        $('#modalBody').empty();
        $('#modalBody').append(
            "<select id='updateEvaSelect'>" +
            "<option value='1'>轻症</option>" +
            "<option value='2'>重症</option>" +
            "<option value='3'>危重症</option>" +
            "</select>"
        );
        $('#modalOKButton').click(function () {
            var d = {};
            d["patientId"] = id;
            d["evaluation"] = $('#updateEvaSelect').val();
            $.post("/Database_PJ_war_exploded/doctor/updateEvaluationOfPatient", d,
                function (result) {
                    console.log(result);
                }
            )
        });
        $('#Modal').modal('show');
    };
    
    function updateLifeStatus(index) {
        console.log(dd);
    };
    
    function addTest() {
        
    };
    
    function outHospital() {

    };
</script>
</body>
</html>
