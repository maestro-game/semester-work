<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход</title>
    <link rel="stylesheet" href="/css/login/main.css">
</head>
<body>
<div class="left-part">
    <h2>Войти</h2>
    <#if warnings??>
        <#list warnings as warning>
            <h3>warning</h3>
        </#list>
    </#if>
    <form method="post" action="/login">
        <input type="text" name="id" placeholder="Логин" <#if id??> value=${id} </#if>>
        <input type="password" name="password" placeholder="Пароль">
        <div>Запомнить меня </div><input style="display: inline" type="checkbox" name="remember" placeholder="Запомнить меня" value="true">
        <input type="submit" placeholder="Войти">
    </form>
</div>
</body>
</html>