$(document).ready(function(){
    console.log("hits in interview edit!");
    let searchParams = new URLSearchParams(window.location.search);
    if(searchParams.has('interviewId')){
        let interviewId = searchParams.get('interviewId');
        $.ajax({
            url:"/interviews/" + interviewId +"/dto"
        }).then(function(data){
            console.log(data);
        }).fail(function(err){
            console.log(err);
            alert("Failure to retrieve application");
        });
    }else{
       alert("no ID provided ");
    }
})