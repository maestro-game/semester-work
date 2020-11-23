<div>
    <div class="right-part">
        <h2>Зарегистрироватся</h2>
        <#if warnings??>
            <#list warnings as warning>
                <h3>${warning}</h3>
            </#list>
        </#if>
        <form method="post" action="/register">
            <input type="text" name="id" placeholder="Логин" <#if (id)??>value="${id}"</#if>>
            <input type="password" name="password" placeholder="Пароль">
            <input type="text" name="name" placeholder="Имя" <#if (name)??>value="${name}"</#if>>
            <input type="text" name="surname" placeholder="Фамилия" <#if (surname)??>value="${surname}"</#if>>
            <input type="email" name="email" placeholder="Email" <#if (email)??>value="${email}"</#if>>
            <input type="date" name="birth" placeholder="Дата рождения" <#if (birth)??>value="${birth}"</#if>>
            <input type="submit" placeholder="Регистрация">
        </form>
    </div>
</div>
