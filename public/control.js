var chessBoard = [];//chess board
var me = true; //user move
var over = false; // game over or not
var clickForbidden = false;
var chess = document.getElementById('chess');
var context = chess.getContext('2d');
var username;
//Background 
var imgs = new Image();
imgs.src = "/assets/wood2.jpg";
imgs.onload = createPat;

function createPat() {
    var bg = context.createPattern(imgs, "no-repeat");
    context.fillStyle = bg;
    context.fillRect(0, 0, chess.width, chess.height);
    //draw the chess board
    context.strokeStyle = "#000000";
    for (var i = 0; i < 15; i++) {
        context.moveTo(15 + i * 30, 15);
        context.lineTo(15 + i * 30, 435);
        context.stroke();
        context.moveTo(15, 15 + i * 30);
        context.lineTo(435, 15 + i * 30);
        context.stroke();
    }
}

document.getElementById("restart").onclick = function () {
    location.reload();
};

document.getElementById("rank").onclick = function () {
    window.location.href = '/assets/rank.html?' + username;
};

// Initialize chessBoard
for (var i = 0; i < 15; i++) {
    chessBoard[i] = [];
    for (var j = 0; j < 15; j++) {
        chessBoard[i][j] = 0;
    }
}

//opens a WebSocket
var webSocket = new WebSocket(((window.location.protocol === "https:") ? "wss://" : "ws://") + location.host + '/ws');
username = window.location.search.substr(1);
var msg = {
    "name": username
};
console.log(username);

// Send the msg object as a JSON-formatted string.
webSocket.addEventListener('open', function () {
    webSocket.send(JSON.stringify(msg))
});
webSocket.addEventListener('open', function () {
    keepAlive()
});

//send json package
function sendText(x, y, status, color) {
    // Construct a msg object containing the data the server needs to process the message from the chat client.
    var msg = {
        "pos": {"x": x, "y": y},
        "status": status,
        "color": color
    };

    // Send the msg object as a JSON-formatted string.
    webSocket.send(JSON.stringify(msg));
}

function delayedAIWin() {
    alert("The AI wins!\nClick \"Rank\" to see the world rank and your score\nOr click \"Restart\" to try again");
}

//listen from server
webSocket.onmessage = function (event) {
    console.log(event.data);
    // if receiving a fake json package, just ignore it
    if (String(event.data) == "\"keep-alive\"") {
        return;
    }

    var msg = JSON.parse(event.data);
    var status = msg.status;
    var color = msg.color;
    var x = msg.pos.x;
    var y = msg.pos.y;

    //updates statistics
    if('log' in msg) {
        document.getElementById("log").value = msg.log;
    }

    if (status == 1) {
        //user win
        alert("You win!\nClick \"Rank\" to see the world rank and your score\nOr click \"Restart\" to try again");
        over = true;
    } else if (status == 2) {
        //computer win
        if (chessBoard[x][y] == 0) {
            chessBoard[x][y] = 1;
            oneStep(x, y, me);
        }
        //need to wait until drawing is complete
        setTimeout(delayedAIWin, 100);

        over = true;
    } else {
        if (chessBoard[x][y] == 0) {
            chessBoard[x][y] = 1;
            oneStep(x, y, me);
        }
        clickForbidden = false;
    }
    if (!over) {
        me = !me;
    } else {
        clickForbidden = true;
    }
};

//keep connected
function keepAlive() {
    var timeout = 30000;
    var fakeJson = {"a": 1};
    if (webSocket.readyState == webSocket.OPEN) {
        webSocket.send(JSON.stringify(fakeJson));
    }
    setTimeout(keepAlive, timeout);
}

//One step when user click the board
var oneStep = function (i, j, me) {
    context.beginPath();
    context.arc(15 + i * 30, 15 + j * 30, 13, 0, 2 * Math.PI);
    context.closePath();
    var gradient = context.createRadialGradient(15 + i * 30 + 2, 15 + j * 30 - 2, 13, 15 + i * 30 + 2, 15 + j * 30 - 2, 0);
    if (me) {
        gradient.addColorStop(0, "#0A0A0A");
        gradient.addColorStop(1, "#636766");
    } else {
        gradient.addColorStop(0, "#D1D1D1");
        gradient.addColorStop(1, "#F9F9F9");
    }
    context.fillStyle = gradient;
    context.fill();
}

//Click event
chess.onclick = function (e) {
    if (over || clickForbidden) {
        return;
    }
    var x = e.offsetX;
    var y = e.offsetY;
    var i = Math.floor(x / 30);
    var j = Math.floor(y / 30);
    if (chessBoard[i][j] == 0) {
        oneStep(i, j, me);
        chessBoard[i][j] = 1;//me
        //send json to server
        sendText(i, j, 0, 1);

        if (!over) {
            me = !me;
        }
        clickForbidden = true;
        //console.log(clickForbidden);
    }
}



