$(document).ready(function(){
    vm = this;
    vm.interviewDto = {};
    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
            $('#scheduledTime').daterangepicker({
                singleDatePicker: true,
                timePicker: true,
                locale: {
                  format: 'M/DD/YYYY hh:mm A'
                }
            });
        }
    });

    let searchParams = new URLSearchParams(window.location.search);
    if(searchParams.has('interviewId')){
        let interviewId = searchParams.get('interviewId');
        $.ajax({
            url:"/interviews/" + interviewId +"/dto"
        }).then(function(data){
            vm.interviewDto = data;
            setFormData(data);
        }).fail(function(err){
            console.log(err);
            swal({
                title: "Error!",
                text: "Failure to get interview! \n" + err.responseJSON.message,
                icon: "error"
            });
        });
    }else{
       swal("Error:", "no ID provided!","error");
    }

    function setFormData(interview){
        setInterviewFormData(interview);
    }

    function setInterviewFormData(interview){
        $('#interview-id').val(interview.interviewId);

        const cardHeader = document.getElementById("card-title");
        const intervieweeName = interview.application.lastName + ", " + interview.application.firstName;
        const intervieweePhone = interview.application.phoneNumber;
        let pTag = document.createElement('p');
        const textNode = document.createTextNode(intervieweeName + " " + intervieweePhone);
        pTag.appendChild(textNode);
        cardHeader.appendChild(pTag);

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
            for(interviewer of options){
                let name = interviewer.firstName + " " + interviewer.lastName;
                $('#interviewDirectors').append("<option value='"+ interviewer.id + "'>"+ name +"</option>");
            }
            $('#interviewDirectors').selectpicker('refresh');

        if(currentInterviewers != null){
            const interviewersId = currentInterviewers.map(interviewer => interviewer.id);
            $('#interviewDirectors').selectpicker('val', interviewersId);

        }
    }

    const checkForTraining = (interviewId, applicationId) =>{
        return new Promise((resolve,reject)=>{
             $.ajax({
                type:"GET",
                url: "/trainings/applications/"+ applicationId +"/interviews/" + interviewId
            }).then(function(response){
                return resolve(response);
            }).fail(function(error){
                console.log(error);
                swal({
                    title: "Error!",
                    text: "Something went wrong when checking if an training already exists! \n" + error.responseJSON.message,
                    icon: "error"
                });
                return reject("failure to get employee status");
            });
        })
    }

    $('#load-layout').on('click','#editInterview', function(event){
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
            let interview = response;
            swal({
                 title: "Success!",
                 text: "You updated an interview",
                 icon: "success",
                 timer: 2000
             });
            if(interview.interviewResult != null){
                if(interview.interviewResult.code != undefined){
                    createTrainingIfHired(response);
                }
            }else{
                location.reload();
            }
        }).fail(function(err){
            console.log(err);
            swal({
                title: "Error!",
                text: "Failure to update interview! \n" + err.responseJSON.message,
                icon: "error"
            });
        });

    });

        function convertFormToJson(form){
            var json = {}
            for(let j of form){
                json[j.name] = j.value || null;
            }
            return json;
        }
        function createTrainingIfHired(interview){
             let scheduleTraining= interview.interviewResult.code == "HIRED";

             checkForTraining(interview.id,interview.application.id).then(hasTraining =>{
                 if(scheduleTraining && !hasTraining){
                    let training = {interview: interview, application: interview.application};
                    $.ajax({
                        type:"POST",
                        url: "/trainings",
                        data: JSON.stringify(training),
                        contentType: "application/json; charset=utf-8"
                    }).then(function(response){
                        swal({
                             title: "Success!",
                             text: "You created an training",
                             icon: "success",
                             timer: 2000
                         }).then(function(){
                            window.location.href = "/recruitment/training/training-details.html" +"?trainingId=" + response.id;
                         })
                    }).fail(function(error){
                        console.log(error);
                        swal({
                            title: "Error!",
                            text: "Something went wrong when creating a training for interview!\n" + error.responseJSON.message,
                            icon: "error"
                        });
                    });
                 }else{
                    location.reload();
                 }
             });

         }

    $('#load-layout').on("click","a.delete-note",function(event){
        event.preventDefault();
        let url = event.target.href;
        swal({
           title: "Are you sure?",
           text: "Once deleted, you will not be able to recover this note!",
           icon: "warning",
           buttons: true,
           dangerMode: true,
         })
         .then((willDelete) => {
            if(willDelete){
                $.ajax({
                    type:"DELETE",
                    url: url
                }).then(function(response){
                    swal({
                         title: "Success!",
                         text: "You deleted a note",
                         icon: "success",
                         timer: 2000
                     }).then(function(){
                         location.reload();
                     });
                }).fail(function(error){
                    console.log(error);
                    swal({
                        title: "Error!",
                        text: "Could NOT remove note!\n" + error.responseJSON.message,
                        icon: "error"
                    });
                });
            }
         });
     })

     $('#load-layout').on('click','#deleteInterview', function(event){
         event.preventDefault();
         let interviewId = $('#interview-id').val();
         swal({
           title: "Are you sure?",
           text: "Once deleted, you will not be able to recover this record!",
           icon: "warning",
           buttons: true,
           dangerMode: true,
         })
         .then((willDelete) => {
            if(willDelete){
                deleteInterview(interviewId);
            }
         });
     });

     function deleteInterview(interviewId){
         $.ajax({
             type: "DELETE",
             url: "/interviews/" + interviewId
         }).then(function(response){
             swal({
                 title: "Success!",
                 text: "You deleted this interview",
                 icon: "success",
                 timer: 2000
             }).then(function(){
                window.location.href = "/recruitment/interview/interview.html";
             });
         }).fail(function(error){
             console.log(error);
             swal({
                 title: "Error!",
                 text: "Something went wrong when creating a training for interview!\n" + error.responseJSON.message,
                 icon: "error"
             });
         });
     }

     $('#load-layout').on('click', '#showApplication', function(event){
         event.preventDefault();
         window.location.href= "/recruitment/application/application-edit.html?applicationId=" + vm.interviewDto.application.id;
      });

})
