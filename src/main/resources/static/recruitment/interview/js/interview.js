$(document).ready(function(){
    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);

            $('#officeModal').modal('show');
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
        }
    });

    function setOfficeOptions(offices){
            for(office of offices){
                $('#officeSelect').append("<option value='"+JSON.stringify(office)+"'>"+ office.name +"</option>");
                $('#officeSelect').selectpicker('refresh');
            }
        }

    function getOfficeOptions(){
        return $.ajax({
            type:"GET",
            url:"/offices"
        });
    }

    $('#officeFormSubmit').on('click', function(event){
        event.preventDefault();
        let validated = validationCheck();
        if(validated){
            $('#officeModal').modal('hide');
            let office = JSON.parse($('#officeSelect').val());
            $("#initialLoad").addClass("spinner-border");
            setDataTable(office.id);

        }
    });

    $('#load-layout').on('click', '#officeButton', function(event){
        event.preventDefault();
        $('#interview-table').DataTable().clear().destroy()
        $('#officeModal').modal('show');
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

    const setDataTable = (officeId) => {
        $('#interview-table').DataTable({
            columnDefs: [
                { visible: false, targets: 2 }
            ],
            "initComplete": function(settings, json){
                $("#initialLoad").removeClass("spinner-border");
            },
            ajax:{
                "url": "/interviews/office/" + officeId,
                "dataSrc": ""
            },
            columns :[
                {"data": "id" },
                {"data" : function(data,type,row,meta){
                    const firstName = data.firstName;
                    const lastName = data.lastName;
                    const applicantName = firstName + " " + lastName;
                    return applicantName;
                    },
                    "defaultContent": ""},
                {"data" : "email","defaultContent":""},
                {"data" : "phoneNumber","defaultContent":""},
                {"data" : function(data,type,row,meta){
                    if(data.scheduledTime == null){
                        return " ";
                    }
                    return moment(data.scheduledTime).format('YYYY-MM-DD h:mm:ss a');
                    },
                    "defaultContent": ""},
                {"data" : function(data,type,row,meta){
                        if(data.interviewers == null){
                            return " ";
                        }
                        return data.interviewers;
                    },
                    "defaultContent":""},
                {"data" : "officeName","defaultContent":"" },
                {   "targets": -1,
                    "data": function(data, type,row,meta){
                        return '<a class="btn btn-warning" href="/recruitment/interview/interview-details.html?interviewId= '+data.id + '">Details</a>'

                    }
                }
            ],
            dom:"Bfrtip",
            buttons: ['copy','csv','pdf']
        });
    }

});