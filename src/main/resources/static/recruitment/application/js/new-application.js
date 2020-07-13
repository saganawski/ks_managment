$(document).ready(function () {
	// TODO: something to load side bar and nav
	console.log("hits");
	/*
	Options
	load content types
	load source
	load results
	*/

	/*
	SET CONTACT OPTIONS
	*/
	getContactOptions()
	    .then(function(data){
	        setContactTypeOptions(data._embedded.applicationContactTypes);
	    })
	    .fail(function(err){
	        console.log(err);
	        alert("problem getting contact types!");
	    });

	function getContactOptions(){
	    return $.ajax({
	        type:"GET",
	        url: "/applicationContactTypes"
	    });
	}
    function setContactTypeOptions(types){
        for(type of types){
            $('#contactType').append("<option name='"+type.code+"'>"+ type.type +"</option>");
        }
    }
    /*
    ***************************************************************************************************
    */

    /*
    	SET SOURCE OPTIONS
    */
    getApplicationSource()
        .then(function(data){
            setSourceOptions(data._embedded.applicationSources);
        })
        .fail(function(err){
            console.log(err);
            alert("Error: could not get Application Source data!");
        });
    function getApplicationSource(){
        return $.ajax({
            type:"GET",
            url:"/applicationSources"
        });
    }
    function setSourceOptions(sources){
        for(source of sources){
            $('#applicationSource').append("<option name='"+source.code+"'>"+ source.source +"</option>");
        }
    }
    /*
        ***************************************************************************************************
    */
    /*
    	SET RESULT OPTIONS
    */

    getResultOptions()
        .then(function(data){
            setResultOptions(data._embedded.applicationResults);
        })
        .fail(function(err){
            console.log(err);
            alert("Error: could not get Application Result data!");
        });
    function getResultOptions(){
        return $.ajax({
            type:"GET",
            url:"/applicationResults"
        });
    }
    function setResultOptions(results){
        for(result of results){
            $('#result').append("<option name='"+result.code+"'>"+ result.result +"</option>");
        }
    }
    /*
            ***************************************************************************************************
    */
	/*

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
    }*/
	
});