$(document).ready(function () {
	// TODO: something to load side bar and nav
    console.log("hits");
    var vm = this;
    vm.offices = [];
    
//    getOffices("/offices");
//    
//    function getOffices(url){
//    	$.ajax({
//    		url: url
//    	}).then(function(data) {
//    		console.log(data);
//    		vm.offices = data._embedded.offices;
//    	}).fail(function(error){
//    		//TODO: ALERT
//    		console.log(error);
//    	});
//    }
    
    $('#office-table').DataTable({
    	ajax: {
            url: "/offices",
            dataSrc: '_embedded.offices'
        },
        columns : [
        	{"data": "name"},
        	{"data": "location"}
        ]
    });
    
});