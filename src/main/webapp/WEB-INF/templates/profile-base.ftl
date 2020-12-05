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
                <p class="owner__birth owner__empty" id="birth">${owner.getBirth()}</p>
            <#else>
                <p class="owner__birth owner__empty" id="birth">Нажмите, чтобы добавить дату рождения формат
                    yyyy-dd-mm</p>
            </#if>
        </div>
        <div class="owner__about-container owner__bio-container">
            <#if owner.about??>
                <p class="owner__about owner__empty" id="about">${owner.getAbout()}</p>
            <#else>
                <p class="owner__about owner__empty" id="about">Нажмите, чтобы добавить информацию о себе! (до 2000
                    символов)</p>
            </#if>
        </div>
    </div>
    <div class="add__post">
        <form action="/post" method="post" class="post__form" enctype="multipart/form-data">
            <input type="text" name="specie">
            <input type="text" name="description">
            <input type="file" name="image">
            <input type="submit" value="Загрузить пост">
        </form>
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
