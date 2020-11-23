<#include "base.ftl">
<@html "messages" "Флудильня" css=false>
<div>
    <#if messages??>
        <#list messages as message>
            <h4>${message.from.name} ${message.from.surname} => ${message.to.name} ${message.to.surname}</h4>
            <h4>${message.date}</h4>
            <h3>${message.text}</h3>
        </#list>
    </#if>
</div>
</@>
