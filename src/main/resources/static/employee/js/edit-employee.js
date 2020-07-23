$(document).ready(function() {
//TODO: make these function a javascript iport?
    getPositionsForDropDown();
        function getPositionsForDropDown(){
            $.ajax({
                type:"GET",
                url: "/positions"
            }).then(function(data){
                setPositionOptions(data);
            }).fail(function(err){
                swal("ERROR", "Could note retrieve positions!","error");
            });

        }

        function setPositionOptions(positions){
            for(position of positions){
                $('#position').append("<option value='"+position.code+"'>"+ position.name +"</option>");
            }
        }

    const officesPromise = new Promise((resolve,reject) => {
        return $.ajax({
                       type:"GET",
                       url: "/offices"
                   }).then(function(data){
                       return resolve(data);
                   }).fail(function(error){
                       return reject(error);
                   });
    });
   async function setOfficeOptions(offices){
        var officeOptionsData = [];
        for(office of offices){
            option ={label:office.name, value:office.id, name:office.name };
            officeOptionsData.push(option);
        }
        $('#officeSelect').multiselect('dataprovider', officeOptionsData);
        return true;
   }
   const employeeDataPromise = new Promise(function(resolve,reject) {
           let searchParams = new URLSearchParams(window.location.search);
           if(searchParams.has('employeeId')){
                let employeeId = searchParams.get('employeeId');
               return $.ajax({
                              url:"/employees/"+ employeeId
                          }).then(function(data){
                              return resolve(data);
                          }).fail(function(err){
                              console.log(err);
                              swal("ERROR", "Could NOT retrieve employees!","error");
                              return reject("failed to retrieve Employee");
                          });
           }else{
               return reject("no ID provided ");
           }
       });

       async function setFieldValuesForEmployee(data){
           employeeId = data.id;
           firstName = data.firstName;
           lastName = data.lastName;
           alias = data.alias;
           email = data.email;
           phoneNumber = data.phoneNumber;
           position = data.position;
           offices = data.offices;

           $("#id").val(employeeId);
           $("#firstName").val(firstName);
           $("#lastName").val(lastName);
           $("#alias").val(alias);
           $("#email").val(email);
           $("#phoneNumber").val(phoneNumber);
           $("#position").val(position.code);
   //            Refresh multiselect with office ids
           var officeIds = [];
           for(office of offices){
               officeIds.push(office.id);
           }
           $('#officeSelect').multiselect('select', officeIds);
           return(true);
       }
    officesPromise
        .then(results => {
            setOfficeOptions(results);
            return employeeDataPromise
                .then(results =>{
                    setFieldValuesForEmployee(results);
                })
        })

    $('#editEmpolyee').on('click', function(event){
        event.preventDefault();
        let formJson = convertFormToJson($("form").serializeArray());
        let selectedOffices = $('#officeSelect').val();
        formJson.officeSelection = selectedOffices;

        sendEmployeeToController(formJson);
    });
//    TODO: place in module
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
            console.log(data);
            swal("Success!","You updated employee","success");
            window.location.href = "/employee/employee.html";
        }).fail(function(error){
            console.log(error);
            swal("ERROR", "Could not update employee!","error");

        });
    }

    $('#deleteEmpolyee').on('click', function(event){
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
            deleteEmployee(employeeId);
         });
    });

    function deleteEmployee(employeeId){
        $.ajax({
            type: "DELETE",
            url: "/employees/" + employeeId
        }).then(function(response){
            swal("Success!","You deleted this employee.","success");
            window.location.href = "/employee/employee.html";
        }).fail(function(error){
            console.log(error);
            swal("ERROR", "Could not delete employee!","error");
        });
    }

});
