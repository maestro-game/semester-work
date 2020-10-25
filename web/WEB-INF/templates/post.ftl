<!DOCTYPE html>
<html lang="ru">
<head>
    <#setting date_format="dd MMM в HH:mm:ss">
    <meta charset="UTF-8">
    <title>Пост</title>
    <link rel="stylesheet" href="/css/post/main.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
<div>
    <div class="left-part">
        <p>${post.image}</p>
        <#if user??>
            <form onsubmit="return send()">
                <input id="comment" type="text" placeholder="Напишите сообщение">
            </form>
            <p id="answers"></p>
            <button style="visibility: hidden" id="cancel" onclick="clearAnswers()">X</button>
        </#if>
    </div>
    <div class="right-part">
        <div>${post.author.image}</div>
        <div>${post.author.name} ${post.author.surname}</div>
        <div>${likes}</div>
        <div id="comments">
            <#if comments??>
                <#list comments as comment>
                    <div <#if comment.answerTo??> class= "answer"</#if> id= ${comment.id}>
                        <p>--------------------------------</p>
                        <h4 id="authorName">${comment.author.name} ${comment.author.surname}</h4>
                        <h4>${comment.timestamp}</h4>
                        <#if comment.answerTo??><h4>ответ для @${comment.answerTo.author.name}</h4></#if>
                        <#if user??>
                            <#if comment.author.id == user.id>
                                <button id="delete" onclick="del(${comment.id})">удалить</button>
                                <button id="edit" onclick="setEdit(${comment.id})">изменить</button>
                            </#if>
                            <button id="answer" onclick="setAnswers(${comment.id})">ответить</button>
                        </#if>
                        <h3 id="text">${comment.text}</h3>
                    </div>
                </#list>
            </#if>
        </div>
    </div>
</div>

<#if user??>
    <script>
        let comment = $('#comment');
        let answers = $('#answers');
        let button = $('#cancel');

        function setEdit(id) {
            button.attr('style', "visibility: visible");
            answers.text('изменить');
            comment.val($("#" + id + " #text").text());
            comment.attr('edit', id);
            comment.focus();
        }

        function setAnswers(id) {
            button.attr('style', "visibility: visible");
            answers.text('ответить @' + $("#" + id + " #authorName").text());
            comment.attr('answers', id);
            comment.focus();
        }

        function clearAnswers() {
            button.attr("style", "visibility: hidden")
            comment.removeAttr('edit');
            comment.removeAttr('answers');
            answers.contents().remove();
            comment.val('');
        }

        function del(id) {
            $.ajax({
                    type: 'DELETE',
                    url: '/comment?id=' + id,
                    success: function () {
                        $("#" + id).remove();
                    },
                    error: function () {
                        window.location.href = "/login";
                    }
                }
            );
            return false;
        }

        function edit(id) {
            let text = comment.val();
            if (text.length == 0) return false;
            $.ajax({
                    type: 'PUT',
                    url: '/comment?text=' + text + '&id=' + id,
                    success: function () {
                        $("#" + id + " #text").text(text);
                        clearAnswers();
                    },
                    error: function () {
                        window.location.href = "/login";
                    }
                }
            );
            return false;
        }

        function send() {
            let editId = comment.attr("edit");
            if (editId !== undefined) {
                edit(editId);
                return false;
            }
            let text = comment.val();
            if (text.length == 0) return false;
            let answers = comment.attr('answers');
            let data = "text=" + text + "&post=" + ${post.id} + (answers !== undefined ? "&answers=" + answers : "");
            let username = '${user.name} ${user.surname}';
            $.post({
                    url: '/comment',
                    data: data,
                    success: function (msg) {
                        answers !== undefined ?
                            drawAnswer(msg, username, answers, $("#" + answers + " #text").text(), msg, text) :
                            drawComment(msg,  username, msg, text);
                        clearAnswers();
                        button.attr('style', "visibility: hidden");
                    },
                    error: function () {
                        window.location.href = "/login";
                    }
                }
            );
            return false;
        }

        let comments = $('#comments');

        function drawAnswer(commentId, name, answersId, answersName, timestamp, text) {
            $("#" + answersId).after('<p>--------------------------------</p>' +
                '<h4>' + name + '</h4><h4>' + timestamp + '</h4>' +
                '<h4>ответ для @' + answersName + '</h4>' +
                '<button id="delete" onclick="del(' + commentId + ')">удалить</button>' +
                '<button id="edit" onclick="setEdit(' + commentId + ')">изменить</button>' +
                '<button id="answer" onclick="setAnswers(' + commentId + ')">ответить</button>' +
                '<h3>' + text + '</h3>');
        }

        function drawComment(commentId, name, timestamp, text) {
            comments.append('<p>--------------------------------</p>' +
                '<h4>' + name + '</h4><h4>' + timestamp + '</h4>' +
                '<button id="delete" onclick="del(' + commentId + ')">удалить</button>' +
                '<button id="edit" onclick="setEdit(' + commentId + ')">изменить</button>' +
                '<button id="answer" onclick="setAnswers(' + commentId + ')">ответить</button>' +
                '<h3>' + text + '</h3>');
        }
    </script>
</#if>
</body>
</html>