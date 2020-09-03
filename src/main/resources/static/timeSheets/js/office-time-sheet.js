$(document).ready(function(){
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

                            $('#officeModal').modal('show');
                        }
                    }
                },

                eventClick: function(arg) {
                    console.log(arg);
                    alert(arg);
//                    swal({
//                       title: "Remove this day from employees schedule?",
//                       icon: "warning",
//                       buttons: true,
//                       dangerMode: true,
//                     })
//                     .then((willDelete) => {
//                        if(willDelete){
//                            arg.event.remove();
//                            deleteEmployeeSchedule(vm.employee.id,arg.event._def.publicId);
//                        }
//                     });
                },

//                events: function (info, successCallback, failureCallback){
//                    let searchParams = new URLSearchParams(window.location.search);
//                    let employeeId = searchParams.get('employeeId');
//                    $.ajax({
//                        type: "GET",
//                        url: "/employees/"+ employeeId + "/schedules",
//
//                        success: function(data){
//                            var events = [];
//                            for(event of data){
//                                events.push(
//                                {title: '',
//                                    start: event.scheduledTime,
//                                    allDay : true,
//                                    id: event.id}
//                                );
//                            }
//                            successCallback(events);
//                        }
//                    });
//                }
            });
            fullCalendar.render();
        }
    });

    function getOfficeOptions(){
        return $.ajax({
            type:"GET",
            url:"/offices"
        });
    }

    function setOfficeOptions(offices){
        for(office of offices){
//            delete office.location;
//            TODO: this has got to be my bug in application-edit
            $('#officeSelect').append("<option value='"+JSON.stringify(office)+"'>"+ office.name +"</option>");
            $('#officeSelect').selectpicker('refresh');
        }
    }

    $('#officeFormSubmit').on('click', function(event){
        event.preventDefault();
        let validated = validationCheck();
        if(validated){
            //reset calaneder events
            // spinner
            fullCalendar.removeAllEvents();
            let office = JSON.parse($('#officeSelect').val());

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
            event.stopPropagation();
            form.classList.add('was-validated');
            return false;
        }

        form.classList.add('was-validated');
        return true;
    }

    function getEventsByOffice(officeId){
        $.ajax({
            type: "Get",
            url: "/employees/schedules/office/" + officeId,
        }).then(function(data){
            console.log(data);
        }).fail(function(error){
            console.log(error);
            swal({
                title: "Error!",
                text: "Could not employee schedules for this office! \n" + error.responseJSON.message,
                icon: "error"
            });

        });
    }
})
