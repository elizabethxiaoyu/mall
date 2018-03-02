<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html>
<body>
<h2>Hello World!</h2>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

Spring MVC 文件上传
<form name="form1" action = "/manage/product/upload.do" method="post" enctype="multipart/form-data">
<input type="file" name="upload_file" />
    <input type="submit" value="spring mvc 上传文件测试"/>

</form>

富文本图片文件上传
<form name="form1" action = "/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="富文本 上传图片测试"/>

</form>

</body>
</html>
