<#ftl encoding='UTF-8'>

<#macro fullname author>
    <#if author.middleName??>
        ${author.getName() + " "
        + author.getMiddleName() + " "
        + author.getSurname()}
    <#else>
        ${author.getName() + " "
        + author.getSurname()}
    </#if>
</#macro>

<#macro html page title css=true js=false>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>${title}</title>
    <link rel="stylesheet" href="/files/css/mainBase.css">
    <#if css>
        <link rel="stylesheet" href="/files/css/${page}.css">
    </#if>
    <#if js>
        <script src="/files/js/${page}.js"></script>
    </#if>
</head>
<body>
<div class="outer">
    <div class="left-up inner">
        <div class="logo">
            <div class="logo__img">
                <img src="/files/images/logo.png" alt="logo">
            </div>
            <div class="logo__name"><h4>Biogram</h4></div>
        </div>
    </div>
    <div class="left-bottom inner">
        <div class="menu">
            <a href="/" class="menu__entry-link">
                <p class="menu__entry">посты</p>
            </a>
            <a href="/" class="menu__entry-link">
                <p class="menu__entry">лента</p>
            </a>
            <a href="/id/<#if user??>${user.getId()}</#if>" class="menu__entry-link">
                <p class="menu__entry">профиль</p>
            </a>
        </div>
    </div>
    <div class="right-up inner">
        <div class="search">
            <form action="/search" method="post">
                <a href="/search"><img
                            src="data:image/png;base64,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"
                            alt="search_icon" class="search__img"></a>
                <input id="searchText" name="searchText" class="search__entry" placeholder="Поиск по сайту"/>
            </form>
        </div>
    </div>
    <div class="right-bottom inner">
        <div class="footer">
            <p class="footer__entry">подвал с контактами</p>
            <p class="footer__entry">и копирайтами</p>
        </div>
    </div>
</div>
<div class="container container_${page}">
    <#nested>
</div>
</body>
</#macro>
