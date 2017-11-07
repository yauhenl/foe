<#import "components.ftl" as y>
<@y.page>
<div class="container">
    <form action="/login" method="post">
        <div class="form-group">
            <label for="sid">Sid</label>
            <input type="text" class="form-control" id="sid" name="sid" placeholder="sid">
        </div>
        <div class="form-group">
            <label for="userKey">User key</label>
            <input type="text" class="form-control" id="userKey" name="userKey" placeholder="User key">
        </div>
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>
</div>
</@y.page>