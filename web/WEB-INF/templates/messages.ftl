<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Флудильня</title>
</head>
<body>
<div>
    <#if messages??>
        <#list messages as message>
            <h4>${message.from.name} ${message.from.surname} => ${message.to.name} ${message.to.surname}</h4>
            <h4>${message.date}</h4>
            <h3>${message.text}</h3>
        </#list>
    </#if>
</div>
</body>
</html>