<#include "base.ftl">
<@html "follow" "Подписки" false false>
    <div class="follows left-part">
        <div class="categories">
            <#list followsCategories as category>
                <p class="category" id="${category.id}">
                    ${category.name}
                </p>
            </#list>
        </div>
    </div>
    <div class="follows right-part">
        <div class="users">
            <#list followsUsers as user>
                <p class="user" id="${user.id}">
                    ${user.name + " " + user.surname}
                </p>
            </#list>
        </div>
    </div>
    <div class="follows add">
        <p class="header__add">
            Подписаться на категории/пользователя:
        </p>
        <form class="follow__form_cat" action="/follow" method="post">
            <input type="text" style="display: none" name="type" value="category">
            <input type="text" name="follow" placeholder="Введите ID категории">
            <input type="submit" value="Подписаться">
        </form>
        <form class="follow__form_user" action="/follow" method="post">
            <input type="text" style="display: none" name="type" value="user">
            <input type="text" name="follow" placeholder="Введите ID пользователя">
            <input type="submit" value="Подписаться">
        </form>
    </div>
</@html>
