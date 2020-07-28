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
            swal("Error:", "Failure to retrieve interview!","error");
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
            swal("Success!","You updated an interview","success");
            if(response.hasShow == true){
                createEmployeeOnHasShow();
            }else{
              location.reload();
            }
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

    function createEmployeeOnHasShow(){
        //TODO: check if employee already exists
        let newEmployeeJson = {};
        newEmployeeJson.firstName = vm.trainingDto.application.firstName;
        newEmployeeJson.lastName = vm.trainingDto.application.lastName;
        newEmployeeJson.email = vm.trainingDto.application.email;
        newEmployeeJson.phoneNumber = vm.trainingDto.application.phoneNumber;
        newEmployeeJson.offices = [vm.trainingDto.application.office];
        newEmployeeJson.position = {"id":	"23",
                                                "name":	"Canvasser",
                                                "code":	"CANVASSER"};
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
            swal("ERROR", "Could not create new Employee!","error");
        });


    }

    $('#note-body').on("click","a.delete-note",function(event){
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
             swal("Success!","You deleted this training","success");
             window.location.href = "/recruitment/training/training.html";
         }).fail(function(error){
             console.log(error);
             swal("ERROR", "Could NOT remove training!","error");
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
