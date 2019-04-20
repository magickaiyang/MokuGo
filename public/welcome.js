var username;

var begin = function(){
    username = document.getElementById("username").value;
    window.location.href = '/assets/index.html?' + username;
}