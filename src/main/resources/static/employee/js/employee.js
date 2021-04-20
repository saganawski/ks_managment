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
            "initComplete": function(settings, json){
                $("div").removeClass("spinner-border");
            },
            "columnDefs":[
                {"targets" : [10],
                "visible" : false,
                "searchable" : false}
            ],
            ajax:{
                "url": "/employees",
                "dataSrc": ""
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
                    let finalNote = "";
                    data.employeeNotes.forEach(note => {finalNote = finalNote + note.note + " | "});
                    return finalNote;
                }, "defaultContent": ""},
                {   "targets": -1,
                    "data": function(data, type,row,meta){
                        return '<a class="btn btn-warning" href="/employee/employee-details.html?employeeId='+ data.id +'">Details</a>'
                    }
                }
            ],
            dom:"Bfrtip",
            buttons: ['copy','csv','pdf']
        });
    }
});