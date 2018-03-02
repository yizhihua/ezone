<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<meta charset="utf-8">
<title>${!(empty journal)?journal.title:''}${!(empty journal)?' -':''}${setting.siteName }</title>
<meta name="keywords" content="${!(empty journal)?journal.title:''}${!(empty journal)?',':''}${setting.keywords }" />
<meta name="description" content="${setting.description }" />
<link href="${basePath }css/base.css" rel="stylesheet">
<link href="${basePath }css/index.css" rel="stylesheet">
<link href="${basePath }css/media.css" rel="stylesheet">
<meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0">
<script src="//cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
<link rel="icon" href="${basePath }assets/img/favicon.png"
	type="image/png">
<!--[if lt IE 9]>
<script src="${basePath }js/modernizr.js"></script>
<![endif]-->
