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
            <form id='comment'>
                <input type="text" name="text" placeholder="Напишите сообщение">
            </form>
        </#if>
    </div>
    <div class="right-part">
        <div>${post.author.image}</div>
        <div>${post.author.name} ${post.author.surname}</div>
        <div>${likes}</div>
        <div id="comments">
            <#if comments??>
                <#list comments as comment>
                    <p>--------------------------------</p>
                    <h4>${comment.author.name} ${comment.author.surname} - ${comment.timestamp}</h4>
                    <h3>${comment.text}</h3>
                </#list>
            </#if>
        </div>
    </div>
</div>

<#if user??>
    <script>
        let answers = null;
        $('#comment').submit(function () {
            let form = $("#comment");
            let data = form.serialize();
            let text = $("#comment input").val();
            data += "&post=" + ${post.id} + (answers !== null ? "&answers=" + answers : "");
            $.post(
                '/comment',
                data,
                function (msg) {
                    drawComment(text, msg);
                    form.val('');
                }
            );
            return false;
        });

        function drawComment(text, timestamp) {
            $('#comments').append("<p>--------------------------------</p>" +
                "<h4>${user.name} ${user.surname} - " + timestamp + "</h4>" +
                "<h3>" + text + "</h3>")
        }
    </script>
</#if>
</body>
</html>