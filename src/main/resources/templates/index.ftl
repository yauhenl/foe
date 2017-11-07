<#import "components.ftl" as y>
<@y.page>
<div class="container">
    <table class="table">
        <thead>
            <tr>
                <th>Clan</th>
                <th>User Name</th>
                <th>Era</th>
            </tr>
        </thead>
        <tr>
            <td>${data.user_data.clan_name}</td>
            <td>${data.user_data.user_name}</td>
            <td>${data.user_data.era.era}</td>
        </tr>
    </table>
    <br/>
    <table class="table">
        <thead>
        <tr>
            <th>Owner Id</th>
            <th>Status</th>
        </tr>
        </thead>
        <#list data.taverns as tavern>
        <tr>
            <td>${tavern.ownerId}</td>
            <td>${tavern.state}</td>
        </tr>
        </#list>
    </table>
</div>
</@y.page>