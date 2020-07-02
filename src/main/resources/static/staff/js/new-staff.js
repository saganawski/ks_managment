$(document).ready(function () {
	
	$('#newStaff').on('click', function (event) {
		event.preventDefault();
		var formJson = convertFormToJson($("form").serializeArray());
    	formJson.endDate = new Date(formJson.endDate);
    	formJson.startDate = new Date(formJson.startDate);
		url = "/staffs"
		$.ajax({
			type:"POST",
			url: url,
			data: formJson
		}).then(function(data){
			console.log(data);
		}).fail(function(error){
			console.log(error);
		});
    });
	
	
	
//	  $('form #updateStaff').click(function(event){
//	    	event.preventDefault();
//	    	var formJson = convertFormToJson($("form").serializeArray());
//	    	formJson.endDate = new Date(formJson.endDate);
//	    	formJson.startDate = new Date(formJson.startDate);
//	    	debugger;
//	    	url = "/staffs";
//			$.ajax({
//				type:"PUT",
//				url: url,
//				data: formJson
//			}).then(function(data){
//				console.log(data);
//			}).fail(function(error){
//				console.log(error);
//			});
//	    });
	    
    function convertFormToJson(form){
    	var json = {}
    	for(let j of form){
    		json[j.name] = j.value || null;
    	}
    	
    	return json;
    }

});