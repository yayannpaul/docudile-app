<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<html>
    <head>
        <title>Docudile</title>
    </head>
    <body>
        <p>hello!</p>
        <form action="submit" method="post" enctype="multipart/form-data">
            <input type="file" name="file">
            <input type="submit" value="Submit">
        </form>
        <a href="dropbox">Dropbox</a>
        <p>${filename}</p>
        <p>${token}</p>
    </body>
</html>