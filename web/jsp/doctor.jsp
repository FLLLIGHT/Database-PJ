<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>doctor</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="http://lib.h-ui.net/bootstrap-datetimepicker/bootstrap-datetimepicker.js"></script>
</head>
<body>
<div><a href="/Database_PJ_war_exploded/jsp/account.jsp" class="btn-primary">个人信息&站内信</a></div>
<div>
    <form action="/Database_PJ_war_exploded/account/logout" method="post">
        <button type="submit" class="btn-primary">登出</button>
    </form>
</div>
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
    <div>
        <h2 id="chiefNurse"></h2>
    </div>
    <div>
        <h2>病房护士</h2>
        <div class="accordion" id="accordion"></div>
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
        var d = {}
        if($('#Indentity').val() == "PatientsByLifeStatus"){
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
                "<td><button class='btn-primary' onclick='addTest($(this).parent().parent().index())'>进行核酸检测</button></td>" +
                "<td><button class='btn-primary' onclick='outHospital($(this).parent().parent().index())'>康复出院</button></td>" +
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
        $('#modalOKButton').unbind('click').click(function () {
            var d = {};
            d["patientId"] = id;
            d["evaluation"] = $('#updateEvaSelect').val();
            $.post("/Database_PJ_war_exploded/doctor/updateEvaluationOfPatient", d,
                function (result) {
                    result = JSON.parse(result);
                    if(result["message"] == "success"){
                        alert("成功修改");
                        window.location.reload();
                    }else {
                        alert("修改失败，" + result["message"]);
                    }
                }
            )
        });
        $('#Modal').modal('show');
    };
    
    function updateLifeStatus(index) {
        let id = patientsIds[index];
        $('#ModalLabel').text("修改生命状态");
        $('#modalBody').empty();
        $('#modalBody').append(
            "<select id='updateLifeSelect'>" +
            "<option value='1'>已治愈</option>" +
            "<option value='2'>住院</option>" +
            "<option value='3'>死亡</option>" +
            "</select>"
        );
        $('#modalOKButton').unbind('click').click(function () {
            var d = {};
            d["patientId"] = id;
            d["lifeStatus"] = $('#updateLifeSelect').val();
            $.post("/Database_PJ_war_exploded/doctor/updateLifeStatusOfPatient", d,
                function (result) {
                    result = JSON.parse(result);
                    if(result["message"] == "success"){
                        alert("成功修改");
                        window.location.reload();
                    }else {
                        alert("修改失败，" + result["message"]);
                    }
                }
            )
        });
        $('#Modal').modal('show');
    };
    
    function addTest(index) {
        let id = patientsIds[index];
        $('#ModalLabel').text("新增核酸检测");
        $('#modalBody').empty();
        $('#modalBody').append(
            "<div><div>检测结果</div><select id='updateTestSelect1'>" +
            "<option value='positive'>阳性</option>" +
            "<option value='negative'>阴性</option>" +
            "</select></div>"
        );
        $('#modalBody').append(
            "<div><div>病情评级</div><select id='updateTestSelect2'>" +
            "<option value='1'>轻症</option>" +
            "<option value='2'>重症</option>" +
            "<option value='3'>危重症</option>" +
            "</select></div>"
        );
        $('#modalBody').append(
            "<div><div>检测时间</div><input id='timePicker'></input></div>"
        );
        $('#timePicker').datetimepicker({
            format: "yyyy-mm-dd hh:ii:ss",
            autoclose: true
        });
        $('#modalOKButton').unbind('click').click(function () {
            var d = {};
            d["patientId"] = id;
            d["result"] = $('#updateTestSelect1').val();
            d["evaluation"] = $('#updateTestSelect2').val();
            d["date"] = $('#timePicker').val();
            $.post("/Database_PJ_war_exploded/doctor/addTestResult", d,
                function (result) {
                    result = JSON.parse(result);
                    if(result["message"] == "success"){
                        alert("添加成功");
                        window.location.reload();
                    }else {
                        alert("添加失败，" + result["message"]);
                    }
                }
            )
        });
        $('#Modal').modal('show');
    };
    
    function outHospital(index) {
        let id = patientsIds[index];
        $('#ModalLabel').text("病人出院");
        $('#modalBody').empty();
        $('#modalOKButton').unbind('click').click(function () {
            var d = {};
            d["patientId"] = id;
            $.post("/Database_PJ_war_exploded/doctor/dischargePatient", d,
                function (result) {
                    result = JSON.parse(result);
                    if(result["message"] == "success"){
                        alert("成功出院");
                        window.location.reload();
                    }else {
                        alert("出院失败，" + result["message"]);
                    }
                }
            )
        });
        $('#Modal').modal('show');
    };

    $(document).ready(function () {
        d = {};
        $.post("/Database_PJ_war_exploded/doctor/queryChiefNurse", d,
            function (result) {
                result = JSON.parse(result);
                $('#chiefNurse').text('护士长: ' + result["chiefNurse"]["name"]);
            }
        );
        $.post("/Database_PJ_war_exploded/doctor/queryWardNurse", d,
            function (result) {
                result = JSON.parse(result);
                for(i=0; i<result["wardNurses"].length; i++){
                    let p = result["wardNurses"][i];
                    let id = p["wardNurseId"];
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
                    $.post('/Database_PJ_war_exploded/doctor/queryPatientsByNurse', dd,
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
                                          "      </h2>" +
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
    });

</script>
</html>
