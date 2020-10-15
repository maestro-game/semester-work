<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Флудильня</title>
    <link rel="stylesheet" href="/css/post/main.css">
</head>
<body>
<div>
    <div class="left-part">
        <p>${img}</p>
    </div>
    <div class="right-part">
        <div>${author}</div>
        <div>${likes}</div>
        <div>
            <#if comments??>
                <#list comments as comment>
                    <p>--------------------------------</p>
                    <h4>${comment.from.name} ${comment.from.surname} - ${comment.timestamp}</h4>
                    <h3>${comment.text}</h3>
                </#list>
            </#if>
        </div>
    </div>
</div>
</body>
</html>