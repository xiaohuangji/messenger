<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../include/jsp.html"%>

<html>
    <head>
        <%@include file="../../include/base.html"%>
        <title>Index</title>

    </head>
    <body>
        <!-- nav -->
        <%@include file="../../include/nav.html"%>

        <div class="container-fluid container">
            <div class="jumbotron">
                <h1>Learn to Create Websites</h1>
                <p>In today's world internet is the most popular way of connecting with the people. At <a href="http://www.tutorialrepublic.com" target="_blank">tutorialrepublic.com</a> you will learn the essential of web development technologies along with real life practice example, so that you can create your own website to connect with the people around the world.</p>
                <p><a href="#"  class="btn btn-success btn-lg">Get started today</a></p>
            </div>
            <div class="row">
                <div class="col-xs-4">
                    <h2>ClientInfo</h2>
                    <p>HTML is a markup language that is used for creating web pages. The HTML tutorial section will help you understand the basics of HTML, so that you can create your own web pages or website.</p>
                    <p><a href="/admin/push/clientInfo"  class="btn btn-success">Enter &raquo;</a></p>
                </div>
                <div class="col-xs-4">
                    <h2>SendPush</h2>
                    <p>CSS is used for describing the presentation of web pages. The CSS tutorial section will help you learn the essentials of CSS, so that you can fine control the style and layout of your HTML document.</p>
                    <p><a href="/admin/push/sendPush"  class="btn btn-success">Enter &raquo;</a></p>
                </div>
                <div class="col-xs-4">
                    <h2>Monitor</h2>
                    <p>Twitter Bootstrap is a powerful front-end framework for faster and easier web development. The Twitter Bootstrap tutorial section will help you learn the techniques of Bootstrap so that you can create web your own website.</p>
                    <p><a href="/admin/push/monitor" class="btn btn-success">Enter &raquo;</a></p>
                </div>
            </div>
        </div>

        <!-- foot -->
        <%@include file="../../include/foot.html"%>

    </body>

</html>