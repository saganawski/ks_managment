$(document).ready(function(){
    vm = this;
    vm.office = {};

    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
            $("div").removeClass("spinner-border");
        }
    });

    getOfficeOptions()
        .then(function(data){
            setOfficeOptions(data);
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
        }
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

    $('#statusFormSubmit').on('click', function(event){
        event.preventDefault();

        let employeeScheduleStatus = JSON.parse($('#statusSelect').val());

        let jsonForm = convertFormToJson($("#statusForm").serializeArray());

        vm.employeeSchedule.employeeScheduleStatus = employeeScheduleStatus;
        let payRate = jsonForm.payRate;
        let timeIn = jsonForm.timeIn;
        let timeOut = jsonForm.timeOut;
        let mileage = jsonForm.mileage;

        let lunchBoxChecked = $('#lunchBreak').prop('checked');

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

                if(lunchBoxChecked){
                    vm.employeeSchedule.employeeSchedulePayroll.lunch = true;
                }else{
                    vm.employeeSchedule.employeeSchedulePayroll.lunch = false;
                }

                vm.employeeSchedule.employeeSchedulePayroll.timeIn = timeIn;
                vm.employeeSchedule.employeeSchedulePayroll.timeOut = timeOut;
                vm.employeeSchedule.employeeSchedulePayroll.mileage = mileage;
            }else{
                let employeeSchedulePayroll = {id:null,payRate:payRate,timeIn:timeIn,timeOut:timeOut,mileage:mileage}
                if(checkBoxChecked){
                    employeeSchedulePayroll.payRate = customPayRate;
                }
                if(lunchBoxChecked){
                    employeeSchedulePayroll.lunch = true;
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
})
