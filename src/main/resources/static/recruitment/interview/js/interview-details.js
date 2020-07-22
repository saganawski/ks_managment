$(document).ready(function(){
    let searchParams = new URLSearchParams(window.location.search);
    if(searchParams.has('interviewId')){
        let interviewId = searchParams.get('interviewId');
        $.ajax({
            url:"/interviews/" + interviewId +"/dto"
        }).then(function(data){
            console.log(data);
            setFormData(data);
        }).fail(function(err){
            console.log(err);
            swal("Error:", "Failure to retrieve interview!","error");
        });
    }else{
       swal("Error:", "no ID provided!","error");
    }

    function setFormData(interview){
        setInterviewFormData(interview);
        setApplicationFormData(interview.application, interview.applicationFormOptionsDto, interview.officeOptions);
    }

    function setInterviewFormData(interview){
        $('#interview-id').val(interview.interviewId);
        if(interview.scheduledTime != null ){
            $('#scheduledTime').val(new Date(interview.scheduledTime).toLocaleString());
        }
        setConfirmationTypeOptions(interview.confirmationTypesOptions,interview.interviewConfirmationType);
        setInterviewResultsOptions(interview.interviewResultsOptions, interview.interviewResult);
        setSchedulerOptions(interview.schedulersOptions, interview.scheduler);
        setInterviewersOptions(interview.interviewersOptions, interview.interviewers);

        for(note of interview.interviewNotes){
            let message = note.note;
            let val = JSON.stringify(note);

            $("<textarea name='interviewNotes' class='form-control' value='"+ val + "' readonly>"+ message + "</textarea> <a href='/interviews/"+
                interview.interviewId+"/notes/"+note.id +"' class='delete-note btn btn-danger'>Delete Note</a>").prependTo('#interview-note-body');
        }

        $("div").removeClass("spinner-border");
    }

    function setConfirmationTypeOptions(options, currentType){
        for(type of options){
            $('#interviewConfirmationType').append("<option value='"+JSON.stringify(type)+"'>"+ type.type +"</option>");
        }

        if(currentType != null){
            $('#interviewConfirmationType').val(JSON.stringify(currentType));
        }
    }

    function setInterviewResultsOptions(options, currentResult){
        for(result of options){
            $('#interviewResult').append("<option value='"+JSON.stringify(result)+"'>"+ result.result +"</option>");
        }

        if(currentResult != null){
            $('#interviewResult').val(JSON.stringify(currentResult));
        }
    }

    function setSchedulerOptions(options, currentScheduler){
        for(scheduler of options){
            let name = scheduler.firstName + " " + scheduler.lastName;
            $('#scheduler').append("<option value='"+JSON.stringify(scheduler)+"'>"+ name +"</option>");
        }

        if(currentScheduler != null){
            $('#scheduler').val(JSON.stringify(currentScheduler));
        }
    }

    function setInterviewersOptions(options, currentInterviewers){
        let interviewerOptionsData = [];
            for(interviewer of options){
                let name = interviewer.firstName + " " + interviewer.lastName;
                option ={label:name, value:interviewer.id, name:name };
                interviewerOptionsData.push(option);
            }
            $('#interviewDirectors').multiselect('dataprovider', interviewerOptionsData);

        if(currentInterviewers != null){
            const interviewersId = currentInterviewers.map(interviewer => interviewer.id);
            $('#interviewDirectors').multiselect('select', interviewersId);

        }
    }

    function setApplicationFormData(application, formOptionsDto, officeOptions){
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

        // set options
        setApplicationContactType(formOptionsDto.applicationContactTypes, application.applicationContactType);
        setApplicationSource(formOptionsDto.applicationSources, application.applicationSource);
        setApplicationResult(formOptionsDto.applicationResults, application.result);
        //set office
        setOffice(officeOptions, application.office);

        for(note of application.applicationNotes){
            let message = note.note;
            let val = JSON.stringify(note);

            $("<textarea name='applicationNotes' class='form-control' value='"+ val + "' readonly>"+ message + "</textarea> <a href='/applications/"+
                application.id+"/notes/"+note.id +"' class='delete-note btn btn-danger'>Delete Note</a>").prependTo('#application-note-body');
        }
    }

    function setApplicationContactType(options, currentType){
        for(type of options){
            $('#applicationContactType').append("<option value='"+JSON.stringify(type)+"'>"+ type.type +"</option>");
        }

        if(currentType != null){
            $('#applicationContactType').val(JSON.stringify(currentType));
        }
    }

    function setApplicationSource(options, currentSource){
        for(source of options){
            $('#applicationSource').append("<option value='"+JSON.stringify(source)+"'>"+ source.source +"</option>");
        }
        if(currentSource != null){
            $('#applicationSource').val(JSON.stringify(currentSource));
        }
    }

    function setApplicationResult(options, currentResult){
        for(result of options){
            $('#applicationResult').append("<option value='"+JSON.stringify(result)+"'>"+ result.result +"</option>");
        }
        if(currentResult != null){
            $('#applicationResult').val(JSON.stringify(application.applicationResult));
        }
    }

    function setOffice(options, currentOffice){
        for(office of options){
            $('#office').append("<option value='"+JSON.stringify(office)+"'>"+ office.name +"</option>");
        }

        if(currentOffice != null ){
            if(currentOffice.location != null){
              delete currentOffice.location;
            }
            $('#office').val(JSON.stringify(currentOffice));
        }
    }

    $('#scheduledTime').daterangepicker({
        singleDatePicker: true,
        timePicker: true,
        locale: {
          format: 'M/DD/YYYY hh:mm A'
        }
    });



    $('#editInterview').on('click', function(event){
        event.preventDefault();
        let jsonForm = convertFormToJson($("form").serializeArray());
        let interviewConfirmationType = JSON.parse($('#interviewConfirmationType').val());
        let interviewResult = JSON.parse($('#interviewResult').val());
        let scheduler = JSON.parse($('#scheduler').val());
        let interviewersId = $('#interviewDirectors').val();
        let scheduledTime = new Date($('#scheduledTime').val()).toJSON();
        let interviewNotes = $('#new-note').val();

        if(jsonForm.interviewNotes != null){
            note = [{id:null,note:jsonForm.interviewNotes}];
            jsonForm.interviewNotes = note;
        }

        jsonForm.interviewConfirmationType = interviewConfirmationType;
        jsonForm.interviewResult = interviewResult;
        jsonForm.scheduler = scheduler;
        jsonForm.interviewersId = interviewersId;
        jsonForm.scheduledTime = scheduledTime;


        $.ajax({
            type: "PUT",
            url:"/interviews/" + jsonForm.id,
            data: JSON.stringify(jsonForm),
            contentType: "application/json; charset=utf-8"
        }).then(function(response){
            swal("Success!","You updated an interview","success");
//            createInterviewIfScheduled(response);
//            if(response.applicationResult.code != "SCHEDULED"){
//                window.location.href = "/recruitment/application/application.html";
//            }
        }).fail(function(err){
            console.log(err);
            swal("Error:", "Failure to update interview!","error");
        });

        });

        function convertFormToJson(form){
            var json = {}
            for(let j of form){
                json[j.name] = j.value || null;
            }
            return json;
        }

    $('#interview-note-body').on("click","a.delete-note",function(event){
        event.preventDefault();
        let url = event.target.href;
        $.ajax({
            type:"DELETE",
            url: url
        }).then(function(response){
            swal("Success!","You deleted a note","success");
            location.reload();
        }).fail(function(error){
            console.log(error);
            swal("ERROR", "Could NOT remove note!","error");
        });
     })

})
