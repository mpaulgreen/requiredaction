<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=!messagesPerField.existsError('x-approved'); section>
    <#if section = "header">
        ${msg("admindisplaytext")}
    </#if>
</@layout.registrationLayout>
