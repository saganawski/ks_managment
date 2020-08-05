$(document).ready(function () {
	// TODO: something to load side bar and nav
	$("div").removeClass("spinner-border");
	$('#roles').multiselect();

	const passwordForm = document.querySelector('#user-form');
	passwordForm.addEventListener('submit',function(event){
	    let validated = validationCheck();
	    if(validated){
            const roles = $('#roles').val().toString();
            let jsonForm = convertFormToJson($("#user-form").serializeArray());
            jsonForm.roles = roles;
            $.ajax({
                type: "POST",
                url:"/users",
                data: JSON.stringify(jsonForm),
                contentType: "application/json; charset=utf-8"
            }).then(function(response){
                swal({
                    title: "Success!",
                    text: "You created a user",
                    icon: "success",
                    timer: 2000
                }).then(function(){
                    window.location.href = "/user/user.html";
                });
            }).fail(function(err){
                console.log(err);
                swal({
                    title: "Error!",
                    text: "Failure to create user! \n" + err.responseJSON.message,
                    icon: "error"
                });
            });
	    }
	});

	function validationCheck(){
	    event.preventDefault();
	    const roles = $('#roles').val().toString();
	    if(roles == null || roles === ""){
            swal({
                title: "Error!",
                text: "Must Select at least one role!",
                icon: "error"
            })
            passwordForm.classList.add('was-validated');
	        return false;
	    }
        if(passwordForm.checkValidity() === false){
            event.stopPropagation();
            passwordForm.classList.add('was-validated');
            return false;
        }
        const password = document.getElementById('password');
        const confirmPassword = document.getElementById('confirmPassword');
        if(password.value.trim() != confirmPassword.value.trim()){
            event.stopPropagation();
            setErrorFor(password, "Passwords Must match");
            setErrorFor(confirmPassword, "Passwords Must match");
            passwordForm.classList.add('was-validated');
            return false;
        }
        passwordForm.classList.add('was-validated');
        return true;
	}

    function setErrorFor(input,message){
        const formGroup = input.parentElement;
        const invalidFeedback = formGroup.querySelector('.invalid-feedback');

        invalidFeedback.innerText = message;
        input.className = 'form-control is-invalid';
    }
	function convertFormToJson(form){
        let json = {};
        for(let j of form){
            json[j.name] = j.value.trim() || null;
        }
        return json;
    }

});