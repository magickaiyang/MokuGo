//fake list
var rankList = [
    {score: 3, username: "Mike" },
    {score: 2, username: "John" },
    {score: 1, username: "Bob" },
    {score: 0, username: "Bruce"}
];

function insert()
{
    var username =  window.location.search.substr(1);
    console.log(username);
    getJSON('https://mokugo.herokuapp.com/ldrbrd/' + username).then(function(data) {
        console.log(data);
        //alert('Your Json result is:  ' + data); //you can comment this, i used it to debug
        var msg = data;
        var userscore = msg.userscore;
        var userrank = msg.userrank;
        var username = msg.username;
        var rank = msg.rank;

        var data=rank;
        for (var i=0;i<data.length;i++){
            if (data[i] != null) {
                var j = i+1
                document.getElementById("list").innerHTML+="<li>"+j+". "+data[i].username+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+data[i].score +"<\/li>"; 
            }
        }
        document.getElementById("you").innerHTML +="Your highest score: "+userscore+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Your rank: "+userrank;
        
    }, function(status) { //error detection....
        alert('Something went wrong.');
    });

}

var getJSON = function(url) {
    console.log(url);
    return new Promise(function(resolve, reject) {
        var xhr = new XMLHttpRequest();
        xhr.open('get', url, true);
        xhr.responseType = 'json';
        xhr.onload = function() {
            var status = xhr.status;
            if (status == 200) {
                resolve(xhr.response);
            } else {
                reject(status);
            }
        };
        xhr.send();
    });
};

