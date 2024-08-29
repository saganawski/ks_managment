$(document).ready(function () {
    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
            setDataTable();
        }
    });
    function setDataTable(){
        $('#employee-table').DataTable({
            "processing": true,
            "serverSide": true,
            "initComplete": function(settings, json){
                $("div").removeClass("spinner-border");
            },
            ajax:{
                "url": "/employees",
                "type": "GET",
                "dataSrc": "data",
                "cache": false,
                "data": function(data){

                    let result = {
                        draw: data.draw,
                        page: data.start/ data.length,
                        size: data.length,
                        sortBy: data.columns[data.order[0].column].data,
                        direction: data.order[0].dir
                    };
                    if(data.search.value != null){
                        result.search = data.search.value;
                    }
                    return result;

                }
            },
            columns :[
                {"data" : "id"},
                {"data" : "firstName"},
                {"data" : "lastName"},
                {"data" : "email"},
                {"data" : "position.name",
                    "defaultContent":""},
                {"data" : function(data,type,row,meta){
                    if(data.offices == undefined || data.offices.length === 0 ){
                        return "";
                    }
                    let officeName = "";
                    for(office of data.offices){
                        if(officeName.length ==0){
                            officeName = office.name;
                        }else{
                            officeName = officeName + ", " + office.name
                        }
                    }
                    return officeName;
                }, "defaultContent": ""},
                {"data": "phoneNumber", "defaultContent": "" },
                {"data": "startDate", "defaultContent": "" },
                {"data": "endDate", "defaultContent": "" },
                {"data": function(data,type,row,meta){
                   if(data.voluntary == null){
                    return "";
                   }

                   let separationType = "";

                   if(data.voluntary == false){
                    separationType = "Involuntary";
                   }else if(data.voluntary){
                    separationType = "Voluntary";
                   }
                   return separationType;

                }, "defaultContent": "" },
                {"data": function(data,type,row,meta){
                    let activationStatus = "";
                    if(data.deleted == false){
                        activationStatus = "Active";
                    }else if (data.deleted == true){
                        activationStatus = "Deactivated";
                    }
                    return activationStatus;
                },"defaultContent": ""},
                {   "targets": -1,
                    "data": function(data, type,row,meta){
                        return '<a class="btn btn-warning" href="/employee/employee-details.html?employeeId='+ data.id +'">Details</a>'
                    }
                }
            ],
            columnDefs: [{
                targets: [5,9,10,11],
                orderable: false
            }],
            dom:"Bfrtip",
            buttons: ['copy','csv','pdf']
        });

        $('#employee-table').on('preXhr.dt', function(e, settings, data) {
                $('#initialLoad').addClass("spinner-border");
        });

        // Hide spinner on AJAX request completion, regardless of success or error
        $('#employee-table').on('xhr.dt', function(e, settings, json, xhr) {
            $('#initialLoad').removeClass("spinner-border");
        });

    }
});