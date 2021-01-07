<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>account</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
<div>
    <button class="btn-primary" onclick="changeInfo()">修改用户名&密码</button>
</div>
<div>
    <form action="/Database_PJ_war_exploded/account/jumpToHome" method="post">
        <button type="submit" class="btn-primary">返回主界面</button>
    </form>
</div>
<div>
    <form action="/Database_PJ_war_exploded/account/logout" method="post">
        <button type="submit" class="btn-primary">登出</button>
    </form>
</div>
<div>
    <h2>未读站内信</h2>
    <div class="accordion" id="accordion"></div>
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
    function changeInfo() {
        $('#ModalLabel').text("修改用户名密码");
        $('#modalBody').empty();
        $('#modalBody').append(
            "<div>用户名<input class='input-group' type='text' id='addName' style='max-width: 300px; height: 40px' placeholder='name'></div>" +
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
            $.post("/Database_PJ_war_exploded/account/changeInfo", d,
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
    }

    var messageIds = []

    $(document).ready(function () {
        d = {};
        $.post("/Database_PJ_war_exploded/message/queryUnreadMessages", d,
            function (result) {
                result = JSON.parse(result);
                messageIds = [];
                for (i = 0; i < result["messages"].length; i++) {
                    let p = result["messages"][i];
                    let id = p["messageId"];
                    messageIds.push(id);
                    var cardElement = $("<div class='card'></div>");
                    var headerElement = $("<div class='card-header' id='heading" + id + "'>" +
                        "      <h2 class='mb-0'>" +
                        "        <button class='btn btn-link' type='button' data-toggle='collapse' data-target='#collapse" + id + "' aria-expanded='true' aria-controls='collapse" + id + "'>" +
                        "未读消息" +
                        "        </button>" +
                        "        </h2><button class='btn-warning' onclick='merkmessage($(this).parent().parent().index())'>已读该信息</button>" +
                        "    </div>");
                    var collapseElement = $("<div id='collapse" + id + "' class='collapse show' aria-labelledby='heading" + id + "' data-parent='#accordion'></div>");
                    var bodyElement = $("<div class=\"card-body\">"+ p["messageContent"] +"</div>");
                    collapseElement.append(bodyElement);
                    cardElement.append(headerElement);
                    cardElement.append(collapseElement);
                    $('#accordion').append(cardElement);
                }
            }
        );
    })

    function merkmessage(index) {
        var id = messageIds[index];
        var d = {};
        d["messageId"] = id;
        $.post("/Database_PJ_war_exploded/message/markMessage", d,
            function (result) {
                result = JSON.parse(result);
                if(result["message"] == "success"){
                    alert("成功已读");
                    window.location.reload();
                }else {
                    alert("已读失败，" + result["message"]);
                }
            }
        )
    }
</script>
</html>
