<div class="profile profile_owner">
    <div class="owner__fullname">
        <p class="owner__name" id="name">${owner.getName()}</p>
        <#if owner.middleName??>
            <p class="owner__middleName" id="middleName">${owner.getMiddleName()}</p>
        </#if>
        <p class="owner__surname" id="surname">${owner.getSurname()}</p>
    </div>
    <div class="owner__bio">
        <div class="owner__birth_container owner__bio-container">
            <#if owner.birth??>
                <p class="owner__birth" id="birth">${owner.getBirth()}</p>
            <#else>
                <p class="owner__birth owner__empty" id="birth">Нажмите, чтобы добавить дату рождения формат
                    yyyy-dd-mm</p>
            </#if>
        </div>
        <div class="owner__about-container owner__bio-container">
            <#if owner.about??>
                <p class="owner__about" id="about">${owner.getAbout()}</p>
            <#else>
                <p class="owner__about owner__empty" id="about">Нажмите, чтобы добавить информацию о себе! (до 2000
                    символов)</p>
            </#if>
        </div>
    </div>
    <#--    <div class="owner__categories">-->
    <#--        <#if !owner.followCats?? || owner.followCats?size == 0>-->
    <#--            <p class="owner__cat">-->
    <#--                У вас нет ни одной подписки на категорию!-->
    <#--            </p>-->
    <#--        <#else>-->
    <#--            <#list owner.getFollowCats() as cat>-->
    <#--                <p class="owner__cat">${cat.getName()}</p>-->
    <#--            </#list>-->
    <#--        </#if>-->
    <#--    </div>-->
    <#--    <div class="owner__users">-->
    <#--        <#if !owner.followUsers?? || owner.followUsers?size == 0>-->
    <#--            <p class="owner__user">-->
    <#--                У вас нет ни одной подписки на пользователя!-->
    <#--            </p>-->
    <#--        <#else>-->
    <#--            <#list owner.getFollowCats() as cat>-->
    <#--                <p class="owner__cat">${cat.getName()}</p>-->
    <#--            </#list>-->
    <#--        </#if>-->
    <#--    </div>-->
</div>
