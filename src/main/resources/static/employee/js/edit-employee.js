$(document).ready(function() {
    vm = this;
    vm.employee = {};
    vm.events = [];

    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
            var calendarEl = document.getElementById('calendar');
            calendar = new FullCalendar.Calendar(calendarEl, {
                initialView: 'dayGridMonth',

                selectable: true,
                selectOverlap: false,
                select: function(info) {
                    calendar.addEvent({
                        title: "Working",
                        start: info.startStr,
                        end: info.endStr,
                        allDay: true
                    });
                    vm.events = calendar.getEvents();
                },
                eventClick: function(arg) {
                    swal({
                       title: "Remove this day from employees schedule?",
                       icon: "warning",
                       buttons: true,
                       dangerMode: true,
                     })
                     .then((willDelete) => {
                        if(willDelete){
                            arg.event.remove();
                            deleteEmployeeSchedule(vm.employee.id,arg.event._def.publicId);
                        }
                     });
                },

                events: function (info, successCallback, failureCallback){
                    let searchParams = new URLSearchParams(window.location.search);
                    let employeeId = searchParams.get('employeeId');
                    $.ajax({
                        type: "GET",
                        url: "/employees/"+ employeeId + "/schedules",

                        success: function(data){
                            var events = [];
                            for(event of data){
                                events.push(
                                {title: '',
                                    start: event.scheduledTime,
                                    allDay : true,
                                    id: event.id,
                                    color: setColorByStatus(event.employeeScheduleStatus)
                                    }
                                );
                            }
                            successCallback(events);
                        }
                    });
                }
            });
            calendar.render();
        }
    });

    function setColorByStatus(employeeScheduleStatus){
        if(employeeScheduleStatus != null){
            status = employeeScheduleStatus.status;
            switch(status){
                case "Unexcused Absence":
                    return "red";
                    break;
                case "Shift Worked":
                    return "#b7e1cd";
                    break;
                case "Double Shift Worked":
                    return "#045c32";
                    break;
                case "Excused Absence":
                    return "#ffc000";
                    break;
                case "Training Shift":
                    return "#00b0f0";
                    break;
                case "Director or Project Manager":
                    return "#5e1678";
                    break;
                default:
                    return "#007bff";
            }
        } else {
            return "#007bff";
        }
    }

    function deleteEmployeeSchedule(employeeId, employeeScheduleId){
        $.ajax({
            type: "DELETE",
            url: "/employees/" + employeeId + "/schedules/" + employeeScheduleId
        }).then(function(response){
            swal({
                title: "Success!",
                text: "You deleted this schedule day",
                icon: "success",
                timer: 2000
            }).then(function(){
                location.reload();
            });
        }).fail(function(error){
            console.log(error.responseJSON);
            swal({
                title: "Error!",
                text: "Could not delete scheduled day! \n" + error.responseJSON.message,
                icon: "error"
            }).then(function(){
                location.reload();
            });
        });
    }

    const employeeSetValuesAndOptions = (() => {
        const getEmployeeDetailsAndOptions = () =>{
            const searchParams = new URLSearchParams(window.location.search);
            if(searchParams.has('employeeId')){
                const employeeId = searchParams.get('employeeId');
                fetch(`/employees/${employeeId}/employeeDTO`)
                    .then(response => response.json())
                    .then(data => setDetailsAndOptions(data));
            }else{
                swal("ERROR", "Page didn't load right. Needs a Employee ID","error");
            }
        }

        const setDetailsAndOptions = (employeeDTO) => {
            setPositionOptions(employeeDTO.positionOptions);
            setOfficeOptions(employeeDTO.officeOptions);
            setEmployeeValues(employeeDTO.employee);
        }

        const setPositionOptions = (positionOptions) => {
            for(position of positionOptions){
                $('#position').append("<option value='"+position.code+"'>"+ position.name +"</option>");
            }
        }

        const setOfficeOptions = (officeOptions) => {
            for(office of officeOptions){
                $('#officeSelect').append("<option value='"+ office.id + "'>"+ office.name +"</option>");
            }
            $('#officeSelect').selectpicker('refresh');
        }

        const setEmployeeValues = (employee) => {
            vm.employee = employee;
            const employeeId = employee.id;
            const firstName = employee.firstName;
           const lastName = employee.lastName;
           const alias = employee.alias;
           const email = employee.email;
           const phoneNumber = employee.phoneNumber;
           const position = employee.position;
           const offices = employee.offices;
           const employeeNotes = employee.employeeNotes;
           const startDate = employee.startDate;
           const endDate = employee.endDate;
           const voluntary = employee.voluntary;

           $("#id").val(employeeId);
           $("#firstName").val(firstName);
           $("#lastName").val(lastName);
           $("#alias").val(alias);
           $("#email").val(email);
           $("#phoneNumber").val(phoneNumber);
           $("#startDate").val(startDate);
           $("#endDate").val(endDate);
           if(voluntary != null){
            $("#voluntary").val(voluntary.toString());
           }
           if(position != null){
                $("#position").val(position.code);
           }

           for(note of employee.employeeNotes){
                const message = note.note;
                const val = JSON.stringify(note);

                $("<textarea name='employeeNotes' class='form-control' value='"+ val + "' readonly>"+ message +
                    "</textarea> <div class='text-left'><a href='/employees/"+ vm.employee.id +"/notes/"+note.id+"' class='delete-note btn btn-danger'>Delete Note</a></div>").prependTo('#note-body');

           }

   //            Refresh multiselect with office ids
           var officeIds = [];
           for(office of offices){
               officeIds.push(office.id);
           }
           $('#officeSelect').selectpicker('val', officeIds);

        }

        return {getEmployeeDetailsAndOptions};
    })();

    employeeSetValuesAndOptions.getEmployeeDetailsAndOptions();

    $('#load-layout').on('click','#editEmployee', function(event){
        event.preventDefault();
        let validated = validationCheck();
        if(validated){
            let formJson = convertFormToJson($("form").serializeArray());
            let selectedOffices = $('#officeSelect').val();
            formJson.officeSelection = selectedOffices;
            if(formJson.employeeNotes != null){
                note = [{id:null,note:formJson.employeeNotes}];
                formJson.employeeNotes = note;
            }
            sendEmployeeToController(formJson);
        }
    });

    function validationCheck(){
        const offices = $('#officeSelect').val().toString();
        if(offices == null || offices === ""){
            swal({
                title: "Error!",
                text: "Must Select at least one office!",
                icon: "error"
            })
            form.classList.add('was-validated');
            return false;
        }
        const form = document.querySelector('#employee-form');
        if(form.checkValidity()  === false){
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

    function sendEmployeeToController(formJson){
        $.ajax({
            type: "PUT",
            url: "/employees/" + formJson.id,
            data: JSON.stringify(formJson),
            dataType: "json",
            contentType: "application/json; charset=utf-8"
        }).then(function(data){
            swal({
                title: "Success!",
                text: "You updated an employee",
                icon: "success",
                timer: 2000
            }).then(function(){
                window.location.href = "/employee/employee.html";
            });
        }).fail(function(error){
            console.log(error);
            swal("ERROR", "Could not update employee!","error");

        });
    }

    $('#load-layout').on('click', '#deleteEmployee', function(event){
        event.preventDefault();
        let employeeId = $('#id').val();
        swal({
           title: "Are you sure?",
           text: "Once deleted, you will not be able to recover this record!",
           icon: "warning",
           buttons: true,
           dangerMode: true,
         })
         .then((willDelete) => {
            if(willDelete){
                deleteEmployee(employeeId);
            }
         });
    });

    function deleteEmployee(employeeId){
        $.ajax({
            type: "DELETE",
            url: "/employees/" + employeeId
        }).then(function(response){
            swal({
                title: "Success!",
                text: "You deleted this employee",
                icon: "success",
                timer: 2000
            }).then(function(){
                window.location.href = "/employee/employee.html";
            });
        }).fail(function(error){
            console.log(error.responseJSON);
            swal({
                title: "Error!",
                text: "Could not delete employee! \n" + error.responseJSON.message,
                icon: "error"
            }).then(function(){
                location.reload();
            });
        });
    }

    $('#load-layout').on('click','#scheduleEmployee', function(event){
        event.preventDefault();

        let eventDates = [];

        for(event of vm.events){
            const startDate = event._instance.range.start
            let endDate = event._instance.range.end;
            endDate.setDate(endDate.getDate() -1);
            eventDates.push(startDate);

            if(startDate.getTime() < endDate.getTime()){
                let nextInRangeDate = new Date(startDate);
                nextInRangeDate.setDate(startDate.getDate() + 1);
                while(nextInRangeDate.getTime() != endDate.getTime() && nextInRangeDate.getTime() < endDate.getTime()){
                    eventDates.push(nextInRangeDate);
                    nextInRangeDate = new Date(nextInRangeDate);
                    nextInRangeDate.setDate(nextInRangeDate.getDate() + 1);
                }
                eventDates.push(endDate);
            }
        }
        $.ajax({
            type: "POST",
            url: "/employees/" + vm.employee.id + "/schedules",
            data: JSON.stringify(eventDates),
            dataType: "json",
            contentType: "application/json; charset=utf-8"
        }).then(function(response){
            swal({
                title: "Success!",
                text: "You Set a scheduled date for this employee",
                icon: "success",
                timer: 2000
            }).then(function(){
                location.reload();
            });
        }).fail(function(error){
            console.log(error.responseJSON);
            swal({
                title: "Error!",
                text: "Could not set schedule for employee! \n" + error.responseJSON.message,
                icon: "error"
            }).then(function(){
                location.reload();
            });
        });
    });


    $('#load-layout').on("click","a.delete-note",function(event){
        event.preventDefault();
        let url = event.target.href;
        swal({
           title: "Are you sure?",
           text: "Once deleted, you will not be able to recover this note!",
           icon: "warning",
           buttons: true,
           dangerMode: true,
         }).then((willDelete) => {
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
                        text: "Could NOT remove note! \n" + error.responseJSON.message,
                        icon: "error"
                    });
                });
            }
         });
     });


});
