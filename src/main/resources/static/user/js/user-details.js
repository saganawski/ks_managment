$(document).ready(function(){
    vm = this;
    vm.user = {};
    vm.employees = {};
    vm.linkedEmployee = {};

    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
            $('#roles').selectpicker('refresh');
        }
    });

    let searchParams = new URLSearchParams(window.location.search);
    if(searchParams.has('userId')){
        const userId = searchParams.get('userId');
        $.ajax({
            url:"/users/" + userId
        }).then(function(data){
            vm.user = data;
            setFormData(data);
            findLinkedEmployee(data);
        }).fail(function(err){
            console.log(err.responseJSON);
            swal("Error:", "Failure to retrieve user!","error");
        });
    }else{
       swal("Error:", "no ID provided!","error");
    }

    function setFormData(user){
       $('#id').val(user.id);
       if(user.isActive != null){
            $('#isActive').val(user.isActive.toString());
       }
       $('#username').val(user.username);

       const rolesArray = user.roles.split(',');

       $('#roles').selectpicker('val', rolesArray);

       $("#initialLoad").removeClass("spinner-border");
    }

    $('#load-layout').on('click', '#updateUser', function(event){
        //TODO: form validation
        event.preventDefault();
        const roles = $('#roles').val().toString();

        let jsonForm = convertFormToJson($("form").serializeArray());
        jsonForm.roles = roles;

        $.ajax({
            type: "PUT",
            url:"/users/" + jsonForm.id,
            data: JSON.stringify(jsonForm),
            contentType: "application/json; charset=utf-8"
        }).then(function(response){
            swal({
                title: "Success!",
                text: "You updated an user",
                icon: "success",
                timer: 2000
            }).then(function(){
                location.reload();
            });
        }).fail(function(err){
            console.log(err.responseJSON);
            swal({
                title: "Error!",
                text: "Failure to update user! \n" + err.responseJSON.message,
                icon: "error"
            }).then(function(){
                location.reload();
            });
        });

    });

    function convertFormToJson(form){
        var json = {}
        for(let j of form){
            json[j.name] = j.value || null;
        }
        return json;
    }

    const passwordForm = document.querySelector('#passwordForm');
    passwordForm.addEventListener('submit', function(event){
        event.preventDefault();
        if(passwordForm.checkValidity() === false){
            event.stopPropagation();
        }
        const password = $('#password1');
        const confirmPassword = $('#password2');
        if(password.val() != confirmPassword.val()){
            event.stopPropagation();
            password.addClass('is-invalid');
            confirmPassword.addClass('is-invalid');
        }
        passwordForm.classList.add('was-validated');

        if(passwordForm.checkValidity() === true && password.val() == confirmPassword.val()){
            let userDTO = {id: vm.user.id, username: vm.user.username, password: password.val()};
            $.ajax({
                type: "PUT",
                url:"/users/" + userDTO.id + "/password",
                data: JSON.stringify(userDTO),
                contentType: "application/json; charset=utf-8"
            }).then(function(response){
                swal({
                    title: "Success!",
                    text: "You updated an user password",
                    icon: "success",
                    timer: 2000
                }).then(function(){
                    location.reload();
                });
            }).fail(function(err){
                console.log(err.responseJSON);
                swal("Error:", "Failure to update interview!","error");
            });
        }
    });

    $('#load-layout').on('click', '#linkEmployee', function(event){
        event.preventDefault();
        /*TODO: think another way of linking to employee. CONCERN: employees can easily get too big
        Maybe do a keyUpSearch?*/
        getEmployeeOptions();

    });
     function getEmployeeOptions(){
        $.ajax({
            url: "/employees/non-canvassers"
        }).then(function(data){
            vm.employees = data;
            setEmployeeOptionsDropDown(data);
        }).fail(function(err){
            console.log(err.responseJSON);
             swal({
                title: "Error!",
                text: "Failure to get employees for drop down! \n" + err.responseJSON.message,
                icon: "error"
            });
        });
    }

    function setEmployeeOptionsDropDown(employees){
        for(employee of employees){
            let optionName = employee.firstName + " " + employee.lastName;
            $('#employeeSelect').append("<option value='"+JSON.stringify(employee)+"'>"+ optionName +"</option>");
        }
        $("div").removeClass("spinner-border");
        $('#employeeSelect').selectpicker('refresh');
    }

    $('#employeeFormSubmit').on('click',function(event){
        event.preventDefault();
        let selectedEmployee = JSON.parse($("#employeeSelect").val());
        //TODO: validation
        if(selectedEmployee == null || selectedEmployee == "null" ){
            swal("Error:", "Must select employee!","error");
        }
        let userEmployeeLinkDto = {};
        userEmployeeLinkDto.user = vm.user;
        userEmployeeLinkDto.employee = selectedEmployee;
        $.ajax({
            type: "POST",
            url:"/users/employees",
            data: JSON.stringify(userEmployeeLinkDto),
            contentType: "application/json; charset=utf-8"
        }).then(function(response){
            swal({
                title: "Success!",
                text: "You linked an user to an employee",
                icon: "success",
                timer: 2000
            }).then(function(){
                location.reload();
            });
        }).fail(function(err){
            console.log(err.responseJSON);
            swal({
                title: "Error!",
                text: "Failure to update user! \n" + err.responseJSON.message,
                icon: "error"
            }).then(function(){
                location.reload();
            });
        });
    });

    //find and set current linked employee
    function findLinkedEmployee(user){
        $.ajax({
            url: "/users/" + user.id + "/employee"
        }).then(function(response){
            vm.linkedEmployee = response;
            if(response != ''){
                setLinkedEmployee(response);
            }
        }).fail(function(err){
            console.log(err.responseJSON);
             swal({
                title: "Error!",
                text: "Failure to get employee info! \n" + err.responseJSON.message,
                icon: "error"
            });
        });
    }
    function setLinkedEmployee(employee){
        $('#firstName').val(employee.firstName);
        $('#lastName').val(employee.lastName);
        $('#email').val(employee.email);
        $('#position').val(employee.position.name);
    }

    $('#load-layout').on('click', '#details',function(event){
        event.preventDefault();
        if(vm.linkedEmployee.id != undefined){
            window.location.href = "/employee/employee-details.html?employeeId="+ vm.linkedEmployee.id;
        }else{
            swal("Error:", "No Employee linked yet!","error");
        }
    });

})
