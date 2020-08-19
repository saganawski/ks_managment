$(document).ready(function () {
	const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
            $("div").removeClass("spinner-border");

            const bulkForm = document.querySelector('#bulk-form');
            bulkForm.addEventListener('submit',function(event){
                let validated = validationCheck(bulkForm);

//                if(validated){
//                    const roles = $('#roles').val().toString();
//                    let jsonForm = convertFormToJson($("#user-form").serializeArray());
//                    jsonForm.roles = roles;
//                    $.ajax({
//                        type: "POST",
//                        url:"/users",
//                        data: JSON.stringify(jsonForm),
//                        contentType: "application/json; charset=utf-8"
//                    }).then(function(response){
//                        swal({
//                            title: "Success!",
//                            text: "You created a user",
//                            icon: "success",
//                            timer: 2000
//                        }).then(function(){
//                            window.location.href = "/user/user.html";
//                        });
//                    }).fail(function(err){
//                        console.log(err);
//                        swal({
//                            title: "Error!",
//                            text: "Failure to create user! \n" + err.responseJSON.message,
//                            icon: "error"
//                        });
//                    });
//                }
            });
        }
    });

	function validationCheck(form){
	    event.preventDefault();
        if(form.checkValidity() === false){
            event.stopPropagation();
            form.classList.add('was-validated');
            return false;
        }

        const fileInput = document.getElementById('fileInput');
        const filePath = fileInput.value;
        const allowedExtensions = /(\.csv)$/i;

        if(!allowedExtensions.exec(filePath)){
            event.stopPropagation();
            form.classList.add('was-validated');
             swal({
                title: "Error!",
                text: "Failure to upload file! \n" + "File must be type of .csv",
                icon: "error"
            });
            return false
        }

        form.classList.add('was-validated');
        return true;
	}

	function convertFormToJson(form){
        let json = {};
        for(let j of form){
            json[j.name] = j.value.trim() || null;
        }
        return json;
    }

});