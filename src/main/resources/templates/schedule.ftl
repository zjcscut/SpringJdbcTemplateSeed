<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8"/>
    <title>任务调度列表</title>
    <style type="text/css">
        table, tbody, thead, th, td {
            border: solid 1px;
        }

        table {
            text-align: center;
        }
    </style>
</head>
<body>
<hr>
<h3>任务调度列表</h3>
<table>
    <thead>
    <tr>
        <th>TASK_ID</th>
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