$(document).ready(function () {

    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
        }
    });
	/*
	SET CONTACT OPTIONS
	*/
	getContactOptions()
	    .then(function(data){
	        setContactTypeOptions(data);
	    })
	    .fail(function(err){
	        console.log(err);
	        swal({
                title: "Error!",
                text: "Problem getting contact types!\n" + err.responseJSON.message,
                icon: "error"
            });
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
            swal({
                title: "Error!",
                text: "could not get Application Source data!\n" + err.responseJSON.message,
                icon: "error"
            });
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
            swal({
                title: "Error!",
                text: "could not get Application Result data!\n" + err.responseJSON.message,
                icon: "error"
            });
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
            swal({
                title: "Error!",
                text: "Could not get offices for drop down!\n" + err.responseJSON.message,
                icon: "error"
            });
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

    $('#load-layout').on('click', '#newApplication', function(event){
        event.preventDefault();

        let validated = validationCheck();
        if(validated){
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
                swal({
                    title: "Success!",
                    text: "You created a new application",
                    icon: "success",
                    timer: 2000
                }).then(function(){
                    if((response.applicationResult == undefined) || (response.applicationResult.code != "SCHEDULED")){
                        window.location.href = "/recruitment/application/application.html";
                    }else{
                        createInterviewIfScheduled(response);
                    }
                });
            }).fail(function(err){
                console.log(err);
                swal({
                    title: "Error!",
                    text: "could not make a new Application!\n" + err.responseJSON.message,
                    icon: "error"
                });
            });
        }
    });

    function convertFormToJson(form){
        var json = {}
        for(let j of form){
            json[j.name] = j.value || null;
        }
        return json;
    }
    function validationCheck(){
        const form = document.querySelector('#app-form');
        if(form.checkValidity()  === false){
            event.stopPropagation();
            form.classList.add('was-validated');
            return false;
        }

        form.classList.add('was-validated');
        return true;
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
                swal({
                    title: "Error!",
                    text: "Could not make a new interview for Application!\n" + error.responseJSON.message,
                    icon: "error"
                });
            });
         }
        }

});