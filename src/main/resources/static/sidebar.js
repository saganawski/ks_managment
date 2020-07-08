$(document).ready(function () {
	//load not a function
	$('#nav').load("_side-bar-nav.html");
	
    $('body').on('click', '#sidebarCollapse', function () {
    	$('#sidebar').toggleClass('active');
    });

});