<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>wardNurse</title>
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
            <option value="RelatedPatients">负责的所有病人</option>
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
            "/Database_PJ_war_exploded/ward/query" + $('#Indentity').val(), d,
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
            "<th>信息登记</th>" +
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
                "<td><button class='btn-primary' onclick='addRecord($(this).parent().parent().index())'>信息登记</button></td>" +
                "</tr>"
            );
        }
        $('#queryResult').css("display", "block");
    };

    function addRecord(index) {
        let id = patientsIds[index];
        $('#ModalLabel').text("新增信息登记");
        $('#modalBody').empty();
        $('#modalBody').append(
            "<div>症状<input class='input-group' type='text' id='addSymptom' style='max-width: 300px; height: 40px' placeholder='name'></div>" +
            "<div>体温<input class='input-group' type='number' step='0.1' id='addTemperature' style='max-width: 300px; height: 40px' placeholder='Temperature'></div>"
        );
        $('#modalBody').append(
            "<div><div>病情评级</div><select id='addRecordSelect'>" +
            "<option value='1'>轻症</option>" +
            "<option value='2'>重症</option>" +
            "<option value='3'>危重症</option>" +
            "</select></div>"
        );
        $('#modalBody').append(
            "<div><div>记录时间</div><input id='timePicker'></input></div>"
        );
        $('#timePicker').datetimepicker({
            format: "yyyy-mm-dd hh:ii:ss",
            autoclose: true
        });
        $('#modalOKButton').unbind('click').click(function () {
            var d = {};
            d["patientId"] = id;
            d["symptom"] = $('#addSymptom').val();
            d["temperature"] = $('#addTemperature').val();
            d["lifeStatus"] = $('#addRecordSelect').val();
            d["date"] = $('#timePicker').val();
            $.post("/Database_PJ_war_exploded//ward/addDailyRecord", d,
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
</script>
</html>
