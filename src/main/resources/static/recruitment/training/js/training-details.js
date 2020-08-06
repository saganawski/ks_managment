$(document).ready(function(){
    vm = this;
    vm.trainingDto = {};

    let searchParams = new URLSearchParams(window.location.search);
    if(searchParams.has('trainingId')){
        let trainingId = searchParams.get('trainingId');
        $.ajax({
            url:"/trainings/" + trainingId +"/dto"
        }).then(function(data){
            console.log(data);
            vm.trainingDto = data;
            setFormData(data);
        }).fail(function(err){
            console.log(err);
            swal({
                title: "Error!",
                text: "Failure to retrieve training! \n" + err.responseJSON.message,
                icon: "error"
            });
        });
    }else{
       swal("Error:", "no ID provided!","error");
    }

    function setFormData(training){
       $('#id').val(training.id);
       if(training.hasShow != null){
            $('#hasShow').val(training.hasShow.toString());
       }
       if(training.scheduledTime != null ){
           $('#scheduledTime').val(new Date(training.scheduledTime).toLocaleString());
       }
       setConfirmationTypeOptions(training.trainingConfirmationTypeOptions,training.trainingConfirmationType);
       setTrainerOptions(training.trainerOptions, training.trainer);
       for(note of training.trainingNotes){
           let message = note.note;
           let val = JSON.stringify(note);

           $("<textarea name='trainingNotes' class='form-control' value='"+ val + "' readonly>"+ message + "</textarea> <a href='/trainings/"+
               training.id+"/notes/"+note.id +"' class='delete-note btn btn-danger'>Delete Note</a>").prependTo('#note-body');
       }

       $("div").removeClass("spinner-border");
    }

    function setConfirmationTypeOptions(options, currentType){
        for(type of options){
            $('#trainingConfirmationType').append("<option value='"+JSON.stringify(type)+"'>"+ type.type +"</option>");
        }

        if(currentType != null){
            $('#trainingConfirmationType').val(JSON.stringify(currentType));
        }
    }

    function setTrainerOptions(options, currentTrainer){
        for(trainer of options){
            let name = trainer.firstName + " " + trainer.lastName;
            $('#trainer').append("<option value='"+JSON.stringify(trainer)+"'>"+ name +"</option>");
        }

        if(currentTrainer != null){
            $('#trainer').val(JSON.stringify(currentTrainer));
        }
    }

    $('#scheduledTime').daterangepicker({
        singleDatePicker: true,
        timePicker: true,
        locale: {
          format: 'M/DD/YYYY hh:mm A'
        }
    });

    const checkIfEmployeeExists = (lastName, email) =>{
        return new Promise((resolve,reject)=>{
             $.ajax({
                type:"GET",
                url: "/employees/lastname/"+ lastName + "/email/" + email
            }).then(function(response){
                return resolve(response);
            }).fail(function(error){
                console.log(error);
                swal({
                    title: "Error!",
                    text: "Something went wrong when checking if an employee already exists! \n" + error.responseJSON.message,
                    icon: "error"
                });
                return reject("failure to get employee status");
            });
        })
    }

    $('#editTraining').on('click', function(event){
        event.preventDefault();
        let jsonForm = convertFormToJson($("form").serializeArray());
        let trainingConfirmationType = JSON.parse($('#trainingConfirmationType').val());
        let hasShow = JSON.parse($('#hasShow').val());
        let trainer = JSON.parse($('#trainer').val());
        let scheduledTime = new Date($('#scheduledTime').val()).toJSON();
        let trainingNotes = $('#new-note').val();

        if(jsonForm.trainingNotes != null){
            note = [{id:null,note:jsonForm.trainingNotes}];
            jsonForm.trainingNotes = note;
        }

        jsonForm.trainingConfirmationType = trainingConfirmationType;
        jsonForm.trainer = trainer;
        jsonForm.scheduledTime = scheduledTime;
        jsonForm.application = vm.trainingDto.application;
        jsonForm.interview = vm.trainingDto.interview;

        $.ajax({
            type: "PUT",
            url:"/trainings/" + jsonForm.id,
            data: JSON.stringify(jsonForm),
            contentType: "application/json; charset=utf-8"
        }).then(function(response){
            swal({
                title: "Success!",
                text: "You updated an interview",
                icon: "success",
                timer: 2000
            }).then(function(){
                if(response.hasShow == true){
                    createEmployeeOnHasShow();
                }else{
                  location.reload();
                }
            });
        }).fail(function(err){
            console.log(err);
            swal({
                title: "Error!",
                text: "Failure to update training! \n" + err.responseJSON.message,
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

    function createEmployeeOnHasShow(){
        checkIfEmployeeExists(vm.trainingDto.application.lastName,vm.trainingDto.application.email).then(hasEmployee =>{
            debugger;
            if(!hasEmployee){
                let newEmployeeJson = {};
                newEmployeeJson.firstName = vm.trainingDto.application.firstName;
                newEmployeeJson.lastName = vm.trainingDto.application.lastName;
                newEmployeeJson.email = vm.trainingDto.application.email;
                newEmployeeJson.phoneNumber = vm.trainingDto.application.phoneNumber;
                newEmployeeJson.offices = [vm.trainingDto.application.office];
                // TODO: this shouldnt be hardcoded
        //        newEmployeeJson.position = {"id":	"23",
        //                                                "name":	"Canvasser",
        //                                                "code":	"CANVASSER"};
                let url = "/employees/new";
                $.ajax({
                    type : "POST",
                    url : url,
                    data: JSON.stringify(newEmployeeJson),
                    contentType: "application/json; charset=utf-8"
                }).then(function(response){
                    swal({
                        title: "Success!",
                        text: "You have created a new employee. You will now be directed to the employee to finalize any details",
                        icon: "success",
                        buttons: ["Stay on current page!", true]
                    }).then((value) => {
                        if(value){
                            window.location.href = "/employee/employee-details.html" +"?employeeId=" + response.id;
                        }else{
                            location.reload();
                        }
                    });
                }).fail(function(error){
                    console.log(error);
                    swal({
                        title: "Error!",
                        text: "Failure to create new employee! \n" + error.responseJSON.message,
                        icon: "error"
                    });
                });
            }
        });

    }

    $('#note-body').on("click","a.delete-note",function(event){
        event.preventDefault();
        let url = event.target.href;
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
                text: "Could NOT remove note! \n" + error.responseJSON.message,
                icon: "error"
            });
        });
     })

     $('#deleteTraining').on('click', function(event){
         event.preventDefault();
         let trainingId = vm.trainingDto.id;
         swal({
           title: "Are you sure?",
           text: "Once deleted, you will not be able to recover this record!",
           icon: "warning",
           buttons: true,
           dangerMode: true,
         })
         .then((willDelete) => {
            deleteTraining(trainingId);
         });
     });

     function deleteTraining(trainingId){
         $.ajax({
             type: "DELETE",
             url: "/trainings/" + trainingId
         }).then(function(response){
             swal({
                 title: "Success!",
                 text: "You deleted this training",
                 icon: "success",
                 timer: 2000
             }).then(function(){
                window.location.href = "/recruitment/training/training.html";
             });
         }).fail(function(error){
             console.log(error);
             swal({
                 title: "Error!",
                 text: "Could NOT remove training! \n" + error.responseJSON.message,
                 icon: "error"
             });
         });
     }

     $('#showApplication').on('click', function(event){
        event.preventDefault();
        window.location.href= "/recruitment/application/application-edit.html?applicationId=" + vm.trainingDto.application.id;
     });

     $('#showInterview').on('click', function(event){
        event.preventDefault();
        window.location.href= "/recruitment/interview/interview-details.html?interviewId=" + vm.trainingDto.interview.id;
     });

})
