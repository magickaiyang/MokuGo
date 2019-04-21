//fake list
var rankList = [
    "1.Mike",
    "2.John",
    "3.Bob",
    "4.Bruce"
];

function insert()
{
    var username =  window.location.search.substr(1);
    console.log(username);
    getJSON('https://mokugo.herokuapp.com/ldrbrd/' + username).then(function(data) {
        console.log(data);
        alert('Your Json result is:  ' + data); //you can comment this, i used it to debug
        
    }, function(status) { //error detection....
        alert('Something went wrong.');
    });


	var data=rankList;
	for (var i=0;i<data.length;i++){
    	document.getElementById("list").innerHTML+="<li>"+data[i]+"<\/li>"; 
	}
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

