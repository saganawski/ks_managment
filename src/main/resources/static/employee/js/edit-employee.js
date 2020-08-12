$(document).ready(function() {
    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
        }
    });

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
        for(office of offices){
            $('#officeSelect').append("<option value='"+ office.id + "'>"+ office.name +"</option>");
        }
        $('#officeSelect').selectpicker('refresh');
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
           if(position != null){
                $("#position").val(position.code);
           }
   //            Refresh multiselect with office ids
           var officeIds = [];
           for(office of offices){
               officeIds.push(office.id);
           }
//           $('#officeSelect').multiselect('select', officeIds);
           $('#officeSelect').selectpicker('val', officeIds);
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

    $('#load-layout').on('click','#editEmployee', function(event){
        event.preventDefault();
        let validated = validationCheck();
        if(validated){
            let formJson = convertFormToJson($("form").serializeArray());
            let selectedOffices = $('#officeSelect').val();
            formJson.officeSelection = selectedOffices;

            sendEmployeeToController(formJson);
        }
    });

    function validationCheck(){
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
        const form = document.querySelector('#employee-form');
        if(form.checkValidity()  === false){
            event.stopPropagation();
            form.classList.add('was-validated');
            return false;
        }

        form.classList.add('was-validated');
        return true;
    }

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
            swal({
                title: "Success!",
                text: "You updated an employee",
                icon: "success",
                timer: 2000
            }).then(function(){
                window.location.href = "/employee/employee.html";
            });
        }).fail(function(error){
            console.log(error);
            swal("ERROR", "Could not update employee!","error");

        });
    }

    $('#load-layout').on('click', '#deleteEmployee', function(event){
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
            if(willDelete){
                deleteEmployee(employeeId);
            }
         });
    });

    function deleteEmployee(employeeId){
        $.ajax({
            type: "DELETE",
            url: "/employees/" + employeeId
        }).then(function(response){
            swal({
                title: "Success!",
                text: "You deleted this employee",
                icon: "success",
                timer: 2000
            }).then(function(){
                window.location.href = "/employee/employee.html";
            });
        }).fail(function(error){
            console.log(error.responseJSON);
            swal({
                title: "Error!",
                text: "Could not delete employee! \n" + error.responseJSON.message,
                icon: "error"
            }).then(function(){
                location.reload();
            });
        });
    }

});
