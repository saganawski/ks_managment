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

    getOfficeOptions()
        .then(function(data){
            setOfficeOptions(data);
        })
        .fail(function(err){
            console.log(err);
            alert("Error: Could not get Offices for drop down");
        });
    function getOfficeOptions(){
        return $.ajax({
            type:"GET",
            url:"/offices"
        });
    };

    function setOfficeOptions(offices){
        for(office of offices){
            delete office.location;
            $('#office').append("<option value='"+JSON.stringify(office)+"'>"+ office.name +"</option>")
        }
    }

    $('#newApplication').on('click', function(event){
        event.preventDefault();
        let jsonForm = convertFormToJson($("form").serializeArray());
        let applicationResult = JSON.parse($('#applicationResult').val());
        let applicationSource = JSON.parse($('#applicationSource').val());
        let applicationContactType = JSON.parse($('#applicationContactType').val());
        let office = JSON.parse($('#office').val());
        jsonForm.applicationResult = applicationResult;
        jsonForm.applicationSource = applicationSource;
        jsonForm.applicationContactType = applicationContactType;
        jsonForm.office = office;
        if(jsonForm.applicationNotes != null){
            note = [{id:null,note:jsonForm.applicationNotes}];
            jsonForm.applicationNotes = note;
        }

        $.ajax({
            type: "POST",
            url:"/applications",
            data: JSON.stringify(jsonForm),
            contentType: "application/json; charset=utf-8"
        }).then(function(response){
            swal("Success!","You created a new application","success");
            createInterviewIfScheduled(response);
            if(response.applicationResult.code != "SCHEDULED"){
                window.location.href = "/recruitment/application/application.html";
            }

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

    function createInterviewIfScheduled(application){
         let scheduleInterview = application.applicationResult.code == "SCHEDULED";
         if(scheduleInterview){
            let interview = {application: application};
            $.ajax({
                type:"POST",
                url: "/interviews",
                data: JSON.stringify(interview),
                contentType: "application/json; charset=utf-8"
            }).then(function(response){
                window.location.href = "/recruitment/interview/interview-details.html" +"?interviewId=" + response.id;
            }).then(function(error){
                console.log(error);
                alert("ERROR: Something went wrong when creating a interview!");
            });
         }
        }

});