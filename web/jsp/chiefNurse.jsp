<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>login</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="http://lib.h-ui.net/bootstrap-datetimepicker/bootstrap-datetimepicker.js"></script>
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
        <table class="table" id="resultTable"></table>
    </div>
    <div>
        <h2>病房护士</h2>
        <div>
            <button class="btn-primary" onclick="addWardNurse()">新增病房护士</button>
        </div>
        <div class="accordion" id="accordion"></div>
    </div>
    <div>
        <h2>床位</h2>
        <div class="accordion" id="accordion1"></div>
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

</body>

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
        var d = {};
        if($('#Indentity').val() == "PatientsByLifeStatus"){
            d['lifeStatus'] = $('#subIndentity').val();
        }
        $.post(
            "/Database_PJ_war_exploded/chiefNurse/query" + $('#Indentity').val(), d,
            function (result) {
                result2Table(JSON.parse(result)["patients"])
            }
        );
    };

    var id2Area = ['', '轻症区', "重症区", "危重症区", "隔离区"];
    var id2Status = ['', "已治愈", "住院", "死亡"];
    var id2Evaluation = ['', "轻症", "重症", "危重症"];
    var patientsIds = [];
    var wardNurseIds = [];

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
                "</tr>"
            );
        }
        $('#queryResult').css("display", "block");
    };

    function addWardNurse(){
        $('#ModalLabel').text("新增病房护士");
        $('#modalBody').empty();
        $('#modalBody').append(
            "<div>姓名<input class='input-group' type='text' id='addName' style='max-width: 300px; height: 40px' placeholder='name'></div>" +
            "<div>密码<input class='input-group' type='text' id='addPassword' style='max-width: 300px; height: 40px' placeholder='password'></div>"
        );
        $('#modalOKButton').unbind('click').click(function () {
            var d = {};
            if ($('addName').val() == "" || $('#addPassword').val() == ""){
                alert("账户名密码不能为空");
                return;
            }
            d["name"] = $('#addName').val();
            d["password"] = $('#addPassword').val();
            $.post("/Database_PJ_war_exploded/chiefNurse/addWardNurse", d,
                function (result) {
                    result = JSON.parse(result);
                    if(result["message"] == "success"){
                        alert("成功添加");
                        window.location.reload();
                    }else {
                        alert("添加失败，" + result["message"]);
                    }
                }
            )
        });
        $('#Modal').modal('show');
    }

    $(document).ready(function () {
        d = {};
        $.post("/Database_PJ_war_exploded/chiefNurse/queryWardNurse", d,
            function (result) {
                result = JSON.parse(result);
                wardNurseIds = [];
                for(i=0; i<result["wardNurses"].length; i++){
                    let p = result["wardNurses"][i];
                    let id = p["wardNurseId"];
                    wardNurseIds.push(id);
                    let name = p["name"];
                    let dd = {};
                    dd["nurseId"] = id;
                    let tableElement = $('<table class="table" id="resultTable"></table>');
                    tableElement.append(
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
                    $.post('/Database_PJ_war_exploded/chiefNurse/queryPatientsByNurse', dd,
                        function (result) {
                            var data = JSON.parse(result)["patients"];
                            for(i=0; i<data.length; i++){
                                let q = data[i];
                                tableElement.append(
                                    "<tr>" +
                                    "<td>" + q["name"] + "</td>" +
                                    "<td>" + q["gender"] + "</td>" +
                                    "<td>" + q["telephone"] + "</td>" +
                                    "<td>" + q["address"] + "</td>" +
                                    "<td>" + id2Evaluation[q["evaluation"]] + "</td>" +
                                    "<td>" + id2Status[q["lifeStatus"]] + "</td>" +
                                    "<td>" + id2Area[q["areaId"]] + "</td>" +
                                    "<td>" + q["nurseName"] + "</td>" +
                                    "</tr>"
                                );
                            }
                        }
                    );
                    var cardElement = $("<div class='card'></div>");
                    var headerElement = $("<div class='card-header' id='heading" + id + "'>" +
                        "      <h2 class='mb-0'>" +
                        "        <button class='btn btn-link' type='button' data-toggle='collapse' data-target='#collapse" + id + "' aria-expanded='true' aria-controls='collapse" + id + "'>" +
                        name +
                        "        </button>" +
                        "        </h2><button class='btn-warning' onclick='deleteWardNurse($(this).parent().parent().index())'>删除该病床护士</button>" +
                        "    </div>");
                    var collapseElement = $("<div id='collapse" + id + "' class='collapse show' aria-labelledby='heading" + id + "' data-parent='#accordion'></div>");
                    var bodyElement = $("<div class=\"card-body\"></div>");
                    bodyElement.append(tableElement);
                    collapseElement.append(bodyElement);
                    cardElement.append(headerElement);
                    cardElement.append(collapseElement);
                    $('#accordion').append(cardElement);
                }
            }
        );

        $.post("/Database_PJ_war_exploded/chiefNurse/queryBeds", d,
            function (result) {
                result = JSON.parse(result);
                for(i=0; i<result["beds"].length; i++){
                    let p = result["beds"][i];
                    let id = p["bedId"];
                    let dd = {};
                    dd["bedId"] = id;
                    let tableElement = $('<table class="table" id="resultTable1"></table>');
                    tableElement.append(
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
                    $.post('/Database_PJ_war_exploded/chiefNurse/queryPatientByBed', dd,
                        function (result) {
                            var q = JSON.parse(result)["patient"];
                            tableElement.append(
                                "<tr>" +
                                "<td>" + q["name"] + "</td>" +
                                "<td>" + q["gender"] + "</td>" +
                                "<td>" + q["telephone"] + "</td>" +
                                "<td>" + q["address"] + "</td>" +
                                "<td>" + id2Evaluation[q["evaluation"]] + "</td>" +
                                "<td>" + id2Status[q["lifeStatus"]] + "</td>" +
                                "<td>" + id2Area[q["areaId"]] + "</td>" +
                                "<td>" + q["nurseName"] + "</td>" +
                                "</tr>"
                            );
                        }
                    );
                    var cardElement = $("<div class='card'></div>");
                    var headerElement = $("<div class='card-header' id='headingbed" + id + "'>" +
                        "      <h2 class='mb-0'>" +
                        "        <button class='btn btn-link' type='button' data-toggle='collapse' data-target='#collapsebed" + id + "' aria-expanded='true' aria-controls='collapsebed" + id + "'>" +
                        id +
                        "        </button>" +
                        "        </h2>" +
                        "    </div>");
                    var collapseElement = $("<div id='collapsebed" + id + "' class='collapse show' aria-labelledby='headingbed" + id + "' data-parent='#accordion1'></div>");
                    var bodyElement = $("<div class=\"card-body\"></div>");
                    bodyElement.append(tableElement);
                    collapseElement.append(bodyElement);
                    cardElement.append(headerElement);
                    cardElement.append(collapseElement);
                    $('#accordion1').append(cardElement);
                }
            }
        );
    });
    
    function deleteWardNurse(index) {
        let id = wardNurseIds[index];
        $('#ModalLabel').text("删除病房护士");
        $('#modalBody').empty();
        $('#modalOKButton').unbind('click').click(function () {
            var d = {};
            d["nurseId"] = id;
            $.post("/Database_PJ_war_exploded/chiefNurse/deleteWardNurse", d,
                function (result) {
                    result = JSON.parse(result);
                    if(result["message"] == "delete success"){
                        alert("成功删除");
                        window.location.reload();
                    }else {
                        alert("删除失败，" + result["message"]);
                    }
                }
            )
        });
        $('#Modal').modal('show');
    }

</script>
</html>
