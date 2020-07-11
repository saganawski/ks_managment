$(document).ready(function () {
	// TODO: something to load side bar and nav
	$.ajax({
		type:"GET",
		url: "/offices"
	}).then(function(data){
		setOfficeOptions(data);
	}).fail(function(error){
		alert(error);
	});
	
	function setOfficeOptions(offices){
		var officeOptionsData = [];
		for(office of offices){
			option ={label:office.name, value:office.id, name:office.name };
			officeOptionsData.push(option);
		}
		$('#officeSelect').multiselect('dataprovider', officeOptionsData);
	}
	
	getPositionsForDropDown();
	function getPositionsForDropDown(){
    $.ajax({
			type:"GET",
			url: "/positions"
		}).then(function(data){
			setPositionOptions(data);
		}).fail(function(err){
			alert(err);
		});

	}

	function setPositionOptions(positions){
		for(position of positions){
			$('#position').append("<option name='"+position.code+"'>"+ position.name +"</option>");
		}
	}

	$('#newEmpolyee').on('click', function(event){
		event.preventDefault();
    	var formJson = convertFormToJson($("form").serializeArray());
    	var selectedOffices = $('#officeSelect').val();
    	formJson.officeSelections = selectedOffices;
    	//Send To controller
    	setUpNewEmployee(formJson)
    	    .then(results => createNewEmployee(results))
    	    .catch((err) => console.error(err));
    	
	});
	
	async function setUpNewEmployee(formJson){
		let position = await getPositionByName(formJson.position);
		formJson.position = position;
		return formJson;
	}

	function getPositionByName(name){
		return $.ajax({
			type:"GET",
			url: "/positions/" + name,
		})
	}
	function createNewEmployee(formJson){

	    	  	$.ajax({
            		type:"POST",
            		url: "/employees",
            		data: JSON.stringify(formJson),
            		contentType: "application/json; charset=utf-8"
            	}).then(function(data){
            		console.log(data);
            		alert("success! You created a new employee");
            		window.location.href = "/employee/employee.html";
            	}).fail(function(error){
            		console.log(error);
            		alert("fail" + error.responseJSON.error);
            	});
	}
	function convertFormToJson(form){
    	var json = {}
    	for(let j of form){
    		json[j.name] = j.value || null;
    	}
    	return json;
    }
	
});