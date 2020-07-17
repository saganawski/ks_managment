$(document).ready(function () {
	// TODO: something to load side bar and nav
	// TODO: break up to sep promises use promise all?
	const setSelectOptions = new Promise(function(resolve,reject){
        /*
        SET CONTACT OPTIONS
        */
        getContactOptions()
            .then(function(data){
                setContactTypeOptions(data);
            })
            .fail(function(err){
                console.log(err);
                return reject("Error: could not get Application Contact Types!");
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
                return reject("Error: could not get Application Source data!");
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
                return reject("Error: could not get Application Result data!");
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

        return resolve(true);
	})

    const getApplicationData = new Promise(function(resolve,reject){
        let searchParams = new URLSearchParams(window.location.search);
        if(searchParams.has('applicationId')){
            let applicationId = searchParams.get('applicationId');
            $.ajax({
                url:"/applications/" + applicationId
            }).then(function(data){
                return resolve(data);
            }).fail(function(err){
                console.log(err);
                return reject("Failure to retrieve application");
                alert("Failure to retrieve application");
            });
        }else{
           return reject("no ID provided ");
        }
    });

    function setApplicationFieldValues(application){
        $('#id').val(application.id);
        $('#firstName').val(application.firstName);
        $('#lastName').val(application.lastName);
        $('#phoneNumber').val(application.phoneNumber);
        $('#email').val(application.email);
        if(application.dateReceived != null ){
            $('#dateReceived').val(application.dateReceived.substr(0,10));
        }
        if(application.callBackDate != null){
            $('#callBackDate').val(application.callBackDate.substr(0,10));
        }
        if(application.office != null ){
            delete application.office.location;
            $('#office').val(JSON.stringify(application.office));
        }
        $('#applicationContactType').val(JSON.stringify(application.applicationContactType));
        $('#applicationSource').val(JSON.stringify(application.applicationSource));
        $('#applicationResult').val(JSON.stringify(application.applicationResult));

        for(note of application.applicationNotes){
            let message = note.note;
            let val = JSON.stringify(note);

            $("<textarea name='applicationNotes' class='form-control' value='"+ val + "' readonly>"+ message + "</textarea> <a href='/applications/"+ application.id+"/notes/"+note.id +"' class='delete-note btn btn-danger'>Delete Note</a>").prependTo('#note-body');
        }
    }

//TODO: clean this up  catch errors
    setSelectOptions.then(function(){
    });

    getApplicationData.then(results => {
        setTimeout(function(){
            setApplicationFieldValues(results);
            $("div").removeClass("spinner-border");
        },300);
    })

    $('#editApplication').on('click', function(event){
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
            type: "PUT",
            url:"/applications/" + jsonForm.id,
            data: JSON.stringify(jsonForm),
            contentType: "application/json; charset=utf-8"
        }).then(function(response){
            alert("Success! You updated an application");
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

     $('#deleteApplication').on('click', function(event){
            event.preventDefault();
            let applicationId = $('#id').val();
            if(confirm("Confirm You want to delete Application!?")){
                deleteApplication(applicationId);
            }
        });

        function deleteApplication(applicationId){
            $.ajax({
                type: "DELETE",
                url: "/applications/" + applicationId
            }).then(function(response){
                alert("Success! You deleted this application.");
                window.location.href = "/recruitment/application/application.html";
            }).fail(function(error){
                console.log(error);
                alert("Error: Could not delete employee.");
            });
        }

     $('#note-body').on("click","a.delete-note",function(event){
        event.preventDefault();
        let url = event.target.href;
        $.ajax({
            type:"DELETE",
            url: url
        }).then(function(response){
            location.reload();
        }).fail(function(error){
            console.log(error);
            alert("ERROR: Could NOT remove note!");
        });
     })


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
                debugger;
                window.location.href = "/recruitment/interview/interview-details.html";
            }).then(function(error){
                console.log(error);
                alert("ERROR: Something went wrong when creating a interview!");
            });
         }
     }
});