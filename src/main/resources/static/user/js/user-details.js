$(document).ready(function(){
    vm = this;
    vm.user = {};
    $('#roles').multiselect();
    let searchParams = new URLSearchParams(window.location.search);
    if(searchParams.has('userId')){
        const userId = searchParams.get('userId');
        $.ajax({
            url:"/users/" + userId
        }).then(function(data){
            console.log(data);
            vm.user = data;
            setFormData(data);
        }).fail(function(err){
            console.log(err);
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

       $('#roles').multiselect('select', rolesArray);

       $("div").removeClass("spinner-border");
    }

    $('#updateUser').on('click', function(event){
        event.preventDefault();
        const roles = $('#roles').val().toString();
        let jsonForm = convertFormToJson($("form").serializeArray());
        jsonForm.password = vm.user.password;
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
            console.log(err);
            swal("Error:", "Failure to update interview!","error");
        });

    });

    function convertFormToJson(form){
        var json = {}
        for(let j of form){
            json[j.name] = j.value || null;
        }
        return json;
    }

})
