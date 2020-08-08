$(document).ready(function () {
	// TODO: something to load side bar and nav
    var vm = this;
    vm.offices = [];
    
    $('#office-table').DataTable({
        "initComplete": function(settings, json){
            $("div").removeClass("spinner-border");
        },
    	ajax: {
            url: "/offices",
            dataSrc: ''
        },
        columns : [
        	{"data": "name"},
        	{"data": "location.address1",
        	    "defaultContent":""
        	}
        ]
    });
    
});