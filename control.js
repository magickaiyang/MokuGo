var chessBoard = [];//chess board
var me = true; //user
var over = false; // game over or not
var username;
var rankList = [
		"1.Mike",
		"2.John",
		"3.Bob",
		"4.Bruce"
];

//send and get json
var webSocket = new WebSocket('wss://mokugo.herokuapp.com/ws');

//send json package
function sendText(x, y, status, color) {
  // Construct a msg object containing the data the server needs to process the message from the chat client.
  var msg = {
    "pos": {"x": x, "y": y},
		"status" : status,
		"color": color
  };

  // Send the msg object as a JSON-formatted string.
  webSocket.send(JSON.stringify(msg));
}

//listen from server
webSocket.onmessage = function(event) {
  	console.log(event.data);
		var msg = JSON.parse(event.data);
		var status = msg.status;
		var color = msg.color;
		var x = msg.pos.x;
		var y = msg.pos.y;
		oneStep(x , y , me);
		if(!over){
			me = !me;
		}
};






// Initialize chessBoard
for(var i=0 ; i<15 ; i++){
	chessBoard[i] = [];
	for(var j=0 ; j<15 ; j++){
		chessBoard[i][j] = 0;
    }
}
 
var chess = document.getElementById('chess');
var context = chess.getContext('2d');
 
context.strokeStyle = "#BFBFBF";
 
document.getElementById("restart").onclick = function(){
	window.location.reload();
}

document.getElementById("rank").onclick = function(){
	window.location.href = "rank.html";
}


var begin = function(){
    username = document.getElementById("username").value;
    console.log(username);
}
 
//draw the board
var drawChessBoard = function(){
	for(var i=0 ; i<15 ; i++){
		context.moveTo(15 + i*30 , 15);
		context.lineTo(15 + i*30 , 435);
		context.stroke();
		context.moveTo(15 , 15 + i*30);
		context.lineTo(435 , 15 + i*30);
		context.stroke();
	}
}
 
var oneStep = function(i , j , me){
	context.beginPath();
	context.arc(15 + i*30 , 15 + j*30 , 13 , 0 , 2 * Math.PI);
	context.closePath();
	var gradient = context.createRadialGradient(15 + i*30 + 2 , 15 + j*30 - 2 , 13 , 15 + i*30 + 2 , 15 + j*30 - 2 , 0);
	if(me){
		gradient.addColorStop(0,"#0A0A0A");
		gradient.addColorStop(1,"#636766");
	}else{
		gradient.addColorStop(0,"#D1D1D1");
		gradient.addColorStop(1,"#F9F9F9");
	}
	context.fillStyle = gradient;
	context.fill();
}
 
chess.onclick = function(e){
	if(over){
		return;
	}
	var x = e.offsetX;
	var y = e.offsetY;
	var i = Math.floor(x / 30);
	var j = Math.floor(y / 30);
	if(chessBoard[i][j] == 0){
		oneStep(i , j , me);
		chessBoard[i][j] = 1;//me
		//send json to server
		sendText(i, j, 0, 1);

		if(!over){
			me = !me;
		}
	}
}

function insert()
{
		var data=rankList;
		for (var i=0;i<data.length;i++){
    	 document.getElementById("list").innerHTML+="<li>"+data[i]+"<\/li>"; 
	  }
}