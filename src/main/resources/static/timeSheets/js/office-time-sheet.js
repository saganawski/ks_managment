$(document).ready(function(){
    vm = this;
    vm.scheduleEvent = {};
    vm.office = {};
    vm.employeeSchedule = {};

    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
            var calendarEl = document.getElementById('calendar');
            fullCalendar = new FullCalendar.Calendar(calendarEl, {
                initialView: 'dayGridMonth',

                selectable: false,
                selectOverlap: false,

                headerToolbar: {
                    left: 'myCustomButton',
                    center: 'title',
                    right: 'prev,next'
                  },

                customButtons: {
                    myCustomButton: {
                        text: 'Choose Office',
                        click: function() {
                            $('#officeModal').modal('show');
                        }
                    }
                },

                eventClick: function(arg) {
                    vm.scheduleEvent = arg.event._def;
                    vm.employeeSchedule = vm.scheduleEvent.extendedProps.employeeSchedule;

                    setEventModalFields(vm.employeeSchedule);
                },


            });
            fullCalendar.render();
            $("div").removeClass("spinner-border");
            //Add pagination to the next button
            document.querySelector("#calendar > div.fc-header-toolbar.fc-toolbar.fc-toolbar-ltr > div:nth-child(3) > div > button.fc-next-button.fc-button.fc-button-primary").addEventListener('click',function(){
                if(!jQuery.isEmptyObject(vm.office)){
                    startOfMonth = fullCalendar.getDate();
                    let year = startOfMonth.getFullYear();
                    let month = startOfMonth.getMonth();
                    let endOfMonth = new Date(year,month + 1, 0);

                    $.ajax({
                        type: "Get",
                        url: "/employees/schedules/office/" + vm.office.id +"/startDate/" + startOfMonth.toISOString()+ "/endDate/" + endOfMonth.toISOString(),
                    }).then(function(data){
                        let employeeSchedules = data;
                        setEvents(employeeSchedules);
                    }).fail(function(error){
                        console.log(error);
                        swal({
                            title: "Error!",
                            text: "Could not employee schedules for this office! \n" + error.responseJSON.message,
                            icon: "error"
                        });

                    });
                }
            });
            //ADD pagination to the previous button
            document.querySelector("#calendar > div.fc-header-toolbar.fc-toolbar.fc-toolbar-ltr > div:nth-child(3) > div > button.fc-prev-button.fc-button.fc-button-primary > span").addEventListener('click',function(){
                if(!jQuery.isEmptyObject(vm.office)){
                    startOfMonth = fullCalendar.getDate();
                    startOfMonth.setMonth(startOfMonth.getMonth() -1);
                    
                    let year = startOfMonth.getFullYear();
                    let month = startOfMonth.getMonth();
                    let endOfMonth = new Date(year,month + 1, 0);

                    $.ajax({
                        type: "Get",
                        url: "/employees/schedules/office/" + vm.office.id +"/startDate/" + startOfMonth.toISOString()+ "/endDate/" + endOfMonth.toISOString(),
                    }).then(function(data){
                        let employeeSchedules = data;
                        setEvents(employeeSchedules);
                        fullCal
                    }).fail(function(error){
                        console.log(error);
                        swal({
                            title: "Error!",
                            text: "Could not employee schedules for this office! \n" + error.responseJSON.message,
                            icon: "error"
                        });

                    });
                }
            });


        }
    });

    function setEventModalFields(employeeSchedule){
        $('#statusForm').trigger('reset');

        $('#statusModal').modal('show');
        $('#statusTitle').text(vm.scheduleEvent.title + ", " + vm.scheduleEvent.extendedProps.firstName);

        if(employeeSchedule.employeeScheduleStatus != null){
            delete employeeSchedule.employeeScheduleStatus.hibernateLazyInitializer;
            delete employeeSchedule.employeeScheduleStatus.id;
        }

        $('#statusSelect').val(JSON.stringify(employeeSchedule.employeeScheduleStatus));

        if(employeeSchedule.employeeSchedulePayroll != null){
            let payRate = employeeSchedule.employeeSchedulePayroll.payRate;
            if(payRate !=null){
                let standardRateOptions = [15,17];
                let isStandardRate = standardRateOptions.includes(payRate);
                if(isStandardRate){
                    $('#payRateSelect').val(payRate);
                    $('#customPayRate').prop('disabled',true);
                    $('#payRateSelect').prop('disabled',false);
                    $('#customPayRateCheckBox').prop('checked',false);
                }else{
                    $('#customPayRate').val(payRate);
                    $('#customPayRate').prop('disabled',false);
                    $('#payRateSelect').prop('disabled',true);
                    $('#customPayRateCheckBox').prop('checked',true);
                }
            }

            let timeIn = employeeSchedule.employeeSchedulePayroll.timeIn;
            if(timeIn != null){
                $('#timeIn').val(timeIn);
            }

            let timeOut = employeeSchedule.employeeSchedulePayroll.timeOut;
            if(timeOut != null){
                $('#timeOut').val(timeOut);
            }

            let mileage = employeeSchedule.employeeSchedulePayroll.mileage;
            if(mileage != null){
                $('#mileage').val(mileage);
            }
        }
    }

    getOfficeOptions()
        .then(function(data){
            setOfficeOptions(data);
            $("div").removeClass("spinner-border");
        })
        .fail(function(err){
            console.log(err);
            swal({
                title: "Error!",
                text: "Could not get Offices for drop down\n" + err.responseJSON.message,
                icon: "error"
            });
        });


    function getOfficeOptions(){
        return $.ajax({
            type:"GET",
            url:"/offices"
        });
    }

    function setOfficeOptions(offices){
        for(office of offices){
            $('#officeSelect').append("<option value='"+JSON.stringify(office)+"'>"+ office.name +"</option>");
            $('#officeSelect').selectpicker('refresh');
        }
    }

    getStatusOptions()
        .then(function(data){
            setStatusOptions(data._embedded.employeeScheduleStatuses);
        })
        .fail(function(err){
            console.log(err);
            swal({
                title: "Error!",
                text: "Could not get statuses for drop down\n" + err.responseJSON.message,
                icon: "error"
            });
        });

    function getStatusOptions(){
        return $.ajax({
            type:"GET",
            url:"/employeeScheduleStatuses"
        });
    }

    function setStatusOptions(statuses){
        statuses.forEach(status => {
            delete status._links;
            $('#statusSelect').append("<option value='"+JSON.stringify(status)+"'>"+ status.status +"</option>");
        });
    }

    $('#officeFormSubmit').on('click', function(event){
        event.preventDefault();
        let validated = validationCheck();
        if(validated){
            let office = JSON.parse($('#officeSelect').val());
            vm.office = office;
            getEventsByOffice(office.id);
        }

    });

    function validationCheck(){
        const form = document.querySelector('#officeForm');
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
        if(form.checkValidity()  === false){
            form.classList.add('was-validated');
            return false;
        }

        form.classList.add('was-validated');
        return true;
    }

    function getEventsByOffice(officeId){
        let today = new Date();
        let year = today.getFullYear();
        let month = today.getMonth();
        let startOfMonth = new Date(year,month,1);
        let endOfMonth = new Date(year,month + 1, 0);

        $.ajax({
            type: "Get",
            url: "/employees/schedules/office/" + officeId +"/startDate/" + startOfMonth.toISOString()+ "/endDate/" + endOfMonth.toISOString(),
        }).then(function(data){
            let employeeSchedules = data;
            setEvents(employeeSchedules);
        }).fail(function(error){
            console.log(error);
            swal({
                title: "Error!",
                text: "Could not employee schedules for this office! \n" + error.responseJSON.message,
                icon: "error"
            });

        });
    }

    function setEvents(employeeSchedules){
        $("#initialLoad").addClass("spinner-border");
        $('#officeModal').modal('hide');

        fullCalendar.removeAllEvents();
        let events = [];

        for(schedule of employeeSchedules){
            event = {
                id: schedule.id,
                title: schedule.employee.lastName,
                start: schedule.scheduledTime,
                allDay : true,
                color : setColorByStatus(schedule.employeeScheduleStatus),
                extendedProps: {
                    status: schedule.employeeScheduleStatus,
                    firstName: schedule.employee.firstName,
                    employeeSchedule: schedule
                }
            };
            events.push(event);
        }

        fullCalendar.addEventSource(events);
        $("div").removeClass("spinner-border");
    }

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

    $('#statusFormSubmit').on('click', function(event){
        event.preventDefault();

        let employeeScheduleStatus = JSON.parse($('#statusSelect').val());

        let jsonForm = convertFormToJson($("#statusForm").serializeArray());

        vm.employeeSchedule.employeeScheduleStatus = employeeScheduleStatus;
        let payRate = jsonForm.payRate;
        let timeIn = jsonForm.timeIn;
        let timeOut = jsonForm.timeOut;
        let mileage = jsonForm.mileage;

        let checkBoxChecked = $('#customPayRateCheckBox').prop('checked');
        if(checkBoxChecked){
            var customPayRate = $('#customPayRate').val();
        }

        let validated = payRollValidation(employeeScheduleStatus, timeIn,timeOut);

        if(validated){
            if(vm.employeeSchedule.employeeSchedulePayroll != null){
                if(checkBoxChecked){
                    vm.employeeSchedule.employeeSchedulePayroll.payRate = customPayRate;
                }else{
                    vm.employeeSchedule.employeeSchedulePayroll.payRate = payRate;
                }
                vm.employeeSchedule.employeeSchedulePayroll.timeIn = timeIn;
                vm.employeeSchedule.employeeSchedulePayroll.timeOut = timeOut;
                vm.employeeSchedule.employeeSchedulePayroll.mileage = mileage;
            }else{
                let employeeSchedulePayroll = {id:null,payRate:payRate,timeIn:timeIn,timeOut:timeOut,mileage:mileage}
                if(checkBoxChecked){
                    employeeSchedulePayroll.payRate = customPayRate;
                }
                vm.employeeSchedule.employeeSchedulePayroll = employeeSchedulePayroll;
            }
            setEmployeeScheduleStatusAndPayRoll(vm.employeeSchedule);
        }

    });

    function payRollValidation(employeeScheduleStatus, timeIn,timeOut){
        const form = document.querySelector('#statusForm');

         if(employeeScheduleStatus == null || employeeScheduleStatus === ""){
            swal({
                title: "Error!",
                text: "Must Select a status!",
                icon: "error"
            });
            form.classList.add('was-validated');
            return false;
        }

        if(timeIn != null && timeOut != null){
            if(timeIn > timeOut){
                swal({
                    title: "Error!",
                    text: "Time Out must be after Time In!",
                    icon: "error"
                });
                const formGroup = document.getElementById('timeOut');
                const invalidFeedback = formGroup.parentElement.querySelector('.invalid-feedback');
                formGroup.className = 'form-control is-invalid';
                form.classList.add('was-validated');
                return false;
            }
        }
        //Checks payRate
        if(form.checkValidity()  === false){
            form.classList.add('was-validated');
            return false;
        }
        return true;
    }

    function setEmployeeScheduleStatusAndPayRoll(employeeSchedule){
        $.ajax({
            type: "POST",
            url: "/employees/employee-schedule/" + employeeSchedule.id + "/status-payroll",
            data: JSON.stringify(employeeSchedule),
            dataType: "json",
            contentType: "application/json; charset=utf-8"
        }).then(function(response){
            swal({
                title: "Success!",
                text: "You updated this Employees Schedule date",
                icon: "success",
                timer: 2000
            }).then(function(){
                getEventsByOffice(vm.office.id);
                $('#statusModal').modal('hide');
            });
        }).fail(function(error){
            console.log(error.responseJSON);
            swal({
                title: "Error!",
                text: "Could not update employee schedule! \n" + error.responseJSON.message,
                icon: "error"
            });
        });
    }

    function convertFormToJson(form){
        var json = {}
        for(let j of form){
            json[j.name] = j.value || null;
        }
        return json;
    }

    $('#customPayRateCheckBox').on('click', function(event){
        let checkBoxChecked = $('#customPayRateCheckBox').prop('checked');
        if(checkBoxChecked){
            $('#customPayRate').prop('disabled',false);
            $('#payRateSelect').prop('disabled',true);
        }else{
            $('#customPayRate').prop('disabled',true);
            $('#payRateSelect').prop('disabled',false);
        }
    });
})
