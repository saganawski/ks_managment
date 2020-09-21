$(document).ready(function () {
	const main = $('#load-layout').html();
    $('#load-layout').load("/common/_layout.html", function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success"){
            $('#load-layout').append(main);
            getOffices();
        }
    });

    function getOffices(){
        $.ajax({
            type:"GET",
            url: "/offices"
        }).then(function(data){
            setOfficeOptions(data);
        }).fail(function(error){
            console.log(error);
            swal("ERROR", "Could not get offices!","error");
        });
    }

    function setOfficeOptions(offices){
        for(office of offices){
            $('#officeSelect').append("<option value='"+ JSON.stringify(office) + "'>"+ office.name +"</option>");
        }
    }

	$('#load-layout').on('click', '#newProject', function(event){
		event.preventDefault();
		let validated = validationCheck();
		if(validated){
            var formJson = convertFormToJson($("form").serializeArray());
            let office = JSON.parse($('#officeSelect').val());
            formJson.office = office;
            //Send To controller
            createNewProject(formJson);
		}

	});

	function validationCheck(){
	    const form = document.querySelector('#project-form');
        if(form.checkValidity()  === false){
            event.stopPropagation();
            form.classList.add('was-validated');
            return false;
        }

        form.classList.add('was-validated');
        return true;
	}
	function createNewProject(formJson){
        $.ajax({
            type:"POST",
            url: "/projects",
            data: JSON.stringify(formJson),
            contentType: "application/json; charset=utf-8"
        }).then(function(data){
            swal("Success!","You created a new project","success");
            window.location.href = "/reports/shift-goal/project-details.html?projectId=" + data.id;
        }).fail(function(error){
            console.log(error);
            swal("ERROR", "Could not create project! \n" + error.responseJSON.error,"error");
        });
	}
	function convertFormToJson(form){
    	var json = {}
    	for(let j of form){
    		json[j.name] = j.value || null;
    	}
    	return json;
    }
	
});