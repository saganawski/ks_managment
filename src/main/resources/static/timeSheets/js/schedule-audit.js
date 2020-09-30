$(document).ready(function(){
    vm = this;
    vm.office = {};

    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
            setDataTable();
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

    function setDataTable(){
        $('#schedule-audit-table').DataTable({
            "initComplete": function(settings, json){
                $("div").removeClass("spinner-border");
            },
            ajax:{
                "url": "/scheduleAudits",
                "dataSrc": ""
            },
            columns :[
                {"data" : "id"},
                {"data" : function(data,type,row,meta){
                    if(data.office == undefined){
                        return "";
                    }
                    return data.office.name;
                }, "defaultContent": ""},
                {"data" : function(data,type,row,meta){
                        if(data.startDate == null){
                            return "";
                        }
                        return moment(data.startDate).format('YYYY-MM-DD');
                    }
                },
                {"data" : function(data,type,row,meta){
                        if(data.endDate == null){
                            return "";
                        }
                        return data.endDate;
                    }
                },
                {   "targets": -1,
                    "data": function(data, type,row,meta){
                        return '<a class="btn btn-warning" href="/employee/employee-details.html?employeeId='+ data.id +'">Details</a>'
                    }
                }
            ]
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


    $('#statusFormSubmit').on('click', function(event){
        event.preventDefault();

    });


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
