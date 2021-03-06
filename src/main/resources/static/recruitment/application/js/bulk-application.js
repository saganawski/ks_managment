$(document).ready(function () {
    vm = this;
    vm.type = "";

	const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
            $("div").removeClass("spinner-border");

            const bulkForm = document.querySelector('#bulk-form');
            bulkForm.addEventListener('submit',function(event){
                let validated = validationCheck(bulkForm);

                if(validated && vm.type.length !=0 ){
                    $("#initialLoad").addClass("spinner-border")
                    $.ajax({
                        type: "POST",
                        url:"/applications/bulk-upload/bulk-type/" + vm.type,
                        data: new FormData(this),
                        enctype: 'multipart/form-data',
                        processData: false,
                        contentType: false,
                        cache: false
                    }).then(function(response){
                        $("#initialLoad").removeClass("spinner-border");
                        swal({
                            title: "Success!",
                            text: "You upload a file",
                            icon: "success",
                            timer: 2000
                        }).then(function(){
                            location.reload();
                        });
                    }).fail(function(err){
                        console.log(err);
                        swal({
                            title: "Error!",
                            text: "Failure to upload applications! \n" + err.responseJSON.message,
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

    $('#load-layout').on("click", ".dropdown-item", function(event){
        event.preventDefault();
        const chosenType = $(this).data("value");
        vm.type = chosenType;
//        replace text of title to chosenType
        $('#choose-type-title').text(chosenType);
        //hide drop down
        $(this).closest('.dropdown').hide();
        //show form
        $('#bulk-form').removeClass('hidden');
    })
});