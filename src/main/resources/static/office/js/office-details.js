$(document).ready(function () {
    vm = this;
    vm.office = {};
    const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
            if(statusTxt == "success"){
                $('#load-layout').append(main);
                $("div").removeClass("spinner-border");

                let searchParams = new URLSearchParams(window.location.search);
                if(searchParams.has('officeId')){
                    const officeId = searchParams.get('officeId');
                    $.ajax({
                        url:"/offices/" + officeId
                    }).then(function(data){
                        vm.office = data;
                        setFormData(data);
                    }).fail(function(err){
                        console.log(err.responseJSON);
                        swal("Error:", "Failure to retrieve office!","error");
                    });
                }else{
                   swal("Error:", "no ID provided!","error");
                }

                const officeForm = document.querySelector('#office-form');
                officeForm.addEventListener('submit',function(event){
                    let validated = validationCheck(officeForm);
                    if(validated){
                        let jsonForm = convertFormToJson($("#office-form").serializeArray());

                        jsonForm.id = vm.office.id;

                        let location = {id: vm.office.location.id,
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
                            type: "PUT",
                            url:"/offices/" + jsonForm.id,
                            data: JSON.stringify(jsonForm),
                            dataType: "json",
                            contentType: "application/json; charset=utf-8"
                        }).then(function(response){
                            swal({
                                title: "Success!",
                                text: "You updated an office",
                                icon: "success",
                                timer: 2000
                            }).then(function(){
                                location.reload();
                            });
                        }).fail(function(err){
                            console.log(err);
                            swal({
                                title: "Error!",
                                text: "Failure to update an office! \n" + err,
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

    function setFormData(office){
        $('#name').val(office.name);
        $('#completed').val(office.completed.toString());
        $('#stateName').val(office.location.stateName);
        $('#cityName').val(office.location.cityName);
        $('#address1').val(office.location.address1);
        $('#address2').val(office.location.address2);
        $('#zip').val(office.location.zip);
    }


});