$(document).ready(function(){
    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
            // add all offices select call TODO:
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
            setDatatable(office.id);

        }
    });

    $('#load-layout').on('click', '#officeButton', function(event){
        event.preventDefault();
        $('#application-table').DataTable().clear().destroy()
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

    function setDatatable(officeId){
        $('#application-table').DataTable({
            responsive: true,
                "fnInitComplete": function (oSettings, json) {
                    $("#initialLoad").removeClass("spinner-border");
                },
                ajax:{
                    "url": "/applications/office/" + officeId,
                    dataSrc: ''
                },
                columns :[
                    {"data" : "id" ,
                       "defaultContent":""},
                    {"data" : "firstName",
                        "defaultContent":""},
                    {"data" : "lastName",
                        "defaultContent":""},
                    {"data" : "phoneNumber",
                        "defaultContent":""},
                    {"data" : "email",
                        "defaultContent":""},
                    {"data" : function(data, type,row,meta){
                        if(data.dateReceived != null){
                            return moment.utc(data.dateReceived).format('YYYY-MM-DD');
                        }else{
                            return " ";
                        }
                        },
                        "defaultContent":""
                    },
                    {"data" : function(data, type,row,meta){
                        if(data.callBackDate != null){
                            return moment.utc(data.callBackDate).format('YYYY-MM-DD');
                        }else{
                            return " ";
                        }
                        },
                        "defaultContent":""
                    },
                    {"data" : "source",
                        "defaultContent":""},

                    {"data" : "result",
                        "defaultContent":""},
                    {"data" : "name",
                        "defaultContent":""},
                    {"data" : function(data, type,row,meta){
                        return moment.utc(data.createdDate).format('YYYY-MM-DD h:mm:ss a');
                        },
                     "defaultContent":""
                    },
                    {   "targets": -1,
                        "data": function(data, type,row,meta){
                            return '<a class="btn btn-warning" href="/recruitment/application/application-edit.html?applicationId='+ data.id +'">Details</a>'
                        },
                         "defaultContent":""
                    }
                ],
                dom:"Bfrtip",
                buttons: ['copy','csv','pdf']
        });
    }
});