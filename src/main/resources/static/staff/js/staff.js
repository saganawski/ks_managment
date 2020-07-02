$(document).ready(function () {
	var vm = this;
	vm.staffs = [];
	getStaffs();
	function getStaffs(){
		url = "/staffs";
		$.ajax({
			url: url
		}).then(function(data){
			vm.staffs = data;
		}).fail(function(error){
			console.log(error);
		});
	}
	// TODO: something to load side bar and nav
//    //TODO: does not handle nulls. maybe do a DTO
   var staffTable = $('#staff-table').DataTable({
    	ajax: {
            url: "/staffs",
            dataSrc: ''
        },
        columns : [
        	{"data": "id"},
        	{"data": "firstName"},
        	{"data": "lastName"},
        	{"data": "alias"},
        	{"data": "email"},
        	{"data": "Actions", "bSortable": false, "bSearchable": false,
        		render: function(data, type,row){
        			return '<button id="row_button" class="btn btn-primary"> Edit</button> '; 
        		}}
        ]
    });
    
    $('#staff-table tbody').on( 'click', 'button', function () {
        var data = staffTable.row( $(this).parents('tr') ).data();
        console.log(data);
        var modal = $('#editModal').modal('show');
        //Title
        modal.find('#modalTitle').text(data.firstName + " " + data.lastName);
        //Form
        modal.find('#id').val(data.id);
        modal.find('#firstName').val(data.firstName);
        modal.find('#lastName').val(data.lastName);
        modal.find('#alias').val(data.alias);
        modal.find('#email').val(data.email);
        modal.find('#phoneNumber').val(data.phoneNumber);
        //TODO: handle null make a dto
//        modal.find('#startDate').val(data.startDate.substring(0,10));
//        modal.find('#endDate').val(data.endDate.substring(0,10));
        
    });
    
    $('form #updateStaff').click(function(event){
    	event.preventDefault();
    	var formJson = convertFormToJson($("form").serializeArray());
    	formJson.endDate = new Date(formJson.endDate);
    	formJson.startDate = new Date(formJson.startDate);
    	debugger;
    	url = "/staffs";
		$.ajax({
			type:"PUT",
			url: url,
			data: formJson
		}).then(function(data){
			console.log(data);
		}).fail(function(error){
			console.log(error);
		});
    });
    
    function convertFormToJson(form){
    	var json = {}
    	for(let j of form){
    		json[j.name] = j.value || null;
    	}
    	
    	return json;
    }
    
    
});