<?php 

function main(array $args) : array
{ 
   
     $countries = array("Albania",
        "Armenia",
        "Australia",
        "Austria",
        "Azerbaijan",
        "Belgium",
        "Bulgaria",
        "Croatia",
        "Cyprus",
        "CzechRepublic",
        "Denmark",
        "Estonia",
        "Finland",
        "France",
        "Germany",
        "Georgia",
        "Greece",
        "Iceland",
        "Ireland",
        "Israel",
        "Italy",
        "Latvia",
        "Lithuania",
        "Malta",
        "Moldova",
        "Montenegro",
        "Netherlands",
        "NorthMacedonia",
        "Norway",
        "Poland",
        "Portugal",
        "Romania",
        "SanMarino",
        "Serbia",
        "Slovenia",
        "Spain",
        "Sweden",
        "Switzerland",
        "Ukraine",
        "UnitedKingdom");
    
     $countrySize = count($countries);
     $url = $_SERVER["__OW_API_HOST"] . "/api/v1/web" . $_SERVER["__OW_ACTION_NAME"];
     $url = str_replace("resultsui","resultsQuery", $url);
     
     $allResults =  "";
     $curl = curl_init();

     for ($i = 0; $i < $countrySize; $i++) {
        $payload = json_encode( array( "country" => strtolower($countries[$i]) ));
         $curl = curl_init();
         curl_setopt($curl, CURLOPT_POST, true);
         curl_setopt($curl, CURLOPT_URL, $url);
         curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
         curl_setopt($curl, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));
         curl_setopt($curl, CURLOPT_POSTFIELDS, $payload );
     
         $result = curl_exec($curl);
         $json = json_decode($result, true);
                 
         $allResults = $allResults . $countries[$i] . " : " . $json . "</br>";
     }
 
     curl_close($curl); 

 $page="
<html>
	<head>
		<title>Eurovision 2022 Results</title>
	</head>
	<body>
        <img src=\"https://static.eurovision.tv/dist/assets/images/esc/2022/logo-black.b9b5bfc57b81d725d184..svg\">
		<h1>Results</h1>
		<p id=\"result\">
		</p>
" . $allResults . "
		</body>
</html>
";
    return ["body" => $page];
}
?>
