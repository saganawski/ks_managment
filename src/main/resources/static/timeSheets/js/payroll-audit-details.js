$(document).ready(function(){
    vm = this;
    vm.scheduleAudit = {};

    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
        }
    });

    let searchParams = new URLSearchParams(window.location.search);
    if(searchParams.has('scheduleAuditId')){
        var scheduleAuditId = searchParams.get('scheduleAuditId');
        $.ajax({
            url:"/scheduleAudits/" + scheduleAuditId + "/payroll"
        }).then(function(data){
            setDataTable(data);
        }).fail(function(err){
            console.log(err);
            swal({
                title: "Error!",
                text: "Failure to retrieve schedule audit! \n" + err.responseJSON.message,
                icon: "error"
            });
        });

        $.ajax({
            url:"/scheduleAudits/" + scheduleAuditId
        }).then(function(data){
            setTitle(data);
        }).fail(function(err){
            console.log(err);
            swal({
                title: "Error!",
                text: "Failure to retrieve schedule audit! \n" + err.responseJSON.message,
                icon: "error"
            });
        });
    }else{
       swal("Error:", "no ID provided!","error");
    }

    function setDataTable(scheduleAudit){
        var table = $('#schedule-audit-table').DataTable({
            "initComplete": function(settings, json){
                $("div").removeClass("spinner-border");
            },
            data: scheduleAudit,
            columns :[
                {"data" : "firstName",
                        "defaultContent": ""},
                {"data" : "lastName",
                    "defaultContent": ""},
                {"className":"sum","data" : "regularHours",
                    "defaultContent": "0"},
                {"data" : "hourlyRate",
                    "defaultContent": "0"},
                {"className":"sum", "data" : "hourlyPay",
                    "defaultContent": "0"},
                {"className":"sum", "data" : "overtimeHours",
                    "defaultContent": "0"},
                {"className":"sum", "data": "overtimeRate",
                    "defaultContent": "0"},
                {"className":"sum", "data" : "overtimePay",
                    "defaultContent": "0"},
                {"className":"sum", "data" : "miles",
                    "defaultContent": "0"},
                {"className":"sum","data" : "mileagePay",
                    "defaultContent": "0"},
                {"className":"sum","data" : "totalPay",
                    "defaultContent": "0"}
            ],
            dom:"Bfrtip",
            buttons: ['copy','csv','pdf'],
            "footerCallback": function(row,data,start,end,display){
                const api = this.api();
                api.columns('.sum',{
                    page: 'current'
                }).every(function () {
                    const columnSum = this.data()
                        .reduce(function(a,b){
                            const x = parseFloat(a) || 0;
                            const y = parseFloat(b) || 0;
                            return x + y;
                        }, 0);

                    const roundedSum = Math.round((columnSum + Number.EPSILON) *100) / 100;
                    $(this.footer()).html(roundedSum);
                });

            }
        });

    }
    function setTitle(scheduleAudit){
        $('.card-header').append(" - " + scheduleAudit.office.name + "<br> Start Date: " + scheduleAudit.startDate + "<br> End Date: " + scheduleAudit.endDate);
    }

     $('#load-layout').on('click', '#delete-report',function(event){
         event.preventDefault();
         swal({
           title: "Are you sure?",
           text: "Once deleted, you will not be able to recover this record!",
           icon: "warning",
           buttons: true,
           dangerMode: true,
         })
         .then((willDelete) => {
            if(willDelete){
                deleteScheduleAudit(scheduleAuditId);
            }
         });
     });

     function deleteScheduleAudit(scheduleAuditId){
         $.ajax({
             type: "DELETE",
             url: "/scheduleAudits/" + scheduleAuditId
         }).then(function(response){
             swal({
                 title: "Success!",
                 text: "You deleted this audit",
                 icon: "success",
                 timer: 2000
             }).then(function(){
                window.location.href = "/timeSheets/payroll.html";
             });
         }).fail(function(error){
             console.log(error);
             swal({
                 title: "Error!",
                 text: "Could NOT remove training! \n" + error.responseJSON.message,
                 icon: "error"
             });
         });
     }
})
