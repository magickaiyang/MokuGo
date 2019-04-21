var rankList = [
    "1.Mike",
    "2.John",
    "3.Bob",
    "4.Bruce"
];

function insert()
{
	var data=rankList;
	for (var i=0;i<data.length;i++){
    	document.getElementById("list").innerHTML+="<li>"+data[i]+"<\/li>"; 
	}
}