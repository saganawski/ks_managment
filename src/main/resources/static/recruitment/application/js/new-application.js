$(document).ready(function () {
	// TODO: something to load side bar and nav
	/*
	SET CONTACT OPTIONS
	*/
	getContactOptions()
	    .then(function(data){
	        setContactTypeOptions(data);
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
            delete type._links;
            $('#applicationContactType').append("<option value='"+JSON.stringify(type)+"'>"+ type.type +"</option>");
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
            setSourceOptions(data);
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
            delete source._links;
            $('#applicationSource').append("<option value='"+JSON.stringify(source)+"'>"+ source.source +"</option>");
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
            setResultOptions(data);
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
            delete result._links;
            $('#applicationResult').append("<option value='"+JSON.stringify(result)+"'>"+ result.result +"</option>");
        }
    }
    /*
            ***************************************************************************************************
    */

    $('#newApplication').on('click', function(event){
        event.preventDefault();
        let jsonForm = convertFormToJson($("form").serializeArray());
        let applicationResult = JSON.parse($('#applicationResult').val());
        let applicationSource = JSON.parse($('#applicationSource').val());
        let applicationContactType = JSON.parse($('#applicationContactType').val());
        jsonForm.applicationResult = applicationResult;
        jsonForm.applicationSource = applicationSource;
        jsonForm.applicationContactType = applicationContactType;

        $.ajax({
            type: "POST",
            url:"/applications",
            data: JSON.stringify(jsonForm),
            contentType: "application/json; charset=utf-8"
        }).then(function(response){
            alert("success! You created a new application");
            window.location.href = "/recruitment/application/application.html";
        }).fail(function(err){
            console.log(err);
            alert("Error: Could not make new application");
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