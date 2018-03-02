<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div class="copyright">
	<ul>
		<p>${setting.copyright }</p>
	</ul>
</div>

<script>
(function(){
    var bp = document.createElement('script');
    var curProtocol = window.location.protocol.split(':')[0];
    if (curProtocol === 'https') {
        bp.src = 'https://zz.bdstatic.com/linksubmit/push.js';        
    }
    else {
        bp.src = 'http://push.zhanzhang.baidu.com/push.js';
    }
    var s = document.getElementsByTagName("script")[0];
    s.parentNode.insertBefore(bp, s);
})();
</script>
<script>
(function(){
   var src = (document.location.protocol == "http:") ? "http://js.passport.qihucdn.com/11.0.1.js?3a7517b696c5be9c8369f000b961805f":"https://jspassport.ssl.qhimg.com/11.0.1.js?3a7517b696c5be9c8369f000b961805f";
   document.write('<script src="' + src + '" id="sozz"><\/script>');
})();
</script>