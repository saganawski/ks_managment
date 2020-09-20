$(document).ready(function(){
    vm = this;
    vm.projectDto = {};

    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
        }
    });

    let searchParams = new URLSearchParams(window.location.search);
    if(searchParams.has('projectId')){
        let projectId = searchParams.get('projectId');
        $.ajax({
            url:"/projects/" + projectId +"/dto"
        }).then(function(data){
            vm.projectDto = data;
            setTableData(data);
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

    function setTableData(projectDto){
       table = $('#project-table').DataTable({
            "initComplete": function(settings, json){
                $("div").removeClass("spinner-border");
            },
            data: projectDto.projectWeeks,
            columns :[
                {"data" : "id",
                    "defaultContent": ""},
                {"data" : "originalShiftGoal",
                    "defaultContent": ""},
                {"data" : "startDate",
                    "defaultContent":""},
                {"data" : "endDate",
                    "defaultContent":""},
                {"data" : "currentShiftGoal",
                    "defaultContent":""},
                {"data" : "shiftsScheduled",
                    "defaultContent":""},
                {"data" : function(data,type,row,meta){
                        return data.shiftsScheduled - data.currentShiftGoal;
                    }
                },
                {"data" : "shiftsCompleted",
                    "defaultContent":""},
                {"data" : function(data,type,row,meta){
                        return data.shiftsCompleted - data.currentShiftGoal
                    }
                },
                {"data" : "remainingWorkingDays",
                    "defaultContent":""},
                {"data" : "shiftsNeeded",
                    "defaultContent":""},
                {   "targets": -1,
                    "data": function(data, type,row,meta){
                        return '<a class="btn btn-warning" href="#">Details</a>'
                    }
                }
            ]
        });
    }

    $('#load-layout').on('click', '#newProjectWeek',function(event){
        event.preventDefault();

        $.ajax({
            type: "POST",
            url:"/projects/" + vm.projectDto.id +"/new-week",
        }).then(function(response){
            swal({
                title: "Success!",
                text: "You created a new week! \n Please update fields",
                icon: "success",
                timer: 2000
            }).then(function(){
                location.reload();
            });
        }).fail(function(err){
            console.log(err);
            swal({
                title: "Error!",
                text: "Failure to create new week! \n" + err.responseJSON.message,
                icon: "error"
            });
        });

    });

    $('#load-layout').on('click', '#project-table tbody a', function () {
            //TODO: launch modal
            // project goals?
            let data = table.row( $(this).closest('tr')).data();

            $('#id').val(data.id);
            $('#originalShiftGoal').val(data.originalShiftGoal);
            $('#startDate').val(data.startDate);
            $('#endDate').val(data.endDate);
            $('#currentShiftGoal').val(data.currentShiftGoal);
            $('#remainingWorkingDays').val(data.remainingWorkingDays);
            $('#weekModal').modal('show');
    });


     $('#weekFormSubmit').on('click', function(event){
        event.preventDefault();
        let validated = validationCheck();
        if(validated){
            let formJson = convertFormToJson($("#weekForm").serializeArray());
            $.ajax({
                type: "PUT",
                url:"/projects/" + vm.projectDto.id +"/week",
                data: JSON.stringify(formJson),
                dataType: "json",
                contentType: "application/json; charset=utf-8"
            }).then(function(response){
                swal({
                    title: "Success!",
                    text: "You updated a week",
                    icon: "success",
                    timer: 2000
                }).then(function(){
                    location.reload();
                });
            }).fail(function(err){
                console.log(err);
                swal({
                    title: "Error!",
                    text: "Failure to update week! \n" + err.responseJSON.message,
                    icon: "error"
                });
            });
        }
    });

    function validationCheck(){
        const form = document.querySelector('#weekForm');
        if(form.checkValidity() === false){
            form.classList.add('was-validated');
            return false;
        }

        form.classList.add('was-validated');
        return true;
    }

    function convertFormToJson(form){
        var json = {}
        for(let j of form){
            json[j.name] = j.value || null;
        }
        return json;
    }

})
