let replyService = (function (){
    let list = function(page, url, num, callback){
        $.getJSON(url + num, {page : page}, function (data){
            callback(data)
        })
    }
    let register = function(reply, url, callback){
        console.log("==========register")
        $.post({
            url : url,
            data : JSON.stringify(reply), // json 문자열
            contentType : "application/json; charset=utf-8"
        }, function (data){
            callback(data) // 콘솔창에 data 출력
        }).fail(function() {
            alert( "error" );
        });
    }
    let modify = function(reply, url, callback){
        $.ajax({
            url : url + reply.rno,
            data : JSON.stringify(reply),
            type : "put",
            contentType: "application/json; charset=utf-8",
            success : function (result){
                callback(result)
            },
            error : function (result, status, error){
                if (jqXHR.status == '401'){
                    alert("로그인 후 이용해 주세요!");
                    location.href = "/members/login";
                } else {
                    console.log(result.status + " " + result.error)
                    alert(result.responseText);
                }
            }
        })
    }
    let read = function(rno, url, callback){
        $.get(url + rno, function (data){
            callback(data)
        })
    }
    let remove = function(rno, url, callback){
        $.ajax({
            url : url + rno,
            type : 'delete',
            dataType : 'json',
            success : function (result){
                callback(result)
            },
            error : function (result, status, error){
                if (jqXHR.status == '401'){
                    alert("로그인 후 이용해 주세요!");
                    location.href = "/members/login";
                } else {
                    console.log(result.status + " " + result.error)
                    alert(result.responseText);
                }
            }
        })
    }
    return{
        list : list,
        register : register,
        modify : modify,
        read : read,
        remove : remove,
    }

})();