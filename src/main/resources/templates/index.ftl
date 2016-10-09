<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8"/>
    <title>Springboot-Jdbc</title>
    <style type="text/css">
        table,tbody,thead {
            border: solid 1px;
        }
    </style>
</head>
<body>
FreeMarker模板引擎
<hr>
用户列表
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>NAME</th>
        <th>AGE</th>
    </tr>
    </thead>
    <tbody>
    <#list users as user>
    <tr>
        <td>${user.id}</td>
        <td>${user.name}</td>
        <td>${user.age}</td>
    </tr>
    </#list>
    </tbody>
</table>

<script type="javascript">

</script>
</body>
</html>