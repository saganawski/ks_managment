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
			$('#officeSelect').append("<option value='"+ office.id + "'>"+ office.name +"</option>");
		}
		$('#officeSelect').selectpicker('refresh');
	}
	
	getPositionsForDropDown();
	function getPositionsForDropDown(){
    $.ajax({
			type:"GET",
			url: "/positions"
		}).then(function(data){
			setPositionOptions(data);
		}).fail(function(err){
		    console.log(err);
			swal("ERROR", "Could not get positions!","error");
		});

	}

	function setPositionOptions(positions){
		for(position of positions){
			$('#position').append("<option name='"+position.code+"'>"+ position.name +"</option>");
		}
	}

	$('#load-layout').on('click', '#newEmployee', function(event){
		event.preventDefault();
		let validated = validationCheck();
		if(validated){
            var formJson = convertFormToJson($("form").serializeArray());
            var selectedOffices = $('#officeSelect').val();
            formJson.officeSelections = selectedOffices;
            //Send To controller
            setUpNewEmployee(formJson)
                .then(results => createNewEmployee(results))
                .catch((err) => console.error(err));
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

	async function setUpNewEmployee(formJson){
		let position = await getPositionByName(formJson.position);
		formJson.position = position;
		return formJson;
	}

	function getPositionByName(name){
		return $.ajax({
			type:"GET",
			url: "/positions/" + name,
		})
	}
	function createNewEmployee(formJson){
        $.ajax({
            type:"POST",
            url: "/employees",
            data: JSON.stringify(formJson),
            contentType: "application/json; charset=utf-8"
        }).then(function(data){
            swal("Success!","You created a new employee","success");
            window.location.href = "/employee/employee.html";
        }).fail(function(error){
            console.log(error);
            swal("ERROR", "Could not create employee! \n" + error.responseJSON.message,"error");
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