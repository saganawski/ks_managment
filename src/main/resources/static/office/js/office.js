$(document).ready(function () {
	// TODO: something to load side bar and nav
    var vm = this;
    vm.offices = [];
    
    //TODO: does not handle nulls. maybe do a DTO
    $('#office-table').DataTable({
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