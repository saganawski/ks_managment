$(document).ready(function () {
    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
            if(statusTxt == "success"){
                $('#load-layout').append(main);
                $("div").removeClass("spinner-border");

                const officeForm = document.querySelector('#office-form');
                officeForm.addEventListener('submit',function(event){
                    let validated = validationCheck(officeForm);
                    if(validated){
                        let jsonForm = convertFormToJson($("#office-form").serializeArray());

                        let location = {id:null,
                            stateName: jsonForm.stateName,
                            cityName: jsonForm.cityName,
                            address1: jsonForm.address1,
                            address2: jsonForm.address2,
                            zip: jsonForm.zip
                            };

                        jsonForm.location = location;

                        delete jsonForm.stateName;
                        delete jsonForm.cityName;
                        delete jsonForm.address1;
                        delete jsonForm.address2;
                        delete jsonForm.zip;

                        $.ajax({
                            type: "POST",
                            url:"/offices",
                            data: JSON.stringify(jsonForm),
                            contentType: "application/json; charset=utf-8"
                        }).then(function(response){
                            swal({
                                title: "Success!",
                                text: "You created a office",
                                icon: "success",
                                timer: 2000
                            }).then(function(){
                                window.location.href = "/office/offices.html";
                            });
                        }).fail(function(err){
                            console.log(err);
                            swal({
                                title: "Error!",
                                text: "Failure to create a new office! \n" + err.responseJSON.message,
                                icon: "error"
                            });
                        });
                    }
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