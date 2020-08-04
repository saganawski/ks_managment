$(document).ready(function(){
    $('#user-table').DataTable({
            ajax:{
                "url": "/users",
                "dataSrc": ""
            },
            columns :[
                {"data": "id" },
                {"data" : "username",
                    "defaultContent": ""},
                {"data" : "isActive"},
                {"data" : function(data,type,row,meta){
                    if(data.createdDate == null){
                        return " ";
                    }
                    return moment(data.createdDate).format('YYYY-MM-DD h:mm:ss a');
                    },
                    "defaultContent": ""},
                {   "targets": -1,
                    "data": function(data, type,row,meta){
                        return '<a class="btn btn-warning" href="/user/user-details.html?userId= '+data.id + '">Details</a>'

                    }
                }
            ]
    });
});