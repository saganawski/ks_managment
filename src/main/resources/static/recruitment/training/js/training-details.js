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
       $('#hasShow').val(training.hasShow.toString());
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
            location.reload();
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
//
//    $('#interview-note-body').on("click","a.delete-note",function(event){
//        event.preventDefault();
//        let url = event.target.href;
//        $.ajax({
//            type:"DELETE",
//            url: url
//        }).then(function(response){
//            swal("Success!","You deleted a note","success");
//            location.reload();
//        }).fail(function(error){
//            console.log(error);
//            swal("ERROR", "Could NOT remove note!","error");
//        });
//     })
//
//     $('#deleteInterview').on('click', function(event){
//         event.preventDefault();
//         let interviewId = $('#interview-id').val();
//         swal({
//           title: "Are you sure?",
//           text: "Once deleted, you will not be able to recover this record!",
//           icon: "warning",
//           buttons: true,
//           dangerMode: true,
//         })
//         .then((willDelete) => {
//            deleteInterview(interviewId);
//         });
//     });
//
//     function deleteInterview(interviewId){
//         $.ajax({
//             type: "DELETE",
//             url: "/interviews/" + interviewId
//         }).then(function(response){
//             swal("Success!","You deleted this interview","success");
//             window.location.href = "/recruitment/interview/interview.html";
//         }).fail(function(error){
//             console.log(error);
//             swal("ERROR", "Could NOT remove interview!","error");
//         });
//     }

})
