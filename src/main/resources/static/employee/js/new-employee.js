$(document).ready(function () {
	// TODO: something to load side bar and nav
//    var vm = this;
	
	
	$.ajax({
		type:"GET",
		url: "/offices"
	}).then(function(data){
		setOfficeOptions(data);
	}).fail(function(error){
		alert(error);
	});
	
	function setOfficeOptions(offices){
		var officeOptionsData = [];
		for(office of offices){
			option ={label:office.name, value:office.id };
			officeOptionsData.push(option);
		}
		$('#officeSelect').multiselect('dataprovider', officeOptionsData);
	}
	
//	TODO: add position to dropdown
//	startingPosition
	
});