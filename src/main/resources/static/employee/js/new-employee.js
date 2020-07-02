$(document).ready(function () {
	// TODO: something to load side bar and nav
//    var vm = this;
	
	
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
		}).fail(function(error){
			alert(error);
		});
		
	}
	
	function setPositionOptions(positions){
		for(position of positions){
			$('#position').append("<option name='"+position.code+"'>"+ position.name +"</option>");
		}
	}
	
	
	/*
	 * Create a new Employee  newEmpolyee
	 */
	
	$('#newEmpolyee').on('click', function(event){
		event.preventDefault();
    	var formJson = convertFormToJson($("form").serializeArray());
    	var selectedOffices = $('#officeSelect').val();
    	formJson.officeSelection = selectedOffices;
    	//Send To controller
    	createNewEmployee(formJson);
    	
	});
	
//	function createNewEmployee(formJson){
//		position = getPositionByName(formJson.position);
//		formJson.position = position;
//		
//		empolyeeOffices = getOfficeValues(formJson);
//		formJson.empolyeeOffices = empolyeeOffices;
//		delete formJson.officeSelection;
//		
//		console.log(formJson);
//		debugger;
//		
//	  	$.ajax({
//    		type:"POST",
//    		url: "/employees",
//    		data: formJson
//    	}).then(function(data){
//    		console.log(data);
//    		alert("success!");
//    	}).fail(function(error){
//    		console.log(error);
//    		alert("fail");
//    	});
//		console.log(formJson);
//	}
	function createNewEmployee(formJson){
		getPositionByName(formJson.position).then(function(responseJson){
			console.log(responseJson);
			formJson.position = position;
			
		}, function(reseaon){
			console.log("error reterive position names", reason);
			alert("error reterive position names", reason);
		});
//		formJson.position = position;
		
		empolyeeOffices = getOfficeValues(formJson);
//		formJson.empolyeeOffices = empolyeeOffices;
//		delete formJson.officeSelection;
//		
//		console.log(formJson);
//		debugger;
//		
//		$.ajax({
//			type:"POST",
//			url: "/employees",
//			data: formJson
//		}).then(function(data){
//			console.log(data);
//			alert("success!");
//		}).fail(function(error){
//			console.log(error);
//			alert("fail");
//		});
		console.log(formJson);
	}
	
	function getOfficeValues(formJson){
		empolyeeOffices = [];
		for(office of formJson.officeSelection){
			$.get("/offices/"+ office, function(data){
				empolyeeOffices.push(data);
			}).fail(function(error){
				alert(error);
			});
		}
		
		return empolyeeOffices;
	}
	
	function getPositionByName(name, callback){
		return $.ajax({
			type:"GET",
			url: "/positions/" + name,
			dataType: "json"
		})
//		position = {};
//		
//		$.get("/positions/" + name, function(data){
//			position = data;
//		}).fail(function(error){
//			alert(error);
//			console.log(error);
//		});
//		
//		return position;
	}
	
	function convertFormToJson(form){
    	var json = {}
    	for(let j of form){
    		json[j.name] = j.value || null;
    	}
    	
    	return json;
    }
	
});