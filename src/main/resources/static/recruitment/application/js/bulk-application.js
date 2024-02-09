$(document).ready(function () {
    vm = this;
    vm.type = "arizona";

	const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){

        if(statusTxt == "success"){
            $('#load-layout').append(main);
            $("div").removeClass("spinner-border");

            getOfficeOptions()
                .then(function(data){
                    setOfficeOptions(data);
                    $("div").removeClass("spinner-border");
                })
                .fail(function(err){
                    console.log(err);
                    swal({
                        title: "Error!",
                        text: "Could not get Offices for drop down\n" + err.responseJSON.message,
                        icon: "error"
                    });
                });

            const bulkForm = document.querySelector('#bulk-form');
            bulkForm.addEventListener('submit',function(event){
                //disable default form submission
                const submitButton = document.getElementById('bulkUploadButton');
                submitButton.disabled = true;

                event.preventDefault()
                const cardBody = document.querySelector('.card-body');

                let validated = validationCheck(bulkForm);

                if(validated){
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
                        swal({
                            title: "Success!",
                            text: "You upload a file",
                            icon: "success",
                            timer: 2000
                        });

                        appendResultsToCard(cardBody, response);
                        submitButton.disabled = false;

                        $("#initialLoad").removeClass("spinner-border");
                    }).fail(function(err){
                        console.log(err);
                        swal({
                            title: "Error!",
                            text: "Failure to upload applications! ",
                            icon: "error"
                        });

                        appendResultsToCard(cardBody, err.responseJSON);
                        submitButton.disabled = false;
                        $("#initialLoad").removeClass("spinner-border");
                    });
                }else{
                    submitButton.disabled = false;
                }
            });
        }
    });

	function validationCheck(form){

        if(form.checkValidity() === false){
            event.preventDefault();
            event.stopPropagation();
            form.classList.add('was-validated');
            return false;
        }

        const fileInput = document.getElementById('fileInput');
        const filePath = fileInput.value;
        const allowedExtensions = /(\.csv)$/i;

        if(!allowedExtensions.exec(filePath)){
            event.preventDefault();
            event.stopPropagation();
            form.classList.add('was-validated');
            fileInput.classList.add('is-invalid');
             swal({
                title: "Error!",
                text: "Failure to upload file! \n" + "File must be type of .csv",
                icon: "error"
            });
            return false
        }

        fileInput.classList.remove('is-invalid');
        form.classList.add('was-validated');
        return true;
	}

    function setOfficeOptions(offices){
            for(office of offices){
                $('#office').append("<option value='"+JSON.stringify(office)+"'>"+ office.name +"</option>")
            }
        }

    function getOfficeOptions(){
        return $.ajax({
            type:"GET",
            url:"/offices"
        });
    }

    function appendResultsToCard(cardBody, data) {
        const resultsDiv = document.createElement('div');
        resultsDiv.classList.add('mt-3'); // Add margin-top for spacing

        Object.entries(data).forEach(([key, value]) => {
            const p = document.createElement('p');
            p.classList.add('text-primary'); // Bootstrap class for styling
            p.textContent = `${value}`;
            resultsDiv.appendChild(p);
        });
        cardBody.appendChild(resultsDiv);
    }
// TODO: add type of file to upload, give examples of acceptable column names and ability to report errors
/*    $('#load-layout').on("click", ".dropdown-item", function(event){
        event.preventDefault();
        const chosenType = $(this).data("value");
        vm.type = chosenType;
//        replace text of title to chosenType
        $('#choose-type-title').text(chosenType);
        //hide drop down
        $(this).closest('.dropdown').hide();
        //show form
        $('#bulk-form').removeClass('hidden');
    })*/
});