<#include "base.ftl">
<@html "search" "Поиск" true true>
    <div class="search__block">
        <#--        <div class="search__user search__container">-->
        <#--            ...         -->
        <#--        </div>-->
        <div class="search__cat search__container">
            <form action="/search" method="post" class="search__form">
                <select class="search__select" hidden id="1">
                </select>
            </form>
        </div>
        <div class="search__button">
            <button class="search__btn">Поиск</button>
        </div>
    </div>
    <div class="result-container">
        <div class="search__result">

        </div>
        <div class="search__dots">

        </div>
    </div>
</@>
